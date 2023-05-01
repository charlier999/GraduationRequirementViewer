# GraduationRequirementViewer
A web application that allows users to search, view, and compare the different programs and courses in a university to your course schedule. This app is currently still in development with sample data from GCU's academic catalog from 2022. 

Click for demonstration \\/

[![Demo](https://img.youtube.com/vi/ohuQ9Zoh8X0/hqdefault.jpg)](https://youtu.be/ohuQ9Zoh8X0)

# Features
- Search and View Academic Program Information
- Search and View Academic Course Information
- Create a schedule of your Course history
- Compare your schedule against different Academic Programs
- Admin: Import new programs and courses through import pages

# Technologies Used
- Java
- Spring Framework Boot
- Spring Boot Security
- Spring Boot Thymeleaf
- MySQL

Why the technologies were used can be found [here](/README/CapstoneDetails.md) along with additional development information.

# Setup
## Prerequisites
- MySQL Server
- Gradle version prior to 8.0

## Installation
1. Clone the repository:
```
git clone https://github.com/charlier999/GraduationRequirementViewer
```
2. Create MySQL Database
    1. Open [Create Gradview Database.sql](/Create%20Gradview%20Database.sql) in a text editor.
    2. On line 243, under the create users table, is an insert command to instert a user. Change the password of the administrator user, *{YOUR PASSWORD HERE}*, to the password you wish to use.
    3. Run the [Create Gradview Database.sql](/Create%20Gradview%20Database.sql) script on your MySQL database.
3. Build Application
```
Run the below command in the cloned repository directory.
./gradlew build
```
4. Run Application
```
Run the below command in the cloned repository directory.
./gradlew bootRun
```
Now when you navigate to http://localhost:8080 you will see home page for the site.


# Additional Information
Additional Information can be found [here](/README/CapstoneDetails.md).

# Guides

## User Guide
### Adding Your Course History
1. Click "Your Program Status" in the navigation bar to be taken to the schedule home.
2. If you already have a portion of you course history still on your web browser, your schedule will be displayed to you. If not, a message saying to import your course history will be displayed instead.
3. Click "Add/Edit Course History" link and you will be taken to the edit course history page.
4. On the top of the page, is your current course history. The bottom of the page contains a fourm for adding your next course to your course history. Using the dropdown menu, select the course you wish to add to your history. 
5. Then check off the checkbox underneth signifying wether you have passed that course or not.
6. Then click submit.
7. Repeat for each course you wish to add to your history.
    
 ### Comparing your Course History Against Programs
1. Click "Your Program Status" in the navigation bar to be taken to the schedule home.
2. If you already have a portion of you course history still on your web browser, your schedule will be displayed to you. If not, a message saying to import your course history will be displayed instead complete the "Adding Your Course History" guide before continuing.
3. Select the program from the dropdown menu to compare against your history.

## Admin Guide
### Signning in as Admin
1. Click login link in navigation bar.
2. Enter in admin credentials that you defined when running the [Create Gradview Database.sql](/Create%20Gradview%20Database.sql).
3. Click Submit.

### Importing Academic Programs
1. Login to the admin account.
2. Click "Import" link in the navigation bar.
3. CLick link to "Import Program" under the page title.
4. Select a program to import into the application from the dropdown box.
5. Click Submit.
6. A link to the imported program and logs from importing are then displayed to you.
   
### Logging Out of the Admin account
1. Click "Logout" in the navigation bar.
