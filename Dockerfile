# Use multi-stage build for slimmer image
FROM maven:3.8.8-openjdk-17 AS build
WORKDIR /app
COPY pom.xml mvnw ./
COPY .mvn .mvn
COPY src src
RUN mvn -B -DskipTests package

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /sales-analytics/target/*.jar sales-analytics.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "sales-analytics.jar"]