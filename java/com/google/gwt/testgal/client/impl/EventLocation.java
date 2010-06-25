/*
 * Copyright 2010 Google Inc.
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
 * The part of a test where an event happened.
 *
 * @author Brian Slesinsky
 */
enum EventLocation {
  TESTGAL("TestGal", null),
  CONSTRUCTOR("a test's constructor", "constructor"),
  SETUP("a test's setUp method", "setUp"),
  TEST_METHOD("a test method", null),
  TEARDOWN("a test's tearDown method", "tearDown"),
  EVENT_HANDLER("an event handler that was called during a test",
      "event handlers");

  private String displayName;
  private String legend;

  EventLocation(String displayName, String legend) {
    this.displayName = displayName;
    this.legend = legend;
  }

  public String getDisplayName() {
    return displayName;
  }

  public String getLegend() {
    return legend;
  }

  public boolean hasLegend() {
    return legend != null;
  }
}
