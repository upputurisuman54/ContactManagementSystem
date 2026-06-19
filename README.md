# Contact Management System

A full-stack contact management web application built with Spring Boot, React, and MySQL. Users can securely register, manage personal contacts, organize them with tags and groups, mark favourites, and import/export contacts via CSV. Includes an admin panel for complete user and contact oversight.

---

## Screenshots

| Contacts Page | Dashboard Page |
|---------------|----------------|
| ![Contacts Page](https://github.com/upputurisuman54/ContactManagementSystem/blob/main/screenshots/contacts-page.png?raw=true) | ![Dashboard Page](https://github.com/upputurisuman54/ContactManagementSystem/blob/main/screenshots/dashboard.png?raw=true) |

| Add Contact | Activity Log |
|-------------|--------------|
| ![Add Contact](https://github.com/upputurisuman54/ContactManagementSystem/blob/main/screenshots/add-contact.png) | ![Activity](https://github.com/upputurisuman54/ContactManagementSystem/blob/main/screenshots/activity-log.png) |

| Import CSV | Export CSV |
|------------|------------|
| ![Import CSV](https://github.com/upputurisuman54/ContactManagementSystem/blob/main/screenshots/import-csv.png?raw=true) | ![Export CSV](https://github.com/upputurisuman54/ContactManagementSystem/blob/main/screenshots/export-csv.png?raw=true) |

| Profile Page | Tags Page |
|--------------|-----------|
| ![Profile](https://github.com/upputurisuman54/ContactManagementSystem/blob/main/Screenshot%202026-06-12%20210447.png?raw=true) | ![Tags](https://github.com/upputurisuman54/ContactManagementSystem/blob/main/Screenshot%202026-06-12%20210509.png?raw=true) |

| Register | Sign In |
|----------|---------|
| ![Register](https://github.com/upputurisuman54/ContactManagementSystem/blob/main/Screenshot%202026-06-12%20210720.png?raw=true) | ![Sign In](https://github.com/upputurisuman54/ContactManagementSystem/blob/main/Screenshot%202026-06-12%20210656.png?raw=true) |

---

## Features

- JWT Authentication with Role-Based Access Control (USER and ADMIN)
- Full CRUD operations for contacts
- Search contacts by name, email, or phone
- Organize contacts into custom groups and tags
- Mark and filter favourite contacts
- Profile photo upload for contacts
- CSV import and export
- Admin panel to view and manage all users and contacts
- Activity and audit log
- Responsive React frontend

---

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
- React Router v6
- Axios
- Vite

---

## Project Structure

```
contact-management/
├── src/main/java/com/example/contactmanager/
│   ├── config/         JWT and security configuration
│   ├── model/          User, Contact, Group entities
│   ├── repository/     JPA repositories
│   ├── service/        Business logic
│   ├── controller/     REST API endpoints
│   └── dto/            Request and response objects
└── frontend/
    └── src/
        ├── pages/      Login, Register, Dashboard, Contacts, Import, Admin
        ├── components/ Reusable UI components
        ├── context/    Authentication context
        ├── api.js      Axios instance with JWT interceptor
        └── App.jsx     Route definitions
```

---

## Getting Started

### Prerequisites

- Java 17+
- Node.js and npm
- MySQL

### 1. Clone the Repository

```bash
git clone https://github.com/upputurisuman54/ContactManagementSystem.git
cd ContactManagementSystem
```

### 2. Backend Setup

Create the MySQL database:

```sql
CREATE DATABASE contact_db;
```

Configure your credentials in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/contact_db
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

Run the backend:

```bash
mvn spring-boot:run
```

Backend runs on `http://localhost:8080`

### 3. Frontend Setup

```bash
cd frontend
npm install
npm run dev
```

Frontend runs on `http://localhost:3000`

---

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/auth/register | Register a new user |
| POST | /api/auth/login | Login and receive JWT |
| GET | /api/contacts | Get all contacts |
| POST | /api/contacts | Create a new contact |
| GET | /api/contacts/{id} | Get contact by ID |
| PUT | /api/contacts/{id} | Update contact |
| DELETE | /api/contacts/{id} | Delete contact |
| GET | /api/contacts/search | Search contacts |
| PUT | /api/contacts/{id}/favourite | Toggle favourite |
| GET | /api/contacts/export | Export contacts as CSV |
| POST | /api/contacts/import | Import contacts from CSV |
| GET | /api/groups | Get all groups |
| POST | /api/groups | Create a group |
| DELETE | /api/groups/{id} | Delete a group |
| GET | /api/admin/users | Admin: list all users |
| GET | /api/admin/contacts | Admin: list all contacts |

---

## CSV Format

```csv
Name,Email,Phone,Company,Favourite,Tags
John Doe,john@example.com,+91 9999999999,Acme Corp,false,friend
```

---

## Author

**Upputuri Suman** — Java Full Stack Developer

- 📧 [upputurisuman53@gmail.com](mailto:upputurisuman53@gmail.com)
- 💼 [linkedin.com/in/upputurisuman](https://www.linkedin.com/in/upputurisuman)
- 🐙 [github.com/upputurisuman54](https://github.com/upputurisuman54)
