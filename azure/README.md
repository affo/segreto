## Install

You will need to follow the steps below in order to be able
to run a query on Azure Streaming Analytics:

__First__ of all:

```
$ virtualenv venv
$ source venv/bin/activate
$ pip install -r requirements.txt
```

In this way, you will be able to run the consume/produce scripts.

__NOTE__: every command highlighted below requires the installation of the
requirements and the activation of the virtual environment (`source venv/bin/activate`).

__Second__:

 * Create a free __Azure account__ and get access to the [Azure Portal](https://portal.azure.com/);
 * create a __Streaming Analytics Job__;
 * create an __Event Hub__ from the service bus section in your portal as described
    [here](https://azure.microsoft.com/en-us/documentation/articles/event-hubs-csharp-ephcs-getstarted/);
 * create a __Queue__ from the service bus section in your portal as described
    [here](https://azure.microsoft.com/en-us/documentation/articles/service-bus-dotnet-get-started-with-queues/).

__Third__:

You have to __create an input and an output__ for your streaming job.  
__Create an event hub__ for input and __create a queue__ for output as described
[here](https://azure.microsoft.com/en-us/documentation/articles/stream-analytics-define-inputs/)
and [here](https://azure.microsoft.com/en-us/documentation/articles/stream-analytics-define-outputs/).

__NOTE__: Remember that you will use the names you give now to the input hub and to the output queue
when running the producer and the consumer scripts.

__Fourth__:

Create a __`servicebus_credentials.txt` file__ in this folder containing your SAS key for the ServiceBus.
You can get your SAS key from you Azure Portal in the ServiceBus section.

__Fifth__:

Eventually, you will need to specify the continuous query:

```
SELECT
    System.Timestamp as ts,
    Collect()
INTO
    [output-queue-alias]
FROM
    [input-hub-alias] TIMESTAMP BY DATEADD(millisecond, tapp, '1970-01-01T00:00:00Z')
GROUP BY
    HoppingWindow(second, [size], [slide])
```

__Sixth__:

__Start__ your streaming analytics job from your portal.

## Produce

Fill the file `experiment.txt` with the desired timestamps separated by
a newline character, and run `python produce.py <input-hub-name>`.

The `input-hub-name` is the one you gave to your hub on creation.

## Consume

Run `python consume.py <output-queue-name>`.

The `output-queue-name` is the name you gave to your output queue on creation.

