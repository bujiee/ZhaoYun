package com.bj.xiiuxiu;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Description：
 * 1.复制事件五分钟
 * 2.用完一次不在用
 * Created by Buuu on 2017/10/19.
 */

public class LoopService extends Service {
    private final LoopBinder binder = new LoopBinder();
    private ClipboardManager clipboardManager;
    private Xiu.ClipboardChangeListener listener;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
//        clipboardManager.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
//            @Override
//            public void onPrimaryClipChanged() {
//                if (listener != null) {
//                    listener.onClipboardChange(ClipboardUtil.getTextFromClip(LoopService.this));
//                }
//            }
//        });

    }


    public void getClipboardData(final Xiu.ClipboardChangeListener listener) {
        if (clipboardManager != null) {
            clipboardManager.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
                @Override
                public void onPrimaryClipChanged() {
                    if (listener != null) {
                        listener.onClipboardChange(ClipboardUtil.getTextFromClip(LoopService.this));
                    }
                }
            });
        }
    }

    public class LoopBinder extends Binder {
        public LoopService getLoopService() {
            return LoopService.this;
        }
    }
}
