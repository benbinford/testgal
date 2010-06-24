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

import com.google.gwt.testgal.client.testing.FakePageHistory;
import com.google.gwt.testgal.client.impl.util.Lists;
import com.google.gwt.testgal.client.impl.util.PageHistory;
import com.google.gwt.testgal.client.testing.FakeTests;
import com.google.gwt.testgal.client.testing.TestGalTestCase;
import com.google.gwt.testgal.api.shared.TestOutput;

/**
 * Verifies that we can print a table of contents for the test gallery.
 *
 * @author Brian Slesinsky
 */
public class ContentsPageTest extends TestGalTestCase {

  private TestOutput out;
  private FakeTests fakeTests;
  private PageHistory history;

  @Override
  protected void gwtSetUp() throws Exception {
    super.gwtSetUp();
    out = new TestOutput(this);
    fakeTests = new FakeTests(this);
    history = new FakePageHistory();
  }

  public void testUntitledEmptySuite() throws Exception {
    TestSuite suite = fakeTests.startSuite().build();
    out.snapshot(makeContents(suite),
        "An empty table of contents");
  }

  public void testSectionWithoutSummaryAndTests() throws Exception {
    TestSuite suite = fakeTests.makeUntitledSuiteWithOneEmptySection();
    out.snapshot(makeContents(suite),
        "A table of contents with one empty section");
  }

  public void testSectionWithoutSummary() throws Exception {
    TestSuite suite = fakeTests.makeSuiteWithOneTest("My Test Suite", "HelloTest",
        TestMethods.makeTestThatPasses("testSomething"));

    out.snapshot(makeContents(suite),
        "A table of contents with one section that has no summary");
  }

  public void testSectionWithSummary() throws Exception {

    Section section = new Section("HelloTest", "A test that says hello.",
        Lists.of(TestMethods.makeTestThatPasses("testSomething")));

    TestSuite suite = fakeTests.startSuite()
        .setTitle("My Test Suite")
        .addSection(section).build();

    out.snapshot(makeContents(suite),
        "A table of contents with one section that has a summary");
  }

  // =========== end of tests =============

  private ContentsPage makeContents(TestSuite suite) {
    return new ContentsPage(suite, history);
  }
}
