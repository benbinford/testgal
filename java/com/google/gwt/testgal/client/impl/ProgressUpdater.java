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

/**
 * Keeps track of how many tests have finished and reports status to a listener, such
 * as the {@link ProgressBar}.
 *
 * @author Brian Slesinsky
 */
class ProgressUpdater implements StatusChangeListener {

  /* null until initialized */
  private ProgressListener listener;

  private int totalTestsCount = 0;
  private int finishedTestsCount = 0;
  private boolean looksGoodSoFar = true;

  public void setProgressListener(ProgressListener newListener) {
    this.listener = newListener;
  }

  public void reset(int totalTestsCount) {
    this.totalTestsCount = totalTestsCount;
    finishedTestsCount = 0;
    looksGoodSoFar = true;
    sendProgress();
  }

  public void statusChanged(TestStatus newStatus) {
    switch(newStatus) {
      case PASSED:
        break;
      case FAILED:
        looksGoodSoFar = false;
        break;
      default:
        // Some other transition, such as starting to run a test.
        // The totals shouldn't change.
        return;
    }
    finishedTestsCount++;
    sendProgress();
  }

  private void sendProgress() {
    if (listener != null) {
      listener.progressChanged(finishedTestsCount, totalTestsCount, looksGoodSoFar);
    }
  }
}
