FROM adoptopenjdk:11 AS builder
COPY gradlew .
COPY settings.gradle .
COPY build.gradle .
COPY gradle gradle
COPY src src
RUN chmod +x ./gradlew
RUN ./gradlew build -x test

FROM adoptopenjdk:11
RUN mkdir /opt/app
COPY --from=builder build/libs/*.jar /opt/app/spring-boot-application.jar
EXPOSE 8080
ENV	PROFILE local
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=${PROFILE}" ,"/opt/app/spring-boot-application.jar"]