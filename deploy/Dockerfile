FROM java:latest
RUN apt-get update && apt-get install -y timidity lame bzip2
VOLUME /tmp
WORKDIR /tmp
RUN wget http://download.linuxaudio.org/lilypond/binaries/linux-64/lilypond-2.18.2-1.linux-64.sh && \
    chmod +x lilypond-2.18.2-1.linux-64.sh && ./lilypond-2.18.2-1.linux-64.sh --batch && \
    rm lilypond-2.18.2-1.linux-64.sh
WORKDIR /opt
ADD lilies.jar /opt/lilies.jar
ENTRYPOINT ["java", "-Xmx256m", "-Xss2m", "-Dfile.encoding=UTF-8", "-Djava.security.egd=file:/dev/./urandom", "-jar", "lilies.jar"]
