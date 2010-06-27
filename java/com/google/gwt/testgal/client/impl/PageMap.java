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

import com.google.gwt.user.client.ui.Widget;

import java.util.HashMap;
import java.util.Map;

/**
 * A collection of all right-side pages in the test runner.
 *
 * @author Brian Slesinsky
 */
public class PageMap {

  /**
   * Maps page id's to result pages.
   */
  private final Map<String, Widget> pages;

  public PageMap() {
    this.pages = new HashMap<String,Widget>();
  }

  public void put(ContentsPage newValue) {
    pages.put(Pages.CONTENTS_ID, newValue);
  }

  public void put(Section section, SectionResultPage newValue) {
    pages.put(Pages.makeResultPageId(section), newValue);
  }

  public void put(Section section, TestMethod method, MethodResultPage newValue) {
    pages.put(Pages.makeResultPageId(section, method), newValue);
  }

  public Widget get(String pageId) {
    return pages.get(pageId);
  }

  public SectionResultPage getSectionResultPage(Section section) {
    String pageId = Pages.makeResultPageId(section);
    return (SectionResultPage) get(pageId);
  }

  public MethodResultPage getMethodResultPage(Section section, TestMethod method) {
    String pageId = Pages.makeResultPageId(section, method);
    return (MethodResultPage) get(pageId);
  }
}
