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
package com.google.gwt.testgal.client.impl.util;

import com.google.gwt.testgal.client.testing.TestGalTestCase;

/**
 * Verifies that HtmlWriter creates elements correctly.
 *
 * @author Brian Slesinsky
 */
public class HtmlWriterTest extends TestGalTestCase {

  public void testWritePreformattedTextWithInnerNewlines() throws Exception {

    HtmlWriter writer = new HtmlWriter();
    writer.textPreformatted("Some\n\nText");
    out.assertEquals("Output from textPreformatted differs",
        "<span>Some<br><br>Text</span>", writer.getTopElement().getInnerHTML());
  }

  public void testWritePreformattedTextWithNewlinesAtEnd() throws Exception {

    HtmlWriter writer = new HtmlWriter();
    writer.textPreformatted("SomeText\n\n");
    out.assertEquals("Output from textPreformatted differs",
        "<span>SomeText<br><br></span>", writer.getTopElement().getInnerHTML());
  }

}
