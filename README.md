#Steps:
=====
#1. employee_management
git clone https://github.com/maharshi49/employee.git

#2. Create the database
CREATE DATABASE ems;

#3. Modify as per the credentails
spring.datasource.url=jdbc:mysql://localhost:3306/ems
spring.datasource.username=root
spring.datasource.password=admin123

#4. To run the java application.

#5. Can verify the application using swagger.
http://localhost:<PORT_NUMBER>/swagger-ui/index.html
For eg: http://localhost:8092/swagger-ui/index.html



