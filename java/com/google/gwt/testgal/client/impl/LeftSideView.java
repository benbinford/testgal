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

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.testgal.client.impl.util.Maps;
import com.google.gwt.testgal.client.impl.util.PageHistory;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;

import java.util.Map;

/**
 * The view of tests on the left side of the runner,
 * including the progress bar and test tree.
 *
 * @author Brian Slesinsky
 */
class LeftSideView extends Composite {

  private final Map<String, TreeItem> pageToItem;
  private final Tree tree;

  LeftSideView(TestSuite suite, PageHistory history) {
    pageToItem = Maps.newStringMap();
    tree = makeLeftSideTree(suite, pageToItem);

    addTestTreeSelectionHandler(tree, history);
    initWidget(tree);

    history.register(new ValueChangeHandler<String>() {
      public void onValueChange(ValueChangeEvent<String> event) {
        pageChanged(event.getValue());
      }
    });
    pageChanged(history.getCurrentPageId());
  }

  /* visible for testing */
  Tree getTree() {
    return (Tree) getWidget();
  }

  private void pageChanged(String historyToken) {
    TreeItem item = pageToItem.get(Pages.normalize(historyToken));
    if (item != null) {
      tree.setSelectedItem(item);
    }
  }

  private static Tree makeLeftSideTree(TestSuite suite, Map<String, TreeItem> pageToItem) {
    Tree tree = new Tree();

    TreeItem toc = new TreeItem(suite.getTitle());
    toc.setUserObject(Pages.CONTENTS_ID);
    pageToItem.put(Pages.CONTENTS_ID, toc);

    for (Section section : suite.getSections()) {

      TreeItem sectionItem = new TreeItem(section.getStatusView());
      String sectionPageId = section.getResultPageId();
      sectionItem.setUserObject(sectionPageId);
      pageToItem.put(sectionPageId, sectionItem);

      makeMethodTreeItems(section, sectionItem, pageToItem);

      sectionItem.setState(true);
      toc.addItem(sectionItem);
    }

    toc.setState(true);

    tree.addItem(toc);

    return tree;
  }

  private static void makeMethodTreeItems(Section section, TreeItem outItem,
      Map<String, TreeItem> pageToItem) {

    for (TestMethod method : section.getMethods()) {
      TreeItem methodItem = new TreeItem(section.getStatusView(method));
      String pageId = section.getMethodResultPageId(method);
      methodItem.setUserObject(pageId);
      pageToItem.put(pageId, methodItem);
      outItem.addItem(methodItem);
    }
  }

  private static void addTestTreeSelectionHandler(Tree testTreeView, final PageHistory history) {

    testTreeView.addSelectionHandler(new SelectionHandler<TreeItem>() {

      public void onSelection(SelectionEvent<TreeItem> event) {
        TreeItem item = event.getSelectedItem();
        Object object = item.getUserObject();
        if (object instanceof String) {
          Pages.changePage((String)object, history);
        }
      }
    });
  }
}
