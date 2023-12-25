package pl.iseebugs.TripReimbursementApp.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.iseebugs.TripReimbursementApp.exception.ReimbursementNotFoundException;
import pl.iseebugs.TripReimbursementApp.logic.ReimbursementService;
import pl.iseebugs.TripReimbursementApp.exception.UserGroupNotFoundException;
import pl.iseebugs.TripReimbursementApp.exception.UserNotFoundException;
import pl.iseebugs.TripReimbursementApp.model.projection.reimbursement.ReimbursementReadModel;
import pl.iseebugs.TripReimbursementApp.model.projection.reimbursement.ReimbursementReadModelShort;
import pl.iseebugs.TripReimbursementApp.model.projection.reimbursement.ReimbursementWriteModel;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/reimbursements")
public class ReimbursementController {

    private final ReimbursementService service;

    public ReimbursementController(ReimbursementService service) {
        this.service = service;
    }

    @GetMapping
    ResponseEntity<List<ReimbursementReadModel>> readAllReimbursement(){
        return ResponseEntity.ok(service.readAll());
    }


    @GetMapping("/{id}")
    ResponseEntity<ReimbursementReadModel> readById(@PathVariable int id) throws ReimbursementNotFoundException {
        return ResponseEntity.ok(service.readById(id));
    }

    @GetMapping("/user/{id}")
    ResponseEntity<List<ReimbursementReadModel>> readAllByUserId(@PathVariable int id) throws UserNotFoundException {
        return ResponseEntity.ok(service.readAllByUser_Id(id));
    }

    @GetMapping("/short")
    ResponseEntity<List<ReimbursementReadModelShort>> readAllReimbursementShort(){
        return ResponseEntity.ok(service.readAllShort());
    }
    @GetMapping("/user/short/{id}")
    ResponseEntity<List<ReimbursementReadModelShort>> readAllShortByUserId(@PathVariable int id) throws UserNotFoundException {
        return ResponseEntity.ok(service.readAllShortByUser_Id(id));
    }

    @PostMapping
    ResponseEntity<Void> createReimbursement(@RequestBody @Valid ReimbursementWriteModel toWrite) throws ReimbursementNotFoundException, UserNotFoundException {
        var result = service.createReimbursement(toWrite);
        return ResponseEntity.created(URI.create("http://localhost:8080/reimbursements/" + result.getId())).build();
    }

    @PutMapping()
    ResponseEntity<ReimbursementReadModel> updateReimbursement(@RequestBody @Valid ReimbursementWriteModel toWrite) throws ReimbursementNotFoundException, UserNotFoundException {
        ReimbursementReadModel updated = service.updateReimbursementById(toWrite);
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/push/{id}")
    ResponseEntity<ReimbursementReadModel> sendForAprovalReimbursement(@PathVariable int id) throws ReimbursementNotFoundException{
        ReimbursementReadModel updated = service.setSendForApprovalToTrue(id);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteReimbursement(@PathVariable int id) throws ReimbursementNotFoundException {
        service.deleteReimbursementById(id);
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

    @ExceptionHandler(ReimbursementNotFoundException.class)
    ResponseEntity<String> handlerReimbursementNotFound(ReimbursementNotFoundException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<String> handlerIllegalArgument(IllegalArgumentException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
