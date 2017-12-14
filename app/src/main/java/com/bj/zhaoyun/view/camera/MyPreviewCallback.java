package com.bj.zhaoyun.view.camera;

import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;


/**
 * Description：Camera.PreviewCallback
 * Created by Buuu on 2017/12/11.
 */

public class MyPreviewCallback implements Camera.PreviewCallback {
    private MyHandler mHandler;
    private int width;
    private int height;

    public MyPreviewCallback() {
//        multiFormatReader.setHints(hints);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                mHandler = new MyHandler();
                Looper.loop();
            }
        }).start();
    }

    public void setPreviewWidthHeight(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        //相机预览资源
        Message message = Message.obtain();
        message.what = 10;
        message.obj = data;
        message.arg1 = width;
        message.arg2 = height;
        mHandler.sendMessage(message);
    }

    static class MyHandler extends Handler {
        private final MultiFormatReader multiFormatReader;

        MyHandler() {
            multiFormatReader = new MultiFormatReader();
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 10) {
                byte[] data = (byte[]) msg.obj;
                decode(data, msg.arg1, msg.arg2);
                Log.d("handleMessage", "handleMessage: "+msg.arg1+"x"+msg.arg2);
            }
        }

        /**
         * @param data   数据流
         * @param width  预览图片宽度
         * @param height 预览图片高度
         */
        private void decode(byte[] data, int width, int height) {
            long start = System.currentTimeMillis();
            Result rawResult = null;
            PlanarYUVLuminanceSource source = buildLuminanceSource(data, width, height);
            if (source != null) {
                BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
                try {
                    rawResult = multiFormatReader.decodeWithState(bitmap);//获取到扫描的结果
                    Log.d("rawResult", "decode: "+rawResult.getText());
                } catch (ReaderException re) {
                    // continue
                } finally {
                    multiFormatReader.reset();
                }
            }
        }

        PlanarYUVLuminanceSource buildLuminanceSource(byte[] data, int width, int height) {
//            Rect rect = getFramingRectInPreview();
            Rect rect = new Rect(0, 0, width, height);
            if (rect == null) {
                return null;
            }
            return new PlanarYUVLuminanceSource(data, width, height, rect.left, rect.top,
                    rect.width(), rect.height(), false);
        }
    }

}
