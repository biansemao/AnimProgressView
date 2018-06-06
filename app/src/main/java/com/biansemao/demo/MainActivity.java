package com.biansemao.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.biansemao.widget.AnimProgressView;

public class MainActivity extends AppCompatActivity {

    private LinearLayout topLl, bottomLl;
    private Button stopBtn;
    private boolean isPause = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        topLl = findViewById(R.id.ll_top);
        bottomLl = findViewById(R.id.ll_bottom);
        stopBtn = findViewById(R.id.btn_stop);

        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPause) {
                    startAnim();
                    stopBtn.setText("暂停");
                } else {
                    stopAnim();
                    stopBtn.setText("继续");
                }
                isPause = !isPause;
            }
        });
        findViewById(R.id.btn_reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetAnim();
                isPause = true;
                stopBtn.setText("开始");
            }
        });

    }

    private void startAnim(){
        for (int i = 0; i < topLl.getChildCount(); i ++){
            View view = topLl.getChildAt(i);
            if(view instanceof AnimProgressView){
                ((AnimProgressView) view).startAnim();
            }
        }
        for (int i = 0; i < bottomLl.getChildCount(); i ++){
            View view = bottomLl.getChildAt(i);
            if(view instanceof AnimProgressView){
                ((AnimProgressView) view).startAnim();
            }
        }
    }

    private void stopAnim(){
        for (int i = 0; i < topLl.getChildCount(); i ++){
            View view = topLl.getChildAt(i);
            if(view instanceof AnimProgressView){
                ((AnimProgressView) view).stopAnim();
            }
        }
        for (int i = 0; i < bottomLl.getChildCount(); i ++){
            View view = bottomLl.getChildAt(i);
            if(view instanceof AnimProgressView){
                ((AnimProgressView) view).stopAnim();
            }
        }
    }

    private void resetAnim(){
        for (int i = 0; i < topLl.getChildCount(); i ++){
            View view = topLl.getChildAt(i);
            if(view instanceof AnimProgressView){
                ((AnimProgressView) view).resetAnim();
            }
        }
        for (int i = 0; i < bottomLl.getChildCount(); i ++){
            View view = bottomLl.getChildAt(i);
            if(view instanceof AnimProgressView){
                ((AnimProgressView) view).resetAnim();
            }
        }
    }

}
