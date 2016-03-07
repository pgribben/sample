# Prime Numbers MicroService

## Configuring the Service

Create a service configuration file (e.g. 'primes.yaml'). See file test/java/resource/primes.yaml for an example. As a minumum specify the following:
   
> 	maxPrimesCount: [default max result size as int]
>     primesAlgoId: [algoid: value 1,2 or 3]

Example:
> 	maxPrimesCount: 1000
>     primesAlgoId: 1


Where the value for algoid identifies the prime computation implementation to use:

* 1: specifies the services.PrimesServiceAlgo1 prime computation implementation. This uses a single-threaded seive algorithm to identify prime numbers.
* 2: specifies the services.PrimesServiceAlgo2 prime computation implementation. This uses a single-threaded iterative algorithm to identify prime numbers. 
* 3: specifies the services.PrimesServiceAlgo3 prime computation implementation. This uses a multi-threaded iterative algorithm to identify prime numbers. 
  
## Starting the Service:

Start the REST service by executing the command:
       
> 	java -jar primeservice-1.0-SNAPSHOT.jar server primes.yaml

## Using the service

Using your browser run any of the following services:

### Validate Primes
* App URL: /primes/{integer}/valid
* Example 1: 
  * Request: /primes/2001/valid
  * Response: false
* Example 2:
  * Request: /primes/9901/valid
  * Response: true

### Primes within domain set of integers
* App URL: /primes/bounded
* Parameters:
  * start: First value in range
  * end: Last value in range
  * maxResultSize: (optional) maximum number of primes to be returned
* Example 1:
  *  /primes/bounded?start=20&end=2000

## Building the project
To build, using Java 8 JDK and Maven 3, run the following command from the sample folder (containing the pom.xml file):
> 	mvn package

## Code Structure:

The project uses Drop Wizard to implement a simple REST service end point. All of the external dependencies and build directives are held in the project's pom.xml file.
The logical structure is set out in the model package, and comprises a service component 'PrimesService', a REST resource 'PrimesResource' and a return type value object 'IntList'. The resource uses the service component to resolve the client request.
All components are constructed via the Google Guice IOC.
  
