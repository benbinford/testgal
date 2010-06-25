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

import com.google.gwt.testgal.client.testing.FakePageHistory;
import com.google.gwt.testgal.client.impl.util.Lists;
import com.google.gwt.testgal.client.testing.TestGalTestCase;

/**
 * Shows how the section result page looks under various conditions.
 *
 * @author Brian Slesinsky
 */
public class SectionResultPageTest extends TestGalTestCase {

  private FakePageHistory history;

  @Override
  protected void gwtSetUp() throws Exception {
    super.gwtSetUp();
    history = new FakePageHistory();
  }

  public void testEmptySection() throws Exception {

    Section section = new Section("com.google.example.FooTest", null,
        Lists.<TestMethod>newList());

    out.snapshot(new SectionResultPage(section, history), "An empty section.");
  }

  public void testSectionWithOneMethod() throws Exception {

    Section section = new Section("com.google.example.FooTest", null,
        Lists.of(TestMethods.makeTestThatPasses("testSomething")));

    out.snapshot(new SectionResultPage(section, history), "A section with one test method.");
  }

}
