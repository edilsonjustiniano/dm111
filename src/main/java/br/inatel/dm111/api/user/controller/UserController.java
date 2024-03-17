package br.inatel.dm111.api.user.controller;

import br.inatel.dm111.api.core.ApiException;
import br.inatel.dm111.api.user.UserResponse;
import br.inatel.dm111.api.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getUsers() throws ApiException {
        var users = service.searchUsers();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/users")
    public ResponseEntity<UserResponse> postUser(@RequestBody UserRequest request) {
        var user = service.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

}
