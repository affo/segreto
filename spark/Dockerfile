FROM tomma/spark:1.6.1

ADD target/spark-1.0.jar $SPARK_HOME/lib/
ADD log4j.properties $SPARK_HOME/conf/
ADD src/main/resources/nationalparks.csv ./
#ADD run.sh ./
#RUN chmod u+x run.sh
#ADD attach_to_out.sh ./
#RUN chmod u+x attach_to_out.sh

#ENTRYPOINT ["./run.sh"]

ENTRYPOINT ["./bin/spark-submit", "--master", "local[2]", "--class" , "it.polimi.nwlu.segreto.spark.Main", "lib/spark-1.0.jar"]