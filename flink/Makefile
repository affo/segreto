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
ifdef SOCK
	docker run -it --link proxy --name $(CNAME) $(TEST_IMG) -c$(MAIN_CLASS) $(OPTS)
else
	docker run -it --name $(CNAME) $(TEST_IMG) -c$(MAIN_CLASS) $(OPTS)
endif

clean:
	docker rm -f $(CNAME); true

# If you want to run socket experiments,
# you need to run the proxy, obtain its IP address
# and run make OPTS='--args 1 -h <IP> -p <PORT>'
run_proxy:
	docker build -t segreto_proxy experiments
	docker run -it --name proxy segreto_proxy
	docker rm -f proxy; true
