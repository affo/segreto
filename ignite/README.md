## Ignite
### Running an Experiment

 * Write the experiment you want to run in `experiments/exp.current` (see
    `experiments/exp.current.sample` for an example).
 * Run `make` in one terminal.
 * In another terminal, run `make W=3 B=1` passing in window size and slide as parameters.
 * Run `make exp` to launch the experiment and press `ENTER` when you are ready.

## WARNING

When running `make` be sure to be connected to the internet, otherwise the
schema for the xml configuration won't be available and you will fall into

```
...
org.xml.sax.SAXParseException; lineNumber: 24; columnNumber: 71;
cvc-elt.1: Cannot find the declaration of element 'beans'
...
```

