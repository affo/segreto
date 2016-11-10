_This project is licensed under the terms of the Apache License Version 2.0_

This repository contains the codebase to run simple experiments
on different SPEs.

The built-in query is a very simple one and it is built in order to apply
a window to the input data and output the window content every time it
is evaluated.

Every system has its own folder that provides a `Makefile` to easily
run the experiments and a `README.md` file that explains how to do it.

The folder `SECRETSimulator` containes a SPE simulator created at ETH University
and available [here](https://www.systems.ethz.ch/research/SECRET/).

The folder `SECRETExperiments` contains Markdown files that highlight each of the
experiments conceived by the SECRET team.
