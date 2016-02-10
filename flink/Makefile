TEST_IMG=flink_segreto
CNAME=segreto-running
MAIN_CLASS=it.polimi.nwlus.segreto.Main
# You can specify arguments to the executable jar
# specifying a --args option.
OPTS=--args 1

all: run

build:
	mvn clean install -Pbuild-jar
	docker build -t $(TEST_IMG) .

run: clean build
	docker run -it -p 48081:8081 --name $(CNAME) $(TEST_IMG) -c$(MAIN_CLASS) $(OPTS)

clean:
	docker rm -f $(CNAME); true
