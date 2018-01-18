package com.hongsup.explog.view.post.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.WindowManager;

import com.hongsup.explog.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Hong on 2018-01-18.
 */

public class PostItemDialog extends Dialog {

    private View.OnClickListener basicTextClickListener;
    private View.OnClickListener highLightTextClickListener;
    private View.OnClickListener photoClickListener;
    private View.OnClickListener pathClickListener;
    private View.OnClickListener layoutClickListener;

    @BindView(R.id.postItemSelectLayout)
    ConstraintLayout postItemSelectLayout;
    @BindView(R.id.btnBasicText)
    FloatingActionButton btnBasicText;
    @BindView(R.id.btnHighLightText)
    FloatingActionButton btnHighLightText;
    @BindView(R.id.btnPhoto)
    FloatingActionButton btnPhoto;
    @BindView(R.id.btnPath)
    FloatingActionButton btnPath;

    public PostItemDialog(@NonNull Context context,
                          View.OnClickListener basicTextClickListener,
                          View.OnClickListener highLightTextClickListener,
                          View.OnClickListener photoClickListener,
                          View.OnClickListener pathClickListener,
                          View.OnClickListener layoutClickListener) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);

        this.basicTextClickListener = basicTextClickListener;
        this.highLightTextClickListener = highLightTextClickListener;
        this.photoClickListener = photoClickListener;
        this.pathClickListener = pathClickListener;
        this.layoutClickListener = layoutClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.7f;
        getWindow().setAttributes(layoutParams);
        setContentView(R.layout.post_item_select);
        ButterKnife.bind(this);

        setListener();
    }

    private void setListener() {
        btnBasicText.setOnClickListener(basicTextClickListener);
        btnHighLightText.setOnClickListener(highLightTextClickListener);
        btnPhoto.setOnClickListener(photoClickListener);
        btnPath.setOnClickListener(pathClickListener);
        postItemSelectLayout.setOnClickListener(layoutClickListener);
    }

}
