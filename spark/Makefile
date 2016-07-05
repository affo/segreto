TEST_IMG=spark_segreto
CNAME=segreto-running
CNAMELOCAL="segreto-local-running"
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

local:
	mvn package
	cp target/spark-1.0.jar spark-local/
	cp log4j.properties spark-local/
	docker build -t spark-local spark-local/
	
runlocal:
	docker run --name $(CNAMELOCAL) spark-local	$(E)

run:
	docker rm $(CNAME); true
	docker run --link proxy --name segreto-running -p 8080:8080 -p 8081:8081 -p 8088:8088 -p 8042:8042 -p 4040:4040 $(TEST_IMG)

clean:
	docker rm -f $(CNAME); true
	docker rmi $(TEST_IMG); true
	docker rm $(CNAMELOCAL); true

proxy:
	docker rmi $(PROXY); true
	docker build -t $(PROXY) experiments
	docker run -it --name proxy $(PROXY)
	docker rm -f proxy; true
