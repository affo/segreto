## Storm
### Running an Experiment

 * Write the experiment you want to run in `experiments/exp.current` (see
    `experiments/exp.current.sample` for an example).
 * Run `make proxy` in one terminal.
 * In another terminal, run `make COUNT=0 W="3 1"` passing `0` or `1` in case you want to use time or count windows,
    plus window size and slide as parameters.
 * You will se, in proxy's terminal, something like

    ```
    First connection OK - 4
    Second connection OK - 5
    ```

    Press `ENTER`.

