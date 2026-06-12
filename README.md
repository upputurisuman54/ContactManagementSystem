# Contact Management System

A full stack contact management web application built with Spring Boot, React, and MySQL. Allows users to securely register, manage personal contacts, organize them into groups, mark favourites, and import/export contacts via CSV. Includes an admin panel for user and contact oversight.

Contacts Page:

![page](https://github.com/upputurisuman54/ContactManagementSystem/blob/main/Screenshot%202026-06-12%20210325.png?raw=true)

Dashboard Page:

![page](https://github.com/upputurisuman54/ContactManagementSystem/blob/main/Screenshot%202026-06-12%20210350.png?raw=true)

Add Contact Page:

![page](https://github.com/upputurisuman54/ContactManagementSystem/blob/main/Screenshot%202026-06-12%20210622.png?raw=true)

Activity Page:

![page](https://github.com/upputurisuman54/ContactManagementSystem/blob/main/Screenshot%202026-06-12%20210412.png?raw=true)

Import CSV page:

![Page](https://github.com/upputurisuman54/ContactManagementSystem/blob/main/Screenshot%202026-06-12%20210559.png?raw=true)

Export Csv file page:

![CSv File](https://github.com/upputurisuman54/ContactManagementSystem/blob/main/Screenshot%202026-06-12%20210535.png?raw=true)

Profile Page:

![Page](https://github.com/upputurisuman54/ContactManagementSystem/blob/main/Screenshot%202026-06-12%20210447.png?raw=true)

Tags Page:

![Page](https://github.com/upputurisuman54/ContactManagementSystem/blob/main/Screenshot%202026-06-12%20210509.png?raw=true)

Register Page:

![Registerpage](https://github.com/upputurisuman54/ContactManagementSystem/blob/main/Screenshot%202026-06-12%20210720.png?raw=true)

Sign in Page:

![sigh in page](https://github.com/upputurisuman54/ContactManagementSystem/blob/main/Screenshot%202026-06-12%20210656.png?raw=true)
## Features

- User registration and login with JWT authentication
- Full CRUD operations for contacts
- Search contacts by name, email, or phone
- Organize contacts into custom groups
- Mark and filter favourite contacts
- Profile photo upload for contacts
- CSV import and export
- Role-based access control (USER and ADMIN)
- Admin panel to view all users and contacts
- Responsive React frontend

## Tech Stack

**Backend**
- Java 17
- Spring Boot 3
- Spring Security
- Spring Data JPA / Hibernate
- MySQL
- JWT (JSON Web Tokens)
- Maven

**Frontend**
- React.js
- React Router
- Axios
- Vite

## Project Structure

contact-management/

├── backend/

│   └── src/main/java/com/example/contactmanager/

│       ├── config/        JWT and security configuration

│       ├── model/          User, Contact, Group entities

│       ├── repository/     JPA repositories

│       ├── service/         Business logic

│       ├── controller/      REST API endpoints

│       └── dto/              Request and response objects

└── frontend/

└── src/

├── pages/            Login, Register, Dashboard, Contacts, Import, Admin

├── components/       Reusable UI components

├── context/          Authentication context

├── api.js            Axios instance with JWT interceptor

└── App.jsx            Route definitions


## Getting Started

### Prerequisites

- Java 17 or higher
- Node.js and npm
- MySQL

### Backend Setup

1. Create a MySQL database:

CREATE DATABASE contact_db;


2. Update `src/main/resources/application.properties` with your MySQL credentials:

spring.datasource.url=jdbc:mysql://localhost:3306/contact_db
spring.datasource.username=root
spring.datasource.password=yourpassword


3. Run the backend:

mvn spring-boot:run


Backend runs on `http://localhost:8080`

### Frontend Setup

1. Navigate to the frontend folder:

cd frontend


2. Install dependencies:

npm install

3. Run the development server:

npm run dev


Frontend runs on `http://localhost:3000`

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/auth/register | Register new user |
| POST | /api/auth/login | Login and receive JWT |
| GET | /api/contacts | Get all contacts |
| POST | /api/contacts | Create new contact |
| GET | /api/contacts/{id} | Get contact by id |
| PUT | /api/contacts/{id} | Update contact |
| DELETE | /api/contacts/{id} | Delete contact |
| GET | /api/contacts/search | Search contacts |
| PUT | /api/contacts/{id}/favourite | Toggle favourite |
| GET | /api/contacts/export | Export contacts as CSV |
| POST | /api/contacts/import | Import contacts from CSV |
| GET | /api/groups | Get all groups |
| POST | /api/groups | Create group |
| DELETE | /api/groups/{id} | Delete group |
| GET | /api/admin/users | Admin: list all users |
| GET | /api/admin/contacts | Admin: list all contacts |

## CSV Format
Name,Email,Phone,Company,Favourite,Tags

John Doe,john@example.com,+91 9999999999,Acme Corp,false,friend

## Author

Upputuri Suman
- Gmail: upputurisuman53@gmail.com
- GitHub: https://github.com/upputurisuman54
- LinkedIn: www.linkedin.com/in/upputuri-suman-a0730726b
