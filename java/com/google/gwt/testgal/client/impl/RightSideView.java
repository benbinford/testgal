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

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.testgal.client.impl.util.PageHistory;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * The view where the selected result page is displayed.
 *
 * @author Brian Slesinsky
 */
class RightSideView extends Composite {

  private final SimplePanel panel;
  private final PageMap pages;

  RightSideView(PageMap pages, PageHistory history) {
    this.pages = pages;

    panel = new SimplePanel();
    panel.addStyleName("tg-rightside");
    initWidget(panel);

    history.register(new ValueChangeHandler<String>() {
      @Override
      public void onValueChange(ValueChangeEvent<String> event) {
        pageChanged(event.getValue());
      }
    });
    pageChanged(history.getCurrentPageId());
  }

  Widget getCurrentPage() {
    return panel.getWidget();
  }

  private void pageChanged(String historyToken) {
    String pageId = Pages.normalize(historyToken);
    Widget newPage = pages.get(pageId);
    if (newPage != null) {
      panel.setWidget(newPage);
    }
  }
}
