FROM eclipse-temurin:17-jre
RUN apt-get update && apt-get install -y timidity lame wget
WORKDIR /opt
RUN wget https://lilypond.org/download/binaries/linux-64/lilypond-2.22.2-1.linux-64.sh && \
    chmod +x lilypond-2.22.2-1.linux-64.sh && ./lilypond-2.22.2-1.linux-64.sh --batch && \
    rm lilypond-2.22.2-1.linux-64.sh
ENV TZ=Asia/Yekaterinburg
ADD build/libs/lilies-2.0-all.jar lilies.jar
ADD add.sh .
ADD database.sql .
RUN mkdir logs && \
    ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone && \
    chmod 755 add.sh
EXPOSE 5050
CMD ["/bin/sh", "-c", "java $JAVA_OPTS -Dfile.encoding=UTF-8 -jar lilies.jar"]
