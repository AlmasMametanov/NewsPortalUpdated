FROM openjdk:11

EXPOSE 8080
ADD /target/NewsPortalUpdated-0.0.1-SNAPSHOT.jar newsPortalService.jar

ENTRYPOINT ["java", "-jar", "newsPortalService.jar"]