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
import com.google.gwt.testgal.client.impl.util.Scheduler;
import com.google.gwt.testgal.client.testing.FakeTests;
import com.google.gwt.testgal.client.testing.TestGalTestCase;

import java.util.Arrays;
import java.util.List;

/**
 * Verifies that a {@link TestSuite} reports status when tests are running.
 *
 * @author Brian Slesinsky
 */
public class TestSuiteTest extends TestGalTestCase {

  private FakeTests fakeTests;
  private RecordingListener listener;

  @Override
  protected void gwtSetUp() throws Exception {
    super.gwtSetUp();
    fakeTests = new FakeTests(this);
    listener = new RecordingListener();
  }

  public void testRunOnePassingTest() throws Exception {
    checkRunOneTest(fakeTests.makeSuiteWithOnePassingTest(), "1/1 ok");
  }

  public void testRunOneFailingTest() throws Exception {
    checkRunOneTest(fakeTests.makeSuiteWithOneFailingTest(), "1/1 failed");
  }

  public void testRunOneTestThatFailsInSetUp() throws Exception {
    TestSuite suiteToRun =
        fakeTests.makeSuiteWithOneTest(
            TestMethods.makeTestThatFailsInSetup("testThatFailsInSetup",
                "This failure is expected."));
    checkRunOneTest(suiteToRun, "1/1 failed");
  }

  // ============== end of tests ===========

  private void checkRunOneTest(TestSuite suite, String resultMessage) {
    Scheduler scheduler = start(suite);
    checkResetProgress(scheduler);
    checkPrepareTest(scheduler);
    checkRunLastTest(scheduler, resultMessage);
  }

  private void checkResetProgress(Scheduler scheduler) {
    assertTrue("sending initial progress should return true", scheduler.runStepsManually());
    listener.checkLogMessages("0/1 ok");
  }

  private void checkPrepareTest(Scheduler scheduler) {
    assertTrue("preparing test should return true", scheduler.runStepsManually());
    listener.checkLogMessages();
  }

  private void checkRunLastTest(Scheduler scheduler, String result) {
    assertFalse("running last test should return false", scheduler.runStepsManually());
    listener.checkLogMessages(result);
  }

  private Scheduler start(TestSuite suite) {
    suite.setProgressListener(listener);
    Scheduler scheduler = new Scheduler();
    scheduler.push(suite.getRunAllTestsCommand());
    return scheduler;
  }

  private class RecordingListener implements ProgressListener {
    final List<String> log = Lists.newList();

    public void progressChanged(int finishedCount, int totalCount, boolean looksGoodSoFar) {
      String progress = finishedCount + "/" + totalCount;
      String status = looksGoodSoFar ? "ok" : "failed";
      log.add(progress + " " + status);
    }

    void checkLogMessages(String... expected) {
      assertEquals(Arrays.asList(expected), log);
      log.clear();
    }
  }
}
