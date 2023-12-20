package pl.iseebugs.TripReimbursementApp.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.iseebugs.TripReimbursementApp.exception.ReceiptTypeNotFoundException;
import pl.iseebugs.TripReimbursementApp.exception.UserGroupNotFoundException;
import pl.iseebugs.TripReimbursementApp.exception.UserNotFoundException;
import pl.iseebugs.TripReimbursementApp.logic.UserGroupService;
import pl.iseebugs.TripReimbursementApp.model.projection.userGroup.UserGroupReadModel;
import pl.iseebugs.TripReimbursementApp.model.projection.userGroup.UserGroupReadModelFull;
import pl.iseebugs.TripReimbursementApp.model.projection.userGroup.UserGroupReadModelShort;
import pl.iseebugs.TripReimbursementApp.model.projection.userGroup.UserGroupWriteModel;

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
    ResponseEntity<List<UserGroupReadModel>> readAllUserGroups(){
        return ResponseEntity.ok(service.readAll());
    }

    @GetMapping("/receiptType/{id}")
    ResponseEntity<List<UserGroupReadModelShort>> readAllByReceiptType_Id(@PathVariable int id) throws UserGroupNotFoundException, ReceiptTypeNotFoundException {
        return ResponseEntity.ok(service.readAllByReceiptType_Id(id));
    }

    @GetMapping("/{id}")
    ResponseEntity<UserGroupReadModelFull> readById(@PathVariable int id) throws UserGroupNotFoundException {
        return ResponseEntity.ok(service.readById(id));
    }

    @PostMapping
    ResponseEntity<Void> createUserGroup(@RequestBody @Valid UserGroupWriteModel toCreate){
        var result = service.createUserGroup(toCreate);
        return ResponseEntity.created(URI.create("http://localhost:8080/groups/" + result.getId())).build();
    }

    @PutMapping()
    ResponseEntity<UserGroupReadModel> updateUserGroup(@RequestBody @Valid UserGroupWriteModel toUpdate) throws UserGroupNotFoundException {
        UserGroupReadModel updated = service.updateUserGroupById(toUpdate);
        return ResponseEntity.ok(updated);
    }


    @PutMapping("/{receiptTypes}")
    ResponseEntity<UserGroupReadModelFull> updateUserGroupWithReceiptTypesIds
            (@RequestBody @Valid UserGroupWriteModel toWrite,
             @PathVariable List<Integer> receiptTypes)
            throws UserGroupNotFoundException {
        var updated = service.updateUserGroupWithReceiptTypesIds(toWrite, receiptTypes);
        return ResponseEntity.ok(updated);
    }

    @PutMapping("add/{receiptTypes}")
    ResponseEntity<UserGroupReadModelFull> updateUserGroupAddReceiptTypesIds
            (@RequestBody int userGroupId,
             @PathVariable List<Integer> receiptTypes)
            throws UserGroupNotFoundException {
        var updated = service.updateUserGroupAddReceiptTypesIds(userGroupId, receiptTypes);
        return ResponseEntity.ok(updated);
    }

    @PutMapping("remove/{receiptTypes}")
    ResponseEntity<UserGroupReadModelFull> updateUserGroupRemoveReceiptTypesIds
            (@RequestBody @Valid int userGroupId,
             @PathVariable List<Integer> receiptTypes)
            throws UserGroupNotFoundException {
        var updated = service.updateUserGroupRemoveReceiptTypesIds(userGroupId, receiptTypes);
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
