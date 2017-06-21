package com.emmanuel.elmrefreshviewdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.indicator.PtrIndicator;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private PtrFrameLayout layoutRefresh;
    private ElmRefreshView refreshView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        layoutRefresh = (PtrFrameLayout) findViewById(R.id.ptr_frame_layout);
        refreshView = (ElmRefreshView) findViewById(R.id.view_fresh);

        layoutRefresh.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                refreshView.setStatus(ElmRefreshView.STATUS_RUNNING);
                layoutRefresh.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        layoutRefresh.refreshComplete();//刷新完毕
                    }
                }, 3000);
            }
        });

        layoutRefresh.addPtrUIHandler(new PtrUIHandler() {
            @Override
            public void onUIReset(PtrFrameLayout frame) {
            }

            @Override
            public void onUIRefreshPrepare(PtrFrameLayout frame) {
            }

            @Override
            public void onUIRefreshBegin(PtrFrameLayout frame) {
            }

            @Override
            public void onUIRefreshComplete(PtrFrameLayout frame) {
                refreshView.setStatus(ElmRefreshView.STATUS_STOP);
                refreshView.setStatus(ElmRefreshView.STATUS_MOVING);
            }

            @Override
            public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {
//                Log.d(TAG, "onUIPositionChange");
                final int height = refreshView.getHeight() / 2;
                float offset = (ptrIndicator.getCurrentPosY() - height) *1.0f;
                if(offset < 0){
                    offset = 0;
                }
                refreshView.setPullPositionChanged(offset / (ptrIndicator.getHeaderHeight() - height));
            }
        });
    }
}
