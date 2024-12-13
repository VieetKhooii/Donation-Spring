# Gabriel
## Donation Website

### Description
By witnessing the cruelty of storms, hurricanes and many tremendous disasters occurring in regions of Viet Nam, the South Central Coast and Northern Central have been suffering the tragedy for many years, now the Red River and neighboring provinces are facing the same fate. We understand their misfortune, but with limited financial resource, we can not afford to provide enough support. Therefore, we created this website in order to help numerous of families be able to manage their expense for recovering from the damage caused by disaster.
### Features:
- User authentication and authorization using JWT with Spring Security.
- Login via Google.
- Forgot password and authenticate via Email.
- Payment integration with VNPay.
- Role-based access control.
- Support questions through AI chat box.
### Technologies Used
This project is built using the following technologies:
#### Back-end:
- **Spring Boot**: Framework for building the back-end of the application.
- **JPA & Hibernate**: ORM (Object-Relational Mapping) framework for interacting with the MySQL database.
- **MySQL**: Database system for storing user and donation information.

#### Front-end:
- **Thymeleaf**: Template engine for rendering dynamic HTML pages.
- **Bootstrap**: Front-end framework for building responsive, mobile-first web pages.
- **AJAX**: For asynchronous web requests, allowing smooth interactions without reloading the page.
- **Chart.js**: JavaScript library for displaying interactive and dynamic charts on the front-end.

#### Authentication and Security:
- **Spring Security**: For securing the application with authentication and authorization features.
- **JWT (JSON Web Tokens)**: For secure token-based authentication.
- **OAuth 2.0**: For implementing Google login functionality.

#### Payment Integration:
- **VNPay**: For integrating payment gateway to handle donations securely.

#### Advanced Features:
- **Google Dialogflow**: Used to integrate an AI-powered chatbox for user interaction.
- **Redis**: For caching and optimizing data retrieval (such as user management and session data).
- **Docker**: For containerizing the Redis service and managing development environment.

#### Other Tools:
- **Maven**: Dependency management and build tool.
- **Git**: Version control system for managing the project's codebase.
### Table of Contents
#### Configuration
Create '.env/ file in project root directory. This file can be used to save any important information, such as information for connecting to database. Here is a concrete examples of the contents of the '.env' file:
    
    SPRING_DATASOURCE_URL=jdbc:mysql://localhost:<your_port>/<database_name>
    SPRING_DATASOURCE_USERNAME=<your_database_username>
    SPRING_DATASOURCE_PASSWORD=<your_database_password>
    
*Make sure that the newly created file name must be the same as the file declared in **application.yml** .*
#### Usage
    1. Run the application.
    2. Navigate to http://localhost:8080 in your browser.
    3. Explore functionalities such as registering, logging in with Google account, using AI chat box and making donations.
    4. Admin features like managing campaigns and monitoring donations can be accessed with admin credentials.
#### Testing
*Ensure the database is correctly set up before running tests. You may need a separate test database.*
#### Collaborators
Below are the GitHub profiles of the project team members:

- [Nguyen Viet Khoi](https://github.com/VieetKhooii)
- [Nguyen Minh Huu](https://github.com/kuzo19032003)
- [Huynh Tuan Dat](https://github.com/axy888)
- [Do Phuoc Hung](https://github.com/dophuochung2428)
####  License
This project is licensed under the MIT License. See the [LICENSE](./LICENSE) file for details.
#### Acknowledgements
This project would not have been possible without the support of:

- The Spring Framework documentation team.
- Inspiration from other charity platforms.

I am grateful for the open-source community and all those who have contributed to these technologies.
### Installation
Clone the repository to your local machine.
   ```bash
   git clone: https://github.com/VieetKhooii/Donation-Spring.git
   ```
Install the sql file gabriel.sql and import this file into your database.