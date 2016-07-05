#!/bin/sh
size=${1:-3}
slide=${2:-3}

echo "size $size, slide $slide"

make clean && \
    sleep 1 && \
    make W="$size $slide" && \
    sleep 3 && \
    make consume
