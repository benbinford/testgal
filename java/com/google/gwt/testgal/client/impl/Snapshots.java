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

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.TableElement;
import com.google.gwt.testgal.client.impl.util.HoverListener;
import com.google.gwt.testgal.client.impl.util.HtmlWriter;
import com.google.gwt.testgal.client.impl.util.Widgets;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

/**
 * Utility methods for creating snapshots of Java objects.
 *
 * @author Brian Slesinsky
 */
class Snapshots {
  static final HoverListener HOVER_LISTENER =
      new HoverListener("tg-rendered-widget-hover");

  private Snapshots() {}

  /** Creates a snapshot, not including the caption. */
  static Widget makeSnapshot(Object target) {
    if (target == null) {
      HtmlWriter out = new HtmlWriter();
      out.startTag("i");
      out.text("null");
      out.endTag("i");
      return makePlainSnapshot(out.getTopElement());
    } else if (target instanceof UIObject) {
      return makeFancySnapshot(((UIObject) target).getElement());
    } else if (target instanceof Element) {
      return makeFancySnapshot((Element) target);
    } else {
      HtmlWriter out = new HtmlWriter();
      out.textPreformatted(target.toString());
      return makePlainSnapshot(out.getTopElement());
    }
  }

  /**
   * Makes a snapshot displaying the specified DOM node with a border around it.
   * The snapshot doesn't include a caption.
   */
  static HTML makePlainSnapshot(Node picture) {
    TableElement table = Document.get().createTableElement();
    table.setAttribute("class", "tg-java-object-snapshot");
    table.insertRow(0).insertCell(0).appendChild(picture);

    return Widgets.fromDomNode(table);
  }

  private static Widget makeFancySnapshot(Element originalElement) {
    Element element = (Element) originalElement.cloneNode(true);
    String html = attachToOuterElement(element).getInnerHTML();

    HtmlWriter writer = new HtmlWriter();
    writer.startTag("div", "class", "tg-rendered-widget");
    writer.element(element);
    writer.endTag("div");
    HTML rendered = Widgets.fromDomNode(writer.getTopElement());
    rendered.addMouseOverHandler(HOVER_LISTENER);
    rendered.addMouseOutHandler(HOVER_LISTENER);

    TabPanel panel = new TabPanel();
    panel.addStyleName("tg-snapshot-tabs");
    panel.add(rendered, "Rendered");
    panel.add(new ElementTreeView(element), "HTML Tree");
    panel.add(new ElementSourceView(html), "HTML Source");

    panel.selectTab(0);
    return panel;
  }

  /**
   * Creates a snapshot of a String with error highlighting starting at the specified index.
   */
  static Widget makeStringSnapshotWithError(String text, int errorIndex) {
    Element div = Document.get().createDivElement();
    HtmlWriter writer = new HtmlWriter(div);

    writer.textPreformatted(text.substring(0, errorIndex));
    SpanElement lastPart = writer.textPreformatted(text.substring(errorIndex));
    lastPart.setAttribute("class", "tg-diff-selected");
    if (errorIndex < text.length()) {
      lastPart.setAttribute("title", "differing char: " + (int)text.charAt(errorIndex));
    }

    return makePlainSnapshot(div);
  }

  private static Element attachToOuterElement(Element elementToDisplay) {
    Element outer = Document.get().createDivElement();
    outer.appendChild(elementToDisplay);
    return outer;
  }
}
