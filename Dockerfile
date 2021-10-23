FROM openjdk:11.0.7-slim

COPY app/covid19tracker-*.jar /covid19tracker.jar

CMD ["java", "-jar", "covid19tracker.jar"]