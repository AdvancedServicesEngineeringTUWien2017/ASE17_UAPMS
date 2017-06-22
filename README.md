# Urban Area Parking Spot Monitoring System
Urban Area Parking Spot Monitoring System. A project for the Course Advanced Services Engineering at TU Wien

## Introduction

This project was developed for the course ‘Advanced Services
Engineering’ at TU Wien. The goal was to devise a scenario that illustrates
important aspects of modern IoT application. Furthermore, another goal of this
project was to identify and implement different data-concerns and to incorporate
analytical services to enable support for elasticity. To this end, we devised a
prototype for a parking spot monitoring system for urban areas. The
decentralized system can incorporate multiple so-called *cluster-masters* that monitor certain sub-areas (e.g. a district of
a city) and provide preprocessed and filtered data over various interfaces
(REST, MoM). In addition, the system contains several higher-level services
that retrieve and process the data of several cluster-masters and make the aggregated
data available over a single interface (instead of querying all
cluster-masters). While a cluster-master only provides information about the
parking spots in its monitored area, the Status Service gathers information
from all available cluster-masters which allows to query the overall state
while still avoiding to centrally store the entire information (it also improves
scalability and avoids a single point of failure). In contrast to the
cluster-masters that only store the current state of the parking spots, the
Statistics Service records all state transitions of all parking spots in the
entire system and persists them in a document based storage. In addition to the
REST interface that allows to query statistical information, the service also
performs analytical tasks, by processing the incoming events via a CEP engine
and performing a MapReduce tasks on the entire dataset to detect peak loads in
the system. 
The last high-level service that can be queried by end-users is the Tour
Service. The service allows a user to query tours between occupied parking
spots. For example, a typical group of users for this service could be the
staff of the department of traffic-management that want to check the parking
tickets of cars more efficient. Of course, this scenario could be extended to
inform the user about changes in the system state (e.g. car on the route has
left) and to re-compute a new route on-the-fly. However, we thought that this
would be far beyond the scope of this class and would most likely be rather
difficult to test. However, we claim that our current architecture would allow
to extend the system by this feature rather easily. 
Finally, to be able to determine QoS & QoD metrics, the system employs an
Evaluation Tool that records incoming requests, the corresponding results and the
response time of the request. Even though we only provide a simple read-only
interface for this data, in a more advanced scenario this data could be used by
other services to determine prices or resource demands (e.g. for on-demand
provisioning). 

The following figure shows a system overview and illustrates the interactions between the different services.  

![img](https://github.com/tkauf15k/ASE17_UAPMS/blob/master/Documents/image.png)


## Data Concerns & Elasticity

As already mentioned previously, one major goal of this project was todevise and implement different data-concerns and to allow for elasticity. 

In general, the *major* data concern used in this project is quality of data. By allowing the user to specify different ranges and time-spans for the queries at all services, the user can influence the quality/granularity of the data (e.g. by queried range in meters) and specify a required confidence of the results (e.g. longer timeline yields more confident statistical results). Since it is inherent in the scenario that the system state changes rapidly and continuously, we claim that the response time is an important QoS metric of all our services.Therefore, we make use of AOP techniques to record the response time of all requests and persist the data at the evaluation tool. While in this scenario the data can only be viewed for informative purposes, more advanced scenario could use the information in various ways. The response time could be injected into the result objects which would allow a client to decide whether the received results are already obsolete (e.g. a request with an unusual high response time of >5s could already be obsolete for a car driving with 130km/hon the highway). Furthermore, other services could also consider this information for pricing models and SLAs enforcement. 

Finally, we introduced elasticity at the tour service by implementing amaster-worker pattern that is controlled by an analytical service. In general,once a tour request is received, the service forwards the job to a set ofworker nodes that process the jobs as soon as possible. On one hand, our systemenables elasticity, by making use of a three-level priority queue that allowsjobs with higher priority to overtake others. On the other hand, our systemallows the user to specify a desired quality level of the solution, that thesystem should try to achieve. To this end, we have implemented three differentsolvers for the Traveling Salesman Problem. While for low quality requests, asimply nearest-neighbor heuristic is used, the system tries to improve such asimple solution with a Simulated Annealing-like meta heuristic for mediumquality requests. For high-quality requests, we make use of aMiller-Tucker-Zemlin MILP formulation that is solved by the IBM CPLEX MILP Solver(it is important to note that this Solver is a commercial product and nosources and libraries are contained in this project. However, the solver isavailable on [https://onthehub.com/ibm/](https://onthehub.com/ibm/) foracademic usage).  However, since the TSPand MILP in general are known to be NP-hard and therefore cannot be solvedefficiently in the general case (unless NP=P), one cannot expect a solution infeasible time for large instances. Therefore, our system makes use of someheuristics to decide when an input instance is too large to be solved by theMILP solver. An important feature that enhances elasticity in the system is theability to autonomously degrade the requested quality level when peak-loads aredetected. To this end, the statistics service uses a MapReduce task on theglobal system state to detect peak loads (unexpectedly high number of statetransitions in a specified time window) and forwards this information to thetour service. The tour service then uses this information together withinformation about its own load to reduce the quality level autonomously (i.e.decrease of the quality level speeds up the jobs and allows for a higherthroughput at peak loads). But since the result could differ a lot from theexpected one, information about this autonomous change is included into theresult object and hence also recorded by the evaluation tool. Again, a moreadvanced scenario could use this information for pricing models that considerthe actual quality level rather than the requested one. 

 

## Simulation & Input Data & UI

Since we were not able to find any real data for simulation purposes, weprovide a small .NET tool that simulates the data sources by directly feeding datainto the respective message queues. Since the tool is based on .NET core, itcan be also run on Linux environments.
For demonstration purposes, we implemented a rudimentary GUI based on bootstrap, jQuery and the Google Maps API to display the results of the simulation.

 

## Technologies & Requirements

In general, our services are based on Spring (Boot, Data, AMQP) and canbe deployed in Docker. Since the cluster-masters only maintain the currentstate and should be able to perform queries on records quite fast, we decidedto make use of a relational database (PostgreSQL, but could easily be replaceddue to the use of Spring Data). On the other hand, the amount of data stored bythe Statistics Service and the Evaluation Tool is obviously much higher (entirehistory vs. current state). Therefore, we decided to make use of NoSQLtechnologies (MongoDB). 

While the higher-level services provide a REST-based interface, thecommunication within the system (i.e. between the components) is based onRabbitMQ. As already mentioned before, for the data analytics we make use ofthe MapReduce feature of MongoDB and perform stream-processing (CEP) withApache Flink (input filtering, generation of higher-level events, detection ofspecific state transition sequences, …). 

Finally, the worker nodes of the Tour Service make use of the CPLEX MILPSolver of IBM. Once more we want to stress, that this is a commercial productand no sources or libraries are included in this repository. However, foracademic usage it is available at [https://onthehub.com/ibm/](https://onthehub.com/ibm/). To buildthis project, one needs to add the respective jar file into the local Mavenrepository and place the respective library file at the java library path.Alternatively, one could also replace the given MILP solver by open sourcealternatives like Google OR-tools or entirely remove it from the project by modifyingthe respective factory.

 

## Deployment

In general, this project is meant to be run within a Docker environment.To this end, we provide a docker-compose.yml file to setup the entire application,several docker-files to create images for each service and Springapplication.properties files for the deployment in docker. This allows toeasily deploy the entire application in a Docker environment just with a fewmanual steps: 

1. Build with maven: (e.g. mvn install in the root directory). To make use of the IBM CLEX MILP Solver, one needs to install the jar file at the localmaven repository. The groupId, artifactId and version for the installation can be found in the pom.xml
2. Copy andrename the generated jar-files from the target directories into the respectivesub-directories in the provided Docker directory of the repository. The expected name of the jar-file can be found in the respective dockerfiles. 
3. To make use of the IBM CLEX MILP Solver, one needs to put the files *cplex.jar* and *libcplex<version>.so* into thedirectory Docker/TourWorker/cplex. 
4. To use the experimental GUI (only a couply of static HTML files) one has to acquire a API Key of the Google Maps API use the key in the respective files. 
5. Once these steps have been performed, the application can then be simply deployed with *docker-compose up --build*

In addition, the sub-projects contain Spring Application.properties files in the resource directories with all necessary default values. These configs can be used when the application is run from an IDE. However, the all services expose their REST interface on different ports, use default configs for credentials (e.g. postgres-123456) and assume that the basic environment has already been setup manually (i.e. postgres, mongodb, rabbitmq)

## Misc

A pdf version of this document and a presentation is available in the documents directory.