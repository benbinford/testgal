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

/**
 * Reflective representation of a JUnit test method. The TestGal GWT module
 * generates a subclass of this interface corresponding to each test method
 * in a JUnit test class. The test runner creates an instance of TestMethod
 * for every method in the gallery, before running any tests.
 *
 * @see com.google.gwt.testgal.rebind.TestSourceGenerator
 *
 * @author Brian Slesinsky
 */
public interface TestMethod {
  String getName();

  /**
   * Creates the instance that can be used to actually run this test method.
   * (This method should be called each time the test is actually run.)
   */
  SingleTest makeTest();
}
