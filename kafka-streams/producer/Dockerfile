FROM python:3.5

RUN mkdir producer
ADD requirements.txt /producer
ADD experiment.txt /producer
ADD produce.py /producer

WORKDIR producer
RUN pip install -r requirements.txt

CMD ["python", "produce.py", "kafka"]
