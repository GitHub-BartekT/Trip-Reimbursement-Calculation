package pl.iseebugs.TripReimbursementApp.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.iseebugs.TripReimbursementApp.exception.*;
import pl.iseebugs.TripReimbursementApp.logic.UserCostService;
import pl.iseebugs.TripReimbursementApp.model.projection.userCost.UserCostReadModel;
import pl.iseebugs.TripReimbursementApp.model.projection.userCost.UserCostWriteModel;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/costs")
public class UserCostController {

    private final UserCostService service;

    @Autowired
    public UserCostController(UserCostService service) {
        this.service = service;
    }

    @GetMapping
    ResponseEntity<List<UserCostReadModel>> readAll(){
        return ResponseEntity.ok(service.readAll());
    }


    @GetMapping("/{id}")
    ResponseEntity<UserCostReadModel> readById(@PathVariable int id) throws UserCostNotFoundException {
        return ResponseEntity.ok(service.readById(id));
    }

    @GetMapping("/reimbursement/{id}")
    ResponseEntity<List<UserCostReadModel>> readAllByReimbursementID(@PathVariable int id)
            throws ReimbursementNotFoundException {
        return ResponseEntity.ok(service.readAllByReimbursement_Id(id));
    }

    @GetMapping("/receipt/{id}")
    ResponseEntity<List<UserCostReadModel>> readAllByReceiptTypeID(@PathVariable int id)
            throws ReceiptTypeNotFoundException {
        return ResponseEntity.ok(service.readAllByReceiptType_Id(id));
    }

    @PostMapping
    ResponseEntity<Void> createUserCost(@RequestBody @Valid UserCostWriteModel toWrite)
            throws Exception {
        var result = service.createUserCost(toWrite);
        return ResponseEntity.created(URI.create("http://localhost:8080/costs/" + result.getId())).build();
    }

    @PutMapping()
    ResponseEntity<UserCostReadModel> updateUserCost(@RequestBody @Valid UserCostWriteModel toWrite)
            throws Exception {
        UserCostReadModel updated = service.updateUserCost(toWrite);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteUserCost(@PathVariable int id)
            throws ReimbursementNotFoundException, UserCostNotFoundException {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(ReceiptTypeNotFoundException.class)
    ResponseEntity<String> handlerReceiptTypeNotFoundException(ReceiptTypeNotFoundException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(UserCostNotFoundException.class)
    ResponseEntity<String> handlerUserCostNotFound(UserCostNotFoundException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(UserGroupNotFoundException.class)
    ResponseEntity<String> handlerUserGroupNotFound(UserGroupNotFoundException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    ResponseEntity<String> handlerUserNotFound(UserNotFoundException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(ReimbursementNotFoundException.class)
    ResponseEntity<String> handlerReimbursementNotFound(ReimbursementNotFoundException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<String> handlerIllegalArgument(IllegalArgumentException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
