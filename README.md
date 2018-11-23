# store-api : 20 July 2018

**REQUIREMENT**

Application represent a response to following requirements:
Create an application that shows the 5 closest Jumbo stores to a given position.
You can find a list of (Jumbo in this solution) stores in JSON format attached to this document.
There are just a few rules:
-  It has to be a Java application.
-  It has to be a web application. 
-  It has to have at least an API
-  It has to have HTML/Javascript frontend.
-  Production code quality level as much as possible.
-  One can easily run the application for evaluation purposes.

**END REQUIREMENT**


## Techinical overview:
 - The application is spring-boot microservice, 2.0.3.RELEASE version. 
 - Java 8 is used for development
 
**Frontend** is placed in **src/main/resources/public/index.html file**, represent a front end of the application, so the entire application implementation can be checked visualy using this html, once you run java app. Open this file in browser (hopefully not some ancient version which does not support es6, since es6 syntax is used).

It consists of three areas:
1. Google map, displaying markers for search result
2. Command area, where you can enter/edit search data for each of web service
3. Preview area (below the command one), where in you have displayed tabelr representation of data
In order to facilitate usage, I combined html/js/css into sigle side, just because of simplification.

 
Backend has exposed one REST based webservice class, with several endpoint, each having its own purpose. Store API web service class, consists of the 6 features, which can be
	  provided by service. Some of them are hybrid (since the requirement was just
	  to return closest 5 stores), like one related to closest store location,
	  include also applied business logic for additional parameters like: radiusKm,
	  numberOfStores, which are defaulted by some values (number of stores with 5,
	  as required). <br />
	  Business logic is extened, and implemented 
	  within an application. During the code review, I put a comments through the code, to clariify and explain business I applied into.

Once downloaded, store-api folder and from command line run: 
**mvn package**

(Of course, installed java is needed)
That will compile and build application and run written Junit tests successfully.

From the same location issue:
**java -jar target/store.jar** 

The application will start, just navigate your browser to: *http://localhost:6789/store-api*, and enjoy!
