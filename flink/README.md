## Flink
### Running an Experiment

 * Write the experiment you want to run in `experiments/exp.current` (see
    `experiments/exp.current.sample` for an example).
 * Run `make proxy` in one terminal.
 * In another terminal, run `make W="3 1"` passing window size and slide as parameters.
 * You will se, in proxy's terminal, something like

    ```
    First connection OK - 4
    Second connection OK - 5
    ```

    Press `ENTER`.

 * If you want to see the output while the experiment is running, open another terminal
    and run `make attach_to_out`.
