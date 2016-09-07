TEST_IMG=segreto-ignite
W=3
B=3

all: dep clean build run

dep:
	docker network create segreto; true

build:
	mvn package -Pstreaming
	docker build -t $(TEST_IMG)-streaming -f Dockerfile.streaming .
	mvn package -Pquery
	docker build -t $(TEST_IMG)-query -f Dockerfile.query .

run:
	docker run --net segreto -d --name segreto-running-streaming $(TEST_IMG)-streaming $(W)
	sleep 2
	docker run --net segreto --name segreto-running-query $(TEST_IMG)-query $(B)

clean:
	docker rm -f segreto-running-streaming; true
	docker rm -f segreto-running-query; true
	docker rmi $(TEST_IMG)-streaming; true
	docker rmi $(TEST_IMG)-query; true

exp: dep
	docker rmi segreto-exp; true
	docker build -t segreto-exp experiments
	docker run --net segreto -it --name segreto-exp \
		--link segreto-running-streaming \
		-e STREAMER_IP=segreto-running-streaming \
		segreto-exp
	docker rm -f segreto-exp; true
