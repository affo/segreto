TEST_IMG=flink_segreto
CNAME=segreto-running
MAIN_CLASS=it.polimi.nwlus.segreto.Main
# You can specify arguments to the executable jar
# specifying a --args option.
COUNT=0
W=3 3

all: run

build:
	mvn clean install -Pbuild-jar
	docker rmi $(TEST_IMG); true
	docker build -t $(TEST_IMG) .

run: clean build
	docker run -it --link proxy --name $(CNAME) $(TEST_IMG) -c$(MAIN_CLASS) --args $(COUNT) $(W)

attach_to_out:
	docker exec -it $(CNAME) ./attach_to_out.sh

clean:
	docker rm -f $(CNAME); true

# If you want to run socket experiments,
# you need to run the proxy, obtain its IP address
# and run make OPTS='--args 1 -h <IP> -p <PORT>'
proxy:
	docker rmi segreto_proxy; true
	docker build -t segreto_proxy experiments
	docker run -it --name proxy segreto_proxy
	docker rm -f proxy; true
