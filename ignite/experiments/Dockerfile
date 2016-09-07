FROM gcc

RUN mkdir /experiments
WORKDIR /experiments
ADD exp.current ./exp
ADD run.sh ./
ADD utils.sh ./

RUN chmod u+x exp
RUN chmod u+x run.sh

CMD ./run.sh
