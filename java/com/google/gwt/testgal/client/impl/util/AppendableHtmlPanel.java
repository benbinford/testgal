/*
 * Copyright 2010 Google Inc.
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

import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A panel that allows arbitrary HTML and widgets to be appended to it,
 * via an internal HtmlWriter.
 *
 * @author Brian Slesinsky
 */
public class AppendableHtmlPanel extends ComplexPanel {

  private HtmlWriter out;

  public AppendableHtmlPanel() {
    out = new HtmlWriter();
    setElement(out.getTopElement());
  }

  @Override
  public void clear() {
    super.clear();
    out.clear();
  }

  public HtmlWriter getWriter() {
    return out;
  }

  @Override
  public void add(Widget child) {
    add(child, out.getAppendElement());
  }
}
