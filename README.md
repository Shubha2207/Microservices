## Learn Microservices - Spring Boot

Monolith vs Microservice
monolithic application is a single unified unit, an architecture breaks it down into a collection of smaller independent units.

Pattern vs technologies
Pattern -> eg. make microservices work well together ie service-discovery
Technology -> libraries and frameworks to solve common problems example Spring-cloud

Service-oriented architecture (SOA) is a 
method of software development that uses software components called services to create business applications.
Each service provides a business capability, and services can also communicate with each other across platforms and languages.
use SOA to reuse services in different systems or combine several independent services to perform complex tasks.

Java Frameworks for Microservices
Spring Boot, Oracle Helidon, AxonIQ, DropWizard, Quarkus


### Communication and Discovery


### Application Details
JS developer requires an API that gives the list of all the movies the rating and short description of movie that the person watched
e.g. 

api -> https://movie-catalog.com/shubham0123

response ->
```yaml
{
  id: "shubham0123",
  movies: [
    {
      id: "",
      name: "",
      description: "",
      rating: ''
    },
    {
      id: "",
      name: "",
      description: "",
      rating: ''
    }
  ]
}
```

#### Microservices
1. Movie Catalog Service
Input: UserId
Output: Movie list with details
2. Ratings Data Service
Input: UserId
Output: MovieIds and ratings
3. Movie Info Service
Input: MovieId
Ouput: Movie Details


#### Data Flow
1) User will call movie-catalog-service by providing userID
2) movie-catalog-service calls rating-data-service and get the list of movieIDs and their rating that the user watched
3) once movie-catalog-service get the list of movieID from ratingd-data-service, it call movie-info-service for each movieID and get the description of that movie


#### Create 3 SpringBoot Applications

+ Ways to create springboot apps
1. Using maven and adding the required dependency
2. using spring cli
3. using spring initializer/start.spring.io

#### Creating RestController

Create a folder controller and add the controller class in it.
Add @RestController annotation to that class to enable it to handle rest calls
Add @RequestMapping("</api>") to response to particular api
Crate a function to provide response to that incoming rest call and add @RestMapping annotation to it.
If we are passing path-variable in request then it has to be in curly braces and while passing it to the function use @PathVariable("<var-name>") in function parameter
If we are return any custom entity then create class for that and put it into model folder.


#### Rest Template vs Web Client

RestTemplate uses Java Servlet API and is therefore synchronous and blocking.
Conversely, WebClient is asynchronous and will not block the executing thread while waiting for the response to come back. The notification will be produced only when the response is ready.

RestTemplate is deprecated since Spring 5.

We are using RestTemplate object to make rest calls to other services.
everytime we make a call a new object will be created, to avoid this let's create bean for that class and 
use Autowired annotation whereever you need to use the restTemplate object.
@Bean is a producer and @Autowired is consumer

#### Mono vs Flux

Mono and Flux are both implementations of the Publisher interface. In simple terms, we can say that when we're doing something like a computation or making a request to a database or an external service, and expecting a maximum of one result, then we should use Mono.

When we're expecting multiple results from our computation, database, or external service call, then we should use Flux.

#### @Value annotation

if you need to take value from application.properties file.

```properties
rating-data-service.base.url=http://localhost:8083/ratings/users/
```

```java
@Value("${rating-data-service.base.url}")
    private String RATING_DATA_SERVICE_URL;
```

User @Value annotation, that way if in future there is any chage, you just have make update in application.properties file