# Gatling is a highly capable load testing tool.
#
# Documentation: https://gatling.io/docs/3.0/
# Cheat sheet: https://gatling.io/docs/3.0/cheat-sheet/

FROM openjdk:8-jdk-alpine

# working directory for gatling
WORKDIR /opt

# gating version
ENV GATLING_VERSION 3.0.3

# create directory for gatling install
RUN mkdir -p gatling

# install gatling
RUN apk add --update wget bash curl jq libc6-compat && \
  mkdir -p /tmp/downloads && \
  wget -q -O /tmp/downloads/gatling-$GATLING_VERSION.zip \
  https://repo1.maven.org/maven2/io/gatling/highcharts/gatling-charts-highcharts-bundle/$GATLING_VERSION/gatling-charts-highcharts-bundle-$GATLING_VERSION-bundle.zip && \
  mkdir -p /tmp/archive && cd /tmp/archive && \
  unzip /tmp/downloads/gatling-$GATLING_VERSION.zip && \
  mv /tmp/archive/gatling-charts-highcharts-bundle-$GATLING_VERSION/* /opt/gatling/ && \
  wget -q -O /tmp/downloads/scalaj-http_2.12-2.4.1.jar \
  http://central.maven.org/maven2/org/scalaj/scalaj-http_2.12/2.4.1/scalaj-http_2.12-2.4.1.jar && \
  mv /tmp/downloads/scalaj-http_2.12-2.4.1.jar /opt/gatling/lib/ && \
  rm -rf /tmp/*

# change context to gatling directory
WORKDIR  /opt/gatling

# set directories below to be mountable from host
VOLUME ["/opt/gatling/conf", "/opt/gatling/results", "/opt/gatling/user-files"]

COPY src/test/resources/application.conf /opt/gatling/conf/

COPY src/test/resources/* /opt/gatling/user-files/resources/

COPY src/test/scala/* /opt/gatling/user-files/simulations/

# set environment variables
ENV PATH /opt/gatling/bin:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin
ENV GATLING_HOME /opt/gatling

ENTRYPOINT ["gatling.sh","-s","HASimulation"]
