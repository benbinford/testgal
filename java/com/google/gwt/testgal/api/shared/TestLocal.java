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
package com.google.gwt.testgal.api.shared;

import junit.framework.TestCase;

import java.util.IdentityHashMap;

/**
 * Provides a variable that's local to each JUnit test in a test suite.
 * A test runner can use a TestLocal to pass values to helper classes
 * used from within tests without using a global variable, which would cause
 * the test runner to be non-reentrant and make it difficult for the
 * test runner to run its own tests.
 *
 * <p> Each TestLocal has to be stored as a singleton somewhere known
 * both to the test runner and the helper class.  For example,
 * see {@link TestOutput#OUTPUT_LISTENERS}. </p>
 *
 * <p> TestGal doesn't need thread-safety because it runs in GWT, but
 * this class is thread-safe anyway. </p>
 *
 * @author Brian Slesinsky
 */
public class TestLocal<R> {
  private String callerName;
  private final IdentityHashMap<TestCase, R> values;

  protected TestLocal(String callerName) {
    this.callerName = callerName;
    this.values = new IdentityHashMap<TestCase, R>();
  }

  /**
   * Makes a resource available to the specified test. Test runners
   * should call this method before running each test.
   *
   * @throws IllegalStateException if a resource has already been
   * registered.
   */
  public synchronized void set(TestCase test, R newValue) {
    if (values.containsKey(test)) {
      throw new IllegalStateException(
          "Value already set for " + test.getName()
          + " in TestLocal created by " + callerName);
    }
    values.put(test, newValue);
  }

  /**
   * Removes a resource from the registry. Test runners should call this
   * method after a test finishes.
   *
   * @throws IllegalStateException if the resource wasn't previously registered.
   */
  public synchronized void unset(TestCase test, R oldValue) {
    if (values.get(test) != oldValue) {
      throw new IllegalStateException(
          "Value not previously set for " + test.getName()
          + " in TestLocal created by " + callerName);
    }
    values.remove(test);
  }

  public synchronized R get(TestCase testCase, R defaultResource) {
    R result = values.get(testCase);
    return result == null ? defaultResource : result;
  }

  /**
   * Creates a new registry.
   * @param caller  the class that created the registry, for error messages.
   */
  public static <R> TestLocal<R> create(Class<?> caller) {
    return new TestLocal<R>(caller.getName());
  }
}
