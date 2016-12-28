#!/bin/bash

function open_socket {
    # STREAMER_IP contains the IP of the streaming job.
    # It is passed with -e at container startup
    exec 3<>/dev/tcp/$STREAMER_IP/9999
}

function send {
    echo $1 >&3
}

function play {
    last_ts=-1
    i=0

    for arg in $@; do
        if [[ $last_ts -gt -1 ]]; then
            delta=`expr $arg - $last_ts`
            echo "Sleeping - "$delta
            sleep $delta
        fi
        last_ts=$arg
        tuple="("$last_ts",t"$i")"
        send $tuple
        echo "Sent - "$tuple

        i=`expr $i + 1`
    done
}
