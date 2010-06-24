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

import com.google.gwt.testgal.client.AbstractTestGallery;
import com.google.gwt.testgal.client.impl.util.PageHistory;
import com.google.gwt.testgal.client.impl.util.YieldingCommand;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Duration;

/**
 * A command that creates a UI and runs all tests in an AbstractTestGallery.
 *
 * @author Brian Slesinsky
 */
public class GalleryRunner implements YieldingCommand {

  private final Panel panel;
  private final PageHistory history;
  private final PageMap pages;
  private final TestSuite suite;

  public GalleryRunner(AbstractTestGallery galleryToRun, Panel displayPanel, PageHistory history) {
    this.panel = displayPanel;
    this.history = history;
    this.pages = new PageMap();
    this.suite = makeTestSuite(galleryToRun, pages, history);
  }

  public void run(Schedule schedule) {
    GWT.log("===== GalleryRunner started =====", null);

    Duration uiDuration = new Duration();
    TestSuite suite = makeTestSuiteWithUI();
    GWT.log("creating TestGal UI took " + uiDuration.elapsedMillis() + " ms", null);

    final Duration runTestsDuration = new Duration();
    schedule.push(new YieldingCommand() {
      public void run(Schedule schedule) {
        GWT.log("running tests took " + runTestsDuration.elapsedMillis() + " ms", null);
        GWT.log("===== GalleryRunner finished =====", null);
      }
    });
    
    schedule.push(suite.getRunAllTestsCommand());
  }

  private TestSuite makeTestSuiteWithUI() {

    Window.setTitle(suite.getTitle()); // replace "Loading..." message in title
    panel.getElement().setInnerHTML(""); // clear "Loading..." message in panel

    pages.put(new ContentsPage(suite, history));

    RightSideView rightSide = new RightSideView(pages, history);

    ProgressBar bar = new ProgressBar();
    suite.setProgressListener(bar);

    LeftSideView leftSide = new LeftSideView(suite, history);

    GalleryView main = new GalleryView(bar, leftSide, rightSide);

    panel.add(main);
    Pages.changePage(history.getCurrentPageId(), history);

    return suite;
  }

  /**
   * Creates a new TestSuite that contains the tests defined by a gallery.
   */
  public static TestSuite makeTestSuite(AbstractTestGallery abstractTestGallery, PageMap pages,
      PageHistory history) {
    TestSuite.Builder builder = TestSuite.builder(pages, history);
    abstractTestGallery.addToSuite(builder);
    return builder.build();
  }
}
