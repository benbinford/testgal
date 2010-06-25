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
package com.google.gwt.testgal.gallery.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.testgal.client.AbstractTestGallery;
import com.google.gwt.testgal.client.AsyncTest;
import com.google.gwt.testgal.client.impl.GalleryRunnerTest;
import com.google.gwt.testgal.client.impl.ContentsPageTest;
import com.google.gwt.testgal.client.impl.ElementTreeViewTest;
import com.google.gwt.testgal.client.impl.LeftSideViewTest;
import com.google.gwt.testgal.client.impl.MethodResultPageTest;
import com.google.gwt.testgal.client.impl.ProgressBarTest;
import com.google.gwt.testgal.client.impl.SectionResultPageTest;
import com.google.gwt.testgal.client.impl.StatusViewTest;
import com.google.gwt.testgal.client.impl.TestSuiteTest;
import com.google.gwt.testgal.client.impl.util.HtmlWriterTest;
import com.google.gwt.testgal.shared.EmptyMethodTest;
import com.google.gwt.testgal.api.shared.TestLocalTest;

/**
 * Defines a gallery containing the test suite of TestGal itself.
 *
 * @author Brian Slesinsky
 */
public class Gallery extends AbstractTestGallery {

  /**
   * Defines the tests.
   *
   * <p> Whenever you add a test, you should also update
   * {@link com.google.gwt.testgal.AllTests}. </p>
   */
  protected void defineGallery() {

    setTitle("TestGal's Tests");

    addDescription("TestGal is an interactive test runner for a GWT application's tests. "
        + "This web page contains a gallery of tests for TestGal itself, and also serves as an "
        + "example showing what you can do with TestGal. The tests ran in your browser "
        + "when you loaded this web page, and hopefully they all passed. You can rerun the tests "
        + "at any time by reloading the page.");

    if (GWT.isScript()) {
     addDescription("If you had visited this page in hosted mode, reloading the page would also "
          + "recompile the source code before re-running the tests.");
    } else {
      addDescription("Since you are running in hosted mode, reloading the page should also "
          + "recompile the source code before re-running the tests.");
    }

    // testgal.client.internal

    addSection(GWT.create(TestSuiteTest.class),
        "Verifies that we can run tests.");

    addSection(GWT.create(ProgressBarTest.class),
        "Shows what progress bars look like at various times when running tests.");

    addSection(GWT.create(GalleryRunnerTest.class),
        "Shows what the errors look like if a gallery is configured incorrectly.");

    // left side

    addSection(GWT.create(LeftSideViewTest.class),
        "Shows what the left-side test tree looks like.");

    addSection(GWT.create(StatusViewTest.class),
        "Shows how an item in the left-side test tree appears at various times "
        + "when running a test.");

    // right side

    addSection(GWT.create(ContentsPageTest.class),
        "Shows how the test runner's table of contents will appear for different test suites.");

    addSection(GWT.create(SectionResultPageTest.class),
        "Shows the section result page under various conditions.");

    addSection(GWT.create(MethodResultPageTest.class),
        "Shows the method result page will appear as the result of running various tests.");

    addSection(GWT.create(ElementTreeViewTest.class),
        "Shows what the 'HTML Tree' tab will look like when a test takes "
        + "a snapshot of a widget or DOM element.");

    // util

    addSection(GWT.create(HtmlWriterTest.class),
        "Verifies that we can create HTML fragments.");

    // testgal.testing

    addSection(GWT.create(TestLocalTest.class),
        "Verifies that TestLocals basically work.");

    // testgal.client

    addSection(GWT.create(AsyncTest.class),
        "Verifies that we can run an asynchronous test.");

    // testgal.shared
    
    addSection(GWT.create(EmptyMethodTest.class),
        "This section just contains a lot of empty test methods, to show what "
        + "happens when a test class has a lot of methods.");
  }
}
