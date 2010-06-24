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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A section within the test suite, which contains all the tests from one test class.
 *
 * @author Brian Slesinsky
 */
public class Section {
  private static final String DEFAULT_NAME = "Untitled Section";
  private static final String DEFAULT_SUMMARY = "TODO: write a description of these tests";

  private final String name;
  private final String description;
  private final List<TestMethod> methods;
  private final StatusView statusView;
  private final Map<String, StatusView> methodStatusViews;

  /**
   * Creates a section that runs the specified test methods.
   * @param name  Usually the name of the test class.  If null, this section will be untitled.
   * @param description  Describes this section.  If null, a to-do will appear instead.
   * @param methods  The methods to run. May not be null. If empty, a to-do will appear.
   */
  public Section(String name, String description, List<TestMethod> methods) {
    this.name = name == null ? DEFAULT_NAME : name;
    this.description = description == null ? DEFAULT_SUMMARY : description;
    this.methods = methods;
    this.statusView = new StatusView(name);
    this.methodStatusViews = makeStatusViews(methods);
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public List<TestMethod> getMethods() {
    return methods;
  }

  public TestStatus getStatus(TestMethod method) {
    return getStatusView(method).getStatus();
  }

  public StatusView getStatusView() {
    return statusView;
  }

  public StatusView getStatusView(TestMethod method) {
    return methodStatusViews.get(method.getName());
  }

  public String getResultPageId() {
    return Pages.makeResultPageId(this);
  }

  public String getMethodResultPageId(TestMethod method) {
    return Pages.makeResultPageId(this, method);
  }

  private static Map<String, StatusView> makeStatusViews(List<TestMethod> methods) {
    Map<String, StatusView> result = new HashMap<String, StatusView>();
    for (TestMethod method : methods) {
      result.put(method.getName(), new StatusView(method.getName()));
    }
    return result;
  }
}
