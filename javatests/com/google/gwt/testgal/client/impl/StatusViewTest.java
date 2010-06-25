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

import com.google.gwt.testgal.client.testing.TestGalTestCase;

/**
 * Verifies that we can display the status of a test using a {@link StatusView}.
 *
 * @author Brian Slesinsky
 */
public class StatusViewTest extends TestGalTestCase {

  public void testAppearance() throws Exception {

    checkView(TestStatus.NOT_STARTED, "testSomething:", 
        "A tree item before its test starts running");

    checkView(TestStatus.RUNNING, "testSomething:running",
        "A tree item while its test is running");

    checkView(TestStatus.PASSED, "testSomething:passed",
        "A tree item after its test passed");

    checkView(TestStatus.FAILED, "testSomething:failed",
        "A tree item after its test failed");
  }

  private void checkView(TestStatus status, String expectedText, String caption) {
    StatusView view = new StatusView("testSomething");
    view.statusChanged(status);
    out.snapshot(view, caption);
    assertEquals(expectedText, view.getElement().getInnerText());
  }

}
