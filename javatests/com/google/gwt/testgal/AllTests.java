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
package com.google.gwt.testgal;

import com.google.gwt.junit.tools.GWTTestSuite;
import com.google.gwt.testgal.client.impl.GalleryRunnerTest;
import com.google.gwt.testgal.client.impl.ContentsPageTest;
import com.google.gwt.testgal.client.impl.ElementTreeViewTest;
import com.google.gwt.testgal.client.impl.LeftSideViewTest;
import com.google.gwt.testgal.client.impl.MethodResultPageTest;
import com.google.gwt.testgal.client.impl.ProgressBarTest;
import com.google.gwt.testgal.client.impl.SectionResultPageTest;
import com.google.gwt.testgal.client.impl.StatusViewTest;
import com.google.gwt.testgal.client.impl.TestSuiteTest;
import com.google.gwt.testgal.client.AsyncTest;
import com.google.gwt.testgal.shared.EmptyMethodTest;
import com.google.gwt.testgal.api.shared.TestLocalTest;

import junit.framework.Test;

/**
 * Runs TestGal's test suite using a standard JUnit test runner.
 *
 * @author Brian Slesinsky
 */
public class AllTests {

  private AllTests() {}
  
  /**
   * Defines the test suite.
   *
   * <p> Whenever you add a test, you should also update the
   * {@link com.google.gwt.testgal.gallery.client.Gallery}. </p>
   */
  public static Test suite() {
    GWTTestSuite result = new GWTTestSuite();

    // testgal.client.internal
    result.addTestSuite(TestSuiteTest.class);

    result.addTestSuite(ProgressBarTest.class);
    result.addTestSuite(ContentsPageTest.class);
    result.addTestSuite(LeftSideViewTest.class);
    result.addTestSuite(StatusViewTest.class);

    result.addTestSuite(SectionResultPageTest.class);
    result.addTestSuite(MethodResultPageTest.class);
    result.addTestSuite(ElementTreeViewTest.class);
    result.addTestSuite(GalleryRunnerTest.class);

    // testgal.client
    result.addTestSuite(AsyncTest.class);

    // testgal.shared
    result.addTestSuite(EmptyMethodTest.class);

    // testgal.testing
    result.addTestSuite(TestLocalTest.class);
    return result;
  }

}
