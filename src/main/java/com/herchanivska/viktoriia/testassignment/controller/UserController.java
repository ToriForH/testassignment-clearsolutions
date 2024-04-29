package com.herchanivska.viktoriia.testassignment.controller;

import com.herchanivska.viktoriia.testassignment.dto.SearchBirthDateRequest;
import com.herchanivska.viktoriia.testassignment.dto.UserRequest;
import com.herchanivska.viktoriia.testassignment.dto.UserResponse;
import com.herchanivska.viktoriia.testassignment.model.User;
import com.herchanivska.viktoriia.testassignment.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Properties;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponse> create(@RequestBody @Valid UserRequest userRequest) throws IOException {
        checkValidAge(userRequest.getBirthDate());
        User user = userRequest.getUserEntity();
        user = userService.create(user);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(user.getId())
                .toUri();
        return ResponseEntity.created(location).body(new UserResponse(user));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponse> updateSomeFields(@PathVariable("id") long userId,
                                                         @RequestBody UserRequest userRequest)
            throws IntrospectionException, InvocationTargetException, IllegalAccessException, IOException {
        User toUpdate = userService.readById(userId);
        if (toUpdate == null) {
            throw new EntityNotFoundException("User with id " + userId + " not exist");
        }
        if (userRequest.getBirthDate() != null) {
            checkValidAge(userRequest.getBirthDate());
        }
        toUpdate = userRequest.getUserEntity();
        toUpdate.setId(userId);
        toUpdate = userService.updateField(toUpdate);
        return ResponseEntity.ok(new UserResponse(toUpdate));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(@PathVariable("id") long userId,
                                               @RequestBody @Valid UserRequest userRequest) throws IOException {
        User toUpdate = userService.readById(userId);
        if (toUpdate == null) {
            throw new EntityNotFoundException("User with id " + userId + " not exist");
        }
        checkValidAge(userRequest.getBirthDate());
        toUpdate = userRequest.getUserEntity();
        toUpdate.setId(userId);
        toUpdate = userService.update(toUpdate);
        return ResponseEntity.ok(new UserResponse(toUpdate));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") long userId) {
        User toDelete = userService.readById(userId);
        if (toDelete == null) {
            throw new EntityNotFoundException("User with id " + userId + " not exist");
        }
        userService.delete(userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserResponse>> searchByBirthDate(@RequestBody SearchBirthDateRequest dateRange) {
        if (dateRange.getTo().isBefore(dateRange.getFrom())) {
            throw new IllegalArgumentException("Invalid date range");
        }
        List<UserResponse> searchResult = userService.searchByBirthDate(dateRange.getFrom(), dateRange.getTo())
                .stream()
                .map(UserResponse::new)
                .toList();
        return ResponseEntity.ok(searchResult);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> readById(@PathVariable("id") long userId) {
        User user = userService.readById(userId);
        if (user != null) {
            return ResponseEntity.ok(new UserResponse(user));
        }
        throw new EntityNotFoundException("User with id " + userId + " not found");
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAll() {
        List<UserResponse> users = userService.getAll().stream().map(UserResponse::new).toList();
        return ResponseEntity.ok(users);
    }

    private boolean checkValidAge(LocalDate birthDate) throws IOException {
        Properties properties = new Properties();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream stream = loader.getResourceAsStream("application.properties");
        properties.load(stream);
        String minAge = properties.getProperty("min.age");
        LocalDate minBirthDate = LocalDate.now().minusYears(Long.parseLong(minAge));
        if (birthDate.isAfter(minBirthDate)) {
            throw new IllegalArgumentException("User should be at least " + minAge + " years old");
        }
        return true;
    }
}
