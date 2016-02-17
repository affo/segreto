#!/bin/bash
./proxy &

echo "Sleeping for 20 secs"
sleep 5

exec 3<>/dev/tcp/localhost/9999

./exp

echo "Sleeping for 5 secs"
sleep 5
