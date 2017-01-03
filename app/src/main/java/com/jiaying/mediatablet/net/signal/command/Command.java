package com.jiaying.mediatablet.net.signal.command;

/**
 * Created by hipil on 2016/9/14.
 */
public abstract class Command {
    //每个命令都必须有一个执行命令的方法
    /**
     * 命令执行成功返回true，执行失败返回false
     */
    public abstract boolean execute();
}
