## This is for testing purposes

# Use the official PostgreSQL image from the Docker Hub
FROM postgres:latest

# Set environment variables for PostgreSQL
ENV POSTGRES_DB test
ENV POSTGRES_USER test
ENV POSTGRES_PASSWORD test

# Expose the PostgreSQL port
EXPOSE 5432