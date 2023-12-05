package pl.iseebugs.TripReimbursementApp.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.iseebugs.TripReimbursementApp.exception.ReceiptTypeNotFoundException;
import pl.iseebugs.TripReimbursementApp.exception.UserGroupNotFoundException;
import pl.iseebugs.TripReimbursementApp.logic.ReceiptTypeService;
import pl.iseebugs.TripReimbursementApp.model.projection.receiptType.ReceiptTypeReadModel;
import pl.iseebugs.TripReimbursementApp.model.projection.receiptType.ReceiptTypeWriteModel;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/receipts")
public class ReceiptTypeController {

    private final ReceiptTypeService service;

    public ReceiptTypeController(ReceiptTypeService service){this.service = service;}

    @GetMapping
    ResponseEntity<List<ReceiptTypeReadModel>> readAll(){
        return ResponseEntity.ok(service.readAll());
    }

    @GetMapping("/userGroup/{id}")
    ResponseEntity<List<ReceiptTypeReadModel>> readAllByUserGroup_Id(@PathVariable int id)
            throws UserGroupNotFoundException {
        return ResponseEntity.ok(service.readAllByUserGroup_Id(id));
    }

    @GetMapping("/{id}")
    ResponseEntity<ReceiptTypeReadModel> readById(@PathVariable int id)
            throws ReceiptTypeNotFoundException {
        return ResponseEntity.ok(service.readById(id));
    }

    @PostMapping("/all")
    ResponseEntity<Void> createReceiptTypeToAllUserGroup
            (@RequestBody @Valid ReceiptTypeWriteModel toWrite) {
        var result = service.saveReceiptTypeToAllUserGroup(toWrite);
        return ResponseEntity.created(URI.create("http://localhost:8080/receipts/" + result.getId())).build();
    }

    @PostMapping
    ResponseEntity<Void> createReceiptTypeWithUserGroupIds
            (@RequestBody @Valid ReceiptTypeWriteModel toWrite, @RequestParam("integerList") List<Integer> integerList)
            throws UserGroupNotFoundException {
        var result = service.saveReceiptTypeWithUserGroupIds(toWrite, integerList);
        return ResponseEntity.created(URI.create("http://localhost:8080/receipts/" + result.getId())).build();
    }

    @PutMapping()
    ResponseEntity<ReceiptTypeReadModel> updateReceiptType
            (@RequestBody @Valid ReceiptTypeWriteModel toWrite) throws ReceiptTypeNotFoundException, UserGroupNotFoundException {
        var updated = service.updateReceiptType(toWrite);
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/{userGroups}")
    ResponseEntity<ReceiptTypeReadModel> updateReceiptTypeWithUserGroupIds
            (@RequestBody @Valid ReceiptTypeWriteModel toWrite,
             @PathVariable List<Integer> userGroups)
            throws ReceiptTypeNotFoundException, UserGroupNotFoundException {
        var updated = service.updateReceiptTypeWithUserGroupIds(toWrite, userGroups);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteReceiptType(@PathVariable int id) throws ReceiptTypeNotFoundException {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(UserGroupNotFoundException.class)
    ResponseEntity<String> handlerUserGroupNotFound(UserGroupNotFoundException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }


    @ExceptionHandler(ReceiptTypeNotFoundException.class)
    ResponseEntity<String> handlerReceiptTypeNotFoundException(ReceiptTypeNotFoundException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<String> handlerIllegalArgument(IllegalArgumentException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
