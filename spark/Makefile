TEST_IMG=spark_segreto
CNAME=segreto-running
PROXY=segreto_proxy
MAIN_CLASS=it.polimi.nwlus.segreto.spark.Main
# You can specify arguments to the executable jar
# specifying a --args option.
E = 1
# E can be either an experiment number 1 to 6

all: run

build:
	mvn package
	docker build -t $(TEST_IMG) .

run:
	docker run --link proxy --name segreto-running $(TEST_IMG) $(E)

clean:
	docker rm -f $(CNAME); true
	docker rmi $(TEST_IMG); true

proxy:
	docker rmi $(PROXY); true
	docker build -t $(PROXY) experiments
	docker run -it --name proxy $(PROXY)
	docker rm -f proxy; true
