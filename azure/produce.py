# Courtesy of ../google-cloud-dataflow/io
import json, random
from utils import *
from azure.servicebus import ServiceBusService

MAX_SIM_TUPLES = 5
MAX_GAP = 5

if __name__ == '__main__':
    import sys, time
    STARTUP_TIME = int(round(time.time() * 1000))

    if len(sys.argv) < 2:
        print 'Specify event hub name, please.'
        sys.exit(1)

    HUB_NAME = sys.argv[1]
    dense = 1
    if len(sys.argv) >= 3:
        dense = int(sys.argv[2])

    creds = get_servicebus_credentials()
    namespace = creds['Endpoint'].split('.')[0][5:]
    sak_name = creds['SharedAccessKeyName']
    sak_value = creds['SharedAccessKey']
    bus_service = ServiceBusService(
        service_namespace=namespace,
        shared_access_key_name=sak_name,
        shared_access_key_value=sak_value
    )

    def pub(ts, tid):
        tapp = STARTUP_TIME + ts * 1000
        value = '({},t-{})'.format(ts, tid)
        data = dict(tapp=tapp, value=value)
        msg = json.dumps(data, encoding='utf-8')
        bus_service.send_event(HUB_NAME, msg, device_id="mylaptop")
        print '>>>', msg

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

