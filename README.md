# Trip-Reimbursement
Program to managing and controlling reimbursement in organization.

## How it looks like
### Brief
The application is designed for organizations that want to divide reimbursements
based on employees' roles. An office worker might require different needs, such as parking fees, compared to a salesman who might claim expenses for car trips and fuel. Similarly, management staff might seek reimbursements for hotel stays and business-related meetings.
The application provides an overview of the current reimbursement situation. 
### Purpose and scope of the project
The application has two main view: One for Administrator and another for the User. 
The administrator can:
 - create user groups, such as CEO, regular worker, office worker
 - set limits for particular user groups
 - manage receipt types assigned to specific user groups
 - review the total monthly reimbursement value
Users can:
 - create new reimbursements
 - review history of their reimbursement
## What Has Been Accomplished💡
Backend development using Java with Spring Boot.
### Architecture ✅
CRUD functionalities with Controllers, Services, Repositories, DTOs, ReadModels, and Mappers. RESTful API endpoints created and tested. Architecture and validation implemented for request handling.
### Database✅
Database setup using HSQLDB with migrations managed by Flyway, supporting ManyToOne, OneToMany, and ManyToMany relationships. Entity Relationship Diagram (ERD) designed and available for reference.
#### Entity Relationship Diagram
![ERD](https://github.com/GitHub-BartekT/Trip-Reimbursement-Calculation/assets/119587290/7d93431f-b973-464b-8cc4-88265b5fb824)
### Tests ✅
Comprehensive testing using JUnit, AssertJ, Mockito, and Postman for integration tests, covering services and controllers with independent, fully-encapsulated tests.✅
### Other technologies
- Hibernate✅
- Logger - slf4j ✅
- Git / GitHub✅
- Logger - slf4j ✅
### Pending Tasks:  
1. Frontend ❌
   - create the main page✅
   - implement user and administrator views with functionalities
   - implement the browsing history of reimbursements for users
   - allow administrators to manage reimbursement settings and create reports
2. Security Implementation ❌
   - security to protect user data and sensitive information
3. Program Profiles ❌
   - configure different profiles for development, testing and production environments.
4. Documentation ❌
   - installation and setup guide
   - usage instructions for users and administrators
5. Improvments and Corrections:
   - funcionalities as required based on user feedback or additional requirements


https://www.linkedin.com/in/bartlomiejtucholski/


 
