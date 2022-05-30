FROM quay.io/keycloak/keycloak:18.0.0-legacy

COPY target/ms-graph-spi-1.0-SNAPSHOT.war /opt/jboss/keycloak/standalone/deployments/