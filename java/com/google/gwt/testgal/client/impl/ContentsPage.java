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

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.testgal.client.impl.util.HtmlWriter;
import com.google.gwt.testgal.client.impl.util.PageHistory;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;

/**
 * Displays the table of contents of a test suite.
 *
 * @author Brian Slesinsky
 */
class ContentsPage extends Composite {

  ContentsPage(TestSuite suite, PageHistory history) {

    FlowPanel panel = new FlowPanel();
    panel.addStyleName("tg-contents");
    writePage(panel, suite, history);

    initWidget(panel);
  }

  private static void writePage(Panel panel, TestSuite suite, PageHistory history) {
    HtmlWriter html = new HtmlWriter(panel.getElement());
    html.textH1(suite.getTitle());
    panel.add(suite.getDescription());

    // Add the link to rerun all tests.
    GalleryRequestBuilder galleryRequestBuilder = new GalleryRequestBuilder(
        Window.Location.createUrlBuilder());
    panel.add(galleryRequestBuilder
        .setRunAll()
        .setAnchorText("Run all tests")
        .build());

    if (suite.getSections().isEmpty()) {
      html.textP("TODO: add some test classes to this gallery.");
      return;
    }

    html.textP("Contents of this gallery:");

    for (Section section : suite.getSections()) {
      panel.add(makeSectionLink(section, history));

      html.startTag("div", "class", "tg-contents-section");
      html.text(section.getDescription());
      html.endTag("div");

      if (section.getMethods().size() == 0) {
        html.textP("TODO: write some tests");
      }
    }
  }

  private static Label makeSectionLink(final Section section, final PageHistory history) {
    Label header = new Label(section.getName());
    header.addStyleName("tg-contents-heading");
    header.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        Pages.changePage(Pages.makeResultPageId(section), history);
      }
    });
    return header;
  }
}
