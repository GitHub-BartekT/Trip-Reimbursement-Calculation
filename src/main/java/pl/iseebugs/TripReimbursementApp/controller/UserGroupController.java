package pl.iseebugs.TripReimbursementApp.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.iseebugs.TripReimbursementApp.exception.UserGroupNotFoundException;
import pl.iseebugs.TripReimbursementApp.logic.UserGroupService;
import pl.iseebugs.TripReimbursementApp.exception.UserNotFoundException;
import pl.iseebugs.TripReimbursementApp.model.projection.UserGroupDTO;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/groups")
public class UserGroupController {

    private final UserGroupService service;

    public UserGroupController(UserGroupService service) {
        this.service = service;
    }

    @GetMapping
    ResponseEntity<List<UserGroupDTO>> readAllUserGroups(){
        return ResponseEntity.ok(service.readAll());
    }

    @GetMapping("/{id}")
    ResponseEntity<UserGroupDTO> readById(@PathVariable int id) throws UserGroupNotFoundException {
        return ResponseEntity.ok(service.readById(id));
    }

    @PostMapping
    ResponseEntity<Void> createUserGroup(@RequestBody @Valid UserGroupDTO userGroupDTO){
        var result = service.createUserGroup(userGroupDTO);
        return ResponseEntity.created(URI.create("http://localhost:8080/groups/" + result.getId())).build();
    }

    @PutMapping()
    ResponseEntity<UserGroupDTO> updateUserGroup(@RequestBody @Valid UserGroupDTO userGroupDTO) throws UserGroupNotFoundException {
        UserGroupDTO updated = service.updateUserGroupById(userGroupDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteUserGroup(@PathVariable int id) throws UserGroupNotFoundException, UserNotFoundException {
        service.deleteUserGroup(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(UserGroupNotFoundException.class)
    ResponseEntity<String> handlerUserGroupNotFound(UserGroupNotFoundException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<String> handlerIllegalArgument(IllegalArgumentException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<String> handleNumberFormatException(NumberFormatException e) {
        return ResponseEntity.badRequest().body("Invalid number format: " + e.getMessage());
    }

}
