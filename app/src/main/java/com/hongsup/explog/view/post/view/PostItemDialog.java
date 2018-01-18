package com.hongsup.explog.view.post.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.view.WindowManager;

import com.hongsup.explog.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Hong on 2018-01-18.
 */

public class PostItemDialog extends Dialog {

    @BindView(R.id.btnBasicText)
    FloatingActionButton btnBasicText;
    @BindView(R.id.btnHighLightText)
    FloatingActionButton btnHighLightText;
    @BindView(R.id.btnPhoto)
    FloatingActionButton btnPhoto;
    @BindView(R.id.btnPath)
    FloatingActionButton btnPath;

    public PostItemDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.8f;
        getWindow().setAttributes(layoutParams);

        setContentView(R.layout.post_item_select);
    }
}
