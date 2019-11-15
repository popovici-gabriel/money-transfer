# money-transfer
Money Transfer Application
Backend Test 
Design and implement a RESTful API (including data model and the backing implementation) for money transfers between accounts. 
Explicit requirements: 
1. You can use Java or Kotlin. 
2. Keep it simple and to the point (e.g. no need to implement any authentication). 
3. Assume the API is invoked by multiple systems and services on behalf of end users. 
4. You can use frameworks/libraries if you like (except Spring), but don't forget about 
requirement #2 and keep it simple and avoid heavy frameworks.
5. The datastore should run in-memory for the sake of this test. 
6. The final result should be executable as a standalone program (should not require a 
pre-installed container/server). 
7. Demonstrate with tests that the API works as expected. 

Implicit requirements: 
1. The code produced by you is expected to be of high quality. 2. There are no detailed requirements, use common sense. 
Please put your work on github or bitbucket. 

## API 
Application starts using a CXF simple server on localhost port 8080 An H2 in memory database. 
For this exercise the Accounts are not stored in DB. 
Reason for doing so is due to exercise. 
ACID operations could have been isolated in a SQL batch like this:
<p>
<code>
BEGIN 
SELECT ...FOR UPDATE 
SELECT ...FOR UPDATE 
UPDATE ACCOUNT 1 
UPDATE ACCOUNT 2 
COMMIT
</code>
</p> 
This exercise is focused on using the mutex Lock in JDK to achieve the same 
ACID as in a Database.  


### REST API 

| HTTP METHOD | PATH | USAGE |
| -----------| ------ | ------ |
| GET | /users/{userName} | get user by user name | 
| POST | /users| create user | 
| GET | /accounts/{accountId} | get account by accountId | 
| POST | /accounts | create a new account
| GET | /accounts | read all accounts 
| DELETE | /accounts/{accountId} | remove account by accountId | 
| PUT | /accounts/{accountId}/debit/{amount} | withdraw money from account | 
| PUT | /accounts/{accountId}/credit/{amount} | deposit money to account | 
| POST | /transfer/{from}/{to}/{amount} | perform transaction between 2 user accounts | 


- 200 OK: The request has succeeded
- 400 Bad Request: The request could not be understood by the server 
- 404 Not Found: The requested resource cannot be found
- 500 Internal Server Error: The server encountered an unexpected condition 

### Scripts 
Please find start.sh and run.sh for usages on how to use the API 

