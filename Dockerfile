FROM adoptopenjdk/openjdk11
COPY target/doc-0.0.1-SNAPSHOT.jar doc.jar

ENTRYPOINT ["java", "-jar", "doc.jar"]

