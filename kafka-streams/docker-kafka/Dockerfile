FROM confluent/kafka

USER root
ADD run.sh /
RUN chmod +x run.sh

USER confluent

ENTRYPOINT ["./run.sh"]

