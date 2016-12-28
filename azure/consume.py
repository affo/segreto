import sys, json
from azure.servicebus import ServiceBusService
from utils import get_servicebus_credentials

if __name__ == '__main__':
    if len(sys.argv) < 2:
        print 'Specify the queue name, please.'
        sys.exit(1)

    Q_NAME = sys.argv[1]

    creds = get_servicebus_credentials()
    namespace = creds['Endpoint'].split('.')[0][5:]
    sak_name = creds['SharedAccessKeyName']
    sak_value = creds['SharedAccessKey']
    bus_service = ServiceBusService(
        service_namespace=namespace,
        shared_access_key_name=sak_name,
        shared_access_key_value=sak_value
    )

    def window_repr(data):
        r = ', '.join([t.get('value') for t in data.get('collect')])
        return '[' + r + ']'

    while True:
        msg = bus_service.receive_queue_message(Q_NAME, peek_lock=False)

        if not msg.body:
            continue

        start = msg.body.find('{')
        end = msg.body.rfind('}')
        body = msg.body[start : end + 1]
        try:
            body = json.loads(body)
            window_end = body.get('ts')
            body = window_repr(body)
            print body, window_end
        except ValueError:
            print body

