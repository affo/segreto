FROM gcc
EXPOSE 9999

RUN mkdir /experiments
WORKDIR /experiments
ADD proxy.c ./
RUN gcc proxy.c -o proxy
ADD exp.current ./exp
ADD run.sh ./
ADD utils.sh ./

RUN chmod u+x exp
RUN chmod u+x run.sh

CMD ./run.sh
