## Technologies Used
### Java
Java is a programming language for developing enterprise-level applications due to its object-oriented nature, cross-platform compatibility, and vast community support.
### Spring Framework Boot
Spring Framework Boot is a framework built on top of Java that provides a comprehensive and flexible infrastructure for developing web applications. It includes features such as dependency injection, aspect-oriented programming, and integration with various data sources.
### Spring Boot Security
Spring Boot Security is a module of the Spring framework that provides security features to web applications, including authentication, authorization, and secure communication. It makes it easy to configure and implement security in a web application.
### Spring Boot Thymeleaf
Spring Boot Thymeleaf is a template engine that integrates with Spring Boot, providing a powerful and flexible way to create dynamic HTML content. It supports server-side rendering and provides a wide range of features, including expression evaluation, layout templates, and more.
### MySQL
MySQL is an open-source relational database management system (RDBMS) that provides a scalable, reliable, and fast way to store and retrieve data. It is widely used in web applications and provides many features, including ACID compliance, data encryption, and replication.

## Best Practaices/DevOps Used
- Model-View-Controller Architecture
- Keep it simple
- Version Control
- Continous Integration
- Logging

## Deployed Where?
The application is currently hosted localy on the host's computer/server.

## New Technologies Learned
- Ajax in Javascirpt
```
Ajax was learned in order to apply a temporrary fix to layout templates for thymleaf not being applied to views. (ex. The navigation bar is rendered using ajax)
```
- Google Charts
```
Google charts was learned due to previously selected chart generation tool not supporting the chart layout desired for accademic program course charts.
```
- Storing Data in Client Local Storage
```
Storing the amount of data for each perspective user's course history is storage heavy and costs a great deal when hosting the database. To solve this, the user's course history is stored in the client web browser's local storage instead.
```

## Technical Approach
### Identifing the Problem
The problem that needed to be solved was the confusion about what is required to graduation a accademic program. To solve this, the solution must:
- Allow users to have a eaiser understanding of the graduation requirements for the an accademic program.

### Architecture
The architecture consisted of three main components:
#### Database

The database needed to store all of the course and program information and allow access to the server to the information stored. 
![ER Diagram](./ER%20diagram.png)

#### Logic
The logic of the project is contained in the different services, data access objects, and controllers. The data access objects take data from the database and pass it to the services the handle the logic that is to be applied to the data. The logic applied to the data in the services and passed to the controllers to be added to the views.
![Data Paths](./ProjectDrawings-System%20Structure.png))


#### Views
The views display the information from the controller to the user.

### Implementing Solution
The main strategy for implementing the solution for this project, is to complete features from bottom up, database, logic, then views. This is further shown in the lack of view decorations and being strait html tags.

## Chalenges 
1. Academic catalog not in a parseable format
```
The academic catalog being used for example data for the project was not in a format to easily parse into useable data due to pdf-to-txt tool being used had missing portions of the catalog and artifacts of incorect conversion in the output.
To solve the issue, the data from the acadmic catalog was hand formated from pdf into a txt format the import tools can use.
```
2. Storing user course history
```
The inital design for haneling the user's course history was to have the database tables store the course history. This inital design did not scale cheeply when new users are made. 
To solve the issue, the web browser's local storage was used to store a JSON of the user's course history. This allowed for the removal of the public users system and thier related tables in the database.
```

## Outstanding Issues
- The user cannot remove courses from their course history
- The compare course history to program page defaults to one of the dropdown options despite another option being selected
- If two programs being imported have the same course in thier import file, a duplicate course will exist.
- Most of the user input fields are unsantized.
- Lack of any view decorations
