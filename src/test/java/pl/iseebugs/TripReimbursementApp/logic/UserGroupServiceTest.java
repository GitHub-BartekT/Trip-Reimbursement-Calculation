package pl.iseebugs.TripReimbursementApp.logic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserGroupServiceTest {

/*    @Test
    void readAll() {
    }

    @Test
    void createUserGroup() {
    }*/

    @Test
    @DisplayName("should throw UserGroupNotFoundException when given id not found")
    void updateUserGroupById_noUserGroup_throwsUserGroupNotFoundException() {
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when given name already exists")
    void updateUserGroupById_userGroupsNameAlreadyExists_throwsIllegalArgumentException() {
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when given name is empty or has only white marks")
    void updateUserGroupById_emptyNameParam_throwsIllegalArgumentException() {
    }

    @Test
    @DisplayName("should rename user group")
    void updateUserGroupById_updatesUserGroup() {
    }


    /*@Test
    void deleteUserGroup() {
    }*/
}