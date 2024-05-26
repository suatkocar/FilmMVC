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

2. Navigate to the project directory:

   ```bash
   cd FilmMVC

3. Set up the database:
   
- Create a MySQL database named filmdb.
- Execute the SQL script located at src/main/resources/createfilms.sql to create the necessary tables.

4. Create and configure the config.properties file:

- Copy the example config file:

 ```bash
 cp src/main/resources/config.properties.example src/main/resources/config.properties
  ```

- Edit the config.properties file and set your database and API credentials:

```properties
jdbcUrl=jdbc:mysql://localhost:3306/filmdb
jdbcUser=your-username
jdbcPassword=your-password
apiKey=your-api-key
```

5. Build the project:

   ```bash
   mvn clean install

6. Deploy the FilmMVC.war file to your servlet container (e.g., Apache Tomcat).

Usage
Access the application at http://localhost:8080/FilmMVC.

Project Structure

```plaintext
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
```

License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details.

Contact

For any inquiries, please contact Suat Kocar at suatkocar.dev@gmail.com.
