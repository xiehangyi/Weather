package com.xhy.weather;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by change100 on 2016/5/31.
 */
public class NetBroadCast extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {


        MainActivity mt = new MainActivity();
        mt.judgeHaveNet();
    }
}
