FROM wire/bots.runtime:latest

COPY target/anna.jar      /opt/anna/anna.jar
COPY conf/anna.yaml       /opt/anna/anna.yaml
COPY certs/keystore.jks   /opt/anna/keystore.jks

WORKDIR /opt/anna
