#!/bin/bash

function send {
    echo $1 >&3
}

# This takes parameters as
# (ts, val), so parameters in odd position
# will be considered ts, others as the vals
# associated
function play {
    if [[ `expr $# % 2` -ne 0 ]]; then
        echo "The number of parameters must be even."
        return 1
    fi

    last_ts=-1
    i=0
    for arg in $@; do
        if [[ `expr $i % 2` -eq 0 ]]; then
            # ok, this is a ts
            if [[ $last_ts -gt -1 ]]; then
                delta=`expr $arg - $last_ts`
                echo "Sleeping - "$delta
                sleep $delta
            fi
            last_ts=$arg
        else
            # it is a value
            tuple="("$last_ts","$arg")"
            send $tuple
            echo "Sent - "$tuple
        fi

        i=`expr $i + 1`
    done
}
