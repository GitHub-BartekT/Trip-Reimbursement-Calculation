package pl.iseebugs.TripReimbursementApp.controller;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

class ReceiptTypeControllerIntegrationTest {

    @Test
    @Sql({"/sql/001-test-schema.sql", "/sql/005-test-data-receipt-types.sql"})
    void readAll() {
    }

    @Test
    void readAllByUserGroup_Id() {
    }

    @Test
    void readById() {
    }

    @Test
    void createReceiptTypeToAllUserGroup() {
    }

    @Test
    void saveReceiptTypeWithUserGroupIds() {
    }

    @Test
    void updateReceiptTypeToAllUserGroups() {
    }

    @Test
    void updateReceiptType() {
    }

    @Test
    void deleteReceiptType() {
    }
}