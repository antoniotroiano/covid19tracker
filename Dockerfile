FROM openjdk:11.0.7-slim

COPY statistics-corona-*.jar /statistic-corona.jar

CMD ["java", "-jar", "statistic-corona.jar"]