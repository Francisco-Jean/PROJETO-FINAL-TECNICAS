package com.brinquedomania.api.controllers;

import com.brinquedomania.api.dtos.UserRecordDto;
import com.brinquedomania.api.dtos.UserLoginRecordDto;
import com.brinquedomania.api.models.UserModel;
import com.brinquedomania.api.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @PostMapping("user/login")
    public ResponseEntity<Object> login(@RequestBody @Valid UserLoginRecordDto userLoginRecordDto){
        String email = userLoginRecordDto.email();
        String senha = userLoginRecordDto.password();
        UserModel user0 = userRepository.findByEmail(email);

        if (user0 == null || !Objects.equals(user0.getPassword(), senha)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("usuário não encontrado");
        }
        return ResponseEntity.status(HttpStatus.OK).body(user0);
    }

    @PostMapping("/user/register")
    public ResponseEntity<UserModel> saveUser(@RequestBody @Valid UserRecordDto userRecordDto) {
        var userModel = new UserModel();
        BeanUtils.copyProperties(userRecordDto, userModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(userRepository.save(userModel));
    }
    @GetMapping("/user/listAll")
    public ResponseEntity<List<UserModel>> getAllUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(userRepository.findAll());
    }
    @GetMapping("/user/listOne/{identifier}")
    public ResponseEntity<Object> getOneUser(@PathVariable(value = "identifier") String identifier) {
        UserModel user0 = userRepository.findByIdentifier(identifier);
        if (user0 == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("usuário não encontrado");
        }
        return ResponseEntity.status(HttpStatus.OK).body(userRepository.findByIdentifier(identifier));
    }

    @PutMapping("/user/edit/{identifier}")
    public ResponseEntity<Object> updateUser(@PathVariable(value="identifier") String identifier,
                                             @RequestBody @Valid UserRecordDto userRecordDto) {
        UserModel user0 = userRepository.findByIdentifier(identifier);
        if(user0 == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
        }
        var userModel = userRepository.findByIdentifier(identifier);
        BeanUtils.copyProperties(userRecordDto, userModel);
        return ResponseEntity.status(HttpStatus.OK).body(userRepository.save(userModel));
    }
    @DeleteMapping("/user/delete/{identifier}")
    public ResponseEntity<Object> deleteUser(@PathVariable(value="identifier") String identifier) {
        UserModel user0 = userRepository.findByIdentifier(identifier);
        if(user0 == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
        }
        userRepository.delete(userRepository.findByIdentifier(identifier));
        return ResponseEntity.status(HttpStatus.OK).body("Usuário deletado com sucesso.");
    }
}
