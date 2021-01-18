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
One way to do this is to manually reset the offset on the docker image.

To do this we need to run the following commands:  

* On your computer run the following command to get a list of docker processes running. Locate the `wurstmeister/kafka` image and fetch its ID.   
  
  `docker ps`
  
* Exec into the pod to be able to run the docker image command line:  

  `docker exec -it *<kafka docker image ID>* /bin/bash`
  
* Reset the offsets: 
 
  `kafka-consumer-groups.sh \
      --bootstrap-server broker:9092 \
      --group kafka-consumer \
      --topic message-topic \
      --reset-offsets \
      --to-earliest \
      --execute`
