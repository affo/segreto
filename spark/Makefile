TEST_IMG=spark_segreto
CNAME=spark_segreto_
PROXY=segreto_proxy
# You can specify arguments to the executable jar
# specifying a --args option.
E = 1
# E can be either an experiment number 1 to 6

all: run

build:
	cd project && mvn package && cd ..
	docker build -t $(TEST_IMG) .

run:
	docker rm $(CNAME); true
	docker run --name $(CNAME) $(TEST_IMG)	$(E)

clean:
	docker rm -f $(CNAME); true
