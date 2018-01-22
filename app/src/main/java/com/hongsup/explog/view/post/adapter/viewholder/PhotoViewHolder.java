package com.hongsup.explog.view.post.adapter.viewholder;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hongsup.explog.R;
import com.hongsup.explog.data.post.Content;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Android Hong on 2017-12-11.
 */

public class PhotoViewHolder extends PostViewHolder {

    @BindView(R.id.imgPhoto)
    ImageView imgPhoto;
    @BindView(R.id.textCreateAt)
    TextView textCreateAt;
    @BindView(R.id.btnMore)
    ImageButton btnMore;

    public PhotoViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bind(Content data) {
        Glide.with(context)
                .load(data.getPhotoPath())
                .into(imgPhoto);

        if(!checkMyPost){
            btnMore.setVisibility(View.GONE);
        }
    }
}
