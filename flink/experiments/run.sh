#!/bin/bash
./proxy &

sleep 2
exec 3<>/dev/tcp/localhost/9999

echo "Sleeping for a minute, so you can run the Flink job..."
sleep 60

./exp

echo "Sleeping for 5 secs"
sleep 5
