FROM openjdk:latest
RUN apt-get update && apt-get install -y timidity lame bzip2 nano
VOLUME /tmp
WORKDIR /tmp
RUN wget http://download.linuxaudio.org/lilypond/binaries/linux-64/lilypond-2.18.2-1.linux-64.sh && \
    chmod +x lilypond-2.18.2-1.linux-64.sh && ./lilypond-2.18.2-1.linux-64.sh --batch && \
    rm lilypond-2.18.2-1.linux-64.sh
RUN wget -O /usr/local/bin/dumb-init https://github.com/Yelp/dumb-init/releases/download/v1.2.0/dumb-init_1.2.0_amd64 && \
    chmod +x /usr/local/bin/dumb-init
ENV TZ=Asia/Yekaterinburg
WORKDIR /opt
ADD lilies.jar /opt/lilies.jar
ADD add.sh /opt/add.sh
RUN mkdir logs && \
    ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone && \
    chmod 755 /opt/add.sh
EXPOSE 5050
ENTRYPOINT ["/usr/local/bin/dumb-init", "--"]
CMD ["java", "-Dfile.encoding=UTF-8", "-jar", "lilies.jar"]
