#Start with a base image containing Java runtime
FROM eclipse-temurin:21-jdk
#Information around who maintains the image
LABEL "org.opencontainers.image.authors"="ranim.com"
# curl is used by the healthchecks of docker-compose.yml
RUN apt-get update && apt-get install -y --no-install-recommends curl && rm -rf /var/lib/apt/lists/*
# Add the application's jar to the image
COPY target/product-microservice-0.0.1-SNAPSHOT.jar product-microservice-0.0.1-SNAPSHOT.jar
# execute the application
ENTRYPOINT ["java", "-jar", "product-microservice-0.0.1-SNAPSHOT.jar"]
