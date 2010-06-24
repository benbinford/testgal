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
package com.google.gwt.testgal.client.impl;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.testgal.client.testing.TestGalTestCase;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;

/**
 * Verifies that we can render an HTML element as a tree using {@link ElementTreeView}.
 *
 * @author Brian Slesinsky
 */
public class ElementTreeViewTest extends TestGalTestCase {

  public void testSingleElement() throws Exception {
    ElementTreeView view = makeView("<div>hello</div>");

    out.snapshot(view, "A div element that contains text.");

    TreeItem div = getTopItem(view);
    checkItem(div, "<DIV>", 1);
    TreeItem text = div.getChild(0);
    checkItem(text, "hello", 0);
  }

  public void testRenderAttributes() throws Exception {
    ElementTreeView view = makeView(
        "<input type=\"text\" name=\"answer\" value=\"42\" style=\"color: blue;\">"
    );

    out.snapshot(view, "An input element with three attributes.");

    TreeItem div = getTopItem(view);
    checkItem(div, "<INPUT name=\"answer\" style=\"color: blue;\" type=\"text\" value=\"42\">", 0);
  }

  // ========= end of tests ==============

  private ElementTreeView makeView(String htmlFragment) {
    ElementTreeView view = new ElementTreeView(fromHtml(htmlFragment));

    assertNull("An ElementTreeView widget is rendered too early", view.getWidget());

    view.setVisible(true);

    assertNotNull("An ElementTreeViewWidget is not rendered after being set visible",
        view.getWidget());

    return view;
  }

  private TreeItem getTopItem(ElementTreeView view) {
    Tree tree = (Tree)view.getWidget();
    out.assertEquals("An ElementTreeView has the wrong number of top-level elements",
        1, tree.getItemCount());
    return tree.getItem(0);
  }

  private void checkItem(TreeItem item, String expectedText, int expectedChildCount) {
    out.assertEquals("The text of a tree item differs", expectedText, item.getText());
    out.assertEquals("The number of children for a tree item differs",
        expectedChildCount, item.getChildCount());
  }

  /**
   * Constructs a single element from the supplied HTML.
   * The returned element will have no parent.
   */
  private static Element fromHtml(String htmlFragment) {
    DivElement temp = Document.get().createDivElement();
    temp.setInnerHTML(htmlFragment);
    if (temp.getChildNodes().getLength() != 1) {
      throw new IllegalArgumentException("not a single element: " + htmlFragment);
    }
    return (Element) temp.getFirstChildElement().cloneNode(true);
  }
}
