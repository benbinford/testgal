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

import junit.framework.Assert;

/**
 * Provides a way to construct simple test methods.
 *
 * @author Brian Slesinsky
 */
public class TestMethods {

  private TestMethods() {}

  public static TestMethod makeTestThatPasses(String testName) {
    return new SimpleTestMethod(testName, new SimpleSingleTest(null, false));
  }

  public static TestMethod makeTestThatFails(String testName, String failureMessage) {
    return new SimpleTestMethod(testName, new SimpleSingleTest(failureMessage, false));
  }

  public static TestMethod makeTestThatFailsInSetup(String testName, String failureMessage) {
    return new SimpleTestMethod(testName, new SimpleSingleTest(failureMessage, true));
  }

  private static class SimpleTestMethod implements TestMethod {

    private final String testName;
    private final SingleTest singleTest;

    public SimpleTestMethod(String testName, SingleTest singleTest) {
      this.testName = testName;
      this.singleTest = singleTest;
    }

    public String getName() {
      return testName;
    }

    public SingleTest makeTest() {
      return singleTest;
    }
  }

  private static class SimpleSingleTest implements SingleTest {
    private final String failureMessage;
    private final boolean failInSetUp;

    SimpleSingleTest(String failureMessage, boolean failInSetUp) {
      this.failureMessage = failureMessage;
      this.failInSetUp = failInSetUp;
    }

    public void __runSetUp() {
      if (failInSetUp) {
        Assert.fail(failureMessage);
      }
    }

    public void __runTestMethod() {
      if (failureMessage != null) {
        Assert.fail(failureMessage);
      }
    }

    public void __runTearDown() {
    }

    public void __setCallback(Callback newCallback) {
    }
  }
}
