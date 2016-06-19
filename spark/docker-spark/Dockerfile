FROM java:8

RUN mkdir /opt/spark
#RUN curl -o - http://apache.panu.it/spark/spark-1.6.1/spark-1.6.1-bin-without-hadoop.tgz | tar -zxf - --strip-components=1 -C /opt/spark
RUN curl -o - http://d3kbcqa49mib13.cloudfront.net/spark-1.6.1-bin-hadoop2.6.tgz | tar -zxf - --strip-components=1 -C /opt/spark

WORKDIR /opt/spark

ENV SPARK_HOME /opt/spark
