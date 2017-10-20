package com.bj.xiiuxiu;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

/**
 * Description：
 * Created by Buuu on 2017/10/19.
 */

public class Xiu {

    private static volatile Xiu INSTANCE;

    public static Xiu getInstance() {
        if (INSTANCE == null) {
            synchronized (Xiu.class) {
                if (INSTANCE == null) {
                    INSTANCE = new Xiu();
                }
            }
        }
        return INSTANCE;
    }

    private MyServiceConnection connection;

    public void startXiuService(Context context, ClipboardChangeListener listener) {
        this.connection = new MyServiceConnection(listener);
        Intent mIntent = new Intent(context, LoopService.class);
        context.bindService(mIntent, connection, Context.BIND_AUTO_CREATE);
//        context.startService(mIntent);
        System.out.println("");
    }

    public void stopXiuService(Context context) {
        if (connection != null)
            context.unbindService(connection);
    }

    private static class MyServiceConnection implements ServiceConnection {
        private ClipboardChangeListener mClipboardChangeListener;

        public MyServiceConnection(ClipboardChangeListener listener) {
            this.mClipboardChangeListener = listener;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LoopService.LoopBinder loopBinder = (LoopService.LoopBinder) service;
            LoopService loopService = loopBinder.getLoopService();
            loopService.getClipboardData(mClipboardChangeListener);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }


    public interface ClipboardChangeListener {
        void onClipboardChange(String result);
    }
}
