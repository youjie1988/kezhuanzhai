package com.huaheng.mobilewms.util;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Vibrator;

import com.huaheng.mobilewms.R;


/**
 * Created by Administrator on 2017-7-27.
 */

public class SoundUtils {

    private static SoundUtils mySoundUtils;
    private static Vibrator vibrator;
    private Context context;
    private SoundPool soundPool;

    public static SoundUtils getInstance(Context context) {
        if (mySoundUtils == null) {
            mySoundUtils = new SoundUtils(context);
        }

        return mySoundUtils;
    }

    private SoundUtils(Context context) {
        this.context = context;
        initSoundPool();
    }

    private void initSoundPool() {
        if (soundPool != null) return;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            SoundPool.Builder builder = new SoundPool.Builder();
            builder.setMaxStreams(5);
            //AudioAttributes是一个封装音频各种属性的方法
            AudioAttributes.Builder attrBuilder = new AudioAttributes.Builder();
            attrBuilder.setLegacyStreamType(AudioManager.STREAM_MUSIC);//设置音频流的合适的属性
            builder.setAudioAttributes(attrBuilder.build());//加载一个AudioAttributes
            soundPool = builder.build();
        } else {
            soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 5);
        }
        soundPool.load(context, R.raw.finish, 1);
        soundPool.load(context, R.raw.open, 1);
        soundPool.load(context, R.raw.beep, 1);
        soundPool.load(context, R.raw.error, 1);
        soundPool.load(context, R.raw.changecode, 1);
    }

    public void finishSound() {
        if (soundPool != null) soundPool.play(1, 0.3f, 0.3f, 100, 0, 1);
    }

    public void openSound() {
        if (soundPool != null) soundPool.play(2, 0.3f, 0.3f, 100, 0, 1);
    }

    public void dingSound() {
        if (soundPool != null) soundPool.play(3, 0.3f, 0.3f, 100, 0, 1);
    }

    public void errorSound() {
        if (soundPool != null) soundPool.play(4, 0.3f, 0.3f, 100, 0, 1);
    }

    public void changeSound() {
        if (soundPool != null) soundPool.play(5, 0.3f, 0.3f, 100, 0 ,1);
    }

}