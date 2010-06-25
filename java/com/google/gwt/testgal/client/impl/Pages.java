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

import com.google.gwt.testgal.client.impl.util.PageHistory;

/**
 * Contains static methods for working with page history.
 *
 * @author Brian Slesinsky
 */
class Pages {
  public static final String CONTENTS_ID = "toc";

  private Pages() {}

  static String makeResultPageId(Section section) {
    return section.getName();
  }

  static String makeResultPageId(Section section, TestMethod method) {
    return section.getName() + "," + method.getName();
  }

  static void changePage(String pageId, PageHistory history) {
    if (!samePage(pageId, history.getCurrentPageId())) {
      history.changePage(normalize(pageId));
    }
  }

  static boolean samePage(String pageId, String currentPageId) {
    return normalize(pageId).equals(normalize(currentPageId));
  }

  static String normalize(String pageId) {
    if (pageId == null) {
      return CONTENTS_ID;
    } else if (pageId.equals("")) {
      return CONTENTS_ID;
    } else {
      return pageId;
    }
  }
}
