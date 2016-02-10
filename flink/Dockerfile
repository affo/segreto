FROM affo/flink:0.10.0

ADD target/flink.jar $FLINK_HOME/lib/
ADD target/flink.jar ./
ADD flink-conf.yaml $FLINK_HOME/conf/flink-conf.yaml
ADD run.sh ./

ENTRYPOINT [ "./run.sh" ]
