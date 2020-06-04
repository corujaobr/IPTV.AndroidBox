package com.cy8018.iptv.player;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.leanback.R;
import androidx.leanback.widget.PlaybackControlsRow;
import androidx.leanback.widget.PlaybackRowPresenter;
import androidx.leanback.widget.PlaybackSeekUi;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.RowPresenter;

import static android.view.View.GONE;


public class PlayControlPresenter extends PlaybackRowPresenter {

    /**
     * A ViewHolder for the PlaybackControlsRow supporting seek UI.
     */
    public class ViewHolder extends PlaybackRowPresenter.ViewHolder implements PlaybackSeekUi {
        final Presenter.ViewHolder mDescriptionViewHolder;
        final ViewGroup mDescriptionDock;

        /**
         * Constructor of ViewHolder of PlaybackTransportRowPresenter
         * @param rootView Root view of the ViewHolder.
         * @param descriptionPresenter The presenter that will be used to create description
         *                             ViewHolder. The description view will be added into tree.
         */
        public ViewHolder(View rootView, Presenter descriptionPresenter) {
            super(rootView);
            mDescriptionDock = (ViewGroup) rootView.findViewById(R.id.description_dock);
            mDescriptionViewHolder = descriptionPresenter == null ? null :
                    descriptionPresenter.onCreateViewHolder(mDescriptionDock);
            if (mDescriptionViewHolder != null) {
                mDescriptionDock.addView(mDescriptionViewHolder.view);
            }
        }

        @Override
        public void setPlaybackSeekUiClient(Client client) {

        }
    }

    Presenter mDescriptionPresenter;

    public PlayControlPresenter() {
        setHeaderPresenter(null);
        setSelectEffectEnabled(false);
    }

    /**
     * @param descriptionPresenter Presenter for displaying item details.
     */
    public void setDescriptionPresenter(Presenter descriptionPresenter) {
        mDescriptionPresenter = descriptionPresenter;
    }

    @Override
    protected RowPresenter.ViewHolder createRowViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext()).inflate(com.cy8018.iptv.R.layout.layout_play_control, parent, false);
        ViewHolder vh = new ViewHolder(v, mDescriptionPresenter);
        return vh;
    }

    @Override
    protected void onBindRowViewHolder(RowPresenter.ViewHolder holder, Object item) {
        super.onBindRowViewHolder(holder, item);

        ViewHolder vh = (ViewHolder) holder;
        PlaybackControlsRow row = (PlaybackControlsRow) vh.getRow();

        if (row.getItem() == null) {
            vh.mDescriptionDock.setVisibility(GONE);
        } else {
            vh.mDescriptionDock.setVisibility(View.VISIBLE);
            if (vh.mDescriptionViewHolder != null) {
                mDescriptionPresenter.onBindViewHolder(vh.mDescriptionViewHolder, row.getItem());
            }
        }
    }

    @Override
    protected void onUnbindRowViewHolder(RowPresenter.ViewHolder holder) {
        ViewHolder vh = (ViewHolder) holder;
        PlaybackControlsRow row = (PlaybackControlsRow) vh.getRow();

        if (vh.mDescriptionViewHolder != null) {
            mDescriptionPresenter.onUnbindViewHolder(vh.mDescriptionViewHolder);
        }
        row.setOnPlaybackProgressChangedListener(null);

        super.onUnbindRowViewHolder(holder);
    }

    @Override
    protected void onRowViewAttachedToWindow(RowPresenter.ViewHolder vh) {
        super.onRowViewAttachedToWindow(vh);
        if (mDescriptionPresenter != null) {
            mDescriptionPresenter.onViewAttachedToWindow(
                    ((ViewHolder) vh).mDescriptionViewHolder);
        }
    }

    @Override
    protected void onRowViewDetachedFromWindow(RowPresenter.ViewHolder vh) {
        super.onRowViewDetachedFromWindow(vh);
        if (mDescriptionPresenter != null) {
            mDescriptionPresenter.onViewDetachedFromWindow(
                    ((ViewHolder) vh).mDescriptionViewHolder);
        }
    }

}
