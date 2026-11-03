# ------------étape 1 : build ------------
FROM maven:3.9-eclipse-temurin-26 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -q clean package -DskipTests

# -----------étape 2 : runtime -----------
FROM tomcat:11.0-jre17

RUN rm -rf /usr/local/tomcat/webapps/*
COPY --from=build /app/target/*.war /usr/local/tomcat/webapps/ROOT.war

# Utilisateur non-root, propriétaire des dossiers que Tomcat écrit
RUN groupadd -r app && useradd -r -g app app && chown -R app:app /usr/local/tomcat
USER app

EXPOSE 8080

# Test du port avec bash : évite de dépendre de curl 
HEALTHCHECK --interval=30s --timeout=5s --start-period=60s --retries=3 CMD bash -c '</dev/tcp/127.0.0.1/8080' || exit 1
CMD [ "catalina.sh", "run" ]