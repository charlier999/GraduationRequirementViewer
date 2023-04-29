# GraduationRequirementViewer
A web application that allows users to search, view, and compare the different programs and courses in a university to your course schedule. This app is currently still in development with sample data from GCU's academic catalog from 2022.

[![Demo](https://img.youtube.com/vi/bf2nV8MJbJc/hqdefault.jpg)](https://youtu.be/bf2nV8MJbJc)

# Features
- Search and View Academic Program Information
- Search and View Academic Course Information
- Create a schedule of your Course history
- Compare your schedule against different Academic Programs
- Admin: Import new programs and courses through import pages

## Technologies Used

- Java
- Spring Framework Boot
- Spring Boot Security
- Spring Boot Thymeleaf
- MySQL

## Setup
**Prerequisites**
- MySQL Server
- Gradle version prior to 8.0

**Installation**
1. Clone the repository:
```
git clone https://github.com/charlier999/GraduationRequirementViewer
```
2. Create MySQL Database
    1. Open [Create Gradview Database.sql](/Create%20Gradview%20Database.sql) in a text editor.
    2. On line 243, under the create users table, is an insert command to instert a user. Change the password of the administrator user, *{YOUR PASSWORD HERE}*, to the password you wish to use.
    3. Run the [Create Gradview Database.sql](/Create%20Gradview%20Database.sql) script on your MySQL database.
3. Build Application
Run the below command in the cloned repository directory.
```
./gradlew build
```
4. Run Application
Run the below command in the cloned repository directory.
```
./gradlew bootRun
```
Now when you navigate to http://localhost:8080 you will see home page for the site.
