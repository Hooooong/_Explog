package com.hongsup.explog.view.post.adapter.viewholder;

import android.view.View;
import android.widget.TextView;

import com.hongsup.explog.R;
import com.hongsup.explog.data.Const;
import com.hongsup.explog.data.post.Content;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Android Hong on 2017-12-11.
 */

public class TextViewHolder extends PostViewHolder {

    @BindView(R.id.textContent)
    TextView textContent;

    public TextViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bind(Content data) {
        if(Const.CONTENT_TEXT_TYPE_BASIC.equals(data.getType())){
            // 일반글일경우
            textContent.setText(data.getContent());
        }else{
            // 강조글일 경우
            textContent.setText(data.getContent());
        }

    }

}
