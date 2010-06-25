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

/**
 * Verifies that test runners will be able to register resources
 * in a {@link TestLocal}, where helper classes used in
 * tests will be able to pick them up.
 *
 * @author Brian Slesinsky
 */
public class TestLocalTest extends TestCase {

  private TestLocal<String> local;

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    local = TestLocal.create(TestLocalTest.class);
  }

  public void testBasics() throws Exception {
    TestLocalTest someTest = new TestLocalTest();

    assertEquals("default", local.get(someTest, "default"));

    local.set(someTest, "hello");
    assertEquals("hello", local.get(someTest, "default"));
    assertEquals("default", local.get(new TestLocalTest(), "default"));
    local.unset(someTest, "hello");

    assertEquals("default", local.get(someTest, "default"));
  }
}
