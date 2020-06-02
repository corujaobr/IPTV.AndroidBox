/*
 * Copyright (C) 2017 The Android Open Source Project
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
 */

package com.cy8018.iptv;

import android.net.Uri;
import android.os.Bundle;

import androidx.leanback.app.VideoFragmentGlueHost;
import androidx.leanback.app.VideoSupportFragment;
import androidx.leanback.app.VideoSupportFragmentGlueHost;
import androidx.leanback.media.MediaPlayerAdapter;
import androidx.leanback.media.PlaybackTransportControlGlue;
import androidx.leanback.widget.PlaybackControlsRow;

/**
 * Handles video playback with media controls.
 */
public class PlaybackVideoFragment extends VideoSupportFragment {

    private VideoMediaPlayerGlue<ExoPlayerAdapter> mMediaPlayerGlue;
    final VideoSupportFragmentGlueHost mHost = new VideoSupportFragmentGlueHost(this);


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ExoPlayerAdapter playerAdapter = new ExoPlayerAdapter(getActivity());
        mMediaPlayerGlue = new VideoMediaPlayerGlue(getActivity(), playerAdapter);
        mMediaPlayerGlue.setHost(mHost);
        mMediaPlayerGlue.setMode(PlaybackControlsRow.RepeatAction.NONE);
        mMediaPlayerGlue.setSeekEnabled(false);
//        MediaMetaData intentMetaData = getActivity().getIntent().getParcelableExtra("metaData");
//        mStationList = getActivity().getIntent().getParcelableArrayListExtra("stationList");
//        currentStation = getActivity().getIntent().getParcelableExtra("currentStation");

//        if (intentMetaData != null) {
//            mMediaPlayerGlue.setTitle(intentMetaData.getMediaTitle());
//            mMediaPlayerGlue.setSubtitle("1/" + currentStation.url.size());
//            mMediaPlayerGlue.getPlayerAdapter().setDataSource(
//                    Uri.parse(intentMetaData.getMediaSourcePath()));
//        } else {
//            mMediaPlayerGlue.setTitle("Diving with Sharks");
//            mMediaPlayerGlue.setSubtitle("A Googler");
//            mMediaPlayerGlue.getPlayerAdapter().setDataSource(Uri.parse(URL));
//        }
//        PlaybackSeekDiskDataProvider.setDemoSeekProvider(mMediaPlayerGlue);
//        playWhenReady(mMediaPlayerGlue);
        setBackgroundType(BG_LIGHT);

        mMediaPlayerGlue.setTitle("test Title");
        mMediaPlayerGlue.setSubtitle("");
        mMediaPlayerGlue.playWhenPrepared();
        playerAdapter.setDataSource(Uri.parse("http://183.207.249.14/PLTV/3/224/3221225560/index.m3u8"));
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mMediaPlayerGlue != null) {
            mMediaPlayerGlue.pause();
        }
    }
}