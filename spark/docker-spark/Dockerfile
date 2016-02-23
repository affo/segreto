FROM java:7

RUN mkdir /opt/spark
RUN curl -o - http://it.apache.contactlab.it/spark/spark-1.6.0/spark-1.6.0-bin-without-hadoop.tgz | tar -zxf - --strip-components=1 -C /opt/spark
WORKDIR /opt/spark
ENV SPARK_HOME /opt/spark
ENV PATH $SPARK_HOME/bin:$PATH
