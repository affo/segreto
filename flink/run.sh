#!/bin/bash
start-local-streaming.sh

i=0
for arg in "$@";
do
    if [[ "$arg" == '--args'  ]];
    then
        break
    fi

    i=$(( $i + 1  ))
done

flink_args=${@:1:$i}
jar_args=${@:$(( $i + 2 ))}

flink run "${flink_args}" flink.jar ${jar_args}

cat $FLINK_HOME/log/flink-*.out
