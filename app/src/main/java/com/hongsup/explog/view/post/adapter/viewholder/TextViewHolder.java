package com.hongsup.explog.view.post.adapter.viewholder;

import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
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
    @BindView(R.id.textCreateAt)
    TextView textCreateAt;
    @BindView(R.id.btnMore)
    ImageButton btnMore;

    @BindView(R.id.imgLeftQuote)
    ImageView imgLeftQuote;

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
            // SP 단위로 입력 가능할 수 있도록 한다.
            textContent.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            textContent.setGravity(Gravity.CENTER_HORIZONTAL);
            textContent.setTypeface(null, Typeface.BOLD);
            imgLeftQuote.setVisibility(View.VISIBLE);
        }

        if(!checkMyPost){
            btnMore.setVisibility(View.GONE);
        }

    }

}
