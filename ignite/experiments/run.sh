#!/bin/bash
source utils.sh

open_socket

echo "Press ENTER to start the experiment"
read

./exp

echo "Experiment finished. Closing..."
sleep 5
