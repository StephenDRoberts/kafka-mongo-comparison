# Kafka Streams - MongoDB Performance Comparison

Dissertation project for Sheffield Hallam University MSc in Digital & Technology Solutions

## Overview

The purpose of this application is to compare the read and write performances of 
a Kafka Streams backed State store against a MongoDB based application within the [Spring Boot](https://spring.io/projects/spring-boot) framework.

## Architecture

![Architecture Diagram for project](https://github.com/StephenDRoberts/kafka-mongo-comparison/blob/master/assets/KafkaMongoArchitectureFull.png?raw=true)

## Pre-requisites
* [Docker](https://www.docker.com/products/docker-desktop)
* [Docker-compose](https://docs.docker.com/compose/install/)

## Running the applications
* With docker running on your desktop open a terminal and navigate to the `kafka-mongo-comparison` folder
* Run the `docker-compose up` command to bring up the Kafka, Zookeeper and MongoDB images as well as other external tools for debugging/visual purposes.
* Run the Setup application. This will only need to be run once. To do so, either:
    1. navigate to the `setup folder` and run `./gradlew clean bootRun`; or
    2. run the application through your IDEs run functionality.
    
    This will place messages onto the `message-topic` to be read by the other applications.
* Run your chosen application (either MongoDBApplication or KafkaStreamsApplication). To do so, either:
    1. navigate to the applications folder and run `./gradlew clean bootRun`; or
    2. run the application through your IDEs run functionality.
 
    The application will process all the messages in the `message-topic` kafka topic. 
    NOTE: once completed, do not stop the application. It will need to stay running for the Poller application to process results.
* Run the Poller application. This will request statistics from the running application and save them to a CSV file. The CSV files for both
Kafka Streams and MongoDB are saved under `poller/src/main/resources/results/<filename>.csv`
The Poller application will bring up a terminal asking which application you would like to populate statistics for (i.e. the running application).
     
     

## Setup Module

The purpose of the setup module is to create the experiment's data set.
This is done via a `@PostConstruct` annotation in the MessageGenerator class that runs a function on setup.

#### Resetting offsets

The setup script will create our data set. Ideally it would only want to be run once to avoid extra work.

To enable this, there are two shell scripts that run as pre-launch configurations for both the MongoDB application, and the Kafka streams version.
These scripts reset the offsets read by the application and need to be run only when the application is in a STOPPED state. 
Therefore, rather than restarting the application you need to ensure that it is stopped fully before restarting, else the reset command will fail. 

To allow for this automatically, the IDE configuration will need to be applied to call the relative reset script as a pre-launch operation.

An alternative way to do this is to manually reset the offset on the docker image. To do this, run the following commands:  

* On your computer run the following command to get a list of docker processes running. Locate the `wurstmeister/kafka` image and fetch its ID.   
  
  `docker ps`
  
* Exec into the pod to be able to run the docker image command line:  

  `docker exec -it *<kafka docker image ID>* /bin/bash`
  
* Navigate to the `opt/kafka/bin` directory
  
* Reset the offsets - normal consumer version (i.e for Mongo app): 
  ```
  kafka-consumer-groups.sh \
      --bootstrap-server localhost:9093 \
      --group mongo-consumer \
      --topic message-topic \
      --reset-offsets \
      --to-earliest \
      --execute
  ```

* Reset the offsets - kafka-streams version:
    ```
  kafka-streams-application-reset.sh \
        --application-id kafka-mongo-comparison \
        --input-topics message-topic \
        --bootstrap-servers localhost:9093 \
        --zookeeper zookeeper:2181
  ```
 