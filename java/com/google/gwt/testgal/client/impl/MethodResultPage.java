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

import com.google.gwt.testgal.client.impl.util.HtmlWriter;
import com.google.gwt.testgal.client.impl.util.Widgets;
import com.google.gwt.testgal.client.impl.util.AppendableHtmlPanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;

/**
 * Holds the output from running a test method.
 *
 * @author Brian Slesinsky
 */
class MethodResultPage extends Composite implements ResultListener {

  private AppendableHtmlPanel outPanel;
  private HtmlWriter html;
  private boolean sentOutput = false;
  private EventLocation currentLocation = EventLocation.TESTGAL;
  private boolean inSection = false;

  public MethodResultPage() {
    outPanel = new AppendableHtmlPanel();
    outPanel.setStyleName("tg-testresult");
    initWidget(outPanel);
    html = outPanel.getWriter();
  }

  public void testStarted(TestMethod methodAboutToRun) {
    outPanel.clear();
    html.textH3(methodAboutToRun.getName());
  }

  public void wroteHeading(String text) {
    startOutput();
    html.textH4(text);
  }

  public void wroteParagraph(String text) {
    startOutput();
    html.textP(text);
  }

  public void tookSnapshot(Object target, String caption) {
    startOutput();
    outPanel.add(Widgets.withCaption(Snapshots.makeSnapshot(target), caption));
  }

  public void wroteDemo(Object demoArgument, String caption) {
    startOutput();
    outPanel.add(Widgets.withCaption(new Demo(demoArgument), caption));
  }

  public void wroteError(String message) {
    startOutput();
    html.startTag("div", "class", "tg-error");
    html.text(message);
    html.endTag("div");
  }

  public void compared(Object expected, Object actual, String caption) {
    startOutput();
    outPanel.add(new DiffView(expected, actual, caption));
  }

  public void assertionFailed(Throwable throwable) {
    startOutput();

    String message = throwable.getMessage();
    if (message == null) {

      html.startTag("div", "class", "tg-error");
      html.textP("This test failed due to an assertion that didn't have any message. You'll "
                 + "have to look at the source code.");
      html.endTag("div");

    } else {
      html.textP("This test failed due to an assertion that reported the following message:");

      html.startTag("div", "class", "tg-error");
      html.textP(throwable.getMessage());
      html.endTag("div");
    }

    appendStackTrace(throwable, "The assertion's stack trace");
  }

  public void threwException(String message, Throwable throwable, String stackTraceCaption) {
    startOutput();
    html.startTag("div", "class", "tg-error");
    html.text(message);
    html.endTag("div");
    appendStackTrace(throwable, stackTraceCaption);
  }

  public void locationChanged(EventLocation location) {
    if (inSection) {
      html.endTag("fieldset");
      inSection = false;
    }
    this.currentLocation = location;
  }

  public void testFinished(long elapsedTime) {
    if (!sentOutput) {
      html.textP("(This test didn't print any output.)");
    }

    html.startTag("div", "class", "tg-testresult-footer");
    html.text("Elapsed time: " + elapsedTime + " ms");
    html.endTag("div");
  }

  // ======== end of public methods ========

  private void startOutput() {
    sentOutput = true;
    if (!inSection && currentLocation.hasLegend()) {
      html.startTag("fieldset");

      html.startTag("legend");
      html.text(currentLocation.getLegend());
      html.endTag("legend");

      inSection = true;
    }
  }

  private void appendStackTrace(Throwable throwable, String captionText) {
    HtmlWriter out = new HtmlWriter();
    out.text(throwable.toString());
    out.br();
    for (StackTraceElement element : throwable.getStackTrace()) {
      out.textPreformatted("  at " + element);
      out.br();
    }

    HTML snapshot = Snapshots.makePlainSnapshot(out.getTopElement());

    this.outPanel.add(Widgets.withCaption(snapshot, captionText));
  }
}
