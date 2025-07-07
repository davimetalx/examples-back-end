package br.com.examples.controller;

import br.com.examples.model.User;
import br.com.examples.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(value = "http://localhost:4200")
@RestController
@RequestMapping(value = "/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User save = userRepository.save(user);
        return ResponseEntity.status(201).body(save);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    BeanUtils.copyProperties(user, existingUser, "id");
                    return ResponseEntity.ok(userRepository.save(existingUser));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            userRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

}
