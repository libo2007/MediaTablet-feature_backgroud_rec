package com.jiaying.mediatablet.net.softfan;

/**
 * Created by Administrator on 2015/9/14 0014.
 */

import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;

public class SoftFanFTPException extends Exception {
    private static final long serialVersionUID = 1L;

    private Exception ex = null;

    public SoftFanFTPException() {
        super();
    }

    public SoftFanFTPException(Exception ex) {
        super(ex.getMessage(), ex);
        this.ex = ex;
    }

    public SoftFanFTPException(String message) {
        super(message);
    }

    public String getMessage() {
        if (super.getMessage() != null)
            return super.getMessage();
        if (ex != null) {
            if (ex instanceof InvocationTargetException) {
                if (((InvocationTargetException) ex).getTargetException() != null) {
                    if (((InvocationTargetException) ex).getMessage() != null) {
                        return ((InvocationTargetException) ex).getMessage();
                    }
                }
            } else {
                if (ex.getMessage() != null) {
                    return ex.getMessage();
                }
            }
        }
        if (getCause() != null) {
            if (getCause().getMessage() != null)
                return getCause().getMessage();
        }
        return "";
    }

    public void printStackTrace() {
        super.printStackTrace();
        if (ex != null)
            ex.printStackTrace();
    }

    public void printStackTrace(PrintStream s) {
        super.printStackTrace(s);
        if (ex != null)
            ex.printStackTrace(s);
    }

    public void printStackTrace(PrintWriter s) {
        super.printStackTrace(s);
        if (ex != null)
            ex.printStackTrace(s);
    }

}