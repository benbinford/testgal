/*
 * Copyright 2010 Google Inc.
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
package com.google.gwt.testgal.client;

import com.google.gwt.testgal.client.testing.TestGalTestCase;
import com.google.gwt.testgal.testing.shared.EchoServiceAsync;
import com.google.gwt.testgal.testing.shared.EchoService;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.core.client.GWT;

import junit.framework.Assert;

/**
 * Demonstrates that an asynchronous test can run in TestGal.
 *
 * @author Brian Slesinsky
 */
public class AsyncTest extends TestGalTestCase {

  public void testFinishTestLater() throws Exception {
    Timer timer = new Timer() {
      public void run() {
        out.paragraph("some output in an event handler");
        finishTest();
      }
    };
    
    delayTestFinish(500);
    timer.schedule(10);
    out.paragraph("some output in the test method");
  }

  // enable this test to see what a failure looks like
  public void XXXtestAssertionFailureInCallback() throws Exception {
    Timer timer = new Timer() {
      public void run() {
        Assert.fail("failed!");
      }
    };

    delayTestFinish(500);
    timer.schedule(10);
  }

  public void testRpc() throws Exception {
    EchoServiceAsync echo = (EchoServiceAsync) GWT.create(EchoService.class);
    ((ServiceDefTarget)echo).setServiceEntryPoint(
        GWT.getModuleBaseURL() + "echo");

    echo.echo("hello", new AsyncCallback<String>() {

      public void onSuccess(String result) {
        testRpcPart2(result);
      }

      public void onFailure(Throwable caught) {
        out.paragraph("onFailure called");
        fail("call failed: " + caught);
      }
    });
    
    delayTestFinish(500);
  }

  private void testRpcPart2(String result) {
    out.paragraph("onSuccess called");
    assertEquals("hello", result);
    finishTest();
  }
}
