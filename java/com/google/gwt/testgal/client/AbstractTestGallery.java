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
package com.google.gwt.testgal.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.testgal.client.impl.GalleryRequest;
import com.google.gwt.testgal.client.impl.GalleryRunner;
import com.google.gwt.testgal.client.impl.Section;
import com.google.gwt.testgal.client.impl.TestClass;
import com.google.gwt.testgal.client.impl.TestMethods;
import com.google.gwt.testgal.client.impl.TestSuite;
import com.google.gwt.testgal.client.impl.util.Lists;
import com.google.gwt.testgal.client.impl.util.PageHistory;
import com.google.gwt.testgal.client.impl.util.YieldingCommands;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Defines an entry point for a gallery of tests that can be run in
 * a web browser.
 *
 * <p> Benefits: since a gallery is an ordinary GWT application,
 * you can run it in hosted mode and it will automatically recompile
 * the source code and run your tests on reload.  This can be
 * considerably faster than waiting for a GWTTestSuite to run
 * after modifying a test. Also, because the output from your tests
 * are rendered as HTML, you can add snapshots of your widgets
 * to the test output, making it considerably easier to see
 * how widgets will appear when they are in various states. </p>
 *
 * <p> (However, a test gallery is not a substitute for running your
 * tests in an ordinary JUnit test runner, because TestGal currently
 * only supports interactive use. You will also want to create a regular
 * test suite for your continuous build. Often you can run the same
 * tests both ways.) </p>
 *
 * <p> To run your tests in a gallery, you must put your tests
 * into a GWT module, which may contain tests that inherit
 * from either GWTTestCase or junit.framework.TestCase, so long as
 * GWT is able to translate them.  Then create another GWT module
 * (by convention, in a subpackage named 'gallery') that inherits
 * your test suite module and the TestGal module, and contains a
 * single entry point that inherits from this class.  You will also
 * need to copy and modify a gallery.html file. </p>
 *
 * <p> When creating a subclass, you must implement
 * {@link #defineGallery} to tell the test runner about each
 * test class you wish to run. </p>
 *
 * @author Brian Slesinsky
 */
public abstract class AbstractTestGallery implements EntryPoint {

  private final String runnerElementId;
  private final GalleryRequest galleryRequest;

  private TestSuite.Builder builder;

  public AbstractTestGallery() {
    this("tg-runner");
  }

  public AbstractTestGallery(String runnerElementId) {
    this.runnerElementId = runnerElementId;
    this.galleryRequest = new GalleryRequest(Window.Location.getParameterMap());
  }

  /**
   * Subclasses implement this method to define the tests
   * that will be run in the gallery.  Typical usage is
   * to repeatedly call {@link #addSection} to add
   * tests from any number of classes to the gallery.
   */
  protected abstract void defineGallery();

  /**
   * Sets the title of this gallery in the table of contents.
   */
  public void setTitle(String newTitle) {
    builder.setTitle(newTitle);
  }

  /**
   * Adds one paragraph to the description appearing just below the title in the
   * test gallery's table of contents.
   */
  public void addDescription(String paragraph) {
    builder.addDescription(paragraph);
  }

  /**
   * Adds all the tests in a test class to the gallery.  This method
   * should only be called from within defineGallery(), and the first
   * argument should be a call to GWT.create() with a class literal.
   * For example:
   * <pre>
   *   protected void defineGallery() {
   *     addSection(GWT.create(RotatingInflammableWidgetTest.class),
   *       "Verifies the widget can rotate and burst into flames.");
   *   }
   * </pre>
   *
   * @param resultFromGwtCreate  The result from calling GWT.create()
   * on a class literal of a class that extends GWTTestCase or
   * junit.framework.TestCase and is translatable by GWT.
   *
   * @param summary  A summary paragraph to appear underneath the test name
   * in the gallery's table of contents.  If null, a to-do message
   * will appear in the output to write a summary.
   */
  protected void addSection(Object resultFromGwtCreate, String summary) {
    if (resultFromGwtCreate instanceof TestClass) {
      TestClass testClass = (TestClass) resultFromGwtCreate;
      if (galleryRequest.includes(testClass.__getName())) {
        builder.addSection(new Section(testClass.__getName(), summary,
            testClass.__getTestMethods()));
      }
    } else {
      builder.addSection(makeInvalidSection(resultFromGwtCreate));
    }
  }

  public void onModuleLoad() {

    RootPanel panel = RootPanel.get(runnerElementId);
    if (panel == null) {
      GWT.log("unable to create test runner because this page doesn't have an element with id="
              + runnerElementId, null);
      return;
    }

    YieldingCommands.start(new GalleryRunner(this, panel, PageHistory.ENABLED));
  }

  /**
   * Called by the test runner to define all the tests in this gallery.
   */
  public void addToSuite(TestSuite.Builder out) {
    this.builder = out;
    defineGallery();
    this.builder = null;
  }

  private Section makeInvalidSection(Object invalidArgument) {
    String galleryClassName = getClass().getName();
    if (invalidArgument == null) {
      return makeInvalidSection("Invalid Section",
          "TODO: fix " + galleryClassName + " so that it doesn't pass null to addSection().");
    } else if (invalidArgument instanceof Class) {
      Class<?> theClass = (Class<?>) invalidArgument;
      return makeInvalidSection(theClass.getName(),
          "TODO: fix " + galleryClassName + " so that it calls addSection(GWT.create(...))");
    } else {
      return makeInvalidSection("Invalid Section",
          "TODO: fix bad call to addSection() in " + galleryClassName);
    }
  }

  private static Section makeInvalidSection(String name, String todo) {
    return new Section(name, todo,
        Lists.of(TestMethods.makeTestThatFails("(generated by test runner)", todo)));
  }
}
