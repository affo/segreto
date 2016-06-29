IMG_NAME=segreto-kafka-streams
IMG_PRODUCER=segreto-kafka-producer
W=3 3

all: build run

dep:
	docker network create segreto; true
	docker-compose up -d

build:
	mvn package
	docker build -t $(IMG_NAME) .

run:
	docker run -d --link kafka \
		--net segreto \
		--name segreto-running $(IMG_NAME) $(W)

build_producer:
#	docker exec -it kafka kafka-console-producer \
#		--broker-list localhost:9092 \
#		--topic InTopic
	docker rmi $(IMG_PRODUCER); true
	docker build -t $(IMG_PRODUCER) producer

produce:
	docker run --link kafka \
		--net segreto \
		-v `pwd`/producer:/producer \
		--rm $(IMG_PRODUCER)

consume:
	docker exec -it kafka kafka-console-consumer \
		--topic OutTopic --from-beginning \
		--zookeeper zookeeper:2181 \
		--property print.key=true \

# clears the queue
clear:
	docker exec kafka kafka-topics \
	--zookeeper zookeeper:2181 \
	--alter --topic OutTopic \
	--config retention.ms=1000
	sleep 60 #wait for the queue to be clear
	docker exec kafka kafka-topics \
	--zookeeper zookeeper:2181 \
	--alter --topic OutTopic \
	--delete-config retention.ms

clean:
	docker-compose stop; true
	docker-compose rm -f; true
	docker rm -f segreto-running; true
	docker rmi $(IMG_NAME); true

