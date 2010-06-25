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

/**
 * A command that runs in the background in multiple steps, allowing for
 * simple cooperative multitasking. A step in a YieldingCommand can schedule
 * itself repeatedly (similar to an
 * {@link com.google.gwt.user.client.IncrementalCommand IncrementalCommand})
 * or schedule other commands to run after it. It can also sleep until
 * either a timeout occurs or an event handler wakes it up.
 *
 * @author Brian Slesinsky
 */
public interface YieldingCommand {

  /**
   * Runs a single step in this command.
   * @param schedule  controls what happens after this step finishes.
   * (The schedule may or may not be empty when run() is called.)
   */
  void run(Schedule schedule);

  /**
   * Controls what happens after the run() method returns.
   *
   * <p>If the same command is scheduled multiple times, its run()
   * method will be called the same number of times.</p>
   */
  public interface Schedule {

    /**
     * Schedules the specified command's run() method to run after the
     * current step finishes. If called multiple times, the steps will be
     * executed last-in first-out. (This can be useful for scheduling cleanup
     * tasks, similar to a TearDownTestCase or a finally clause.)
     */
    Step push(YieldingCommand commandToRunNext);

    /**
     * Schedules the run() method for each command to be called after
     * the current step finishes. The commands will be run in the same
     * order as the iterator.  (However, if push() is called multiple
     * times, the sequences of commands will be executed last-in, first-out.)
     */
    void push(Iterable<? extends YieldingCommand> commands);

    /**
     * Schedules a command's run() method step to run after all other
     * steps in the schedule have run. If called multiple times, the steps
     * will be run first-in, first-out.
     */
    Step addLast(YieldingCommand commandToRunLast);

    /**
     * Sets how long this command will yield and allow other event handlers
     * to run after the current step returns.
     *
     * <p> If the current step doesn't call this method, the scheduler
     * will call the next step immediately without yielding to event handlers.
     * If the current step calls this method multiple times, the last delay
     * time will be used. </p>
     *
     * @param delayMillis how long to wait before running the next step.
     * @param reasonMessage reason for yielding (for log message)
     */
    void sleepAfterThisStep(int delayMillis, String reasonMessage);
  }

  /**
   * Represents a step in a running command.
   */
  public interface Step {

    /**
     * Delays execution of a step for at least the specified number of
     * milliseconds. The delay is measured from the time this method is called.
     *
     * <p>The actual delay may be longer if previous steps in the schedule
     * haven't finished yet, or other event handlers are running.</p>
     *
     * <p>If called multiple times, the last delay will be used. For example, the
     * caller can wake a sleeping command by setting the delay on the next step
     * to zero.</p>
     *
     * @param delayMillis How long to wait; must be non-negative.
     * @param reasonMessage reason for yielding (for log message)
     */
    public void setMinDelayBeforeThisStep(int delayMillis, String reasonMessage);
  }
}
