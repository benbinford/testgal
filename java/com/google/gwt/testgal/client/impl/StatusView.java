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

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;

/**
 * A widget in the test tree that shows whether a test passed or failed.
 *
 * @author Brian Slesinsky
 */
class StatusView extends Composite implements StatusChangeListener {

  private final InlineLabel statusLabel;
  private TestStatus status;

  public StatusView(String testName) {
    this.status = TestStatus.NOT_STARTED;
    InlineLabel statusLabel = new InlineLabel(status.getLabelText());
    statusLabel.addStyleName("tg-status-indicator");
    this.statusLabel = statusLabel;

    FlowPanel view = new FlowPanel();
    view.addStyleName("tg-status");
    view.add(new InlineLabel(testName + ":"));
    view.add(statusLabel);
    initWidget(view);
  }

  public TestStatus getStatus() {
    return status;
  }

  public void statusChanged(TestStatus newStatus) {
    this.status = newStatus;
    statusLabel.setText(newStatus.getLabelText());

    if (newStatus == TestStatus.PASSED) {
      statusLabel.addStyleName("tg-passed");
    } else {
      statusLabel.removeStyleName("tg-passed");
    }

    if (newStatus == TestStatus.FAILED) {
      statusLabel.addStyleName("tg-failed");
    } else {
      statusLabel.removeStyleName("tg-failed");
    }
  }
}
