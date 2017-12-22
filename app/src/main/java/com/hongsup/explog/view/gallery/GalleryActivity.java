package com.hongsup.explog.view.gallery;

/**
 * Created by 정인섭 on 2017-11-30.
 */

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.hongsup.explog.R;
import com.hongsup.explog.data.Const;
import com.hongsup.explog.data.photo.Photo;
import com.hongsup.explog.util.GalleryUtil;
import com.hongsup.explog.view.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class GalleryActivity extends BaseActivity implements GalleryListener {

    private static final String TAG = "GalleryActivity";
    private Menu menu;
    private Toolbar toolbar;
    private RecyclerView recyclerGallery;
    private GalleryAdapter galleryAdapter;
    private ProgressBar progressBar;
    private LinearLayout countLayout;

    public GalleryActivity() {

        super(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE});

    }

    @Override
    public void init() {
        setContentView(R.layout.activity_gallery);
        initLayout();
        initGalleryAdapter();
        setGallery();
    }


    private void initLayout() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        recyclerGallery = (RecyclerView) findViewById(R.id.recyclerGallery);
        galleryAdapter = new GalleryAdapter(this);
        recyclerGallery.setAdapter(galleryAdapter);
        recyclerGallery.setLayoutManager(new GridLayoutManager(this, 3));
    }

    private void initGalleryAdapter() {
        galleryAdapter = new GalleryAdapter(this);
        recyclerGallery.setAdapter(galleryAdapter);
        recyclerGallery.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        recyclerGallery.setItemAnimator(new DefaultItemAnimator());
        // RecyclerView 애니메이션 제거
        ((SimpleItemAnimator) recyclerGallery.getItemAnimator()).setSupportsChangeAnimations(false);
    }

    /**
     * Gallery 사진 불러오기
     * <p>
     * Thread 로 돌린다.
     */
    @SuppressLint("StaticFieldLeak")
    private void setGallery() {
        new AsyncTask<String, Void, List<Photo>>() {
            @Override
            protected void onPreExecute() {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected List<Photo> doInBackground(String... strings) {
                return GalleryUtil.getAllPhotoPathList(GalleryActivity.this);
            }

            @Override
            protected void onPostExecute(List<Photo> result) {
                progressBar.setVisibility(View.GONE);
                galleryAdapter.setPhotoList(result);
            }
        }.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_gallery, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_next:
                if (item.getIntent() != null) {
                    // 선택된 사진이 있으면 다음 Intent 로 이동한다.
                    ArrayList<Photo> selectPhotoList = galleryAdapter.getSelectPhotoList();
                    setResult(RESULT_OK, item.getIntent().putExtra(Const.INTENT_EXTRA_PHOTO, selectPhotoList));
                    finish();
                }
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void PhotoClick(int position) {
        Log.d("클릭되었습니다", "클릭");

        Photo photo = galleryAdapter.getPhotoList().get(position);
        List<Photo> selectPhotoList = galleryAdapter.getSelectPhotoList();

        if (selectPhotoList.contains(photo)) {
            galleryAdapter.removeSelectPhotoList(photo);
        } else {
            galleryAdapter.addSelectPhotoList(photo);
        }

        if(selectPhotoList.size()==2){
            galleryAdapter.removeSelectPhotoList(galleryAdapter.getSelectPhotoList().get(0));
        }

    }

    @Override
    public void changeView(int count) {
        if (count == 0) {
            // icon 변경 및 intent 설정
            changeMenu(R.id.action_next, R.drawable.ic_keyboard_arrow_right_false, false);
        } else {
            // icon 변경 및 intent 설정
            changeMenu(R.id.action_next, R.drawable.ic_keyboard_arrow_right_true, true);
//            String cnt = Integer.toString(count);
//            textCount.setText(cnt);
        }
    }

    @Override
    public void selectError() {
        Toast.makeText(this, "사진은 10장까지만 선택하실 수 있어요", Toast.LENGTH_SHORT).show();
    }

    /**
     * Menu Icon 이미지 변경 및 Intent 설정
     *
     * @param id      menu_gallery id 값
     * @param iconRes 변경할 Resource 값
     */
    private void changeMenu(int id, int iconRes, boolean check) {
        MenuItem item = menu.findItem(id);
        item.setIcon(iconRes);
        if (check) {
            Intent intent = new Intent();
            item.setIntent(intent);
        } else {
            item.setIntent(null);
        }
    }
}