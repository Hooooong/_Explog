package com.hongsup.explog.view.cover.view;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hongsup.explog.R;
import com.hongsup.explog.data.Const;
import com.hongsup.explog.data.post.PostCover;
import com.hongsup.explog.util.DateUtil;
import com.hongsup.explog.util.DialogUtil;
import com.hongsup.explog.view.cover.contract.CoverContract;
import com.hongsup.explog.view.custom.LimitedEditText;
import com.hongsup.explog.view.gallery.GalleryActivity;
import com.hongsup.explog.view.post.PostActivity;
import com.jakewharton.rxbinding2.widget.RxTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.hongsup.explog.data.Const.REQ_GALLERY;

/**
 * Created by Android Hong on 2017-12-14.
 */

public class CoverView implements CoverContract.iView {

    private View view;
    private Context context;
    private CoverContract.iPresenter presenter;
    private Menu menu;
    private Intent postIntent;
    private DatePickerDialog dialog;

    private boolean endFlag;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.editTitle)
    LimitedEditText editTitle;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    /*
    @BindView(R.id.textCount)
    TextView textCount;
    */
    @BindView(R.id.textStartDate)
    TextView textStartDate;
    @BindView(R.id.textEndDate)
    TextView textEndDate;
    @BindView(R.id.imgCover)
    ImageView imgCover;
    @BindView(R.id.textArea)
    TextView textArea;
    @BindView(R.id.textCoverPath)
    TextView textCoverPath;
    @BindView(R.id.progressBarLayout)
    RelativeLayout progressBarLayout;

    public CoverView(Context context) {
        this.context = context;
        view = LayoutInflater.from(context).inflate(R.layout.activity_cover, null);
        ButterKnife.bind(this, view);
        postIntent = new Intent(context, PostActivity.class);

        initToolbar();
        initListener();
        setStartDate();
    }

    @Override
    public View getView() {
        return view;
    }

    @Override
    public void setPresenter(CoverContract.iPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showProgress() {
        progressBarLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBarLayout.setVisibility(View.GONE);
    }

    @Override
    public void showError(String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setMenu(Menu menu) {
        this.menu = menu;
        ((Activity)context).getMenuInflater().inflate(R.menu.menu_cover, menu);

        /**
         *  EditTitle 변화
         */
        RxTextView.textChangeEvents(editTitle)
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
                PostCover cover = new PostCover();
                String area = textArea.getText().toString();

                if(textCoverPath.getTag() != null){
                    cover.setCoverPath((String) textCoverPath.getTag());
                }
                /*
                  지역 설정
                 */
                if(area.equals("") ){
                    Toast.makeText(context, "지역을 선택해주시기 바랍니다.", Toast.LENGTH_SHORT).show();
                    break;
                }else{
                    if(area.equals(context.getResources().getString(R.string.asia))){
                        cover.setContinent("1");
                    }else if(area.equals(context.getResources().getString(R.string.europe))){
                        cover.setContinent("2");
                    }else if(area.equals(context.getResources().getString(R.string.north_americas))){
                        cover.setContinent("3");
                    }else if(area.equals(context.getResources().getString(R.string.south_americas))){
                        cover.setContinent("4");
                    }else if(area.equals(context.getResources().getString(R.string.oceania))){
                        cover.setContinent("5");
                    }else if(area.equals(context.getResources().getString(R.string.africa))){
                        cover.setContinent("6");
                    }else{
                        cover.setContinent("0");
                    }
                }

                /*
                타이틀 설정
                 */
                cover.setTitle(editTitle.getText().toString());

                /*
                 시작 시간 설정 (yyyy-MM-ddT00:00:00)
                 */
                cover.setStartDate(DateUtil.setConvertDate(textStartDate.getText().toString()));

                /*
                종료 시간 설정 (yyyy-MM-ddT00:00:00)
                 */
                if(!textEndDate.getText().toString().equals(context.getResources().getString(R.string.txt_end_date))){
                    cover.setEndDate(DateUtil.setConvertDate(textEndDate.getText().toString()));
                }else{
                    cover.setEndDate(DateUtil.setConvertDate(textStartDate.getText().toString()));
                }

                presenter.createCover(cover);
                break;
        }
    }

    private void initToolbar() {
        ((AppCompatActivity) context).setSupportActionBar(toolbar);
        ((AppCompatActivity) context).getSupportActionBar().setDisplayShowTitleEnabled(false);

        /**
         * Toolbar Setting
         */
        toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) context).finish();
            }
        });
    }

    /**
     * editText 리스너 설정
     */
    private void initListener() {
        editTitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                /**
                 * focus 있을 경우
                 * ScrollView 하단으로 스크롤하는 메소드
                 *
                 * 수정사항 )
                 *
                 * ScrollView 를 움직일 경우
                 * focus 의 문제로 인해?
                 * 뒤로가기를 눌렀을 시에도 focus 가 있는 경우가 있다.
                 */
                if (hasFocus) {
                    scrollView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.smoothScrollBy(0, scrollView.getHeight());
                            editTitle.requestFocusFromTouch();
                        }
                    }, 280);
                }
            }
        });
    }

    /**
     * Start Date 설정
     */
    private void setStartDate() {
        textStartDate.setText(DateUtil.currentDate());
    }


    /**
     * Start Date 눌렀을 시
     */
    @OnClick(R.id.textStartDate)
    public void changeStartDate() {
        dialog = DialogUtil.showDatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                textStartDate.setText(String.format("%d.%d.%d", year, month + 1, day));
            }
        }, textStartDate.getText().toString());

        if (endFlag) {
            /**
             *  최대 날짜 설정
             */
            dialog.getDatePicker().setMaxDate(DateUtil.getConvertDate(textEndDate.getText().toString()));
        }
        dialog.show();
    }

    /**
     * End Date 눌렀을 시
     */
    @OnClick(R.id.textEndDate)
    public void changeEndDate() {
        dialog = DialogUtil.showDatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                endFlag = true;
                textEndDate.setText(String.format("%d.%d.%d", year, month + 1, day));
            }
        }, textEndDate.getText().toString());

        /**
         * 최소 날짜 설정
         */
        dialog.getDatePicker().setMinDate(DateUtil.getConvertDate(textStartDate.getText().toString()));
        dialog.show();
    }

    /**
     * Cover 변경 눌렀을 시
     */
    @OnClick(R.id.btnChangeCover)
    public void changeCover() {
        Intent intent = new Intent(context, GalleryActivity.class);
        ((Activity) context).startActivityForResult(intent, REQ_GALLERY);
    }

    /**
     * Area 변경 눌렀을 시
     */
    @OnClick(R.id.btnArea)
    public void setArea() {
        DialogUtil.showAreaDialog(context, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String area;
                switch (which) {
                    case 0:
                        area = context.getResources().getString(R.string.asia);
                        break;
                    case 1:
                        area = context.getResources().getString(R.string.europe);
                        break;
                    case 2:
                        area = context.getResources().getString(R.string.north_americas);
                        break;
                    case 3:
                        area = context.getResources().getString(R.string.south_americas);
                        break;
                    case 4:
                        area = context.getResources().getString(R.string.oceania);
                        break;
                    case 5:
                        area = context.getResources().getString(R.string.africa);
                        break;
                    default:
                        area = "띠용";
                        break;
                }
                textArea.setVisibility(View.VISIBLE);
                textArea.setText(area);
            }
        }).show();
    }

    /**
     * Cover 변경
     *
     * @param imagePath
     */
    @Override
    public void setCover(String imagePath) {
        Glide.with(context)
                .load(imagePath)
                .centerCrop()
                .into(imgCover);
        imgCover.setColorFilter(ContextCompat.getColor(context, R.color.colorPostTint), PorterDuff.Mode.SRC_OVER);
        textCoverPath.setTag(imagePath);
    }

    @Override
    public void goToPost(PostCover postCover) {
        postIntent = new Intent(context, PostActivity.class);
        postIntent.putExtra(Const.INTENT_EXTRA_COVER, postCover);
        context.startActivity(postIntent);
        ((Activity)context).finish();
    }

    /**
     * editTitle 변화에 대한 Menu 변환
     *
     * @param id    Menu Resource ID
     * @param check flag
     */
    private void changeMenu(int id, boolean check) {
        MenuItem item = menu.findItem(id);
        if (check) {
            item.setIcon(R.drawable.ic_check_white_24dp);
            item.setIntent(postIntent);
            item.setEnabled(true);
        } else {
            item.setIcon(R.drawable.ic_check_gray_24dp);
            item.setIntent(null);
            item.setEnabled(false);
        }
    }


}
