package com.hongsup.explog.view.post;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.hongsup.explog.data.Const;
import com.hongsup.explog.data.photo.Photo;
import com.hongsup.explog.view.post.contract.PostContract;
import com.hongsup.explog.view.post.listener.OnPostContentClickListener;
import com.hongsup.explog.view.post.presenter.PostPresenter;
import com.hongsup.explog.view.post.view.PostView;

import java.util.ArrayList;

public class PostActivity extends AppCompatActivity implements OnPostContentClickListener {

    private static final String TAG = "PotsActivity";

    private PostContract.iView postView;
    private PostContract.iPresenter postPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        postView = new PostView(this);
        postPresenter = new PostPresenter();

        postPresenter.attachView(postView);
        postView.setPresenter(postPresenter);

        setContentView(postView.getView());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        postView.setMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        postView.onMenuClick(item);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Const.REQ_TEXT:
                if (resultCode == RESULT_OK) {
                    /*
                     Post 의 글 작성이 완료된 경우
                     */
                    String text = data.getStringExtra(Const.INTENT_EXTRA_TEXT);
                    String type = data.getStringExtra(Const.INTENT_EXTRA_TYPE);

                    postPresenter.uploadPostText(text, type);
                }
                break;
            case Const.REQ_GALLERY:
                if (resultCode == RESULT_OK) {
                    /*
                     Post 의 사진 작성이 완료된 경우
                     */
                    ArrayList<Photo> photoList = (ArrayList<Photo>) data.getSerializableExtra(Const.INTENT_EXTRA_PHOTO);
                    postPresenter.uploadPostPhoto(photoList.get(0).getImagePath());
                }
                break;
            case Const.REQ_PATH:
                if (resultCode == RESULT_OK) {
                    /*
                     Post 의 장소 작성이 완료된 경우
                     */
                    Place place = PlacePicker.getPlace(this, data);
                    LatLng latLng = place.getLatLng();
                    postPresenter.uploadPostPath(latLng.latitude, latLng.longitude);
                }
                break;
        }
    }
}
