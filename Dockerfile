FROM eclipse-temurin:17-jre
ENV TZ=Asia/Yekaterinburg
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
WORKDIR /opt
ADD build/libs/lilies-3.0.jar lilies.jar
EXPOSE 8080
CMD ["/bin/sh", "-c", "java $JAVA_OPTS -Dfile.encoding=UTF-8 -jar lilies.jar --spring.config.name=lilies"]
