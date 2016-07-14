# use yours
PROJ_ID=segreto-1369
# use yours
BUCKET_ID=segreto
MAIN=it.polimi.nwlus.segreto.dataflow.Main
W=3
B=3

define ARGS
--project=$(PROJ_ID) \
--stagingLocation=gs://$(BUCKET_ID)/staging/ \
--windowSize=$(W) \
--windowSlide=$(B) \
--runner=BlockingDataflowPipelineRunner
endef

build:
	mvn compile exec:java \
		-Dexec.mainClass=$(MAIN) \
		-Dexec.args="$(ARGS)"
