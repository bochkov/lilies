FROM eclipse-temurin:25-jre-alpine
ENV TZ=Asia/Yekaterinburg
ENV LANG=ru_RU.UTF-8
ENV LANGUAGE=ru_RU:en
ENV LC_ALL=ru_RU.UTF-8
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
WORKDIR /opt
COPY build/libs/lilies.jar lilies.jar
EXPOSE 8080
CMD ["/bin/sh", "-c", "java $JAVA_OPTS -Dfile.encoding=UTF-8 -jar lilies.jar"]
