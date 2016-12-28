FROM tomma/spark:2.0.2

ADD project/target/segreto_spark-2.1.0.jar $SPARK_HOME/lib/segreto_spark-2.1.0.jar
ADD log4j.properties $SPARK_HOME/conf/
ADD experiments ./experiments

ENTRYPOINT ["./bin/spark-submit", "--master", "local[4]", "--class" , "it.polimi.nwlu.segreto.Main", "lib/segreto_spark-2.1.0.jar"]
