FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
COPY pos-domain/pom.xml pos-domain/pom.xml
COPY pos-usecases/pom.xml pos-usecases/pom.xml
COPY pos-adapters-web/pom.xml pos-adapters-web/pom.xml
COPY pos-adapters-persistence/pom.xml pos-adapters-persistence/pom.xml
COPY pos-adapters-external/pom.xml pos-adapters-external/pom.xml
COPY pos-adapters-messaging/pom.xml pos-adapters-messaging/pom.xml
COPY pos-infrastructure/pom.xml pos-infrastructure/pom.xml
RUN chmod +x mvnw && ./mvnw -q -B dependency:go-offline
COPY . .
RUN ./mvnw -q -B -DskipTests package

FROM eclipse-temurin:17-jre-alpine AS runtime
WORKDIR /app
COPY --from=build /app/pos-infrastructure/target/pos-farmacia.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
