#!/bin/bash
./proxy &

sleep 2
exec 3<>/dev/tcp/localhost/9999

echo
echo "Press ENTER when the Flink job is ready"
echo

read

./exp

echo "Sleeping for 5 secs"
sleep 5