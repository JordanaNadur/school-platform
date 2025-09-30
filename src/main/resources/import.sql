
INSERT INTO students (name, email) VALUES ('Gustavo Farias', 'gustavo@example.com');
INSERT INTO students (name, email) VALUES ('Maria Silva', 'maria@example.com');
INSERT INTO students (name, email) VALUES ('João Souza', 'joao@example.com');

INSERT INTO subjects (name) VALUES ('Mathematics');
INSERT INTO subjects (name) VALUES ('History');
INSERT INTO subjects (name) VALUES ('Science');

INSERT INTO exams (title, subject_id) VALUES ('Math Exam 1', 1);
INSERT INTO exams (title, subject_id) VALUES ('History Exam 1', 2);
INSERT INTO exams (title, subject_id) VALUES ('Science Exam 1', 3);
INSERT INTO exams (title, subject_id) VALUES ('Math Exam 2', 1);

INSERT INTO grades (student_id, exam_id, value) VALUES (1, 1, 9.5);
INSERT INTO grades (student_id, exam_id, value) VALUES (2, 1, 8.0);
INSERT INTO grades (student_id, exam_id, value) VALUES (3, 2, 7.5);
INSERT INTO grades (student_id, exam_id, value) VALUES (1, 4, 10.0);
INSERT INTO grades (student_id, exam_id, value) VALUES (2, 3, 9.0);
INSERT INTO grades (student_id, exam_id, value) VALUES (3, 3, 8.5);
