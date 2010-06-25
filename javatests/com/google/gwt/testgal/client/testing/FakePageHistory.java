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
package com.google.gwt.testgal.client.testing;

import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.testgal.client.impl.util.Lists;
import com.google.gwt.testgal.client.impl.util.PageHistory;

import java.util.List;

/**
 * A page history that isn't actually connected to the browser,
 * but still calls listeners when the page changes. This is
 * useful in tests where we don't actually want to change
 * the browser's history.
 *
 * @author Brian Slesinsky
 */
public class FakePageHistory implements PageHistory {

  private final List<String> history = Lists.newList();
  private HandlerManager handlers = new HandlerManager(null);

  public FakePageHistory() {
    history.add("");
  }

  public String getCurrentPageId() {
    return history.get(history.size() - 1);
  }

  public void changePage(String pageId) {
    history.add(pageId);
    ValueChangeEvent.fire(new HasValueChangeHandlers<String>() {
      @Override
      public void fireEvent(GwtEvent<?> event) {
        handlers.fireEvent(event);
      }

      @Override
      public HandlerRegistration addValueChangeHandler(
          ValueChangeHandler<String> handler) {
        throw new UnsupportedOperationException();
      }
    }, pageId);
  }

  public void register(ValueChangeHandler<String> listener) {
    handlers.addHandler(ValueChangeEvent.getType(), listener);
  }
}
