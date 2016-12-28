import random

SERVICEBUS_CREDS = 'servicebus_credentials.txt'

def get_servicebus_credentials():
    creds = {}

    with open(SERVICEBUS_CREDS, 'r') as f:
        line = f.readline()
        kvs = line.split(';')
        for kv in kvs:
            k, v = kv.strip().split('=', 1)
            creds[k] = v

    return creds

def unbalanced_randint(upper):
    assert upper >= 2

    if random.random() < 0.7:
        return 1

    return random.randint(2, upper)

def load_input(fname):
    timestamps = []
    with open(fname, 'r') as f:
        for tup in f.readlines():
            tup = tup.strip()
            if not tup or tup.startswith('#'):
                continue

            ts = int(tup)
            timestamps.append(ts)

    return timestamps

