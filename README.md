# Kafka Streams - MongoDB Performance Comparison

Dissertation project for Sheffield Hallam University MSc in Digital & Technology Solutions

## Overview

The purpose of this application is to compare the read and write performances of 
a Kafka Streams backed State store against a MongoDB based application.

## Architecture

![Architecture Diagram for project](https://github.com/StephenDRoberts/kafka-mongo-comparison/blob/master/assets/KafkaMongoArchitecture.png?raw=true)

## Setup Module

The purpose of the setup module is to create the experiment's data set.
This is done via a `@PostConstruct` annotation in the MessageGenerator class that runs a function on setup.
 

#### Manually reset offsets

The setup script will create our data set and ideally we only want to run the script once to avoid extra work.

To enable this, we have provided two shell scripts that run as pre-launch configurations for both the MongoDB application and the Kafka streams version.
These scripts 

One way to do this is to manually reset the offset on the docker image.

To do this we need to run the following commands:  

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
 