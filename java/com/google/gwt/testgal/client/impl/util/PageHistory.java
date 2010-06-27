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

import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;

/**
 * Swappable interface to browser history.  This is here because there seems
 * not be be a way to disable the {@link History} class in GWT, and it's static.
 * In tests we need to be able to swap in a version that doesn't actually
 * update the browser history and mess up the test runner's UI.
 *
 * @author Brian Slesinsky
 */
public interface PageHistory {

  void register(ValueChangeHandler<String> listener);

  String getCurrentPageId();

  /**
   * Puts a new page id onto the page history and calls each listener.
   */
  void changePage(String pageId);

  /**
   * This version enables integration with the Browser's history, for use
   * in production code.
   */
  PageHistory ENABLED = new PageHistory() {

    public String getCurrentPageId() {
      return History.getToken();
    }

    public void changePage(String pageId) {
      History.newItem(pageId);
    }

    public void register(ValueChangeHandler<String> listener) {
      History.addValueChangeHandler(listener);
    }
  };
}
