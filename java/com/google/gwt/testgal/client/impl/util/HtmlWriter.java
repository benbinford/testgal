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
package com.google.gwt.testgal.client.impl.util;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * Appends HTML to a subtree of an HTML page.
 *
 * <p> The subtree is detached from the HTML page by default, but if
 * it's attached and made visible, the HTML will be visible to the
 * user immediately after it's appended. </p>
 *
 * @author Brian Slesinsky
 */
public class HtmlWriter {

  /**
   * The HTML document where this writer appends DOM nodes.
   */
  private final Document doc;

  /**
   * The root of the DOM subtree where this writer appends DOM nodes.
   */
  private final Element top;

  /**
   * The element where HTML will be appended. This is always a decendent
   * of {@link #top}.
   */
  private Element appendElement;

  /**
   * Creates an HtmlWriter that writes to a new &lt;div&gt; element,
   * which can be retrieved with {@link #getTopElement}.
   */
  public HtmlWriter() {
    this(Document.get().createDivElement());
  }

  /**
   * Creates an HtmlWriter that writes to an existing element.
   */
  public HtmlWriter(com.google.gwt.dom.client.Element top) {
    this.doc = top.getOwnerDocument();
    this.top = (Element) top.cast();
    this.appendElement = this.top;
  }

  public void clear() {
    top.setInnerHTML("");
    appendElement = top;
  }

  /**
   * Returns the element where this HtmlWriter appends its output.
   */
  public Element getTopElement() {
    return top;
  }

  /**
   * Appends an element with the specified tag and makes it the new
   * append location.
   */
  public void startTag(String tagName) {
    Element newElement = (Element) doc.createElement(tagName).cast();
    appendElement.appendChild(newElement);
    appendElement = newElement;

    // browser workaround for Mozilla rendering issue; fix is based on CaptionPanel
    // TODO(skybrian): implement for non-Mozilla, use deferred binding, etc.
    if (tagName.equalsIgnoreCase("fieldset")) {
      appendElement.getStyle().setProperty("display", "none");
    }
  }

  public void startTag(String tagName, String attName, String value) {
    startTag(tagName);
    if (value != null) {
      appendElement.setAttribute(attName, value);
    }
  }

  /**
   * Changes the append location to the parent of the current location.
   * @param tagName the name of the current tag; used for a sanity check
   */
  public void endTag(String tagName) {
    assert appendElement != top && appendElement.getTagName().equalsIgnoreCase(tagName);

    // browser workaround
    if (tagName.equalsIgnoreCase("fieldset")) {
      DOM.setStyleAttribute(appendElement, "display", "");
    }

    appendElement = (Element) appendElement.getParentElement().cast();
  }

  public void text(String text) {
    appendElement.appendChild(doc.createTextNode(text));
  }

  public void textH1(String text) {
    startTag("h1");
    text(text);
    endTag("h1");
  }

  public void textH2(String text) {
    startTag("h2");
    text(text);
    endTag("h2");
  }

  public void textH3(String text) {
    startTag("h3");
    text(text);
    endTag("h3");
  }

  public void textH4(String text) {
    startTag("h4");
    text(text);
    endTag("h4");
  }

  public void textP(String text) {
    startTag("p");
    text(text);
    endTag("p");
  }

  /**
   * Appends a span of preformatted text, preserving spaces and converting
   * newlines to &lt;br&gt; tags.
   */
  public SpanElement textPreformatted(String textToAppend) {
    SpanElement span = doc.createSpanElement();

    textToAppend = textToAppend.replaceAll(" ", "\u00A0");

    int index = 0;
    while (index < textToAppend.length()) {
      int nextNewline = textToAppend.indexOf('\n', index);
      if (nextNewline == -1) {
        span.appendChild(doc.createTextNode(textToAppend.substring(index)));
        break;
      }
      if (nextNewline > index) {
        span.appendChild(doc.createTextNode(
            textToAppend.substring(index, nextNewline)));
      }
      span.appendChild(Document.get().createBRElement());
      index = nextNewline + 1;
    }

    appendElement.appendChild(span);
    return span;
  }

  /**
   * Appends an element directly to this HTML fragment. Doesn't clone the element, so if
   * it's used elsewhere, it will be removed.
   */
  public void element(com.google.gwt.dom.client.Element element) {
    appendElement.appendChild(element);
  }

  public void br() {
    appendElement.appendChild(doc.createBRElement());
  }

  // ========= end of public methods =======

  Element getAppendElement() {
    return appendElement;
  }
}
