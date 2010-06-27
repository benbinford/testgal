/*
 * Copyright 2009 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.gwt.testgal.client.impl.util;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;

import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;

/**
 * Runs the steps of a command in sequence, optionally sleeping between steps.
 * The command (or sometimes an event handler) controls how long it sleeps.
 *
 * @author Brian Slesinsky
 */
public class Scheduler implements YieldingCommand.Schedule {

  private static int nextId = 1;

  /** The id of this scheduler; used for logging. */
  private final String id = "sched-" + nextId++;

  /**
   * The queue of steps that scheduler will run. A running step can modify the
   * queue to change what happens after it runs.
   */
  private final LinkedList<Step> stepsToRun;

  /**
   * If positive, this is the amount of time we should yield to other event handlers
   * before running the next step.
   */
  private int yieldMillis = 0;
  private String yieldReason;

  /**
   * Creates a scheduler with an empty schedule.
   */
  public Scheduler() {
    stepsToRun = new LinkedList<Step>();
  }

  // =========== modifying the schedule =========

  public YieldingCommand.Step push(YieldingCommand commandToRunNext) {
    Step result = new Step(this, commandToRunNext);
    stepsToRun.add(0, result);
    return result;
  }

  public void push(Iterable<? extends YieldingCommand> commandsToRunNext) {
    List<Step> stepsToAdd = new ArrayList<Step>();
    for (YieldingCommand command : commandsToRunNext) {
      stepsToAdd.add(new Step(this, command));
    }
    stepsToRun.addAll(0, stepsToAdd);
  }

  public YieldingCommand.Step addLast(YieldingCommand commandToRunLast) {
    Step result = new Step(this, commandToRunLast);
    stepsToRun.add(result);
    return result;
  }

  public void sleepAfterThisStep(int yieldMillis, String reason) {
    this.yieldMillis = yieldMillis;
    this.yieldReason = reason;
  }

  // ========== running the scheduler ===========

  /**
   * Starts the scheduler after yielding for the specified amount of time.
   * The scheduler will run until there are no more steps in its queue.
   * @param yieldMillis how long to yield. Must be positive.
   * @param yieldReason why we are yielding (for logging)
   */
  public void startAfterYield(int yieldMillis, String yieldReason) {
    Step first = stepsToRun.peek();
    if (first != null) {
      log("yielding for " + yieldMillis + " ms " + yieldReason);
      first.startTimer(yieldMillis, yieldReason);
    }
  }

  /**
   * Runs the next step in the scheduler immediately, ignoring any yield or
   * delay, followed by any subsequent steps up to the next yield or delay.
   * 
   * <p> Visible for testing. This method allows a test to run a {@link YieldingCommand}
   * without it needing to be an asynchronous test. </p>
   *
   * @return true if there are more steps to run.
   */
  public boolean runStepsManually() {
    if (!stepsToRun.isEmpty()) {
      log("running scheduler manually");
      yieldMillis = 0;
      yieldReason = null;
      runNextStep();
      while (!stepsToRun.isEmpty() && !stepsToRun.peek().hasTimer() && yieldMillis <= 0) {
        runNextStep();
      }
    }
    return !stepsToRun.isEmpty();
  }

  /**
   * Runs steps until the next point where the scheduler has to yield, and then
   * schedule the next step (if any) to run after the yield,
   */
  private void runUntilYield() {
    while (true) {
      if (stepsToRun.isEmpty()) {
        return;
      }

      Step nextStep = stepsToRun.peek();
      if (nextStep.hasTimer()) {
        log("yielding " + nextStep.timerReason);
        return;
      }
      if (yieldMillis > 0) {
        break;
      }
      runNextStep();
    }

    startAfterYield(yieldMillis, yieldReason);
    yieldMillis = 0;
    yieldReason = null;
  }

  private void runNextStep() {
    stepsToRun.peek().runIfNext();
  }

  private boolean isNext(Step candidate) {
    return candidate == stepsToRun.peek();
  }

  private boolean removeIfNext(Step candidate) {
    if (isNext(candidate)) {
      stepsToRun.remove();
      return true;
    } else {
      return false;
    }
  }

  void log(String message) {
    GWT.log(id + " : " + message, null);
  }

  /**
   * A step in the schedule. The step runs itself when a timer
   * goes off, but only if it's next in the schedule.
   */
  private static class Step implements YieldingCommand.Step {
    private final YieldingCommand commandToRun;
    private final Scheduler parent;

    /**
     * A timer that will try to run this step when it fires.
     * If null, this step isn't scheduled to run.
     */
    private Timer timer;
    private String timerReason;

    private Step(Scheduler parent, YieldingCommand commandToRun) {
      this.commandToRun = commandToRun;
      this.parent = parent;
    }

    public void setMinDelayBeforeThisStep(int delayMillis, String delayReason) {
      if (delayMillis < 0) {
        throw new IllegalArgumentException("delay must be non-negative");
      }

      // Override any previous timer
      cancelTimer();

      if (delayMillis > 1) {
        // set a timer that keeps track of how long we have to wait before
        // running this step. (The timer will have only have an effect if
        // the step is next to run when it goes off.)
        startTimer(delayMillis, delayReason);
      } else if (parent.isNext(this)) {
        // We must set a timer because this method can be called from an event
        // handler to reschedule the next step; without the timer, the next
        // step would never run.  (But the timer will only have an effect if
        // the step is next to run when it goes  off.)
        startTimer(1, delayReason);
      }

      // Otherwise, we don't need to start a timer because the steps
      // that run before this one will create it if needed.
    }

    /**
     * Schedules a timer to run the schedule starting at this step, provided
     * that this step is next when the timer goes off.
     * @param delayMillis must be positive
     */
    private void startTimer(int delayMillis, String delayReason) {
      if (timer == null) {
        timer = new Timer() {
          public void run() {
            boolean ran = runIfNext();
            if (ran) {
              parent.runUntilYield();
            }
          }
        };
        timerReason = delayReason;
        timer.schedule(delayMillis);
      }
    }

    /**
     * Cancels the timer associated with this step, if any.
     */
    private void cancelTimer() {
      if (timer != null) {
        timer.cancel();
        timer = null;
        timerReason = null;
      }
    }

    private boolean hasTimer() {
      return timer != null;
    }

    /**
     * If this step is next in the schedule, run it now.
     * @return true if it actually ran.
     */
    private boolean runIfNext() {
      if (!parent.removeIfNext(this)) {
        return false;
      }
      cancelTimer();
      commandToRun.run(parent);
      return true;
    }
  }
}
