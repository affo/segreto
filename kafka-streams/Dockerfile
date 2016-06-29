FROM java:8

ADD target/segreto-standalone.jar /segreto.jar
ADD run.sh /
RUN chmod u+x run.sh

ENTRYPOINT ["./run.sh"]

