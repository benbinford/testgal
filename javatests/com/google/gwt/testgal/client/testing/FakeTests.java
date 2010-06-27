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
package com.google.gwt.testgal.client.testing;

import com.google.gwt.testgal.client.impl.PageMap;
import com.google.gwt.testgal.client.impl.Section;
import com.google.gwt.testgal.client.impl.TestMethod;
import com.google.gwt.testgal.client.impl.TestMethods;
import com.google.gwt.testgal.client.impl.TestSuite;
import com.google.gwt.testgal.client.testing.FakePageHistory;
import com.google.gwt.testgal.client.impl.util.Lists;
import com.google.gwt.testgal.client.impl.util.PageHistory;
import com.google.gwt.testgal.api.shared.OutputListener;
import com.google.gwt.testgal.api.shared.TestLocal;

import junit.framework.TestCase;

/**
 * Provides utility methods for creating fake test suites.
 *
 * <p> (These suites won't receive output from ordinary JUnit tests because
 * an alternate TestLocal is used.) </p>
 *
 * @author Brian Slesinsky
 */
public class FakeTests {

  private TestLocal<OutputListener> alternateOutputListener;
  private final PageMap pages;
  private final PageHistory history;

  public FakeTests(TestCase thisTest) {
    alternateOutputListener = TestLocal.create(thisTest.getClass());
    pages = new PageMap();
    history = new FakePageHistory();
  }

  public TestSuite makeUntitledSuiteWithOneEmptySection() {
    Section section = new Section("HelloTest", null, Lists.<TestMethod>newList());
    return startSuite().addSection(section).build();
  }

  public TestSuite makeSuiteWithOnePassingTest() {
    return makeSuiteWithOneTest(TestMethods.makeTestThatPasses("testThatPasses"));
  }

  public TestSuite makeSuiteWithOneFailingTest() {
    return makeSuiteWithOneTest(TestMethods.makeTestThatFails("testThatFails",
        "This failure is expected."));
  }

  public TestSuite makeSuiteWithOneTest(TestMethod testMethod) {
    return makeSuiteWithOneTest("A fake test suite", "com.google.example.ExampleTest",
        testMethod);
  }

  public TestSuite makeSuiteWithOneTest(String suiteTitle, final String className,
      final TestMethod methodName) {

    return startSuite()
        .setTitle(suiteTitle)
        .addSection(new Section(className, null, Lists.of(methodName)))
        .build();
  }

  public TestSuite.Builder startSuite() {
    return TestSuite.builder(pages, history)
        .setOutputListeners(alternateOutputListener);
  }

  public PageMap getResultPages() {
    return pages;
  }
}
