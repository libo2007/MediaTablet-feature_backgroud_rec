package com.jiaying.mediatablet.net.signal.command;

import com.jiaying.mediatablet.net.signal.receiver.Receiver;

/**
 * Created by hipil on 2016/9/14.
 */
public class AuthResTimeoutCommand extends Command {
    private Receiver receiver;

    public AuthResTimeoutCommand(Receiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public boolean execute() {
        receiver.work();
        return false;
    }
}
