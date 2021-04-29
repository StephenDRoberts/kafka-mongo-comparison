#!/usr/bin/env bash

# Get kafka container ID
echo "Fetching container ID"
CONTAINER_ID=$(docker ps --filter NAME="broker" --format "{{.ID}}")
echo $CONTAINER_ID
# Reset offsets
echo $(docker exec "$CONTAINER_ID" /opt/kafka/bin/kafka-streams-application-reset.sh --application-id kafka-mongo-comparison --input-topics message-topic --bootstrap-servers localhost:9093 --zookeeper zookeeper:2181)
echo "Kafka-streams offsets reset"

exit $?
