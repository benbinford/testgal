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

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Widget;

/**
 * A JUnit-style progress bar that turns red when something went wrong.
 *
 * @author Brian Slesinsky
 */
class ProgressBar extends Widget implements ProgressListener {

  private DivElement bar;

  ProgressBar() {
    bar = Document.get().createDivElement();
    DivElement outline = Document.get().createDivElement();
    outline.appendChild(bar);
    setElement(outline);
    setStyleName("tg-progress-bar");
  }

  public void progressChanged(int finishedCount, int totalCount, boolean looksGoodSoFar) {
    int percent = 100 * finishedCount / totalCount;
    String color = looksGoodSoFar ? "green" : "red";
    bar.getStyle().setWidth(percent, Unit.PCT);
    bar.getStyle().setHeight(100, Unit.PCT);
    bar.getStyle().setBackgroundColor(color);
  }
}
