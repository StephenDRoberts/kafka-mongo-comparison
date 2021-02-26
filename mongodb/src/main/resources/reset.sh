#!/usr/bin/env bash

# Get kafka container ID
CONTAINER_ID=$(docker ps --filter NAME="broker" --format "{{.ID}}")

# Reset offsets
echo $(docker exec "$CONTAINER_ID" /opt/kafka/bin/kafka-consumer-groups.sh --group mongo-consumer --topic message-topic --bootstrap-server localhost:9093 --reset-offsets --to-earliest --execute)

echo "Kafka offsets for Mongo reset"

exit $?