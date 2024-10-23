package com.skythinker.gptassistant.util;

import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.TextView;

import com.skythinker.gptassistant.R;


/**
 */
public class SmsCodeHelper {
    private Button buttonView;
    private final long seconds;

    public SmsCodeHelper() {
        seconds = 0;
    }
    public SmsCodeHelper(Button button, long seconds) {
        this.seconds = seconds * 1000;
        this.buttonView = button;
    }

    public interface SmsTimerCall {
        void call(boolean finished);
    }

    public interface SmsTimerCallTwo {
        void call(boolean finished, long l);
    }

    public void smsCodeGet(SmsTimerCall call) {
        CountDownTimer timer = new CountDownTimer(this.seconds, 1000) {
            @Override
            public void onTick(long l) {
                buttonView.setClickable(false);
                buttonView.setEnabled(false);
                buttonView.setBackgroundResource(R.drawable.bt_round_login_gray);
                buttonView.setText(String.format("%s秒后重试", l / 1000 + 1));
                call.call(false);
            }

            @Override
            public void onFinish() {
                buttonView.setText("重新获取");
                buttonView.setClickable(true);
                buttonView.setEnabled(true);
                buttonView.setBackgroundResource(R.drawable.bt_round_login);
                call.call(true);
            }
        };
        timer.start();
    }

    public void smsCodeGet(TextView mView, String tickStr, String finishStr, long seconds, SmsTimerCall call) {
        CountDownTimer timer = new CountDownTimer(seconds * 1000, 1000) {
            @Override
            public void onTick(long l) {
                mView.setText(String.format(tickStr, l / 1000 + 1));
                call.call(false);
            }

            @Override
            public void onFinish() {
                mView.setText(finishStr);
                call.call(true);
            }
        };
        timer.start();
    }

    public void smsCodeGet(TextView mView, String tickStr, long seconds, SmsTimerCall call) {
        CountDownTimer timer = new CountDownTimer(seconds * 1000, 1000) {
            @Override
            public void onTick(long l) {
                mView.setText(String.format(tickStr, l / 1000 + 1));
                call.call(false);
            }

            @Override
            public void onFinish() {
                call.call(true);
            }
        };
        timer.start();
    }

    public CountDownTimer  smsCodeGet(long seconds, SmsTimerCallTwo call) {
        CountDownTimer timer = new CountDownTimer(seconds * 1000, 1000) {
            @Override
            public void onTick(long l) {
                call.call(false, l / 1000);
            }

            @Override
            public void onFinish() {
                call.call(true, 0);
            }
        };
        timer.start();
        return timer;
    }
}
