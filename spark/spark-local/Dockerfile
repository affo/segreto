FROM tomma/spark:1.6.1

ADD spark-1.0.jar $SPARK_HOME/lib/
ADD log4j.properties $SPARK_HOME/conf/

#ENTRYPOINT ["./bin/spark-submit", "--master local[2]", " --class it.polimi.nwlu.segreto.spark.Main ", "lib/spark-1.0.jar"]
ENTRYPOINT ["./bin/spark-submit", "--master", "local[4]", "--class" , "it.polimi.nwlu.segreto.spark.Main", "lib/spark-1.0.jar"]
