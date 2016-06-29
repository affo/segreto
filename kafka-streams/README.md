## How to

 * Build the producer (required only once): `make build_producer`
 * Run the dependencies: `make dep`
 * Run the kafka-streams query: `make`
 * In another terminal, consume: `make consume`
 * Now you can produce: `make produce`
 * Clean everything (if you want): `make clean`

To run different experiments, edit the file `producer/experiments.txt`.
A sample experiment could be:

```
38
39
42
43-hello
45
50-world
55
56
58
60
```

Every line should contain either an integer representing the timestamp of the
tuple (the value will be automatically assigned), or `<ts>-<value>`.
