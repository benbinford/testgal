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

import com.google.gwt.core.client.Duration;
import com.google.gwt.core.client.GWT;
import com.google.gwt.testgal.api.shared.OutputListener;
import com.google.gwt.testgal.api.shared.TestLocal;
import com.google.gwt.testgal.client.impl.util.Lists;
import com.google.gwt.testgal.client.impl.util.YieldingCommand;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import java.util.List;

/**
 * Runs a single test method. This happens in multiple steps so that
 * event handlers can run.
 *
 * @author Brian Slesinsky
 */
class MethodRunner implements YieldingCommand {

  private final TestMethod methodToRun;
  private final TestLocal<OutputListener> outputListeners;
  private final ResultListener resultListener;

  private final List<StatusChangeListener> statusListeners;
  private TestStatus status;
  private final MethodRunner.UncaughtHandler uncaughtHandler;

  MethodRunner(TestMethod methodToRun,
      TestLocal<OutputListener> outputListeners,
      ResultListener resultListener) {
    this.methodToRun = methodToRun;
    this.outputListeners = outputListeners;
    this.resultListener = resultListener;

    this.statusListeners = Lists.newList();
    this.status = TestStatus.NOT_STARTED;
    this.uncaughtHandler = new UncaughtHandler();
  }

  void addStatusListener(StatusChangeListener newListener) {
    this.statusListeners.add(newListener);
  }

  TestStatus getStatus() {
    return status;
  }

  /**
   * Prepares the UI for running this test and schedules the rest of the
   * test to run.
   */
  public void run(Schedule schedule) {
    takeBlame(); // not catching exceptions yet, but it's a good habit

    GWT.log("=== " + methodToRun.getName() + " ===", null);
    updateStatus(TestStatus.RUNNING);
    resultListener.testStarted(methodToRun);

    // schedule the rest of the test to run after we yield
    schedule.push(new RunTestCommand(uncaughtHandler));

    // yield to non-testgal code
    schedule.sleepAfterThisStep(10, "for UI update before test");
    blame(EventLocation.EVENT_HANDLER);
  }

  // ============ end of public methods ===========

  /**
   * Take the blame for any uncaught exceptions that might happen in TestGal itself.
   *
   * <p>This should be called on any transition from non-TestGal to TestGal code, but
   * never in a finally clause.</p>
   */
  private void takeBlame() {
    uncaughtHandler.blame(EventLocation.TESTGAL);
  }

  /**
   * Blame uncaught exceptions on the specified culprit from now on.
   *
   * <p>This should be called on any transition between TestGal and non-TestGal code,
   * but never in a finally clause.</p>
   */
  private void blame(EventLocation newLocation) {
    uncaughtHandler.blame(newLocation);
  }

  private void reportException(String message, Throwable caught, String stackTraceCaption) {
    GWT.log(methodToRun.getName() + ": " + message, caught);
    resultListener.threwException(message, caught, stackTraceCaption);
  }

  private void updateStatus(TestStatus newStatus) {
    status = newStatus;
    for (StatusChangeListener listener : statusListeners) {
      listener.statusChanged(newStatus);
    }
  }

  private static native void scrollToTop() /*-{
    $wnd.scrollTo(0, 0);
  }-*/;

  /**
   * Constructs the test, runs it, and yields before running the tearDown() method.
   * Also installs handlers to tear down and clean up after yielding.
   */
  private class RunTestCommand implements YieldingCommand {

    private final UncaughtHandler uncaughtHandler;

    public RunTestCommand(UncaughtHandler uncaughtHandler) {
      this.uncaughtHandler = uncaughtHandler;
    }

    public void run(Schedule schedule) {
      takeBlame();
      runTest(schedule);
      blame(EventLocation.EVENT_HANDLER);
    }

    private void runTest(Schedule schedule) {

      // Warning: the exception handling doesn't have tests yet
      // TODO: write test cases for all the different ways a test can fail

      final Duration duration = new Duration();

      final SingleTest currentTest;
      try {
        blame(EventLocation.CONSTRUCTOR);
        currentTest = methodToRun.makeTest();
        takeBlame();
      } catch (AssertionFailedError e) {
        handleConstructorFailed(duration, e);
        return;
      } catch (AssertionError e) {
        handleConstructorFailed(duration, e);
        return;
      } catch(Exception e) {
        handleConstructorFailed(duration, e);
        return;
      }

      // Start sending this test's output to the result page.
      // (The instance check is just paranoia in case someone
      // figures out how to run a test without using JUnit 3.)
      if (currentTest instanceof TestCase) {
        outputListeners.set((TestCase) currentTest, resultListener);
      }

      // Schedule cleanup for everything up to the setUp() method
      schedule.push(new CleanupHandler(currentTest, duration));

      final TearDownHandler tearDown = new TearDownHandler(currentTest);

      // Start catching all exceptions so we can report failures in event handlers.
      // (The handler will also be called if there's an uncaught exception in TestGal.)
      uncaughtHandler.install(tearDown);

      // run the setup method
      try {
        blame(EventLocation.SETUP);
        currentTest.__runSetUp();
        takeBlame();
      } catch (AssertionFailedError e) {
        handleSetupFailed(e);
        return;
      } catch (AssertionError e) {
        handleSetupFailed(e);
        return;
      } catch (Exception e) {
        handleSetupFailed(e);
        return;
      }

      // now that setUp is finished, tearDown can be installed.
      // TODO: should this be installed earlier? If setUp() fails should we call tearDown()?
      final Step tearDownStep = schedule.push(tearDown);

      // Install async test support.
      // This allows the current test to control when the tearDown step runs.
      currentTest.__setCallback(new SingleTest.Callback() {
        public void delayTestFinish(int timeoutMillis) {
          tearDownStep.setMinDelayBeforeThisStep(timeoutMillis,
              "to wait for event handlers of async test");
          tearDown.handleDelayTestFinishCalled();
        }

        public void finishTest() {
          tearDownStep.setMinDelayBeforeThisStep(0,
              "to run tearDown after async test finished");
          tearDown.handleFinishTestCalled();
        }
      });

      // Run the actual test
      try {
        blame(EventLocation.TEST_METHOD);
        currentTest.__runTestMethod();
        takeBlame();
        tearDown.handleTestMethodReturnedNormally();

      } catch (AssertionFailedError e) {
        handleTestMethodFailure(e, tearDown);
      } catch (AssertionError e) {
        handleTestMethodFailure(e, tearDown);
      } catch (Exception e) {
        handleTestMethodError(e, tearDown);
      }

      // TearDown will happen in a separate step, possibly after running event handlers
      // if this is an async test.
    }

    // =========== exception handlers ========

    private void handleConstructorFailed(Duration duration, Throwable caught) {
      takeBlame();

      reportException("This test failed in its constructor.", caught,
          "Stack trace from the constructor call");

      // Need to do this ourselves because tearDown handler isn't installed yet.
      updateStatus(TestStatus.FAILED);

      // The cleanup handler isn't installed yet either.
      resultListener.testFinished(duration.elapsedMillis());
    }

    private void handleSetupFailed(Throwable caught) {
      takeBlame();
      reportException("This test failed in its setUp() method.", caught,
          "Stack trace from setUp()");

      // tearDown not installed yet
      updateStatus(TestStatus.FAILED);
    }

    private void handleTestMethodFailure(Throwable e, TearDownHandler tearDown) {
      takeBlame();
      resultListener.assertionFailed(e);
      tearDown.handleTestThrewException();
    }

    private void handleTestMethodError(Exception caught, TearDownHandler tearDown) {
      takeBlame();
      reportException("The test method threw an exception.", caught,
          "Stack trace from test method");
      tearDown.handleTestThrewException();
    }
  }

  private class UncaughtHandler implements GWT.UncaughtExceptionHandler {

    private GWT.UncaughtExceptionHandler previousHandler;
    private TearDownHandler tearDown;

    /**
     * The location to blame for an uncaught exception. This should never be null
     * and should be changed whenever entering or leaving TestGal.
     */
    private EventLocation blameLocation = EventLocation.TESTGAL;

    void blame(EventLocation newBlameLocation) {
      if (blameLocation == newBlameLocation) {
        return;
      }
      blameLocation = newBlameLocation;
      resultListener.locationChanged(newBlameLocation);
    }

    void install(TearDownHandler tearDown) {
      this.previousHandler = GWT.getUncaughtExceptionHandler();
      this.tearDown = tearDown;
      GWT.setUncaughtExceptionHandler(this);
    }

    void uninstall() {
      if (previousHandler != null) {
        GWT.setUncaughtExceptionHandler(previousHandler);
        previousHandler = null;
        tearDown = null;
      }
    }

    public void onUncaughtException(Throwable e) {
      String locationToBlame = blameLocation.getDisplayName();
      takeBlame();

      if (e instanceof AssertionFailedError) {
        resultListener.assertionFailed(e);
      } else {
        resultListener.threwException("uncaught exception in " + locationToBlame,
            e, "Stack trace of uncaught exception");
      }
      tearDown.handleTestThrewException();


      // rethrow if there's another error handler; this could happen in TestGal's own tests
      if (previousHandler != null) {
        previousHandler.onUncaughtException(e);
      }

      // leaving TestGal now
      blame(EventLocation.EVENT_HANDLER);
    }
  }

  /**
   * Runs the tearDown handler and changes the test's status from RUNNING to either
   * PASSED or FAILED.
   */
  private class TearDownHandler implements YieldingCommand {

    private final SingleTest currentTest;
    private boolean async = false;

    private boolean failedYet = false;
    private boolean finishedNormally = false;

    public TearDownHandler(SingleTest currentTest) {
      this.currentTest = currentTest;
    }

    void handleDelayTestFinishCalled() {
      async = true;
    }

    void handleTestMethodReturnedNormally() {
      if (!async) {
        finishedNormally = true;
      }
    }

    void handleTestThrewException() {
      failedYet = true;
      if (status != TestStatus.RUNNING) {
        // might be a late event handler running after tearDown
        updateStatus(TestStatus.FAILED);
      }
    }

    void handleFinishTestCalled() {
      finishedNormally = true;
    }

    public void run(Schedule schedule) {
      takeBlame();

      if (async && !finishedNormally) {
        resultListener.wroteError("asynchronous test timed out");
        failedYet = true;
      }

      try {

        blame(EventLocation.TEARDOWN);
        currentTest.__runTearDown();
        takeBlame();

      } catch (AssertionFailedError e) {
        handleFailedTearDown(e);
      } catch (AssertionError e) {
        handleFailedTearDown(e);
      } catch (Exception e) {
        handleFailedTearDown(e);
      }

      updateStatus(failedYet ? TestStatus.FAILED : TestStatus.PASSED);
      blame(EventLocation.EVENT_HANDLER);
    }

    private void handleFailedTearDown(Throwable caught) {
      takeBlame();

      String message;
      if (failedYet) {
        message = "This test passed but its tearDown() method failed.";
      } else {
        message = "This test failed and its tearDown() method also failed.";
      }
      reportException(message, caught, "Stack trace from tearDown method");
      failedYet = true;
    }
  }

  /**
   * Runs after tearDown and cleans up everything up to the setUp() method.
   * (It also runs if setUp() fails so that tearDown() isn't run at all.)
   */
  private class CleanupHandler implements YieldingCommand {

    private final SingleTest currentTest;
    private final Duration testDuration;

    CleanupHandler(SingleTest currentTest, Duration testDuration) {
      this.currentTest = currentTest;
      this.testDuration = testDuration;
    }

    public void run(Schedule schedule) {
      takeBlame();

      // stop sending the test's output to the result page, and finish the page.
      if (currentTest instanceof TestCase) {
        outputListeners.unset((TestCase) currentTest, resultListener);
      }
      resultListener.testFinished(testDuration.elapsedMillis());

      uncaughtHandler.uninstall();

      // Cleanup in case the test scrolled the window.
      // (The user will still see a flicker while running the test.)
      scrollToTop();

      // (We aren't catching exceptions anymore, but if one happens anyway,
      // it's not our fault.)
      blame(EventLocation.EVENT_HANDLER);
    }
  }
}
