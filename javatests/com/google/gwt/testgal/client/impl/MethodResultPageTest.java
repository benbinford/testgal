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

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.testgal.api.shared.ComparisonFailedError;
import com.google.gwt.testgal.client.testing.TestGalTestCase;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HorizontalSplitPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;

import junit.framework.AssertionFailedError;

import java.util.Arrays;

/**
 * Verifies that we can render output to a {@link MethodResultPage}.
 *
 * @author Brian Slesinsky
 */
public class MethodResultPageTest extends TestGalTestCase {

  private MethodResultPage page;

  @Override
  protected void gwtSetUp() throws Exception {
    super.gwtSetUp();
    page = new MethodResultPage();
    page.testStarted(TestMethods.makeTestThatPasses("testSomething"));
  }

  public void testPassingTestWithNoOutput() throws Exception {
    startTestMethod();
    page.locationChanged(EventLocation.TEARDOWN);
    finishPage("A test that passed but sent no output.");
  }

  public void testFailingTestWithNoOutput() throws Exception {
    startTestMethod();

    AssertionFailedError failure = new AssertionFailedError("something failed");
    failure.fillInStackTrace();
    page.assertionFailed(failure);

    page.locationChanged(EventLocation.TEARDOWN);

    finishPage("A test that failed due to an assertion.");
  }

  public void testFailInSetupWithNoOutput() throws Exception {
    page.locationChanged(EventLocation.CONSTRUCTOR);
    page.locationChanged(EventLocation.SETUP);

    AssertionFailedError failure = new AssertionFailedError("something failed");
    failure.fillInStackTrace();
    page.assertionFailed(failure);

    finishPage("A test that failed in setUp() due to an assertion.");
  }

  public void testFailingTestWithNoOutputAndNoMessage() throws Exception {
    startTestMethod();

    AssertionFailedError failure = new AssertionFailedError(null);
    failure.fillInStackTrace();
    page.assertionFailed(failure);

    page.locationChanged(EventLocation.TEARDOWN);

    finishPage("A test that failed due to an assertion that didn't send a message.");
  }

  public void testOutputInRegularTest() throws Exception {
    startTestMethod();

    page.wroteHeading("Heading 1");
    page.wroteParagraph("Some text. This is a test of what a long line looks like. "
                   + "Blah blah blah blah blah blah quux.");

    page.locationChanged(EventLocation.TEARDOWN);

    finishPage("A test that writes a headline and some text to the output page.");
  }

  public void testOutputInAllPartsOfRegularTest() throws Exception {

    page.locationChanged(EventLocation.CONSTRUCTOR);

    page.wroteParagraph("wrote something in constructor");

    page.locationChanged(EventLocation.SETUP);

    page.wroteParagraph("wrote something in setup");

    page.locationChanged(EventLocation.TEST_METHOD);

    page.wroteHeading("Heading 1");
    page.wroteParagraph("Some text. This is a test of what a long line looks like. "
                        + "Blah blah blah blah blah blah quux.");

    page.locationChanged(EventLocation.TEARDOWN);

    page.wroteParagraph("wrote something in teardown");

    finishPage("A test that writes a headline and some text to the output page.");
  }

  public void testJavaObjectSnapshots() throws Exception {
    startTestMethod();

    page.tookSnapshot(null, "A snapshot of a null.");

    page.tookSnapshot("hello world",  "A string.");
    page.tookSnapshot("hello\nworld\n",  "A multiline string");
    page.tookSnapshot("<b>hello</b> world",  "A string containing HTML");
    
    page.tookSnapshot("hello  world",
        "A string, with two spaces between the words.");

    page.tookSnapshot(Arrays.asList("a", "b", "c"),
        "A list.");

    finishPage("A test that takes a snapshot of some Java objects.");
  }

  public void testWidgetSnapshots() throws Exception {
    startTestMethod();

    page.tookSnapshot(new Label("hello"), "A label.");
    page.tookSnapshot(new Button("don't press this"), "A button.");

    ListBox box = new ListBox();
    box.addItem("first");
    box.addItem("second");
    box.addItem("third");
    page.tookSnapshot(box, "A list box");

    finishPage("A test that takes a snapshot of some widgets.");
  }

  public void testFailedStringComparison() throws Exception {
    startTestMethod();

    comparisonFailed("This is A.", "This is B.", "Strings that differ");
    finishPage("The result of a test that failed due to a difference between two strings.");
  }

  public void testUnexpectedNullComparison() throws Exception {
    startTestMethod();
    comparisonFailed("This is A.", null, "Shouldn't be null");
    finishPage("The result of a test that failed due to an unexpected null.");
  }

  public void testExpectedNullComparison() throws Exception {
    startTestMethod();
    comparisonFailed(null, "This is B.", "Should be null");
    finishPage("The result of a test that failed because it didn't get a null.");
  }

  public void testDifferentLabelsComparison() throws Exception {
    startTestMethod();
    comparisonFailed(new Label("label one"), new Label("label two"), "Labels that differ");
    finishPage("The result of a test that failed due to a failed comparison between two labels.");
  }

  public void testPageWithButtonDemo() throws Exception {
    startTestMethod();
    page.wroteDemo(makeButtonDemo(), "A button demo");
    finishPage("The result of a test that outputs a demo");
  }

  public void testDemos() throws Exception {
    out.demo(makeButtonDemo(), "A Button demo");
    out.demo(makeDisclosureDemo(), "A DisclosurePanel demo");
    out.demo(makeHorizontalSplitPanelDemo(), "A HorizontalSplitPanel demo");
    out.demo(makeTabPanelDemo(), "A TabPanel demo");
  }

  // ============== end of tests =============

  private void startTestMethod() {
    page.locationChanged(EventLocation.CONSTRUCTOR);
    page.locationChanged(EventLocation.SETUP);
    page.locationChanged(EventLocation.TEST_METHOD);
  }

  private void finishPage(String caption) {
    page.locationChanged(EventLocation.TESTGAL);
    page.testFinished(12345);
    out.snapshot(page, caption);
  }

  private void comparisonFailed(Object expected, Object actual, String caption) {
    page.compared(expected, actual, caption);
    AssertionFailedError t = new ComparisonFailedError(caption, expected, actual);
    t.fillInStackTrace();
    page.assertionFailed(t);
  }

  private Widget makeButtonDemo() {
    Button demo = new Button("Press Me");
    demo.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        Window.alert("hello");
      }
    });
    return demo;
  }

  private Widget makeDisclosureDemo() {
    DisclosurePanel demo = new DisclosurePanel("Something is inside");
    demo.setAnimationEnabled(true);
    demo.setContent(new Label("hello"));
    return demo;
  }

  private Widget makeHorizontalSplitPanelDemo() {
    HorizontalSplitPanel panel = new HorizontalSplitPanel();
    panel.setLeftWidget(new Label("left side"));
    panel.setRightWidget(new Label("right side"));
    panel.setSplitPosition("50%");
    panel.setHeight("50pt");
    return panel;
  }

  private Widget makeTabPanelDemo() {
    TabPanel panel = new TabPanel();
    panel.add(new Label("left contents"), "left");
    panel.add(new Label("right contents"), "right");
    panel.selectTab(0);
    return panel;
  }
}
