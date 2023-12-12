### `Introduction`:
Write a program in Java or Kotlin based on the below requirements. State any assumptions used during the implementation. 
Describe key architectural decisions including any applied design patterns. Please also consider any non-functional requirements such as performance.
Please include all artefacts required to build and run the solution and make this available via Github, Dropbox, GoogleDrive or similar alternative.

### `Palindrome Inspector`
Develop a REST API to accept a request containing a username and text value. 
The program will process the request and return a response indicating whether the value is a palindrome.
A palindrome is a word, phrase, or sequence that reads the same backwards as forwards, such as madam or civic.

### `Requirements`:
1.	Validate the input. Reject values with numbers, spaces and punctuation.
2.	Improve performance by caching previously processed values.
3.	Persist the processed value using the file system or database.
4.	Populate the cache on startup from the persisted values.
5.	Include appropriate tests.

### `Running the application`:
I have used the Spring Boot framework to create this application. You can use the mvn clean install command to build the application using the dependencies in the pom.xml file.

To start the application, you can run the following command in the root directory of the project: mvn spring-boot:run, or you can run the main method in the LoweConexApplication class.

To run the tests, you can run the following command in the root directory of the project: mvn test.

To connect to the database, you can use the H2 console at http://localhost:8080/h2-console. The JDBC URL is jdbc:h2:file:~/TechChallengeDB/LoweConexDB. The username is sa and the password is left blank.

__IMPORTANT!!!__: The database will be created in the home directory of the current user. If you want to change this, you can change the JDBC URL in the application.properties file.
You can set the database to be an in-memory database by changing the JDBC URL to jdbc:h2:mem:LoweConexDB. This will create the database in memory and it will not persist between application restarts.
If you proceed with the file-based database, ensure that you delete the folder after you are finished with the application. The folder will be located in the home directory of the current user.

If you want to make requests to the application, you can use the following endpoint: http://localhost:8080/checkPalindrome?text={YOUR_TEXT_HERE}. This endpoint accepts a GET request with a query parameter called text.
I would recommend using Postman to make requests to the application. An example request is: http://localhost:8080/checkPalindrome?text=racecar. This will return a JSON response with the result of the palindrome check.

You can also use the command line to make requests to the application. An example curl request is: curl -X GET "http://localhost:8080/checkPalindrome?text=racecar" .
You can also hit the delete endpoint to clear the database. This endpoint is http://localhost:8080/clearDatabase. This will delete all entries in the database.

### `Design principle - Separation of concerns`
Separation of Concerns is a concept that suggests breaking a program into distinct sections, each addressing a specific concern or responsibility. 
The goal is to improve maintainability, readability, and modularity by isolating different aspects of the application.

### `Validation`
For validation, I have created a helper function that uses a regular expression to check that the input string contains only letters (String regex = "^[a-zA-Z]*$";)
The main method that saves the result to the database calls this helper function to validate the input string before checking if the input string is a palindrome, and then saving it to the database.
If the input string is not valid, the method immediately returns false.


### `Caching`
I used spring boots caching (@Cacheable) to cache the results of the palindrome check.
If the user enters a string that has already been checked, the result will be returned from the cache instead of being computed again.

### `Persistence`
I have assumed that the application will be small scale and will not require a large database. The database will be migrated to a larger database if the application is scaled up such as PostgreSQL.
Because of these assumptions, I decided to use an in-memory database (H2) for this project as it supports SQL and is easy to set up and use, and would allow for easy migration to a larger database in the future. 

The database is created when the application starts up and was initially destroyed when the application shuts down.
However, I have configured the database to persist between application restarts by adding the following lines to the application.properties file: 
spring.datasource.url=jdbc:h2:file:~/TechChallengeDB/LoweConexDB;DB_CLOSE_ON_EXIT=FALSE; 
spring.jpa.hibernate.ddl-auto=update

This means that the database will not be destroyed when the application shuts down, and will be re-used when the application starts up again. 
The database will write to a file called LoweConexDB in the home directory of the current user.

I have added a method to clear the database (clearDatabase()) so that the database can be cleared and re-created when the application starts up.

I have also created a initializeCache() method in the palindromeService class that populates the cache with the contents of the database when the application starts up.


### `Integration Testing`
An integration test should test multiple components working together.

I have created an integration test that tests the caching behaviour of the checkPalindrome() method in the palindromeService class. While it only tests a single method,
it also interacts with the CacheManager and cache to ensure that multiple classes work together as expected.

I have also created an integration test to simulate a GET request to the /checkPalindrome endpoint. This test involves the integration of the controller and service classes as well
as the spring mvc framework itself to check the behaviour of the system as a whole when a request is made to the endpoint.


### `Unit Testing`
A unit test should test a single method or component in isolation.

For unit testing, I use the arrange, act, assert pattern. I have created a unit test that directly tests the behaviour of the isPalindrome() method in the palindromeService class.
This unit test uses the mockito library to create mock objects for the jdbcTemplate and cacheManager. The mock objects are then injected into the palindromeService class.
The test then uses a series of assertions to check that the method behaves as expected.

### `Manual Testing`
The application was also extensively manually tested by running the application and making requests to the /checkPalindrome endpoint using Postman, as well as 
checking the database to ensure that the results were being saved correctly. I also manually tested the clearDatabase() method to ensure that it was working as expected.

I was able to test the caching behaviour by clearing the database and then making requests to the /checkPalindrome endpoint. I could see that the results were not being saved to the database,
because the database was empty, but the results were being returned from the cache.

### `Logging`
For logging I have used the SLF4J and Logback libraries. SLF4J allows for use of any framework at runtime, while using the SLF4J API in the application code. 
You can see an example of logging in the clearDatabase() method in the PalindromeService class. The actual logging is done by Logback. 
In the application.properties file, I have set the logger to write to a file called palindrome.log. This file is located in the root directory of the project.

### `Performance`
When considering the performance of the application, I thought about caching and persistence. I have used spring boots caching (@Cacheable) to cache the results of the palindrome check so that the 
database does not have to be queried every time a palindrome check is performed. If the user enters a string that has already been checked, the result will be returned from the cache instead of being computed again.

I also decided on using H2 as it is lightweight and fast. Based on the requirements, I have assumed that the application will be small scale and will not require a large database, so H2 
would be a good performance choice. If the application is scaled up, the database can rather easily be migrated to a larger database such as PostgreSQL.

Based on the requirements, I know that the valid input string will only contain letters, so I did not need to worry about numbers, spaces and punctuation etc. 
Because of the simple validation requirements, and the assumption that the input strings should all be fairly small, I decided that the most performant 
way to handle the validation was to use a regular expression.
I also decided on using a stringbuilder to reverse the input string as it is faster than using a for loop. 

### `Future considerations`
If I had more time, I would have liked to add more unit tests and integration tests. I would also have liked to add more logging to the application.
Containerization would also be a good idea to make the application more portable and easier to deploy. 
Migration to a larger database such as PostgreSQL or MongoDB would also be a good idea if the application is scaled up.
Environment variables could also be introduced for secruity and configuration between environments e.g database credentials.
CI/CD pipeline could also be introduced to automate the build, test and deployment process.

### `If I was to do the project again`
I would look into using Redis as a database and cache as it has some of the same benefits as H2 but could be more performant.
I would also like to write a few more unit tests and integration tests, particularly for testing that database interactions are working as expected. These interactions
were mostly tested manually during development.
