package ru.romananchugov.sunset;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;

/**
 * Created by romananchugov on 21.01.2018.
 */

public class SunsetFragment extends Fragment {

    private View mSceneView;
    private View mSunView;
    private View mSkyView;

    private int mBlueSkyColor;
    private int mSunsetSkyColor;
    private int mNightSkyColor;

    private boolean endAnimation;

    public static SunsetFragment newInstance(){
        return new SunsetFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sunset, container, false);
        mSceneView = view;
        mSunView = view.findViewById(R.id.sun);
        mSkyView = view.findViewById(R.id.sky);

        endAnimation = false;

        mSceneView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(endAnimation){
                    returnAnimation();
                }else {
                    startAnimation();
                }
            }
        });

        Resources resources = getResources();
        mBlueSkyColor = resources.getColor(R.color.blue_sky);
        mSunsetSkyColor = resources.getColor(R.color.sunset_sky);
        mNightSkyColor = resources.getColor(R.color.night_sky);

        return view;
    }


    public void startAnimation(){
        float sunYStart = mSunView.getTop();
        float sunYEnd = mSkyView.getBottom();

        ObjectAnimator heightAnimator = ObjectAnimator.ofFloat(mSunView, "y", sunYStart, sunYEnd)
                .setDuration(3000);
        heightAnimator.setInterpolator(new AccelerateInterpolator());

        ObjectAnimator sunsetSkyAnimator = ObjectAnimator.ofInt(mSkyView, "backgroundColor", mBlueSkyColor, mSunsetSkyColor)
                .setDuration(3000);
        sunsetSkyAnimator.setEvaluator(new ArgbEvaluator());

        ObjectAnimator nightSkyAnimator = ObjectAnimator
                .ofInt(mSkyView, "backgroundColor", mSunsetSkyColor, mNightSkyColor)
                .setDuration(1500);
        nightSkyAnimator.setEvaluator(new ArgbEvaluator());

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet
                .play(heightAnimator)
                .with(sunsetSkyAnimator)
                .before(nightSkyAnimator);
        animatorSet.start();


        endAnimation = true;

    }
    public void returnAnimation(){
        float sunYStart = mSkyView.getBottom();
        float sunYEnd = (mSkyView.getHeight() - mSunView.getHeight()) / 2;

        ObjectAnimator heightAnimator = ObjectAnimator.ofFloat(mSunView, "y", sunYStart, sunYEnd)
                .setDuration(3000);
        heightAnimator.setInterpolator(new AccelerateInterpolator());

        ObjectAnimator nightSkyAnimator = ObjectAnimator.ofInt(mSkyView, "backgroundColor", mNightSkyColor, mSunsetSkyColor)
                .setDuration(1500);
        nightSkyAnimator.setEvaluator(new ArgbEvaluator());

        ObjectAnimator sunsetSkyAnimator = ObjectAnimator.ofInt(mSkyView, "backgroundColor", mSunsetSkyColor, mBlueSkyColor)
                .setDuration(3000);
        sunsetSkyAnimator.setEvaluator(new ArgbEvaluator());

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet
                .play(heightAnimator)
                .with(sunsetSkyAnimator)
                .after(nightSkyAnimator);


        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                sunShake();
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                sunShake();
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });

        animatorSet.start();
        endAnimation = false;
    }
    public void sunShake(){
        float sunXRightStart = mSunView.getLeft();
        float sunXRightEnd = mSunView.getLeft() + 3;
        float sunXLeftStart = mSunView.getLeft();
        float sunXLeftEnd = mSunView.getLeft() - 3;


        ObjectAnimator rightAnimator = ObjectAnimator.ofFloat(mSunView, "x", sunXRightStart, sunXRightEnd)
                .setDuration(500);

        final ObjectAnimator righttoLeftAnimator = ObjectAnimator.ofFloat(mSunView, "x", sunXRightEnd, sunXLeftEnd)
                .setDuration(250);
        righttoLeftAnimator.setRepeatCount(Animation.INFINITE);
        final ObjectAnimator leftToRightAnimator = ObjectAnimator.ofFloat(mSunView, "x", sunXLeftEnd, sunXRightEnd)
                .setDuration(500);


        rightAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                righttoLeftAnimator.start();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        rightAnimator.start();
    }
}
