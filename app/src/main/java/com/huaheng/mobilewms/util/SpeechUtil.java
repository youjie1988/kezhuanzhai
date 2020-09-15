package com.huaheng.mobilewms.util;

import android.content.Context;
import android.os.RemoteException;

import com.iflytek.speech.ErrorCode;
import com.iflytek.speech.ISpeechModule;
import com.iflytek.speech.InitListener;
import com.iflytek.speech.SpeechConstant;
import com.iflytek.speech.SpeechSynthesizer;
import com.iflytek.speech.SynthesizerListener;

public class SpeechUtil {

    private SpeechSynthesizer mTts;
    private Context mContext;
    private static SpeechUtil instance;

    public SpeechUtil(Context context) {
        mContext = context;
        initSpeech();
    }

    public static SpeechUtil getInstance(Context context) {
        if(instance == null) {
            instance = new SpeechUtil(context);
        }
        return instance;
    }
    /**
     * 初期化监听。
     */
    private InitListener mTtsInitListener = new InitListener() {

        @Override
        public void onInit(ISpeechModule arg0, int code) {
            WMSLog.d("InitListener init() code = " + code);
            if (code == ErrorCode.SUCCESS) {
                WMSLog.d("onInit  SUCCESS");
                // speeking2();
            }
        }
    };

    /**
     * 合成回调监听。
     */
    private SynthesizerListener mTtsListener = new SynthesizerListener.Stub() {
        @Override
        public void onBufferProgress(int progress) throws RemoteException {
        }

        @Override
        public void onCompleted(int code) throws RemoteException {
            WMSLog.d("onCompleted code =" + code);
        }

        @Override
        public void onSpeakBegin() throws RemoteException {
            WMSLog.d( "onSpeakBegin");
        }

        @Override
        public void onSpeakPaused() throws RemoteException {
            WMSLog.d( "onSpeakPaused.");
        }

        @Override
        public void onSpeakProgress(int progress) throws RemoteException {
        }

        @Override
        public void onSpeakResumed() throws RemoteException {
            WMSLog.d( "onSpeakResumed.");
        }
    };

    public void initSpeech() {
        mTts = new SpeechSynthesizer(mContext, mTtsInitListener);
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, "local");

        mTts.setParameter(SpeechSynthesizer.VOICE_NAME, "xiaoyan");
        mTts.setParameter(SpeechSynthesizer.SPEED, "50");

        mTts.setParameter(SpeechSynthesizer.PITCH, "50");

        mTts.setParameter(SpeechSynthesizer.VOLUME, "30");
    }

    public void releaseSpeechSynthesizer() {
        if (mTts != null) {
            mTts.stopSpeaking(mTtsListener);
            // 退出时释放连接
            mTts.destory();
        }
    }

    /**
     * 根据内容发声
     *
     * @param speekingContent
     */
    public void speech(String speekingContent) {
        if (speekingContent == null || speekingContent.length() == 0) {
            return;
        }

        WMSLog.d("bizAppNotifyHandle  speekingContent:" + speekingContent);
        // 播放,code,为还有多少个子为合成
        mTts.startSpeaking(speekingContent, mTtsListener);
    }
}
