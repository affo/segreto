import random

MAX_SIM_TUPLES = 5
MAX_GAP = 5

def unbalanced_randint(upper):
    assert upper >= 2

    if random.random() < 0.7:
        return 1

    return random.randint(2, upper)

# Courtesy of kafka-streams
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


if __name__ == '__main__':
    import sys, time

    if len(sys.argv) < 3:
        print 'Specify topic, type of experiment.'
        print 'Type of experiment: 0 for random, 1 for dense and unique.'
        sys.exit(1)

    from gcloud import pubsub

    topic_name = sys.argv[1]
    dense = int(sys.argv[2])

    client = pubsub.Client()
    topic = client.topic(topic_name)

    def pub(ts, tid):
        value = '({},t-{})'.format(ts, tid)
        topic.publish(value.encode(), ts=str(ts * 1000))
        print '>>>', value

    if not topic.exists():
        topic.create()
        print 'Topic {} created'.format(topic_name)

    timestamps = load_input('experiment.txt')

    last_ts = -1
    last_id = 0
    for ts in timestamps:
        if last_ts < 0:
            last_ts = ts

        time.sleep(abs(ts - last_ts))
        last_ts = ts

        pub(last_ts, last_id)
        last_id += 1

    print
    print "The experiment is finished."
    print "Now we send an infinite sled of tuples."
    print
    time.sleep(3)
    last_ts += 3

    # Ok, the experiment has been played.
    # Now go ahead with other tuples to make
    # Google output something.
    while True:
        # choose number of simultaneous tuples
        no_sim_tuples = 1
        if not dense:
            no_sim_tuples = unbalanced_randint(MAX_SIM_TUPLES)
        # choose a gap in seconds
        gap = 1
        if not dense:
            gap = unbalanced_randint(MAX_GAP)

        # sleep for gap
        time.sleep(gap)

        # update timestamps
        last_ts += gap
        for _ in xrange(no_sim_tuples):
            pub(last_ts, last_id)
            last_id += 1
