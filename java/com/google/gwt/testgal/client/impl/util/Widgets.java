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

import com.google.gwt.dom.client.Node;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Generic utility methods for making widgets.
 *
 * @author Brian Slesinsky
 */
public class Widgets {

  private Widgets() {}

  /**
   * Given a DOM node, creates an HTML widget.
   */
  public static HTML fromDomNode(Node node) {
    HTML html = new HTML();
    html.getElement().appendChild(node);
    return html;
  }

  /** Puts a caption underneath a widget. */
  public static Widget withCaption(Widget widgetToWrap, String captionText) {
    VerticalPanel panel = new VerticalPanel();
    panel.addStyleName("tg-caption-panel");
    panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
    panel.add(widgetToWrap);
    Label label = new Label(captionText);
    panel.add(label);
    return panel;
  }

  /**
   * Wraps a widget in an extra div and sets the inner and outer heights to the specified
   * values.
   *
   * <p> Just setting the height of an element to 100% CSS unfortunately
   * doesn't include the borders or margins, so the actual width would be 100% plus the
   * 2 * the border and margin widths.  By wrapping the widget in another div without
   * borders or margins, we can set it to exactly what we want, and then use a fudge
   * factor to get the inner widget to almost the right size.  For example, if the desired
   * height is 50% and we estimate that borders and margins will fit in 2% of that for
   * most window sizes, the call might be: </p>
   *
   * <pre>
   *   fudgeHeight("50%", "98%", widget);
   * </pre>
   */
  public static <W extends Widget> W fudgeHeight(String outerHeight, String innerHeight,
      W widget) {
    widget.setHeight(innerHeight);
    FlowPanel result = new FlowPanel();
    result.setHeight(outerHeight);
    result.add(widget);
    return widget;
  }
}
