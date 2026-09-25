# Online Examination Management System

Features added:
- Login (`student/student` and `admin/admin` work as demo accounts).
- Subject selection and randomized question order.
- Configurable negative marking (default 0.25).
- Result history stored in MySQL.
- Admin panel for adding questions.
- MySQL persistence with an in-memory fallback so the demo still runs without MySQL.

## Run

1. Install MySQL and execute `database/schema.sql`.
2. Add the MySQL Connector/J JAR to the classpath.
3. Change `URL`, `USER`, and `PASSWORD` in `DatabaseManager.java`.
4. Compile and run:

```bash
javac -cp mysql-connector-j.jar *.java
java -cp .:mysql-connector-j.jar OnlineExam
```

On Windows, use `;` instead of `:` in the classpath.
