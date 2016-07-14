## Google Cloud Dataflow
### Running an Experiment

In order to run an experiment you need a Google account and you need to follow
the quickstart at
https://cloud.google.com/dataflow/docs/quickstarts/quickstart-java-maven. You
can ignore the part in which they explain how to create a project, because you
do not need to create it, but you only need to use ours.

In order to run experiments, you need to create two topics from the
[Google Cloud Console](https://console.cloud.google.com/cloudpubsub)
called `input` and `output` and then use the producer and the consumer
as explained in the `io` directory.

To build and deploy the topology you have to run:

```
$ make PROJ_ID=<project-identifier> BUCKET_ID=<storage-bucket-id> \
    W=<window-size> B=<window-slide>
```
