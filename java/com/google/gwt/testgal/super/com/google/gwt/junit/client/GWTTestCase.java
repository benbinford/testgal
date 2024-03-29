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
package com.google.gwt.junit.client;

import com.google.gwt.junit.client.impl.JUnitResult;

import junit.framework.TestCase;

/**
 * The translatable version of GWTTestCase that's used by TestGal.
 * This is here only because delayTestFinish() and finishTest() are final
 * in GWT's translatable version and we need to override them.
 *
 * @author Brian Slesinsky
 */
public abstract class GWTTestCase extends TestCase {

  public GWTTestCase() {
  }

  public final void addCheckpoint(String msg) {
  }

  public boolean catchExceptions() {
    return true;
  }

  public final void clearCheckpoints() {
  }

  public final String[] getCheckpoints() {
    return null;
  }

  public abstract String getModuleName();

  protected void delayTestFinish(int timeoutMillis) {
  }

  protected void finishTest() {
  }

  protected void gwtSetUp() throws Exception {
  }

  protected void gwtTearDown() throws Exception {
  }

  @Override
  protected final void setUp() throws Exception {
    gwtSetUp();
  }

  protected boolean supportsAsync() {
    return true;
  }

  @Override
  protected final void tearDown() throws Exception {
    gwtTearDown();
  }

  // the following methods are needed to avoid a compile warning in Benchmark.

  public void __doRunTest() {}

  protected JUnitResult __getOrCreateTestResult() {
    return null;
  }
}
