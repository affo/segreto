TEST_IMG=segreto-storm
MAIN_CLASS=it.polimi.nwlus.segreto.storm.Main
W=3 3

all: clean build run

build:
	mvn package
	docker build -t $(TEST_IMG) .

run:
	docker run --link proxy --name segreto-running $(TEST_IMG) $(MAIN_CLASS) $(W)

clean:
	docker rm -f segreto-running; true
	docker rmi $(TEST_IMG); true

proxy:
	docker rmi segreto_proxy; true
	docker build -t segreto_proxy experiments
	docker run -it --name proxy segreto_proxy
	docker rm -f proxy; true
