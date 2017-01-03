package com.jiaying.mediatablet.net.signal;

import com.jiaying.mediatablet.net.signal.command.Command;

/**
 * Created by hipil on 2016/9/14.
 */
public class Invoker {
    private Command command;

    public void setCommand(Command command) {
        this.command = command;
    }

    public void action() {
        this.command.execute();
    }
}
