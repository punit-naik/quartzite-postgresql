#!/bin/bash

## This is for testing purposes

IMAGE_NAME="test_postgres_image"
CONTAINER_NAME="test_postgres_container"
DEFAULT_PORT=5432
POSTGRES_DB="test"
POSTGRES_USER="test"
POSTGRES_PASSWORD="test"

function image_exists() {
  docker images -q $IMAGE_NAME
}

function container_running() {
  docker ps -q -f name=$CONTAINER_NAME
}

function container_exists() {
  docker ps -a -q -f name=$CONTAINER_NAME
}

function build_image() {
  if [ -n "$(image_exists)" ]; then
    echo "Image is already built."
  else
    echo "Building Docker image..."
    docker build -t $IMAGE_NAME .
  fi
}

function start_container() {
  if [ -n "$(container_running)" ]; then
    echo "Container is already running."
  elif [ -n "$(container_exists)" ]; then
    echo "Starting existing Docker container..."
    docker start $CONTAINER_NAME
  else
    echo "Starting new Docker container..."
    docker run --name $CONTAINER_NAME -d -p $POSTGRES_PORT:5432 \
      -e POSTGRES_DB=$POSTGRES_DB \
      -e POSTGRES_USER=$POSTGRES_USER \
      -e POSTGRES_PASSWORD=$POSTGRES_PASSWORD \
      $IMAGE_NAME
  fi
}

function stop_container() {
  if [ -n "$(container_running)" ]; then
    echo "Stopping Docker container..."
    docker stop $CONTAINER_NAME
    docker rm $CONTAINER_NAME
  else
    echo "Container is not running."
  fi
}

function usage() {
  echo "Usage: $0 {start|stop} [port]"
  echo "  start        Start the PostgreSQL container"
  echo "  stop         Stop the PostgreSQL container"
  echo "  port         Optional. Specify the port to map to PostgreSQL (default is $DEFAULT_PORT)"
}

if [ "$#" -eq 0 ]; then
  usage
  exit 1
fi

COMMAND=$1
PORT=${2:-$DEFAULT_PORT}

POSTGRES_PORT=$PORT

if [ "$COMMAND" == "start" ]; then
  build_image
  start_container
elif [ "$COMMAND" == "stop" ]; then
  stop_container
else
  usage
  exit 1
fi