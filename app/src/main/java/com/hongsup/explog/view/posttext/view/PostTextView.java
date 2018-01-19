package com.hongsup.explog.view.posttext.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hongsup.explog.R;
import com.hongsup.explog.data.Const;
import com.hongsup.explog.data.post.Content;
import com.hongsup.explog.view.post.PostActivity;
import com.hongsup.explog.view.posttext.contract.PostTextContract;
import com.jakewharton.rxbinding2.widget.RxTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Hong on 2017-12-14.
 */

public class PostTextView implements PostTextContract.iView {

    private static final String TAG = "PostTextView";

    private Context context;
    private PostTextContract.iPresenter presenter;
    private View view;
    private Intent postIntent;
    private Intent uploadIntent;
    private Menu menu;
    private Content content;
    private String type;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.textCount)
    TextView textCount;
    @BindView(R.id.editText)
    EditText editText;

    public PostTextView(Context context) {
        this.context = context;
        view = LayoutInflater.from(context).inflate(R.layout.activity_post_text, null);
        ButterKnife.bind(this, view);

        /*
         Upload 할 Intent 설정
         */
        uploadIntent = new Intent(context, PostActivity.class);
        postIntent = ((Activity) context).getIntent();

        /*
         수정 시 들어오는 Content
         */
        content = (Content) postIntent.getSerializableExtra(Const.INTENT_EXTRA_TEXT);
        type =  postIntent.getStringExtra(Const.INTENT_EXTRA_TYPE);

        initToolbar();
        initView();
        setContentData();
    }

    private void initView() {
        /*
         EditText Count 설정
         */
        if (Const.CONTENT_TEXT_TYPE_BASIC.equals(type)){
            textCount.setVisibility(View.GONE);
            editText.setTypeface(null, Typeface.NORMAL);
        }else{
            textCount.setVisibility(View.VISIBLE);
            editText.setGravity(Gravity.CENTER_HORIZONTAL);
            editText.setTypeface(null, Typeface.BOLD);
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});

            RxTextView.textChangeEvents(editText)
                    .subscribe(ch -> {
                        textCount.setText(ch.text().length() + " / 20");
                    });
        }

    }

    @Override
    public void setPresenter(PostTextContract.iPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public View getView() {
        return view;
    }

    @Override
    public void setMenu(Menu menu) {
        this.menu = menu;
        ((Activity) context).getMenuInflater().inflate(R.menu.menu_cover, menu);

        RxTextView.textChangeEvents(editText)
                .subscribe(ch -> {
                    if (ch.text().length() > 0) {
                        changeMenu(R.id.action_ok, true);
                    } else {
                        changeMenu(R.id.action_ok, false);
                    }
                });
    }

    @Override
    public void onMenuClick(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_ok:
                if (content != null) {
                    // 글 수정인 경우

                } else {
                    // 글 작성인 경우
                    uploadIntent.putExtra(Const.INTENT_EXTRA_TEXT, editText.getText().toString());
                    uploadIntent.putExtra(Const.INTENT_EXTRA_TYPE, type);
                    ((Activity) context).setResult(Activity.RESULT_OK, uploadIntent);
                    ((Activity) context).finish();
                }
                break;
        }
    }

    private void initToolbar() {
        ((AppCompatActivity) context).setSupportActionBar(toolbar);

        if (Const.CONTENT_TEXT_TYPE_BASIC.equals(type)){
            ((AppCompatActivity) context).getSupportActionBar().setTitle(context.getResources().getString(R.string.post_content_basic_text));
        }else{
            ((AppCompatActivity) context).getSupportActionBar().setTitle(context.getResources().getString(R.string.post_content_highlight_text));
        }

        ((AppCompatActivity) context).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) context).finish();
            }
        });
    }

    private void setContentData() {
        if (content != null) {
            editText.setText(content.getContent());
        }
    }

    /**
     * editText 변화에 대한 Menu 변환
     *
     * @param id    Menu Resource ID
     * @param check flag
     */
    private void changeMenu(int id, boolean check) {
        MenuItem item = menu.findItem(id);
        if (check) {
            item.setIcon(R.drawable.ic_check_white_24dp);
            item.setIntent(uploadIntent);
            item.setEnabled(true);
        } else {
            item.setIcon(R.drawable.ic_check_gray_24dp);
            item.setIntent(null);
            item.setEnabled(false);
        }
    }


}
