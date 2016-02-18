Compile SecretMainExecuter.java

```
javac SecretMainExecuter.java
```

Run `SecretMainExecuter`. If no arguments is specified, it simply runs the simulator for VLDB Journal experiments.

```
java SecretMainExecuter
```

It takes following arguments to run a specific settings:

 * arg0: input data file can be found inside data directory
 * arg1: application time of the first tuple in the input stream
 * arg2: name of the engine
 * arg3: size of the window in the query
 * arg4: slide of the window in the query
 * arg5: window type s.t 0 for time-based, 1 for tuple-based windows

There are four packages under src folder.

 1. basic:  includes basic classes such as query, interval, tuple, stream
 2. engine: includes engine objects: Coral8, Oracle CEP, STREAM, StreamBase
 3. params: includes implementation of model parameters
 4. data:   stores source files of the simulator. Each line in the file represents a tuple having following format `<tsys, tid, tapp, bid>`.
