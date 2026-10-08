# Shared-Database Multi-Tenant SaaS Backend

A backend-focused **SaaS application** built using **Java, Spring Boot, Spring Security, JWT, Hibernate/JPA, and MySQL**.

The application supports multiple organizations (**tenants**) using a **shared database architecture**. Each tenant uses the same application and database, while tenant-specific data is logically isolated using `tenant_id`.

The project also implements **JWT authentication, Role-Based Access Control (RBAC), project management, task management, customer management, subscription plans, usage limits, email notifications, REST APIs, Swagger/OpenAPI documentation, and Postman-based API testing**.

---

## 📌 Project Overview

The purpose of this project is to demonstrate how a real-world SaaS backend can support multiple organizations using a **shared database** while maintaining tenant-level data isolation.

Each organization has its own:

* Users
* Projects
* Tasks
* Customers
* Subscription
* Plan limits

Although all tenants use the same database, application-level tenant isolation ensures that one tenant cannot access another tenant's data.

### Example

```text
                    SaaS Application
                           |
          -------------------------------------
          |                                   |
       Tenant A                            Tenant B
          |                                   |
    --------------                       --------------
    |     |      |                       |     |      |
 Users Projects Tasks                  Users Projects Tasks
    |                                    |
Customers                            Customers
```

---

# 🏗️ Architecture

The application follows a layered backend architecture.

```text
                    Client / Postman
                           |
                           v
                    REST Controller
                           |
                           v
                      Service Layer
                           |
                           v
                    Repository Layer
                           |
                           v
                     Hibernate/JPA
                           |
                           v
                       MySQL DB
```

### Authentication and Authorization Flow

```text
Client
  |
  | Authorization: Bearer JWT
  v
JWT Authentication Filter
  |
  v
JWT Validation
  |
  v
Spring Security
  |
  v
Authentication
  |
  v
Role Authorization
  |
  v
Tenant Identification
  |
  v
Controller
  |
  v
Service
  |
  v
Repository
  |
  v
MySQL
```

---

# ✨ Features

## 1. 🏢 Tenant Management

The application supports multiple organizations/tenants.

Each tenant has its own isolated business data.

### Tenant Information

* Company name
* Subdomain
* Active status
* Tenant administrator
* Users
* Projects
* Tasks
* Customers
* Subscription
* Plan limits

### Example

```text
Tenant 1
Company: ABC Technologies

Tenant 2
Company: XYZ Solutions
```

---

# 2. 📝 Tenant Registration

A new organization can register through the registration API.

### Registration Flow

```text
Company Registration
        |
        v
Create Tenant
        |
        v
Create Tenant Admin
        |
        v
Assign TENANT_ADMIN Role
        |
        v
Encrypt Password
        |
        v
Save User + Tenant Relationship
```

The first user created for the organization is assigned the:

```text
TENANT_ADMIN
```

role.

---

# 3. 🔐 JWT Authentication

The application uses **JSON Web Token (JWT)** for stateless authentication.

### Login Flow

```text
User
 |
 | Email + Password
 v
Login API
 |
 v
Validate Credentials
 |
 v
Generate JWT
 |
 v
Return JWT
```

The client sends the JWT with protected API requests.

### Example

```http
Authorization: Bearer <JWT_TOKEN>
```

---

# 4. 🔒 Password Encryption

User passwords are never stored as plain text.

The application uses:

```text
BCryptPasswordEncoder
```

### Example

```text
Original Password:

mypassword123


Stored Password:

$2a$10$xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
```

This provides secure password storage.

---

# 5. 👥 Role-Based Access Control (RBAC)

The application implements **Role-Based Access Control** using Spring Security.

### Current Roles

```text
TENANT_ADMIN
MANAGER
EMPLOYEE
```





# 6. 🏢 Shared Database Multi-Tenant Architecture

This project uses a:

```text
Shared Database Architecture
```

It does **not** create a separate database for every tenant.

All tenants use the same MySQL database.

### Database Structure

```text
MySQL Database
│
├── tenants
├── users
├── projects
├── tasks
├── customers
├── plans
└── subscriptions
```

Tenant-specific records contain a tenant relationship.

### Example

```text
projects

+----+------------------+-----------+
| id | name             | tenant_id |
+----+------------------+-----------+
|  1 | E-Commerce       |     1     |
|  2 | CRM Application  |     1     |
|  3 | HR Management    |     2     |
|  4 | Inventory System |     2     |
+----+------------------+-----------+
```

Therefore:

```text
Tenant 1
   |
   +-- Project 1
   +-- Project 2


Tenant 2
   |
   +-- Project 3
   +-- Project 4
```

Tenant 1 should only be able to access:

```text
Project 1
Project 2
```

Tenant 2 should only be able to access:

```text
Project 3
Project 4
```

---

# 7. 🔐 Tenant Data Isolation

Tenant isolation is one of the most important features of this project.

The application identifies the tenant associated with the authenticated user.

### Tenant Identification Flow

```text
JWT Token
    |
    v
Authenticated User
    |
    v
User's Tenant
    |
    v
Tenant ID
    |
    v
Tenant-specific Query
    |
    v
Return Only Tenant Data
```

Instead of retrieving all projects:

```sql
SELECT * FROM projects;
```

the application should retrieve data belonging to the current tenant:

```sql
SELECT *
FROM projects
WHERE tenant_id = ?;
```

This prevents cross-tenant data access.

---

# 8. 📁 Project Management

The application provides REST APIs for project management.

### Project Features

* Create project
* Get all projects
* Get project by ID
* Update project
* Delete project
* Associate project with tenant
* Tenant-based project isolation

### Example

```text
Tenant
   |
   +-- Project A
   |
   +-- Project B
   |
   +-- Project C
```

Each project belongs to a specific tenant.

---

# 9. ✅ Task Management

Tasks can be created and managed inside projects.

### Task Features

* Create task
* Get tasks
* Get task by ID
* Update task
* Update task status
* Assign task
* Delete task
* Associate task with project
* Tenant-based task isolation

### Example

```text
Project
   |
   +-- Task 1
   |
   +-- Task 2
   |
   +-- Task 3
```

---

# 10. 👤 Customer Management

The application also provides customer management APIs.

### Customer Features

* Create customer
* Get customers
* Get customer by ID
* Update customer
* Delete customer
* Tenant-specific customer data
* Role-based access control

### Example

```text
Tenant A
   |
   +-- Customer 1
   +-- Customer 2
   +-- Customer 3
```

Tenant B cannot access Tenant A's customers.

---

# 11. 💳 Subscription Management

The project includes SaaS subscription concepts.

A tenant can have a subscription associated with a specific plan.

### Example

```text
Tenant
   |
   v
Subscription
   |
   v
Plan
```

### Example Plans

```text
FREE
PRO
ENTERPRISE
```

---

# 12. 📊 Plan-Based Usage Limits

Different subscription plans can have different resource limits.

### Free Plan

```text
FREE PLAN

Maximum Users     = 10
Maximum Projects  = 3
Maximum Tasks     = 100
```

### Pro Plan

```text
PRO PLAN

Maximum Users     = 100
Maximum Projects  = 20
Maximum Tasks     = 5000
```

### Enterprise Plan

```text
ENTERPRISE PLAN

Maximum Users     = Higher Limit
Maximum Projects  = Higher Limit
Maximum Tasks     = Higher Limit
```

### Limit Checking Flow

Before creating a resource, the application can check the tenant's plan limit.

```text
Create Project
      |
      v
Check Tenant Plan
      |
      v
Check Project Limit
      |
      +-------------------+
      |                   |
   Limit OK           Limit Reached
      |                   |
      v                   v
Create Project       Reject Request
```

---

# 13. 📧 Email Notifications

The project includes email notification functionality.

### Notification Flow

```text
Application Event
       |
       v
Notification Service
       |
       v
Email Service
       |
       v
Send Email
```

### Example Use Cases

* User registration notification
* Login notification
* Task-related notification
* Application event notification

---

# 14. 🔔 Notification System

The application provides a foundation for notification handling.

### Example

```text
User Action
    |
    v
Business Event
    |
    v
Notification Service
    |
    +----> Email
    
    
```

The notification layer can later be extended to support additional channels.

---

# 15. 🗄️ Database Design

The application uses **MySQL** as the relational database.

### Main Entities

```text
Tenant
User
Project
Task
Customer
Plan
Subscription
```

### Basic Relationship

```text
Tenant
  |
  +---- Users
  |
  +---- Projects
  |
  +---- Tasks
  |
  +---- Customers
  |
  +---- Subscription
             |
             +---- Plan
```

JPA/Hibernate is used to map Java entities to database tables.

---

# 16. 🔗 Entity Relationships

### Tenant → Users

```text
Tenant
   |
   | 1
   |
   |------ * Users
```

### Tenant → Projects

```text
Tenant
   |
   | 1
   |
   |------ * Projects
```

### Project → Tasks

```text
Project
   |
   | 1
   |
   |------ * Tasks
```

### Tenant → Customers

```text
Tenant
   |
   | 1
   |
   |------ * Customers
```

### Tenant → Subscription

```text
Tenant
   |
   | 1
   |
   |------ 1 Subscription
```

### Subscription → Plan

```text
Subscription
   |
   | *
   |
   |------ 1 Plan
```

---


