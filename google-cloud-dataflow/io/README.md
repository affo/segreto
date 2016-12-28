## Install

```
$ virtualenv venv
$ source venv/bin/activate
$ pip install -r requirements.txt
```

In order to use the Google Cloud client libraries you need to authenticate first.  
Follow [Google's documentation](http://gcloud-python.readthedocs.io/en/latest/gcloud-auth.html)
to authenticate the client libraries using the Google Cloud SDK (recommended).

## Produce

__Requires__:

```
$ source venv/bin/activate
```

Fill the file `experiment.txt` with the desired timestamps separated by
a newline character, and run `python produce.py <input-topic> 0`.

The `input-topic` is `input`; `0` specifies wether to run casual
or dense on monotonically increasing tuples after the experiment.

## Consume

__Requires__:

```
$ source venv/bin/activate
```

Run `python consume.py <output-topic>`.

The `output-topic` is `output`.

## Clean up

Remember to clean up topics and subscriptions once done using [Google
Cloud Console](https://console.cloud.google.com/cloudpubsub).
