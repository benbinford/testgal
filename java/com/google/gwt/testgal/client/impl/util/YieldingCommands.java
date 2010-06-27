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
package com.google.gwt.testgal.client.impl.util;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Utility methods for working with {@link YieldingCommand YieldingCommands}.
 *
 * @author Brian Slesinsky
 */
public class YieldingCommands {

  /**
   * Schedules a command to run after the current event handler returns.
   */
  public static void start(YieldingCommand commandToRun) {
    Scheduler scheduler = new Scheduler();
    scheduler.push(commandToRun);
    scheduler.startAfterYield(1, "to start a new command");
  }

  /**
   * Creates a new YieldingCommand that will run the given commands in order.
   */
  public static YieldingCommand concat(Iterable<? extends YieldingCommand> commandsToRun) {
    final Queue<YieldingCommand> commands = new LinkedList<YieldingCommand>();
    for (YieldingCommand command : commandsToRun) {
      commands.add(command);
    }

    return new YieldingCommand() {
      public void run(Schedule schedule) {
        schedule.push(commands);
      }
    };
  }
}
