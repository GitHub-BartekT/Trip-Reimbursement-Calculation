package pl.iseebugs.TripReimbursementApp.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.iseebugs.TripReimbursementApp.adapter.SqlUserGroupRepository;
import pl.iseebugs.TripReimbursementApp.logic.UserGroupService;
import pl.iseebugs.TripReimbursementApp.model.UserGroupRepository;

@RestController
@RequestMapping(value = "/supergroups")
public class UserGroupController {

    private final UserGroupService service;

    public UserGroupController(UserGroupService service) {
        this.service = service;
    }
}
