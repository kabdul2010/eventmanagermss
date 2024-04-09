# Use an official OpenJDK runtime as a parent image
FROM openjdk

# Set the working directory in the container
WORKDIR /app

# Copy the application JAR file into the container at the working directory
COPY target/timesheet-0.0.1-SNAPSHOT.jar /app/timesheet-0.0.1-SNAPSHOT.jar

# Copy the application.properties file into the container at the working directory
COPY src/main/resources/application.properties /app/application.properties

# Expose the port defined in application.properties
EXPOSE 9090

# Specify the command to run on container startup
CMD ["java", "-jar", "timesheet-0.0.1-SNAPSHOT.jar"]

