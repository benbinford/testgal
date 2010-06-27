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
package com.google.gwt.testgal.api.client;

import com.google.gwt.core.client.EntryPoint;

/**
 * An entry point that does nothing, to use in a TestCase module to satisfy GWT's
 * requirement that a module has at least one entry point.
 *
 * @author Brian Slesinsky
 */
public class EmptyEntryPoint implements EntryPoint {
  // TODO(skybrian) is this still needed?

  public void onModuleLoad() {
  }
}
