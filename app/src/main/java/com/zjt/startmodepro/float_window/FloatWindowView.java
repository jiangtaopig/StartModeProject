package com.zjt.startmodepro.float_window;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.zjt.startmodepro.R;

public class FloatWindowView extends LinearLayout {

    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mWmParams;
    private int statusBarHeight = 0;
    private int mScreenWidth;
    private int mScreenHeight;

    public FloatWindowView(Context context) {
        this(context, null);
    }

    public FloatWindowView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public FloatWindowView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.float_window, this);

        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        mWmParams = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            mWmParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            mWmParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        } else {
            mWmParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }
        mWmParams.format = PixelFormat.RGBA_8888;
        mWmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mWmParams.gravity = Gravity.START | Gravity.TOP;
        mWmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mWmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mWmParams.x = 0;
        mWmParams.y = 0;

        mWindowManager.addView(this, mWmParams);

        final int mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }

        mScreenWidth = getScreenWidth(context);
        mScreenHeight = getScreenHeight(context);

        setOnTouchListener(new View.OnTouchListener() {
            int startX, startY;  //?????????
            boolean isPerformClick;  //????????????
            int finalMoveX;  //?????????????????????mView???X??????????????????finalMoveX

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = (int) event.getX();
                        startY = (int) event.getY();
                        isPerformClick = true;
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        //?????????CLICK??????MOVE
                        //???????????????????????????????????????
                        if (Math.abs(startX - event.getX()) >= mTouchSlop || Math.abs(startY - event.getY()) >= mTouchSlop) {
                            isPerformClick = false;
                        }
                        if (isPortrait()) {
                            mWmParams.x = (int) (event.getRawX() - startX);
                            //??????????????????????????????????????????????????????y?????????????????????????????????????????????????????????????????????????????????????????????
                            mWmParams.y = (int) (event.getRawY() - startY - statusBarHeight);
                        } else {
                            mWmParams.x = (int) (event.getRawX() - startX - statusBarHeight);
                            mWmParams.y = (int) (event.getRawY() - startY);
                        }
                        updateCollectViewLayout();
                        return true;
                    case MotionEvent.ACTION_UP:
                        if (isPerformClick) {
                            performClick();
                        }
                        //??????mView??????Window??????????????????????????????
                        if (mWmParams.x + getMeasuredWidth() / 2 >= mScreenWidth / 2) {
                            finalMoveX = mScreenWidth - getMeasuredWidth();
                        } else {
                            finalMoveX = 0;
                        }
//                        stickToSide(); // ???????????????
                        return !isPerformClick;
                }
                return false;
            }
        });
    }

    private void updateCollectViewLayout() {
        mWindowManager.updateViewLayout(this, mWmParams);
    }

    private boolean isPortrait() {
        Configuration configuration = getResources().getConfiguration();
        int orientation = configuration.orientation;
        return Configuration.ORIENTATION_PORTRAIT == orientation;
    }

    public void show() {
        setVisibility(VISIBLE);
    }

    private int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        if (wm != null) {
            wm.getDefaultDisplay().getMetrics(outMetrics);
        }
        return outMetrics.widthPixels;
    }

    private int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        if (wm != null) {
            wm.getDefaultDisplay().getMetrics(outMetrics);
        }
        return outMetrics.heightPixels;
    }
}
