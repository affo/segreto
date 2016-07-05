import time
from kafka import KafkaProducer

def get_producer(ip, port=9092):
    addr = "{}:{}".format(ip, port)
    return KafkaProducer(bootstrap_servers=addr)

def load_input(fname):
    tuples = []
    with open(fname, 'r') as f:
        counter = 0
        for tup in f.readlines():
            tup = tup.strip()
            if not tup or tup.startswith('#'):
                continue

            value = '{},t{}'
            if '-' in tup and not tup.startswith('-'):
                ts = int(tup.split('-')[0])
                value = tup.split('-')[1]
            else:
                ts = int(tup)
                value = 't{}'.format(counter)

            tuples.append((ts, value))
            counter += 1

    return tuples

if __name__ == '__main__':
    import sys

    if len(sys.argv) < 2:
        print("Kafka broker IP required")
        sys.exit(1)

    ip = sys.argv[1]
    topic = 'InTopic'
    producer = get_producer(ip)
    values = load_input('experiment.txt')

    last_ts = values[0][0]
    for v in values:
        new_ts = v[0]
        time.sleep(abs(new_ts - last_ts))
        last_ts = new_ts

        v = '{},{}'.format(new_ts, v[1])
        producer.send(topic, v.encode())
        producer.flush()
        print('>>>', v)
