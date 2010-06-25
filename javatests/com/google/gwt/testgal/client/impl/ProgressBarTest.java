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
import com.google.gwt.testgal.client.testing.TestGalTestCase;

import java.util.Arrays;
import java.util.Iterator;

/**
 * Verifies that we can display a progress bar.
 *
 * @author Brian Slesinsky
 */
public class ProgressBarTest extends TestGalTestCase {

  public void testAppearance() throws Exception {
    ProgressBar bar = new ProgressBar();
    out.snapshot(bar, "The progress bar when uninitialized");
    assertEquals("tg-progress-bar", bar.getStyleName());
    checkBarStyle(bar, "");

    bar.progressChanged(0, 2, true);
    out.snapshot(bar, "The progress bar when no tests have finished");
    assertEquals("tg-progress-bar", bar.getStyleName());
    checkBarStyle(bar, "width: 0%; height: 100%; background-color: green;");

    bar.progressChanged(1, 2, true);
    out.snapshot(bar, "The progress bar when halfway finished");
    checkBarStyle(bar, "width: 50%; height: 100%; background-color: green;");

    bar.progressChanged(1, 2, false);
    out.snapshot(bar, "The progress bar when halfway finished, and a test has failed");
    checkBarStyle(bar, "width: 50%; height: 100%; background-color: red;");

    bar.progressChanged(2, 2, true);
    out.snapshot(bar, "The progress bar when all tests passed");
    checkBarStyle(bar, "width: 100%; height: 100%; background-color: green;");

    bar.progressChanged(2, 2, false);
    out.snapshot(bar, "The progress bar when all tests are finished but some failed");
    checkBarStyle(bar, "width: 100%; height: 100%; background-color: red;");
  }

  private void checkBarStyle(ProgressBar bar, String expectedStyle) {
    DivElement barDiv = (DivElement) bar.getElement().getFirstChildElement();
    // Directly getting "style" attribute has a number of browsers
    // inconsistencies:
    // - in Chrome, an extra space may be added to the end of style value.
    // - in IE, get "style" attribute returns a string "[object]" (!!)
    // - in IE, the actual style value can be converted to uppercase and there
    // may be no trailing semi-colon
    // - in IE8, the order of individual style value may change
    // To retreive the value in "style" attribute across browsers, we will need
    // to use the property "cssText" from style object instead. And to properly
    // compare the actual style with our expected style, we will need to massage
    // the browser value to normalize it.
    String styleText = barDiv.getStyle().getProperty("cssText");
    out.paragraph("progress bar styles: \"" + styleText + "\"");

    // separator for style attribute value (includes whitespace in separator to
    // normalize whitespace in the splitted values)
    final String separator = ";\\s?";

    String[] expectedValues = expectedStyle.split(separator);
    // normalize styleText:
    // - trim: to remove extra whitespace for Chrome
    // - toLowerCase: to convert all values to lower case for IE
    String[] actualValues = styleText.trim().toLowerCase().split(separator);

    // sort both array to compare them
    Arrays.sort(expectedValues);
    Arrays.sort(actualValues);

    assertEquals("styles are different", join("; ", expectedValues), join("; ",
        actualValues));
  }

  /**
   * Returns a string containing the {@code tokens}, separated by {@code
   * delimiter}. If {@code tokens} is empty, it returns an empty string.
   * <p>
   * Implementation is essentially the same as
   * {@link com.google.common.base.Joiner#join(Object[])}, simplified.
   */
  private static String join(String separator, String[] tokens) {
    StringBuilder appendable = new StringBuilder();
    Iterator<String> iterator = Arrays.asList(tokens).iterator();
    if (iterator.hasNext()) {
      appendable.append(iterator.next());
      while (iterator.hasNext()) {
        appendable.append(separator);
        appendable.append(iterator.next());
      }
    }
    return appendable.toString();
  }
}
