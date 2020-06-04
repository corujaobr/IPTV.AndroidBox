/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 *
 */

package com.cy8018.iptv.player;

import android.app.Activity;
import android.net.TrafficStats;
import android.os.Handler;

import androidx.leanback.media.PlaybackTransportControlGlue;
import androidx.leanback.media.PlayerAdapter;
import androidx.leanback.widget.PlaybackRowPresenter;
import androidx.leanback.widget.Presenter;

import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cy8018.iptv.R;
import com.cy8018.iptv.model.Station;

import org.jetbrains.annotations.NotNull;
import java.lang.ref.WeakReference;

/**
 * PlayerGlue for video playback
 * @param <T>
 */
public class VideoMediaPlayerGlue<T extends PlayerAdapter> extends PlaybackTransportControlGlue<T> {
    private Station currentStation;

    private long lastTotalRxBytes = 0;

    private long lastTimeStamp = 0;

    Activity mContext;

    public static final int MSG_UPDATE_NETWORK_SPEED = 0;

    public Station getCurrentStation() {
        return currentStation;
    }

    public static String gNetworkSpeed = "";

    private static final String TAG = "VideoMediaPlayerGlue";

    public void setCurrentStation(Station currentStation) {
        this.currentStation = currentStation;
    }

    public VideoMediaPlayerGlue(Activity context, T impl) {
        super(context, impl);
        mContext = context;
    }

    @Override
    protected PlaybackRowPresenter onCreateRowPresenter() {
        PlayControlPresenter presenter = new PlayControlPresenter();
        presenter.setDescriptionPresenter(new MyDescriptionPresenter());
        return presenter;
    }

    private long getNetSpeed() {

        long nowTotalRxBytes = TrafficStats.getUidRxBytes(mContext.getApplicationContext().getApplicationInfo().uid) == TrafficStats.UNSUPPORTED ? 0 : TrafficStats.getTotalRxBytes();
        long nowTimeStamp = System.currentTimeMillis();
        long calculationTime = (nowTimeStamp - lastTimeStamp);
        if (calculationTime == 0) {
            return calculationTime;
        }

        long speed = ((nowTotalRxBytes - lastTotalRxBytes) * 1000 / calculationTime);
        lastTimeStamp = nowTimeStamp;
        lastTotalRxBytes = nowTotalRxBytes;
        return speed;
    }

    public String getNetSpeedText(long speed) {
        String text = "";
        if (speed >= 0 && speed < 1024) {
            text = speed + " B/s";
        } else if (speed >= 1024 && speed < (1024 * 1024)) {
            text = speed / 1024 + " KB/s";
        } else if (speed >= (1024 * 1024) && speed < (1024 * 1024 * 1024)) {
            text = speed / (1024 * 1024) + " MB/s";
        }
        return text;
    }

    public void getBufferingInfo() {
        gNetworkSpeed = getNetSpeedText(getNetSpeed());
        Log.d(TAG, gNetworkSpeed);
    }

    private class MyDescriptionPresenter extends Presenter {
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_tv_info, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object item) {

            VideoMediaPlayerGlue glue = (VideoMediaPlayerGlue) item;
            ((ViewHolder)viewHolder).channelName.setText(glue.getTitle());
            ((ViewHolder)viewHolder).sourceInfo.setText(glue.getSubtitle());
            Glide.with(getContext())
                    .asBitmap()
                    .load(glue.getCurrentStation().logo)
                    .into(((ViewHolder)viewHolder).logo);
        }

        @Override
        public void onUnbindViewHolder(Presenter.ViewHolder viewHolder) {

        }


        class ViewHolder extends Presenter.ViewHolder {

            TextView channelName;
            TextView sourceInfo;
            TextView networkSpeed;
            ImageView logo;

            private ViewHolder (View itemView)
            {
                super(itemView);
                channelName = itemView.findViewById(R.id.channel_name);
                sourceInfo = itemView.findViewById(R.id.source_info);
                networkSpeed = itemView.findViewById(R.id.network_speed);
                logo = itemView.findViewById(R.id.logo);

                new Thread(networkSpeedRunnable).start();
            }

            public void UpdateNetworkSpeed() {
                networkSpeed.setText(gNetworkSpeed);
            }

            public final MsgHandler mHandler = new MsgHandler(this);

            public class MsgHandler extends Handler {
                WeakReference<ViewHolder> mViewHolder;

                MsgHandler(ViewHolder viewHolder) {
                    mViewHolder = new WeakReference<ViewHolder>(viewHolder);
                }

                @Override
                public void handleMessage(@NotNull Message msg) {
                    super.handleMessage(msg);

                    ViewHolder vh = mViewHolder.get();
                    if (msg.what == MSG_UPDATE_NETWORK_SPEED) {
                        getBufferingInfo();
                        vh.UpdateNetworkSpeed();
                    }
                }
            }

            Runnable networkSpeedRunnable = new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            mHandler.sendEmptyMessage(MSG_UPDATE_NETWORK_SPEED);
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
        }
    }
}