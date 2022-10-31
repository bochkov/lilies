FROM eclipse-temurin:17-jre
RUN apt-get -y update && \
    DEBIAN_FRONTEND=noninteractive apt-get install -y --no-install-recommends bzip2 timidity lame && \
    rm -rf /var/lib/apt/lists/*
WORKDIR /opt
RUN wget https://lilypond.org/download/binaries/linux-64/lilypond-2.22.2-1.linux-64.sh && \
    chmod +x lilypond-2.22.2-1.linux-64.sh && sh lilypond-2.22.2-1.linux-64.sh && \
    rm lilypond-2.22.2-1.linux-64.sh
ENV TZ=Asia/Yekaterinburg
ADD build/libs/lilies-3.0.jar lilies.jar
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
EXPOSE 8080
CMD ["/bin/sh", "-c", "java $JAVA_OPTS -Dfile.encoding=UTF-8 -jar lilies.jar --spring.config.name=lilies"]
