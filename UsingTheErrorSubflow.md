The idea of the error subflow is to be a generic and simple solution for how to handle errors in common message flows. The currently implementation targets flows with MQ input nodes and persistent messages.

## Installation ##
  1. Download the [message flow project](http://wmb-util.googlecode.com/svn/wmb-util-flows/) and the [Java project](http://wmb-util.googlecode.com/svn/wmb-util-flows-java/).
  1. Add the downloaded projects to your workspace.
  1. Add a reference to the error subflow project from your message flow project
  1. Add the subflow to your flow, for example my drag-n-drop
  1. Connect the catch terminal of your MQ input node to the Catch input terminal on the subflow
  1. Do the same for the failure terminals

## What it does ##
During an error, the message will be rolled back to the input node and go to the Catch terminal. From there, it will go to the connected subflow and the message will be logged.

Depending on the value of backout threshold set on the input queue, the message might be retried a number of times. It is also possible to set a sleep time in the subflow configuration, the message will then wait before being retried.

For each failed retry, the message will be passed on the catch terminal. After which it will make the same number of retries on the Failure terminal. After that, the message will be stored on the backout queue set on the input queue, or possible the dead-letter queue.

## Caveats ##
Note that this error flow is only useful in some scenarios. Carefully investigate if it does what you want before using it.