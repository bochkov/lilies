FROM openjdk:jre-slim
RUN apt-get update && apt-get install -y timidity lame wget
VOLUME /tmp
WORKDIR /tmp
RUN wget http://download.linuxaudio.org/lilypond/binaries/linux-64/lilypond-2.18.2-1.linux-64.sh && \
    chmod +x lilypond-2.18.2-1.linux-64.sh && ./lilypond-2.18.2-1.linux-64.sh --batch && \
    rm lilypond-2.18.2-1.linux-64.sh
RUN wget -O /usr/local/bin/dumb-init https://github.com/Yelp/dumb-init/releases/download/v1.2.1/dumb-init_1.2.1_amd64 && \
    chmod +x /usr/local/bin/dumb-init
RUN rm -rf /var/lib/apt/lists/*
ENV TZ=Asia/Yekaterinburg
WORKDIR /opt
ADD build/libs/lilies-2.0-all.jar lilies.jar
ADD add.sh .
ADD database.sql .
RUN mkdir logs && \
    ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone && \
    chmod 755 add.sh
EXPOSE 5050
ENTRYPOINT ["/usr/local/bin/dumb-init", "--"]
CMD ["/bin/sh", "-c", "java $JAVA_OPTS -Dfile.encoding=UTF-8 -jar lilies.jar"]
