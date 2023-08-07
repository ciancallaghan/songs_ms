package de.htwb.ai.authservice.controller;

import de.htwb.ai.authservice.model.User;
import de.htwb.ai.authservice.repo.UserRepository;
import de.htwb.ai.authservice.util.TokenCreator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserRepository userRepository;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<?> auth(@RequestBody User toAuth) {
        User user = userRepository.getByUserid(toAuth.getUserid());
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        if (toAuth.getPassword().equals(user.getPassword())) {
            TokenCreator tokenCreator = new TokenCreator();
            String token = tokenCreator.create(toAuth.getUserid());
            return new ResponseEntity<>(token, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
