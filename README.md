# Trip-Reimbursement
Program to managing and controlling reimbursement in organization.

## How it looks like
### Brief
The application is intended for organizations that want to divide reimbursements
depending on the role of employees. An office worker will have different needs(train, parking) 
than a salesperson (car trips, fuel) and management staff(hotels, business meetings).
The application gives the opportunity to summarize the current reimbursement situation. 
### Purpose and scope of the project
The application has two view. One for Admin and the other for User. 
The administrator can:
 - creates user groups like example CEO, regular worker, office worker, seller.
 - set limits for individual user groups
 - manage receipt types assigned to specific user groups
 - read the total monthly reimbursement value
User can:
 - create new reimbursement
 - browsing history of reimbursement
### What is to do✅
1. Backend
   - Rest, CRUD application✅
   - Databases - OneToMany, ManyToMany, ManyToOne✅
   - Controllers, Services, Repositories, DTO, ReadModels, Mappers✅
   - Tests
     - JUnit - ✅
     - AssertJ - ✅
     - unit tests - InMemoryRepositories ✅
     - integration tests - SQL scripts ✅
     - Postman - E2E tests ✅
   - Security ❌
   - Program profiles ❌
2. Frontend
   - Frontend❌
     - Main page❌
     - User view❌
       - Creating new reimbursement depending on:❌
         - duration ❌
         - distance traveled by car ❌
         - user receipts ❌
       - Browsing history of reimbursements❌
     - Administrator❌
       - Managing reimbursements settings depending on:❌
         - duration ❌
         - distance traveled by car ❌
         - receipt types ❌
         - user groups ❌
       - creating a simple report ❌
   - Connections
     - HTTP - methods DO, POST, GET, DELETE✅❌ 
## Technologies
1.  Backend
    - Environment - Intellij IDEA
        - Java 17
        - Maven
        - Spring Boot
    - Databases
        - HSQLDB
        - Hibernate
        - FlyWay
    - Testing
        - JUnit
        - AssertJ
        - Postman
2.  Communication
  - HTTP
3. Frontend
   - HTML
   - CSS
   - JavaScript
4.  Version Control
    - Git / GitHub
### What hasn't been done💡
1. Frontend
### Installation and setup
### Usage
### What I will correct or upgrade



 