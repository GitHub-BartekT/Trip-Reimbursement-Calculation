package pl.iseebugs.TripReimbursementApp.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.iseebugs.TripReimbursementApp.logic.UserGroupNotFoundException;
import pl.iseebugs.TripReimbursementApp.logic.UserNotFoundException;
import pl.iseebugs.TripReimbursementApp.logic.UserService;
import pl.iseebugs.TripReimbursementApp.model.projection.UserDTO;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    ResponseEntity<List<UserDTO>> readAllUsers(){
        return ResponseEntity.ok(service.readAll());
    }

    @GetMapping("/{id}")
    ResponseEntity<UserDTO> readById(@PathVariable int id) throws UserNotFoundException, UserGroupNotFoundException {
        return ResponseEntity.ok(service.readById(id));
    }

    @PostMapping
    ResponseEntity<Void> createUser(@RequestBody @Valid UserDTO userDTO) throws UserGroupNotFoundException, UserNotFoundException {
        var result = service.createUser(userDTO);
        return ResponseEntity.created(URI.create("http://localhost:8080/users/" + result.getId())).build();
    }

    @PutMapping()
    ResponseEntity<UserDTO> updateUser(@RequestBody @Valid UserDTO userDTO) throws UserGroupNotFoundException, UserNotFoundException {
        UserDTO updated = service.updateUserById(userDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteUser(@PathVariable int id) throws UserNotFoundException {
        service.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(UserGroupNotFoundException.class)
    ResponseEntity<String> handlerUserGroupNotFound(UserGroupNotFoundException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    ResponseEntity<String> handlerUserNotFound(UserNotFoundException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<String> handlerIllegalArgument(IllegalArgumentException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
