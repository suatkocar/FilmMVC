# FilmMVC (HTTP/MVC Web Application)

## Project Overview
FilmMVC is a film management system developed using Java Servlets and JSP. It includes CRUD operations and external API integration.

## Features
- Add, edit, delete, and list films
- Search films by various criteria
- Integrate with external APIs for additional film information

## Technologies Used
- **Backend:** Java, Servlets, JSP
- **Database:** MySQL
- **Libraries:** Gson, Jakarta XML Bind
- **Build Tool:** Apache Maven

## Getting Started

### Prerequisites
- Java Development Kit (JDK) 8 or later
- Apache Maven
- MySQL Server

### Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/suatkocar/FilmMVC.git
Navigate to the project directory:

bash
Kodu kopyala
cd FilmMVC
Set up the database:

Create a MySQL database named filmdb.
Execute the SQL script located at src/main/resources/createfilms.sql to create the necessary tables.
Configure the database connection in WEB-INF/web.xml:

xml
Kodu kopyala
<context-param>
   <param-name>jdbcUrl</param-name>
   <param-value>jdbc:mysql://localhost:3306/filmdb</param-value>
</context-param>
<context-param>
   <param-name>jdbcUser</param-name>
   <param-value>your-username</param-value>
</context-param>
<context-param>
   <param-name>jdbcPassword</param-name>
   <param-value>your-password</param-value>
</context-param>
Build the project:

bash
Kodu kopyala
mvn clean install
Deploy the FilmMVC.war file to your servlet container (e.g., Apache Tomcat).

Usage
Access the application at http://localhost:8080/FilmMVC.

Project Structure
plaintext
Kodu kopyala
FilmMVC
├── build
├── deploy
├── src
│   ├── main
│   │   ├── java
│   │   │   ├── controller
│   │   │   ├── dao
│   │   │   ├── listener
│   │   │   └── model
│   │   ├── resources
│   │   └── webapp
│   │       ├── META-INF
│   │       ├── WEB-INF
│   │       ├── audio
│   │       ├── css
│   │       ├── images
│   │       ├── js
│   │       └── json
└── README.md
License
This project is licensed under the MIT License - see the LICENSE.md file for details.

Contact
For any inquiries, please contact Suat Kocar at suatkocar.dev@gmail.com.
