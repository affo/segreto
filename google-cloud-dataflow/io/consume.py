if __name__ == '__main__':
    import sys

    if len(sys.argv) < 2:
        print("Topic required")
        sys.exit(1)

    from gcloud import pubsub

    topic_name = sys.argv[1]

    client = pubsub.Client()
    topic = client.topic(topic_name)
    if not topic.exists():
        topic.create()
        print 'Topic {} created'.format(topic_name)

    sub_name = topic_name + '_sub'
    sub = topic.subscription(sub_name)
    if not sub.exists():
        sub.create()
        print 'Subscription {} created'.format(sub_name)

    while True:
        messages = sub.pull(max_messages=10)

        to_ack = []
        for ack_id, msg in messages:
            print msg.data
            to_ack.append(ack_id)

        sub.acknowledge(to_ack)

