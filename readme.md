## Restaurant Voting API(without frontend) ##

Voting system for deciding where to have lunch.

 * 2 types of users: admin regular users
 * Admin can input a restaurant and it's lunch menu of the day (2-5 items usually, just a dish name and price)
 * Menu changes each day (admins do the updates)
 * Users can vote on which restaurant they want to have lunch at
 * Only one vote counted per user
 * If user votes again the same day:
    - If it is before 11:00 we assume that he changed his mind.
    - If it is after 11:00 then it is too late, vote can't be changed

Each restaurant provides new menu each day.

----------
###Applied technologies stack:

1. **Apache Maven** is used to build, execute tests, and package `voting-api` into a `.war` file.
1. **Tomcat 8** 
       * Embedded servlet container with **Maven Cargo Plugin**
       * Configuration used Java config **without web.xml** 
1. **Spring framework** the core framework provides dependency injection in application.
1. **Spring MVC** provides structure required to create REST endpoints and serve these endpoints over HTTP.   
       * Mapping Requests with Content-type: `application/json`
       * Validation request body
       * Obtaining request data from service layer
       * Generating responses in Content-type: `application/hal+json`
       * Exception handling
1. **Spring Data JPA** used for the implementation of the persistence layer.
       * as JPA Provider is used **Hibernate**
       * as database is in-memory **HSQLDB** which gets populated at startup with data
       * as Hibernate Second Level cache providers is **EHCache**(second level and query level with strategy `NONSTRICT_READ_WRITE`)     
1. **Spring Security** to authenticate and authorize users by using information from the database
      * Application supports role-based access control with roles ROLE_USER and ROLE_ADMIN  
    
                User login: user1@yandex.ru
                  password: password1          
        
               Admin login: admin@mail.ru
                  password: admin
            
1. **Spring HATEOAS** is used for creating links pointing to Spring MVC controllers is build 
   up resource representations, and control how they're rendered into supported hypermedia formats `HAL`.
1. **SLF4J** for logging(Console logging with level `debug`).
1. **Testing**
      * **JUnit 4**
      * **Spring Test** is used for to integrate Spring with JUnit and created MockMvc objects for testing and validation requests on a specific endpoint
      * **Hamcrest** is used for for writing assertions on the response and use variety of Matchers to validate and expect the response.
      * **JsonPath** is used for access to the response body in format JSON  returned by controllers and asserting the value found at the JSON path with hamcrest Matchers.      

   


### Run(application deployed in application context path "/voting-api"")

##### 1. Deploy project in embedded Tomcat8 using  Maven Cargo Plugin(from directory where pom.xml).

This command will download the tomcat 8.5.20 version and deploy **voting-api.war** into it.
        
    $ mvn clean verify org.codehaus.cargo:cargo-maven2-plugin:run

##### 2. Deploy project in non-embedded Tomcat 8(from directory where pom.xml).
         
    $ mvn clean package    

then copy  **voting-api.war** from target folder into the Tomcat CATALINA_HOME/webapps/ directory and run Tomcat.

----------

### Test RootController

#### Access unauthenticated
 
**Returns link's information about all controller's base endpoint for navigate:**

     curl -s http://localhost:8080/voting-api/rest

----------

### Test AdminUserController

**find user with id=100:**

     curl -s http://localhost:8080/voting-api/rest/admin/users/100 --user admin@mail.ru:admin

**find user by email:**

     curl -s http://localhost:8080/voting-api/rest/admin/users/by-email?email=user1@yandex.ru --user admin@mail.ru:admin find bu email

**find all users:** `paging default page=0, size=20`
    
     curl -s http://localhost:8080/voting-api/rest/admin/users --user admin@mail.ru:admin

**delete user with id=100:**

     curl -s -X DELETE http://localhost:8080/voting-api/rest/admin/users/100 --user admin@mail.ru:admin

**update user with id=101:**

     curl -s -X PUT -d "{\"name\":\"UpdatedUser\",\"email\":\"updated@mail.ru\",\"password\":\"newpassword\",\"roles\":[\"ROLE_USER\"]}" -H "Content-Type: application/json" http://localhost:8080/voting-api/rest/admin/users/101 --user admin@mail.ru:admin

**create user:**

     curl -s -X POST -d "{\"name\":\"CreatedUser\",\"email\":\"created@mail.ru\",\"password\":\"newpassword\",\"roles\":[\"ROLE_USER\"]}" -H "Content-Type: application/json" http://localhost:8080/voting-api/rest/admin/users --user admin@mail.ru:admin
    
**disable user with id=100:**

     curl -s -X PATCH http://localhost:8080/voting-api/rest/admin/users/100/enabled?enabled=false

 ----------

### Test ProfileUserController

**get profile of authorized user:**

     curl -s http://localhost:8080/voting-api/rest/profile --user user1@yandex.ru:password1    

**update profile of authorized user:**
   
     curl -s -X PUT -d "{\"name\":\"UpdatedUser\",\"email\":\"updated@mail.ru\",\"password\":\"newpassword\"}" -H "Content-Type: application/json" http://localhost:8080/voting-api/rest/profile --user user1@yandex.ru:password1   
 
**delete profile of authorized user:**
   
     curl -s -X DELETE http://localhost:8080/voting-api/rest/profile --user user1@yandex.ru:password1       

----------

### Test RestaurantController

#### Access for User     
----------
**find restaurant by id=103:**

     curl -s http://localhost:8080/voting-api/rest/restaurants/103 --user user1@yandex.ru:password1   
  
**find restaurant by name like "grill":** `paging default page=0, size=20`

     curl -s http://localhost:8080/voting-api/rest/restaurants/by-name?name=grill --user user1@yandex.ru:password1 --find by name   

**find all restaurant:** `paging default page=0, size=20`

     curl -s http://localhost:8080/voting-api/rest/restaurants --user user1@yandex.ru:password1   

----------

#### Access for Admin     
----------
**delete restaurant with id=103:**

     curl -s -X DELETE http://localhost:8080/voting-api/rest/restaurants/103 --user admin@mail.ru:admin --delete   

**update restaurant with id=103:**

     curl -s -X PUT -d "{\"name\":\"Updated Restaurant\"}" -H "Content-Type: application/json" http://localhost:8080/voting-api/rest/restaurants/103 --user admin@mail.ru:admin   

**create restaurant:**

     curl -s -X POST -d "{\"name\":\"Created Restaurant\"}" -H "Content-Type: application/json" http://localhost:8080/voting-api/rest/restaurants --user admin@mail.ru:admin   

**create menu for restaurant with id 103:**

     curl -s -X POST -d "{\"date\":\"2018-01-05\"}" -H "Content-Type: application/json" http://localhost:8080/voting-api/rest/restaurants/103/menus --user admin@mail.ru:admin   

----------

### Test MenuController

#### Access for User     
----------
**find menu by id=106:**

     curl -s http://localhost:8080/voting-api/rest/menus/106 --user user1@yandex.ru:password1       

**find all menus:** `paging default page=0, size=20`

     curl -s http://localhost:8080/voting-api/rest/menus --user user1@yandex.ru:password1       

**find menus by date="2017-12-30":** `paging default page=0, size=20`

     curl -s http://localhost:8080/voting-api/rest/menus/by-date?date=2017-12-30 --user user1@yandex.ru:password1       

**find menus by restaurant with id=103:** `paging default page=0, size=20`

     curl -s http://localhost:8080/voting-api/rest/menus/by-restaurant?id=103 --user user1@yandex.ru:password1       

**find restaurant by menu:**

    curl -s http://localhost:8080/voting-api/rest/menus/106/restaurant --user user1@yandex.ru:password1       

----------

#### Access for Admin     
----------
**delete menu with id=106:**

    curl -s -X DELETE http://localhost:8080/voting-api/rest/menus/106 --user admin@mail.ru:admin           

**update menu with id=106:**

    curl -s -X PUT -d "{\"date\":\"2018-01-03\"}" -H "Content-Type: application/json" http://localhost:8080/voting-api/rest/menus/106 --user admin@mail.ru:admin          

**create dish for menu with id 106:**

    curl -s -X POST -d "{\"name\":\"New dish\", \"price\":\"540.20\"}" -H "Content-Type: application/json" http://localhost:8080/voting-api/rest/menus/106/dishes --user admin@mail.ru:admin          

----------

### Test DishController

#### Access for User     
----------
**find dish by id=110:**

    curl -s http://localhost:8080/voting-api/rest/dishes/110 --user user1@yandex.ru:password1     

**find all dishes:** `paging default page=0, size=20`

    curl -s http://localhost:8080/voting-api/rest/dishes --user user1@yandex.ru:password1     

**find dishes by date="2017-12-30":** `paging default page=0, size=20`

    curl -s http://localhost:8080/voting-api/rest/dishes/by-date?date=2017-12-30 --user user1@yandex.ru:password1     

**find dishes by menu with id=106:** `paging default page=0, size=20`

    curl -s http://localhost:8080/voting-api/rest/dishes/by-menu?id=106 --user user1@yandex.ru:password1     

**find menu by dish:**

    curl -s http://localhost:8080/voting-api/rest/dishes/110/menu --user user1@yandex.ru:password1     

----------

#### Access for Admin     
----------
**delete dish with id=110:**

    curl -s -X DELETE http://localhost:8080/voting-api/rest/dishes/110 --user admin@mail.ru:admin     

**update dish with id=110:**

    curl -s -X PUT -d "{\"name\":\"New Dish\", \"price\":\"760\"}" -H "Content-Type: application/json" http://localhost:8080/voting-api/rest/dishes/110 --user admin@mail.ru:admin     

----------

### Test VoteController

**vote for menu with id=108:**

    curl -s -X POST http://localhost:8080/voting-api/rest/vote/108 --user user1@yandex.ru:password1     

**vote for menu with id=109:**

    curl -s -X POST http://localhost:8080/voting-api/rest/vote/109 --user admin@mail.ru:admin     

**get your voice by date="2017-12-30":** `if data parameter isn't specified, returns the current voice`

    curl -s http://localhost:8080/voting-api/rest/vote?date=2017-12-30 --user user1@yandex.ru:password1     
 
**get voting results by date="2017-12-30":** `if data parameter isn't specified, returns the current result`

    curl -s http://localhost:8080/voting-api/rest/vote/result?date=2017-12-30 --user user1@yandex.ru:password1     
 
----------
