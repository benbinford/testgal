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

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Common list methods.
 *
 * @author Brian Slesinsky
 */
public class Lists {

  private Lists() {}

  public static <T> List<T> newList() {
    return new ArrayList<T>();
  }

  public static <T> List<T> of(T item) {
    return Collections.singletonList(item);
  }

  public static <T> List<T> concat(T firstItem, List<? extends T> items) {
    List<T> result = new ArrayList<T>();
    result.add(firstItem);
    result.addAll(items);
    return result;
  }

  public static <T> Queue<T> newQueue(List<T> contents) {
    return new LinkedList<T>(contents);
  }
}
