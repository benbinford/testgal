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

import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.Widget;

/**
 * A listener that adds a CSS style on hover.
 *
 * @author Brian Slesinsky
 */
public class HoverListener implements MouseOverHandler, MouseOutHandler {

  private final String hoverStyle;

  public HoverListener(String hoverStyle) {
    this.hoverStyle = hoverStyle;
  }

  public void onMouseOver(MouseOverEvent event) {
    Widget sender = (Widget) event.getSource();
    sender.addStyleName(hoverStyle);
  }

  public void onMouseOut(MouseOutEvent event) {
    Widget sender = (Widget) event.getSource();
    sender.removeStyleName(hoverStyle);
  }
}
