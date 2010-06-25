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
package com.google.gwt.testgal.client.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A request to run specific TestGal tests.
 *
 * @author Rod McChesney
 */
public class GalleryRequest {

  /** The query parameter specifying test classes to run */
  public static final String RUN_PARAM = "run";

  private Set<String> testClassNames = new HashSet<String>();

  public GalleryRequest(Map<String, List<String>> paramMap) {
    if (paramMap.containsKey(RUN_PARAM)) {
      testClassNames.addAll(paramMap.get(RUN_PARAM));
    }
  }

  /**
   * Returns true if the given test class name is present in a
   * "run" query parameter, or if there is no such parameter specified.
   */
  public boolean includes(String testClassName) {
    return testClassNames.isEmpty() ? true : testClassNames.contains(testClassName);
  }
}
