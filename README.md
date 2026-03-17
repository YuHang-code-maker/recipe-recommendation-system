# Recipe Recommendation System

A Full-Stack Recipe Recommendation System built with Spring Boot, MySQL and JavaScript/jQuery.

The system allows users to search recipes by title or ingredients, while administrators can create, update and delete recipes through secured REST APIs.

---

## Technologies Used

Backend
- Spring Boot
- Spring Security
- JWT Authentication
- JPA / Hibernate

Database
- MySQL

Frontend
- HTML
- JavaScript
- jQuery
- Bootstrap

Testing
- JUnit
- REST Integration Tests
- Karate API Tests
- Selenium UI Tests

---

## Setup Instructions

### 1. Clone the Repository

```bash
git clone https://github.com/YuHang-code-maker/recipe-recommendation-system.git
cd recipe-recommendation-system
```

---

### 2. Database Setup

Create the database in MySQL:

```sql
CREATE DATABASE recipe_db;
```

Update the database configuration in `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/recipe_db
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=create
```

Hibernate will automatically create the tables when the application starts.

---

### 3. Run the Backend

Start the Spring Boot application:

```bash
mvn spring-boot:run
```

The backend server will start at:

```
http://localhost:8085
```

---

### 4. Run the Frontend

Open `index.html` in a web browser.

When the application loads, it checks whether a JWT token exists in localStorage.

- If a valid token exists → the recipe list is displayed
- If no token exists → the login page is shown

Users must log in before accessing the system.

---

### 5. Login

Login using the authentication endpoint.

Example admin credentials:

```
username: admin
password: admin
```

After successful login, the server returns a JWT token.

The frontend stores the token in localStorage and includes it in all protected API requests:

```
Authorization: Bearer <token>
```

If the token expires or the user logs out, the application redirects back to the login page.

---

## Main API Endpoints

| Method | Endpoint | Description |
|------|------|------|
| GET | /api/recipes | Get all recipes |
| GET | /api/recipes/{id} | Get recipe by ID |
| GET | /api/recipes/search | Search recipes by title |
| GET | /api/recipes/searchByIngredients | Search recipes by ingredients |
| POST | /api/recipes | Create recipe (Admin only) |
| PUT | /api/recipes/{id} | Update recipe (Admin only) |
| DELETE | /api/recipes/{id} | Delete recipe (Admin only) |
| POST | /auth/login | Login and obtain JWT token |

---

## Running Tests

Run all tests with Maven:

```bash
mvn test
```

The project includes:
- Unit tests
- REST integration tests
- Karate API tests
- Selenium UI tests

---

## API Documentation

A Postman collection documenting the REST API endpoints is included in this repository:

/postman/recipe-api.postman_collection.json

---

## Author

Yu Hang  
Web Technologies Module
