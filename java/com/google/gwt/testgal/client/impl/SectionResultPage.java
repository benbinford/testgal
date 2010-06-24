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
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;

/**
 * The page that's displayed when you click on the name of a test class.
 *
 * @author Brian Slesinsky
 */
class SectionResultPage extends Composite {

  SectionResultPage(Section section, PageHistory history) {

    FlowPanel panel = new FlowPanel();
    panel.addStyleName("tg-section");
    writePage(panel, section, history);

    initWidget(panel);
  }

  private void writePage(FlowPanel panel, Section section, PageHistory history) {
    HtmlWriter writer = new HtmlWriter(panel.getElement());
    writer.textH2(section.getName());

    // Add the description and a link to rerun the single test.
    FlowPanel descriptionPanel = new FlowPanel();
    descriptionPanel.addStyleName("tg-section");
    descriptionPanel.add(new InlineLabel(section.getDescription()));
    GalleryRequestBuilder galleryRequestBuilder = new GalleryRequestBuilder(
        Window.Location.createUrlBuilder());
    descriptionPanel.add(galleryRequestBuilder
        .setRunOne(section.getName())
        .setAnchorText("Rerun this test")
        .addStyleName("tg-rerun-link")
        .build());
    panel.add(descriptionPanel);

    if (section.getMethods().isEmpty()) {
      writer.textP("TODO: write some tests");
      return;
    }

    writer.textP("Contents of this section:");

    for (TestMethod method : section.getMethods()) {
      panel.add(makeMethodLink(section, method, history));
    }
  }

  private static Label makeMethodLink(final Section section, final TestMethod method,
      final PageHistory history) {

    Label header = new Label(method.getName());
    header.addStyleName("tg-section-method");
    header.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        Pages.changePage(Pages.makeResultPageId(section, method), history);
      }
    });
    return header;
  }

}
