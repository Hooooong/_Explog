package com.hongsup.explog.view.post.adapter.viewholder;

import android.view.View;
import android.widget.TextView;

import com.hongsup.explog.R;
import com.hongsup.explog.data.post.Content;
import com.hongsup.explog.view.post.listener.PostContentListener;

/**
 * Created by Android Hong on 2017-12-11.
 */

public class PathViewHolder extends PostViewHolder {

    private int position;
    private PostContentListener postContentListener;

    private TextView textPath;

    public PathViewHolder(View itemView) {
        super(itemView);
        textPath = itemView.findViewById(R.id.textPath);
    }

    @Override
    public void setListener(PostContentListener listener) {
        this.postContentListener = postContentListener;
    }

    @Override
    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public void bind(Content data) {
        String path = data.getLat() +" . " + data.getLng();
        textPath.setText(path);
    }
}
