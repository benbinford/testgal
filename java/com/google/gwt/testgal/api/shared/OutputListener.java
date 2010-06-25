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

/**
 * A listener that's called whenever a particular test sends output.
 * Test runners implement this class to receive output from the
 * tests that use the TestOutput class.
 *
 * @author Brian Slesinsky
 */
public interface OutputListener {

  void wroteHeading(String text);

  void wroteParagraph(String line);

  void tookSnapshot(Object target, String caption);

  void wroteDemo(Object demo, String caption);

  void wroteError(String message);

  /**
   * Reports to the test runner that the test compared two objects, not necessarily
   * for equality. This allows the test runner to show a fancier diff.
   */
  void compared(Object expected, Object actual, String caption);

  /**
   * A test listener that sends basic output to System.out, to be used
   * when no test runner has registered a listener.
   */
  OutputListener DEFAULT = new OutputListener() {

    public void wroteHeading(String text) {
      System.out.println();
      System.out.println(text);
      System.out.println("===");
      System.out.println();
    }

    public void wroteParagraph(String line) {
      System.out.println(line);
    }

    public void tookSnapshot(Object target, String caption) {
      System.out.println();
      if (target == null) {
        System.out.println("[null]");
      } else {
        String image = target.getClass().getName();
        System.out.println("[image of " +  image + "]");
      }
      if (caption != null) {
        System.out.println(caption);
      }
      System.out.println();
    }

    public void wroteDemo(Object demo, String caption) {
      System.out.println("[demo not available for: " + demo + "]");
    }

    public void wroteError(String message) {
      System.out.println("*** error: " + message + " ***");
    }

    public void compared(Object expected, Object actual, String caption) {
      // no extra output, other than the failure from assertEquals()
    }
  };
}
