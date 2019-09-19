# API Testing
## Goal
To automate the following workflows :
- Search for the given user by username.
- Fetch the posts added by the given user.
- Validate the email format in each comment in the comments section.

## Project Breakdown 
### Tests
- src/test/java//SchemaTest.java
  > Consists of the validation of JSON schema for all the endpoints.
- src/test/java//ApiTest.java
  Consists of the flow validations for the requested cases.

### Helpers
- src/main/java//ApiHelper.java
          - Consists of all the HTTP methods that returns the response
          - Response specification to validate status code and content-type for every request.

***NOTE : This will be useful in maintaining the project in a better way. If there is any change in the status code returned by the API, changing the accepted status code in the relevant methods will suffice.***

- src/main/java//BlogHelpers.java
          - Consists of methods that processes the response and returns it in relevant format 
- src/main/java//BlogEndpoints.java
          - Consists of the base URI and the api endpoints required for the test implementation.

### Test Data
- src/test/java//UserBlogData.java
          - Contains the Dataprovider implementaion that returns the test data for the test cases.

***NOTE: This has been implemented in such a way that multiple usernames can also be accepted as input and it will return the respective posts and comments of the given username to perform the email validation.*** 
### Reports
- src/main/java//CommonUtils.java
          - Contains the configuration for the reports to be generated based on the statuses of the tests.
          - Success and Failure scenarios are logged in the respective cases. 
          - Logs contain the name of the test case methods for easier identification.

## Test Execution Steps
- Switch to the project's root directory
- Run the command : `mvn clean test` from the terminal 

## To view the reports
- Switch to the project's /test-output folder
- Open <UserBlogTest.html> file to view in browser  

## Screenshot of reports