# School Platform API

This is a Spring Boot application for managing a school platform with students, subjects, exams, and grades.

## Entities

- **Student**: Represents a student with name and email.
- **Subject**: Represents a subject with name.
- **Exam**: Represents an exam with title and associated subject.
- **Grade**: Represents a grade with value, associated student and exam.

## Relationships

- Student has many Grades
- Subject has many Exams
- Exam has many Grades
- Grade belongs to Student and Exam

## API Endpoints

### Students
- GET /api/students - List all students
- GET /api/students/{id} - Get student by id
- POST /api/students - Create student
- PUT /api/students/{id} - Update student
- DELETE /api/students/{id} - Delete student

### Subjects
- GET /api/subjects - List all subjects
- GET /api/subjects/{id} - Get subject by id
- POST /api/subjects - Create subject
- PUT /api/subjects/{id} - Update subject
- DELETE /api/subjects/{id} - Delete subject

### Exams
- GET /api/exams - List all exams
- GET /api/exams/{id} - Get exam by id
- POST /api/exams - Create exam
- PUT /api/exams/{id} - Update exam
- DELETE /api/exams/{id} - Delete exam

### Grades
- GET /api/grades - List all grades
- GET /api/grades/{id} - Get grade by id
- POST /api/grades - Create grade
- PUT /api/grades/{id} - Update grade
- DELETE /api/grades/{id} - Delete grade

## User Stories

1. Como um administrador, quero cadastrar um novo aluno para que ele possa ser matriculado na escola.
2. Como um professor, quero criar uma nova matéria para organizar as provas.
3. Como um professor, quero criar uma prova para uma matéria específica.
4. Como um professor, quero atribuir uma nota a um aluno em uma prova.
5. Como um aluno, quero visualizar minhas notas.
6. Como um administrador, quero listar todos os alunos.

## Running the Application

1. Build the project: `mvn clean install`
2. Run the application: `mvn spring-boot:run`

## Docker

To dockerize the application:

1. Build the jar: `mvn clean package`
2. Build the image: `docker build -t school-platform .`
3. Run the container: `docker run -p 8080:8080 school-platform`

## Access

- Console H2: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
- Swagger UI: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
- Documentation OpenAPI (JSON): [http://localhost:8080/school-platform-api](http://localhost:8080/school-platform-api)
