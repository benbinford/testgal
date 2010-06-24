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
package com.google.gwt.testgal.client.impl;

import com.google.gwt.testgal.client.impl.util.Lists;
import com.google.gwt.testgal.client.impl.util.YieldingCommand;

import java.util.List;
import java.util.Queue;

/**
 * Runs all the tests in a section.
 *
 * @author Brian Slesinsky
 */
class RunSectionCommand implements YieldingCommand {

  // state for running tests
  private TestStatus status;
  private final Queue<MethodRunner> remainingRunners;
  private final StatusChangeListener statusListener;

  RunSectionCommand(List<MethodRunner> methodRunners, StatusChangeListener statusListener) {
    this.status = TestStatus.NOT_STARTED;
    this.remainingRunners = Lists.newQueue(methodRunners);
    this.statusListener = statusListener;
  }

  public void run(Schedule schedule) {

    final MethodRunner nextMethod = remainingRunners.poll();
    if (nextMethod == null) {
      return;
    }

    // set this section's status to running
    if (status != TestStatus.FAILED) {
      updateStatus(TestStatus.RUNNING);
    }

    // schedule section-level cleanup after the test method finishes
    schedule.push(new YieldingCommand() {
      public void run(Schedule schedule) {
        onTestMethodFinished(schedule, nextMethod);
      }
    });

    // run the test method
    schedule.push(nextMethod);
  }

  // =========== end of public methods ========

  private void onTestMethodFinished(Schedule schedule, MethodRunner methodThatFinished) {
    // if the method failed, the section failed too
    if (methodThatFinished.getStatus() == TestStatus.FAILED) {
      updateStatus(TestStatus.FAILED);
    }

    if (remainingRunners.isEmpty()) {
      onAllTestMethodsFinished(schedule);
    } else {
      // if there's another method to run, goto run().
      schedule.push(this);
    }
  }

  private void onAllTestMethodsFinished(Schedule schedule) {
    if (status == TestStatus.RUNNING) {
      updateStatus(TestStatus.PASSED);
    }
    schedule.sleepAfterThisStep(5, "for UI update after section");
  }

  private void updateStatus(TestStatus newStatus) {
    status = newStatus;
    statusListener.statusChanged(newStatus);
  }
}
