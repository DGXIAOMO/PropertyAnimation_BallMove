package com.example.miku.propertyanimation_day2_test;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.graphics.PointF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView imageView;
    private Button start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.imageView);
        start = (Button) findViewById(R.id.Start);
        start.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Start:
                Paowuxian(imageView);
                break;
            default:
                break;
        }
    }

    //实现自由落体
    private void Vertical(final View view) {
        ValueAnimator animator = ValueAnimator.ofFloat(0, (float) (1280 - view.getBottom()));
        animator.setTarget(view);
        animator.setObjectValues(new Float(0));
        animator.setDuration(2000).start();
        animator.setEvaluator(new TypeEvaluator<Float>() {
            @Override
            public Float evaluate(float fraction, Float startValue, Float endValue) {
                float t = 2 * fraction;
                Float data[] = new Float[0];
                data[0] = 0.5f * t * t * 200;
                return data[0];
            }
        });
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float data = (Float) animation.getAnimatedValue();
                imageView.setTranslationY(data);
            }
        });
    }

    // 实现抛物线(平抛或者斜抛)、弹跳
    private void Paowuxian(final View view) {
        //设置动画合集
        ValueAnimator animator = new ValueAnimator();
        AnimatorSet anim = new AnimatorSet();
        animator = new ValueAnimator();
        animator.setTarget(view);
        animator.setDuration(4000);
        //旋转动画
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(view, "rotation", 0, 360).setDuration(400);
        //设置差值器，匀速进行
        animator1.setInterpolator(new LinearInterpolator());
        //通过总时间来计算重复次数
        animator1.setRepeatCount(9);
        //animator.setRepeatCount(1);
        //animator.setRepeatMode(2);
        animator.setObjectValues(new PointF(0, 0));
        animator.setInterpolator(new LinearInterpolator());
        //animator.setInterpolator(new BounceInterpolator());
        //采用估值器确定点的坐标，从而定位小球位置。
        animator.setEvaluator(new TypeEvaluator<PointF>() {
            @Override
            public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
                //fraction=t/duration
                float t = 4f * fraction;
                PointF point = new PointF();
                if (t <= 2) {
                    point.x = 200 * t;
                    point.y = t * t * 0.5f * 200 - 200 * t;
                } else if (2 < t && t <= 3) {
                    point.x = 400 + 200 * (t - 2);
                    point.y = (t - 2) * (t - 2) * 0.5f * 200 - 100 * (t - 2);
                } else if (3 < t && t <= 3.5) {
                    point.x = 600 + 200 * (t - 3);
                    point.y = (t - 3) * (t - 3) * 0.5f * 200 - 50 * (t - 3);
                } else {
                    point.x = 700 + 40 * (t - 3.5f);
                    point.y = 0;
                }

                return point;
            }
        });
        //anim.setStartDelay(1000);
        anim.playTogether(animator, animator1);
        anim.start();
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                Toast.makeText(MainActivity.this, "动画结束！", Toast.LENGTH_SHORT).show();
            }
        });
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PointF point = (PointF) animation.getAnimatedValue();
                imageView.setTranslationX(point.x);
                imageView.setTranslationY(point.y);

            }
        });
    }
}
