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

import com.google.gwt.testgal.api.shared.OutputListener;
import com.google.gwt.testgal.api.shared.TestLocal;
import com.google.gwt.testgal.api.shared.TestOutput;
import com.google.gwt.testgal.client.impl.util.YieldingCommand;
import com.google.gwt.testgal.client.impl.util.YieldingCommands;
import com.google.gwt.testgal.client.impl.util.HtmlWriter;
import com.google.gwt.testgal.client.impl.util.Lists;
import com.google.gwt.testgal.client.impl.util.PageHistory;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

import java.util.List;
import java.util.ArrayList;

/**
 * A suite of tests to run and display in a gallery.
 *
 * @author Brian Slesinsky
 */
public class TestSuite {

  private static final String DEFAULT_TITLE = "Untitled";
  private static final String DEFAULT_DESCRIPTION = "TODO: describe the contents of this gallery";

  private final String title;
  private final Widget description;
  private final List<Section> sections;
  private final ProgressUpdater progressUpdater;
  private final YieldingCommand runAllTests;

  protected TestSuite(String title, Widget description, List<Section> sections,
      ProgressUpdater progressUpdater, YieldingCommand runAllTests) {
    this.title = title;
    this.description = description;
    this.sections = sections;
    this.progressUpdater = progressUpdater;
    this.runAllTests = runAllTests;
  }

  public void setProgressListener(ProgressListener listener) {
    this.progressUpdater.setProgressListener(listener);
  }

  public String getTitle() {
    return title;
  }

  public Widget getDescription() {
    return description;
  }

  public List<Section> getSections() {
    return sections;
  }

  public int getTestMethodCount() {
    return countTestMethods(sections);
  }

  public YieldingCommand getRunAllTestsCommand() {
    return runAllTests;
  }

  public static Builder builder(PageMap pages, PageHistory history) {
    return new Builder(pages, history);
  }

  // ===== end of public methods =====

  private static int countTestMethods(List<Section> sections) {
    int total = 0;
    for (Section section: sections) {
      total += section.getMethods().size();
    }
    return total;
  }

  public static class Builder {
    private final PageMap pages;
    private final PageHistory history;

    private String title = DEFAULT_TITLE;
    private final List<String> descriptionParagraphs = Lists.newList();
    private final List<Section> sections = Lists.newList();
    private TestLocal<OutputListener> outputListeners = TestOutput.OUTPUT_LISTENERS;

    protected Builder(PageMap pages, PageHistory history) {
      if (pages == null) {
        throw new NullPointerException("PageMap shouldn't be null");
      } else if (history == null) {
        throw new NullPointerException("PageHistory shouldn't be null");
      }
      this.pages = pages;
      this.history = history;
    }

    public Builder setTitle(String newTitle) {
      if (newTitle == null) {
        throw new NullPointerException("title shouldn't be null");
      }
      this.title = newTitle;
      return this;
    }

    public void addDescription(String paragraph) {
      if (paragraph == null) {
        throw new NullPointerException("paragraph shouldn't be null");
      }
      this.descriptionParagraphs.add(paragraph);
    }

    /**
     * Sets an alternate location where this test suite will register its
     * listeners. This has no effect on where normal tests send their output,
     * so it's useful only for testing when you can build tests that also
     * use the alternate registry.
     */
    public Builder setOutputListeners(TestLocal<OutputListener> alternateListener) {
      this.outputListeners = alternateListener;
      return this;
    }

    public Builder addSection(Section section) {
      sections.add(section);
      pages.put(section, new SectionResultPage(section, history));
      return this;
    }

    public TestSuite build() {

      final ProgressUpdater progressUpdater = new ProgressUpdater();

      YieldingCommand resetProgress = new YieldingCommand() {
        public void run(YieldingCommand.Schedule schedule) {
          progressUpdater.reset(countTestMethods(sections));
          schedule.sleepAfterThisStep(10, "for progress bar update");
        }
      };

      List<YieldingCommand> commands = new ArrayList<YieldingCommand>();
      commands.add(resetProgress);
      addRunSectionCommands(sections, outputListeners, progressUpdater, pages, commands);
      YieldingCommand runAllTests = YieldingCommands.concat(commands);

      if (descriptionParagraphs.isEmpty()) {
        descriptionParagraphs.add(DEFAULT_DESCRIPTION);
      }

      HTML description = new HTML();
      description.setStyleName("tg-description");
      HtmlWriter writer = new HtmlWriter(description.getElement());
      for (String paragraph : descriptionParagraphs) {
        writer.textP(paragraph);
      }

      return new TestSuite(title, description, sections, progressUpdater, runAllTests);
    }

    private static void addRunSectionCommands(List<Section> sections,
        TestLocal<OutputListener> outputListeners, ProgressUpdater updater,
        PageMap pages, List<YieldingCommand> out) {

      for (Section section: sections) {
        List<MethodRunner> methodRunners =
            makeMethodRunners(section, outputListeners, updater, pages);
        out.add(new RunSectionCommand(methodRunners, section.getStatusView()));
      }
    }

    private static List<MethodRunner> makeMethodRunners(Section section,
        TestLocal<OutputListener> outputListeners, ProgressUpdater updater,
        PageMap pages) {

      List<MethodRunner> runners = Lists.newList();

      for (TestMethod method : section.getMethods()) {
        MethodResultPage resultPage = new MethodResultPage();
        pages.put(section, method, resultPage);
        MethodRunner runner = new MethodRunner(method, outputListeners, resultPage);
        runner.addStatusListener(section.getStatusView(method));
        runner.addStatusListener(updater);

        runners.add(runner);
      }

      return runners;
    }
  }

}
