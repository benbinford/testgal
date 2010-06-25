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
package com.google.gwt.testgal.api.shared;

import junit.framework.TestCase;

/**
 * Provides methods that a test case can use to send output to the test runner.
 * If the test is running in a gallery then output will go to the test's
 * result page in the gallery. Otherwise, output goes to stdout.  Example usage:
 *
 * <pre>
 * protected void setUp() {
 *   out = new TestOutput(this);
 * }
 *
 * public void testSomething() {
 *   out.paragraph("hello!");
 * }
 * </pre>
 *
 * @author Brian Slesinsky
 */
public class TestOutput {

  /**
   * Contains the output listener to use for each test.
   */
  public static final TestLocal<OutputListener> OUTPUT_LISTENERS =
      TestLocal.create(TestOutput.class);

  private final TestCase testCase;

  public TestOutput(TestCase testCase) {
    this.testCase = testCase;
  }

  /**
   * Appends a heading to this test's output.  If running in a
   * test gallery, it will appear in corresponding result page for
   * this test.  Otherwise, it will be printed to stdout.
   */
  public void heading(String text) {
    getListener().wroteHeading(text);
  }

  /**
   * Appends a paragraph of text to this test's output.  If running in a
   * test gallery, it will appear in corresponding result page for
   * this test.  Otherwise, it will be printed to stdout.
   */
  public void paragraph(String line) {
    getListener().wroteParagraph(line);
  }

  /**
   * Appends a picture of the supplied object to this test's output.
   * A test may attempt to take a picture of anything, but the test
   * runner's display may vary.  A reasonable minimal implementation
   * is to call Object.toString(), but there may be special handling
   * for other types.  For example, the test gallery has special
   * handling for Widgets and DOM elements.
   *
   * @param target  the object to display.  May be null.
   * @param caption  the caption that will appear under the snapshot.  May be null for
   * no caption.
   */
  public void snapshot(Object target, String caption) {
    getListener().tookSnapshot(target, caption);
  }

  /**
   * Adds an interactive demo to this test's output.  If a test runner
   * doesn't know how to create a demo for the supplied object, it will
   * instead append a default message indicating that it's unavailable.
   *
   * <p> For a test gallery, you can use this method to append a Widget to
   * the test output page, so that the user can interact with.  (For slow
   * widgets, a LazyPanel is recommended so that startup cost will only be
   * paid if the user looks at that page.) </p>
   *
   * @param demo  the demo to display.  May be null.
   * @param caption  the caption that will appear under the demo.  May be null for
   * no caption.
   */
  public void demo(Object demo, String caption) {
    getListener().wroteDemo(demo, caption);
  }

  /**
   * Appends a comparison between two objects to the test's output.
   * The test runner may display a fancier diff, if supported for
   * the compared objects.
   */
  public void compare(Object expected, Object actual, String caption) {
    getListener().compared(expected, actual, caption);
  }

  /**
   * This is just like Assert.assertEquals() except that if the two objects differ,
   * the test runner may display the difference in a nicer way.
   */
  public void assertEquals(String caption, Object expected, Object actual) {
    if (expected == null && actual == null)
      return;
    if (expected != null && expected.equals(actual))
      return;
    compare(expected, actual, caption);
    throw new ComparisonFailedError(caption, expected, actual);
  }


  public void assertSame(String caption, Object expected, Object actual) {
    if (expected != actual) {
      compare(expected, actual, caption);
      throw new ComparisonFailedError(caption, expected, actual);
    }
  }

  // ==== end of public methods ===

  private OutputListener getListener() {
    return OUTPUT_LISTENERS.get(testCase, OutputListener.DEFAULT);
  }
}
