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

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.testgal.client.impl.util.LazyPanel;
import com.google.gwt.testgal.client.impl.util.Lists;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.Label;

import java.util.Collections;
import java.util.List;

/**
 * Represents an HTML element and its children as a tree.
 *
 * @author Brian Slesinsky
 */
class ElementTreeView extends LazyPanel {

  private final Element elementToShow;

  public ElementTreeView(Element elementToShow) {
    this.elementToShow = elementToShow;
  }

  @Override
  protected Widget createWidget() {
    Tree tree = new Tree();
    tree.addItem(makeTreeItem(elementToShow));
    return tree;
  }

  static TreeItem makeTreeItem(Element element) {

    TreeItem result = new TreeItem();
    result.setWidget(makeTagLabel(element));
    NodeList<Node> nodeList = element.getChildNodes();
    for (int i = 0; i < nodeList.getLength(); i++) {
      Node node = nodeList.getItem(i);
      if (node.getNodeType() == Node.ELEMENT_NODE) {
        result.addItem(makeTreeItem((Element) node));
      } else if (node.getNodeType() == Node.TEXT_NODE) {
        result.addItem(new Label(node.getNodeValue()));
      } else {
        result.addItem("unknown node: " + node);
      }
    }
    result.setState(true);
    return result;
  }

  private static Label makeTagLabel(Element element) {
    StringBuilder tagText = new StringBuilder();
    tagText.append("<" + element.getTagName());
    for (String name : getAttributeNames(element)) {
      tagText.append(" " + name + "=" + "\"" + element.getAttribute(name) + "\"");
    }
    tagText.append(">");

    Label tagLabel = new Label(tagText.toString());
    tagLabel.addStyleName("tg-tag");
    return tagLabel;
  }

  /**
   * Attempts to get a list of all attributes of an Element. (We're limited
   * by <a href="http://www.quirksmode.org/dom/w3c_core.html#nodeinformation"
   * >browser quirks</a>.)
   */
  private static List<String> getAttributeNames(Element element) {
    try {
      List<String> result = Lists.newList();
      JsArrayString candidates = getAttributeCandidatesNative(element);
      for (int i = 0; i < candidates.length(); i++) {
        String name = candidates.get(i);
        if (hasAttribute(element, name)) {
          result.add(name);
        }
      }
      return result;
    } catch (JavaScriptException e) {
      GWT.log("unable to get attributes for: " + element +
          " in: " + element.getParentElement().getInnerHTML(),
          e);
      return Collections.emptyList();
    }
  }

  /**
   * Returns the attribute if present, or null if it isn't there. (This method is
   * here because {@link Element#getAttribute} returns an empty string for an absent
   * attribute, making it impossible to tell whether the attribute is there or
   * just empty.)
   */
  private static native boolean hasAttribute(Element elt, String name) /*-{
    return elt.getAttribute(name) != null;
  }-*/;

  /**
   * Attempts to return a superset of the attributes this element might have.
   * @return a list of attribute names, in the order they will be displayed.
   */
  private static native JsArrayString getAttributeCandidatesNative(Element elt) /*-{
    var result = [];
    for (var i = 0; i < elt.attributes.length; i++) {
      var att = elt.attributes[i];
      if (att.specified) {
        result.push(att.name);
      }
    }
    result.sort();
    return result;
  }-*/;
}
