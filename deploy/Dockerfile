FROM java:8
RUN apt-get update && apt-get -y dist-upgrade && apt-get install ghostscript lilypond
VOLUME /tmp
WORKDIR /opt
ADD lilies-1.0.jar /opt/lilies-1.0.jar
CMD ["java", "-Xms512m", "-Xmx2g", "-jar", "lilies-1.0.jar"]