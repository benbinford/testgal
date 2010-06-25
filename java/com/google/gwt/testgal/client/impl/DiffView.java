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
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;


/**
 * A view that shows the difference between two objects.
 *
 * @author Brian Slesinsky
 */
class DiffView extends Composite {

  DiffView(Object expected, Object actual, String caption) {

    HorizontalPanel panel = new HorizontalPanel();
    panel.addStyleName("tg-diff");
    if (expected == null || actual == null) {
      addGenericDiff(panel, expected, actual);
    } else {
      addStringDiff(panel, expected.toString(), actual.toString());
    }
    initWidget(Widgets.withCaption(panel, caption));
  }

  /**
   * Shows two objects side-by-side, without attempting to diff them.
   */
  private void addGenericDiff(Panel panel, Object expected, Object actual) {
    panel.add(Snapshots.makeSnapshot(expected));
    panel.add(Snapshots.makeSnapshot(actual));
  }

  /**
   * Shows two string objects, highlighting the point where the first difference is.
   * A tooltip shows the character value at the first difference.
   */
  private void addStringDiff(Panel panel, String expectedString, String actualString) {
    int firstDiff = findFirstDiffIndex(expectedString, actualString);
    panel.add(Snapshots.makeStringSnapshotWithError(expectedString, firstDiff));
    panel.add(Snapshots.makeStringSnapshotWithError(actualString, firstDiff));
  }

  private int findFirstDiffIndex(String expected, String actual) {
    int end = Math.min(expected.length(), actual.length());
    for (int i = 0; i < end; i++) {
      if (expected.charAt(i) != actual.charAt(i)) {
        return i;
      }
    }
    return end;
  }
}
