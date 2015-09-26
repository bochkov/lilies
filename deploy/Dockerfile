FROM java:8
RUN apt-get update && apt-get -y dist-upgrade && apt-get install lilypond timidity lame
VOLUME /tmp
WORKDIR /opt
ADD lilies.jar /opt/lilies.jar
CMD ["java", "-Xms512m", "-Xmx2g", "-jar", "lilies.jar"]