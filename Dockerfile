FROM openjdk:11.0.7-slim

COPY v2-*.jar /statistic-corona.jar

CMD ["java", "-jar", "statistic-corona.jar"]