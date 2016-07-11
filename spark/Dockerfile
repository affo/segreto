FROM tomma/spark:1.6.1

ADD target/spark-1.0.jar $SPARK_HOME/lib/
ADD log4j.properties $SPARK_HOME/conf/
ADD current.experiment ./
ADD run.sh ./
RUN chmod u+x run.sh

ENTRYPOINT ["./run.sh", "1"]

