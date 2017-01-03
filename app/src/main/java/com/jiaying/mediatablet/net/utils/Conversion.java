package com.jiaying.mediatablet.net.utils;

import com.jiaying.mediatablet.net.signal.RecSignal;

/**
 * Created by hipil on 2016/4/13.
 */
public class Conversion {
    public static RecSignal conver(String strcmd) {
        if ("confirm".equals(strcmd)) {
            return RecSignal.CONFIRM;
        } else if ("auth_info".equals(strcmd)) {
            return RecSignal.CONFIRM;
        } else if ("cancel_donor".equals(strcmd)) {
            return RecSignal.CANCLEAUTHPASS;
        } else if ("timestamp".equals(strcmd)) {
            return RecSignal.TIMESTAMP;
        } else if ("startInfating".equals(strcmd)) {
            return RecSignal.COMPRESSINON;
        } else if ("zxdc_rev_authentication_donor".equals(strcmd)) {
            return RecSignal.ZXDCAUTHRES;
        } else if ("zxdc_recv_auth_pass".equals(strcmd)) {
            return RecSignal.ZXDCAUTHRES;
        } else if ("start".equals(strcmd)) {
            return RecSignal.START;
        } else if ("end".equals(strcmd)) {
            return RecSignal.END;
        } else if ("pipeLow".equals(strcmd)) {
            return RecSignal.PIPELOW;
        } else if ("pipeNormal".equals(strcmd)) {
            return RecSignal.PIPENORMAL;
        } else if ("timestamp".equals(strcmd)) {
            return RecSignal.TIMESTAMP;
        } else if ("autotranfusion_start".equals(strcmd)) {
            return RecSignal.AUTOTRANFUSIONSTART;
        } else if ("autotranfusion_end".equals(strcmd)) {
            return RecSignal.AUTOTRANFUSIONEND;
        } else if ("plasma_weight".equals(strcmd)) {
            return RecSignal.PLASMAWEIGHT;
        }
        return RecSignal.NOTHING;
    }
}
