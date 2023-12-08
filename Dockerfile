FROM alibabadragonwell/dragonwell:21-ubuntu

LABEL cc.allio.uno.turbo.author=jiangw1027@gmail.com

WORKDIR /turbo

COPY ./target/Turbo-0.1.0.RELEASE.jar app.jar

EXPOSE 8600

ENV TZ=Asia/Shanghai JAVA_OPTS="-Xms128m -Xmx256m -Djava.security.egd=file:/dev/./urandom -Dspring.profiles.active=prod --add-opens java.base/java.lang=ALL-UNNAMED"

CMD java $JAVA_OPTS -jar app.jar