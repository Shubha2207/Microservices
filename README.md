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

[12 Factos App - Idel Microservices Architecture](https://12factor.net/)


### 1. Communication and Service Discovery

+ Why hardcoded URLs are bad??
changes in url, requires code updates
Dynamic URLs in the cloud
Load Balancing
Multiple Environments

+ Discovery Server
When application starts, it registers itself in discovery server registry.
if consumer service will go to discovery-server to get the address of producer service and then make a it is client side service discovery
if cosumer service provides message to discovery-server and then discovery-server passes that to producer service then it's server-side service discovery.

Spring Cloud uses client-side-service-discovery
Technology: Eureka
Netflix OSS products used for microservices are Eureka, Ribbon, Hysterix, Zuul

To make spring-application to work as a Eureka server, we need to add eureka-server dependency in pom and add @EnableEurekaServer annotation to main class. And other microservices will be annoted with @EnableEurekaClient annotation.

+ Common Issues

> Could not transfer artifacts from/to central maven repo
Try force refreshing your dependencies. Specifying -U does that

```cmd
mvn clean install -U
```

> Cannot resolve org.springframework.cloud:spring-cloud-starter-netflix-eureka-server:unknown

```xml
<!-- add version to the dependecy -->
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
  <version>2.2.10.RELEASE</version>
</dependency>
```

or add dependencyManagement tag below dependencies

```xml
<dependencyManagement>
  <dependencies>
    <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-dependencies</artifactId>
      <version>${spring-cloud.version}</version>
      <type>pom</type>
      <scope>import</scope>
    </dependency>
  </dependencies>
</dependencyManagement>
```

```xml
<properties>
  <spring-cloud.version>2021.0.5</spring-cloud.version>
</properties>
```

And reload the project 
> right click -> maven -> reload project

Check the Compatiblity between Spring Cloud and Spring Boot [here](https://spring.io/projects/spring-cloud)

> Eureka server register to it's own registry
Add following properties
```properties
eureka.client.register-with-eureka=false
eureka.client.fetch-registry=false
```

+ @LoadBalanced Annotation
the bean we created for resttemplate will call the rest service.
since we have configured eureka server, it would first go to eureka servier and get the address of target service.
though it's a client-side-service discovery, the resttemplate provides an abstraction over this and it seems like server-side-discovery is going on.
So to enable this we need add @LoadBalanced annotation to resttemplate and simply call the service with it's name.


### 2. Fault Tolerance and Resilience

Fault tolerance refers to the ability of a system (computer, network, cloud cluster, etc.) to continue operating without interruption when one or more of its components fail.

Resiliency is the ability of a server, network, storage system or an entire data center to recover quickly and continue operating even when there has been an equipment failure, power outage or other disruption.

fault tolerant service suffers no down time even if the machine it is running on crashes, whereas the potential data fault in a fault resilient service counts toward down time.

+ Issue with microservices

1. Microservice instance goes down
-> Solution: have multiple instances of that microservice. Client side load balancing can be done using Ribbon

2. Microservice instance is slow
-> if any one microservice is slow then it will affect the other services as well. eg. requests come for slow microservices will use all the threads causing requests for other microservices to wait until thread get available.
Solution: set timeouts so that threads won't be occupied by slow services.
Setting timeouts on spring rest-template.

```java
public RestTemplate getRestTemplate(){
		HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
		clientHttpRequestFactory.setConnectTimeout(3000); // setting up connection timeouts of 3s for resilience
		return new RestTemplate(clientHttpRequestFactory);
	}
```

This will solve the problem partially. But if the request rate to too high then we will see performance issue again

Correct Solution  

**Circuit Breaker Pattern**

Detect if something is wrong with service -> 
take temporary steps to avoid sending requests ->
deactivate problem component so that it doesn't affect downstream components -> 
once service is back again, try sending requests

When does the circuit trip??
- last n requests to consider for the decision
- how many of those should fail??
- timeout duration

When does the circuit get back to normal flow??
- how long after a circuit trip to try again??

eg.
no of requests to consider=5
how many should fail=3
timeout to consider a request as fail=2s
how long to wait before next try=10s

Circuit is tripped, Now What if request comes in?
Setup a fallback mechanism
Return a fallback default response or save a previous responses in cache and use that if possible

Why circuit breaker??
- failing fast
- fallback functionality
- automatic recovery

Tool
**Hystrix** - 
open source project, created by netflix
it implements circuit breaker pattern, you just have to provide the configuration params

+ Hystrix

Add dependency spring-cloud-starter-netflix-hystrix
Add annotation to main class @EnableCircuitBreaker
Add annotation to method that needs circuit breaker @HystrixCommand and provide fallback method as argument
Configure Hystrix behaviour by passing commandProperties argument to @HystricCommand which includes timeout value, no of requests, no of failed request percentage, sleep window.

How does Hystrix work??
- hystrix wraps the class with @EnableHystrix annotation in proxy-class
- so hystrix monitors and acts as a proxy to all the requests comming to that method with @HystrixCommand annotation
- hystrix constantly monitors and redirect to fallback method if circuit breaking happens

Hystrix will not work if you try to implement logic in which you're calling another method in same class and expect to fallback that particular method during circuit breaking.
Because here proxy is not checking internal method calls. the call has to go from one class method to another class method

+ Hystrix Dashboard
Add dependency spring-cloud-starter-netflix-hystrix-dashboard and spring-boot-starter-actuator
Add @EnableHystrixDashboard annotation to main class
Add this property in application.properties to get the hystrix stream for dashboard
> management.endpoints.web.exposure.include=hystrix.stream

+ Bulkhead Pattern
Having separate thread-pools for each service. So even if a service is slow it will not hold the threads for other services. And other service will work fine and will not be affected.

you can implement bulkhead-pattern using @HystrixCommand as follows

```java
@HystricsCommand(
  fallbackMethod = "getFallbackCatalogItem",
  threadPoolKey = "movieInfoPool"
  threadPoolProperties = {
    @HystrixProperty(name = "coreSize", value = "20"),
    @HystrixProperty(name = "maxQueueSize", value = "10")
  }
)
```

threadPoolkey refers the pool that will be assigned to that method
coresize is max no. of threads in pool
maxQueueSize is max no. of request that can wait in queue before passing to thread


### 3. Microservices Configuration

What and Why??
Suppose your app is connecting to db, so it would make more sense to keep all the configuration like db connection string, username, password in separate file than hard coding it.

Examples of configuration
DB connections, credentials
Feature flag -> configure some features on app for certain duration or conditionns
Business logic configuration parameters
Scenario testing -> configuration for traffic management
Spring Boot Configuration -> configuration for hystrix, eureka, tomcat server etc

Tools for configuration with spring boot:
properties, yaml, json files

Benefits:
Externalized configs
Env Specific configs
consitent configs among all the microservices
version history
real time management of configs without stopping the app

+ application.properties

Use @Value annotation to get values from property file

// application.properties
```properties
key=this is the key value
```

```java
@Value("${key}")
private String key_from_property;
```

You can reuse values in application.properties file

```properties
key1=value1
key2=value of key1 is ${key1}
```

Using application.properties helps us to keep all config in single place and avoiding harcoding but still the configs are not externalized from codebase. If we have to update the config, will have to rebuild the entire project since property file is also part of app jar.

+ Use external property sources

You can create external application.properties file in target folder, so when you run the jar, the properties of inner application.properties file will be overwritten by external application.properties file.

You can overwrite the property of external application.properties by passing property in command line.

```cmd
java -jar application-0.1.0-snapshot.jar --server.port=9090
```

+ @Value annotation tricks

1. Assign value to variable

```java
@Value("this is string value")
private String str;
```

2. Set default value for property

if api.key is not present in property file, use default value provided

```java
@Value("${api.key: default_value}")
private String str;
```

3. Get list of values from property file

```properties
app.list.values=abc,xyx,pqr
```
```java
@Value("${app.list.values}")
private List<String> list;
```

4. Get nested key values

```properties
dbconfigs={connectionString: 'https://dburl.com', userName: 'app_login', password: 'login@123'}
```

We will use #, so that spring will conside the dbconfigs as a expression also known as spring-expression-language

```java
@Value("#{${dbconfigs}}")
private Map<String, String> dbconfigs;
```

+ @ConfigurationProperties Annotaiton

retrieve group of configs at once from property file.

```properties
db.connectionString=https://dburl.com
db.userName=app_login
db.password=app_pass
db.port=5671
```

All these properties can be grouped as db properties. Hence we can create a bean for them and use the bean in controller class

```java
@Configuration
@ConfigurationProperties("db")
public class DbSettings{
  private String connectionString;
  private String userName;
  private String password;
  private int port;

  // constructors 
  // getters and setters
}
```

```java
@Autowired
private DbSettings dbSettings;
```

all values from property file would be in the dbSettings object and you can retrieve using getters

+ Spring Boot Actuator

To get the details of configuration used by application, add this property to enable actuator to do that

```properties
// Expose all the management endpoints
management.endpoints.web.exposure.include=*
```

GET > localhost:8080/actuator/configprops

+ Configuration management in yaml file

yaml has benefits of nesting

> application.properties

```properties
app.name=MyApp
app.port=8081
app.image.repo=docker
app.image.tag=0.1.0
```

Repetitive keys in the property with .properties file.

> application.yaml

```yaml
app:
  name: "MyApp"
  port: 8081
  image:
    repo: "docker"
    tag: "0.1.0"
```

Use spaces for indentation instead of tabs

+ Spring Profiles

```yaml
spring.profiles.active: test
```
default profile is always active profile, so you can put this property in default property file to tell spring that what the active profile really is
eg. application-<profile>.extension

you can also provide this value in cli
> java -jar app-snapshot.jar --spring.profiles.active=test

you can also restrict spring from initializing bean by adding @Profile annotation.
eg. @Profile("dev") annotation on bean class allows the class to be instantiated only for dev profile

+ Environment Object [Not Recommended]

Spring provides Environment class which consists for methods which can provide details regarding environment

```java
@Autowired
private Environment env;

@RequestMapping("/envDetails")
public String getEnvDetails(){
  return env.toString();
}
```

+ **Spring Cloud Config Server**

Config as a microservice
Tools: Apache Zookeeper, ETCD, Hashicorp Consul, Spring Cloud Config Server

Spring Cloud Config Server can connect to Git-Repo/SVN/Hashicorp Vault to fetch the properties.

So without rebuilding the application, we can upate the configs, we just have to push the new configs to git repo and config server will fetch new configs.
Also provides version control since configs are stored in git repo.

To create config-server

Add spring-boot config-server dependency.
Add @EnableConfigServer annotation to main class.
Add spring.cloud.config.server.git.uri property, and provide the location of property file either local or remote git repo.
Also provide the branch name details spring.cloud.config.server.git.default-label=master

Access the config-server using below url
> http://localhost:8888/<file-name>/<profile>

Even if you make any change to property file in git-repo, you don't have to restart config-server. It's always fetch the latest props

To create cloud config-client

Add spring-cloud-starter-config dependency
Add the location of spring cloud config server in property file
spring.cloud.config.uri=http://localhost:8888

When a microservice gets start, it will make call to config-server and retrieve all the props.

if you want to add service specific props, then name the props file by the name of that microservice. This will allow us have props which service specific eg. server port etc.

+ Dynamic Configs Update for cloud-config-client

In case of cloud-config-client, the props don't get updated for client even if config-server is always updated.
So to get the latest props without restart the service, 
Add Actuator dependency, which provide an endpoint to which when we make post call 

> POST http://localhost:8083/actuator/refresh

it refreshes all the configs for particular bean which annoted with @RefreshScope annotation

+ Security for configurations

use Spring security with spring-cloud-config-server, so that only authorized services get access to configs.
use /encrypt and /decrypt functionality of config-server which work with JCE(java cryptography extension), to add or fetch the configs from git securely

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

Use @Value annotation, that way if in future there is any chage, you just have make update in application.properties file