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

import com.google.gwt.testgal.client.impl.util.Widgets;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalSplitPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * The two-panel view that displays the test runner.
 *
 * @author Brian Slesinsky
 */
class GalleryView extends Composite {

  GalleryView(ProgressBar progress, LeftSideView leftSide, RightSideView rightSide) {

    FlowPanel all = new FlowPanel();
    all.setStyleName("tg-top");

    // leaving 2% for borders
    all.add(Widgets.fudgeHeight("5%", "3%", progress));
    all.add(Widgets.fudgeHeight("95%", "93%", makeSplitPanel(leftSide, rightSide)));

    initWidget(all);
  }

  private static Widget makeSplitPanel(Widget leftView, Widget rightView) {
    HorizontalSplitPanel split = new HorizontalSplitPanel();
    split.addStyleName("tg-splitpanel");
    split.setSplitPosition("45%");
    split.add(leftView);
    split.add(rightView);
    return split;
  }
}
