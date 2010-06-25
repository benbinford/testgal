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

import junit.framework.AssertionFailedError;

/**
 * The API for running a single test.  The TestGal GWT module generates
 * subclasses of JUnit test classes that implement this interface.
 * SingleTest instances are normally created just before running a test
 * method and thrown away immediately after.
 *
 * <p> Since the person writing a test case might not know how
 * the code generation works, these method names all start  with "__" to
 * avoid any conflicts with other methods that someone might have added
 * to a test case. </p>
 *
 * @see com.google.gwt.testgal.rebind.TestSourceGenerator
 *
 * @author Brian Slesinsky
 */
public interface SingleTest {

  void __runSetUp() throws Exception;
  void __runTestMethod() throws Exception, AssertionFailedError;
  void __runTearDown() throws Exception;
  void __setCallback(Callback newCallback);

  interface Callback {
    void delayTestFinish(int timeoutMillis);
    void finishTest();
  }
}
