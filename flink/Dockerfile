FROM affo/flink:1.1.4

ADD target/flink.jar $FLINK_HOME/lib/
ADD target/flink.jar ./
ADD flink-conf.yaml $FLINK_HOME/conf/flink-conf.yaml
ADD run.sh ./
ADD attach_to_out.sh ./

RUN chmod u+x run.sh
RUN chmod u+x attach_to_out.sh

ENTRYPOINT [ "./run.sh" ]
