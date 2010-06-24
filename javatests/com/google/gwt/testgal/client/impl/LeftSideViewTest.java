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
import com.google.gwt.testgal.client.testing.FakeTests;
import com.google.gwt.testgal.client.testing.TestGalTestCase;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;

/**
 * Verifies that we can display a progress bar and a tree of tests
 * using {@link LeftSideView}.
 *
 * @author Brian Slesinsky
 */
public class LeftSideViewTest extends TestGalTestCase {

  private FakeTests fakeTests;
  private PageMap pages;
  private FakePageHistory history;
  private RightSideView rightSide;

  @Override
  protected void gwtSetUp() throws Exception {
    super.gwtSetUp();
    fakeTests = new FakeTests(this);
    pages = fakeTests.getResultPages();
    history = new FakePageHistory();
    rightSide = new RightSideView(pages, history);
  }

  public void testAppearance() throws Exception {
    LeftSideView view = makeLeftSide(fakeTests.makeSuiteWithOnePassingTest());

    out.snapshot(view, "A view with a single test");
  }

  public void testSelectSection() throws Exception {
    TestSuite suite = fakeTests.makeSuiteWithOnePassingTest();
    LeftSideView view = makeLeftSide(suite);

    selectSection(view, 0);

    out.assertSame("right side should show selected section",
        getSectionResultPage(suite, 0), rightSide.getCurrentPage());
  }

  public void testSelectMethod() throws Exception {
    TestSuite suite = fakeTests.makeSuiteWithOnePassingTest();
    LeftSideView view = makeLeftSide(suite);

    selectMethod(view, 0, 0);

    out.assertSame("right side should show selected method",
        getMethodResultPage(suite, 0, 0), rightSide.getCurrentPage());
  }

  // =============== end of tests ==============

  private LeftSideView makeLeftSide(TestSuite suite) {
    return new LeftSideView(suite, history);
  }

  private static void selectSection(LeftSideView view, int sectionIndex) {
    Tree tree = view.getTree();
    TreeItem item = tree.getItem(0).getChild(sectionIndex);
    tree.setSelectedItem(item);
  }

  private static void selectMethod(LeftSideView view, int sectionIndex, int methodIndex) {
    Tree tree = view.getTree();
    TreeItem item = tree.getItem(0).getChild(sectionIndex).getChild(methodIndex);
    tree.setSelectedItem(item);
  }

  private Widget getSectionResultPage(TestSuite suite, int sectionIndex) {
    Section section = suite.getSections().get(sectionIndex);
    return pages.getSectionResultPage(section);
  }

  private MethodResultPage getMethodResultPage(TestSuite suite, int sectionIndex, int methodIndex) {
    Section section = suite.getSections().get(sectionIndex);
    TestMethod method = section.getMethods().get(methodIndex);
    return pages.getMethodResultPage(section, method);
  }
}
