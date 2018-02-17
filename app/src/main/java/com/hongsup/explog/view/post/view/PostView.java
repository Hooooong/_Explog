package com.hongsup.explog.view.post.view;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.hongsup.explog.R;
import com.hongsup.explog.data.Const;
import com.hongsup.explog.data.post.PostCover;
import com.hongsup.explog.data.user.source.UserRepository;
import com.hongsup.explog.util.DateUtil;
import com.hongsup.explog.util.DialogUtil;
import com.hongsup.explog.view.gallery.GalleryActivity;
import com.hongsup.explog.view.post.adapter.PostAdapter;
import com.hongsup.explog.view.post.contract.PostContract;
import com.hongsup.explog.view.posttext.PostTextActivity;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.view.View.VISIBLE;

/**
 * Created by Android Hong on 2017-12-14.
 */

public class PostView implements PostContract.iView {

    private Context context;
    private PostContract.iPresenter presenter;
    private View view;
    private Intent coverIntent;
    private Intent contentIntent;
    private PostCover cover;
    private PostAdapter postAdapter;
    private int menuId;
    private PostItemDialog postItemDialog;

    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.imgCover)
    ImageView imgCover;
    @BindView(R.id.textTitle)
    TextView textTitle;
    @BindView(R.id.textDate)
    TextView textDate;
    @BindView(R.id.textWriter)
    TextView textWriter;
    @BindView(R.id.imgProfile)
    CircleImageView imgProfile;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.progressBarLayout)
    RelativeLayout progressBarLayout;

    @BindView(R.id.postContentLayout)
    ConstraintLayout postContentLayout;

    @BindView(R.id.likeAndAuthorLayout)
    ConstraintLayout likeAndAuthorLayout;
    @BindView(R.id.imgAuthorProfile)
    CircleImageView imgAuthorProfile;
    @BindView(R.id.lineView)
    View lineView;
    @BindView(R.id.authorLayout)
    RelativeLayout authorLayout;
    @BindView(R.id.textAuthor)
    TextView textAuthor;
    @BindView(R.id.textEmail)
    TextView textEmail;
    @BindView(R.id.imgLike)
    ImageButton imgLike;
    @BindView(R.id.textLikeCount)
    TextView textLikeCount;

    @BindView(R.id.initLayout)
    ConstraintLayout initLayout;
    @BindView(R.id.textInitWriter)
    TextView textInitWriter;
    @BindView(R.id.textSummary)
    TextView textSummary;


    public PostView(Context context) {
        this.context = context;
        view = LayoutInflater.from(context).inflate(R.layout.activity_post, null);
        ButterKnife.bind(this, view);

        coverIntent = ((Activity) context).getIntent();
        cover = (PostCover) coverIntent.getSerializableExtra(Const.INTENT_EXTRA_COVER);

        initToolbar();
        initAdapter();
        setCoverData();
    }

    @Override
    public void setPresenter(PostContract.iPresenter presenter) {
        this.presenter = presenter;
        this.presenter.setPostAdapterModel(postAdapter);
        this.presenter.setPostAdapterView(postAdapter);
        this.presenter.loadPostContent(cover);
    }

    @Override
    public View getView() {
        return view;
    }

    @Override
    public void showProgress() {
        progressBarLayout.setVisibility(VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBarLayout.setVisibility(View.GONE);
    }

    @Override
    public void showError() {
    }

    @Override
    public void setMenu(Menu menu) {
        if (UserRepository.getInstance().getUser() != null && cover.getAuthor().getEmail().equals(UserRepository.getInstance().getUser().getEmail())) {
            // 내 글인경우
            menuId = R.menu.menu_my_post;
        } else {
            // 남의 글인경우
            menuId = R.menu.menu_your_post;
        }
        ((AppCompatActivity) context).getMenuInflater().inflate(menuId, menu);
    }

    @Override
    public void onMenuClick(MenuItem item) {
        if (menuId == R.menu.menu_my_post) {
            // 내 글이면
            int id = item.getItemId();

            switch (id) {
                case R.id.action_cover_change:
                    Toast.makeText(context, "cover 변경", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.action_delete:
                    DialogUtil.showBasicDialog(context, context.getResources().getString(R.string.post_delete), context.getResources().getString(R.string.post_delete_msg), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 확인을 누를 경우
                            presenter.deletePost(cover.getPk());
                        }
                    }).show();
                    break;
            }
        } else {
            // 남 글이면

        }
    }

    @Override
    public void deletePost(boolean flag) {
        if (flag) {
            Toast.makeText(context, "여행기가 삭제되었습니다.", Toast.LENGTH_SHORT).show();
            ((Activity) context).finish();
        } else {
            Toast.makeText(context, "여행기 삭제가 실패햐였습니다. 다시 시도해주시기 바랍니다.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void setInit(boolean flag) {

        if (flag) {
            initLayout.setVisibility(VISIBLE);
            recyclerView.setVisibility(View.GONE);

            if (UserRepository.getInstance().getUser() != null && cover.getAuthor().getEmail().equals(UserRepository.getInstance().getUser().getEmail())) {
                // 내 글이면
                textInitWriter.setText("안녕하세요. " + UserRepository.getInstance().getUser().getUsername() + " 님!");
                textSummary.setText("당신의 여행 이야기를 작성해보세요");

            } else {
                // 남 글이면
                textInitWriter.setText("음... 아직 작성이 안되었네요!");
                textSummary.setText("다음 기회에 여행 이야기를 둘러보세요");
            }

            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(postContentLayout);
            constraintSet.connect(likeAndAuthorLayout.getId(), ConstraintSet.TOP, postContentLayout.getId(), ConstraintSet.BOTTOM, 0);
            constraintSet.applyTo(postContentLayout);

        } else {
            initLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(VISIBLE);

            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(postContentLayout);
            constraintSet.connect(likeAndAuthorLayout.getId(), ConstraintSet.TOP, postContentLayout.getId(), ConstraintSet.BOTTOM, 0);
            constraintSet.applyTo(postContentLayout);
        }
        likeAndAuthorLayout.setVisibility(VISIBLE);
    }

    private void initToolbar() {
        ((AppCompatActivity) context).setSupportActionBar(toolbar);
        ((AppCompatActivity) context).getSupportActionBar().setTitle("");
        ((AppCompatActivity) context).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) context).finish();
            }
        });

        /*
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(appBarLayout.getTotalScrollRange()) == Math.abs(verticalOffset)) {
                    // 같으면 Toolbar 에 있는 Layout 을 보여준다.
                    toolbar.setVisibility(View.VISIBLE);
                } else {
                    // 다를 경우 Toolbar 에 있는 Layout 을 감춘다.
                    toolbar.setVisibility(View.INVISIBLE);
                }
            }
        });*/
    }

    private void initAdapter() {
        if (UserRepository.getInstance().getUser() != null) {
            // 로그인한 상태이면
            postAdapter = new PostAdapter(context, cover.getAuthor().getEmail().equals(UserRepository.getInstance().getUser().getEmail()));
        } else {
            // 로그인하지 않은 상태이면면
            postAdapter = new PostAdapter(context, false);
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(postAdapter);
    }

    private void setCoverData() {
        if (cover != null) {
            // Cover 에서 작성한 경우
            textTitle.setText(cover.getTitle());
            textDate.setText(DateUtil.getConvertDate(cover.getStartDate(), cover.getEndDate()));
            textWriter.setText(cover.getAuthor().getUsername());

            Glide.with(context)
                    .load(cover.getCoverPath())
                    .centerCrop()
                    .into(imgCover);
            imgCover.setColorFilter(ContextCompat.getColor(context, R.color.colorPostTint), PorterDuff.Mode.SRC_OVER);

            Glide.with(context)
                    .load(cover.getAuthor().getImg_profile())
                    .into(imgProfile);

            // 1. 좋아요 설정
            setLiked(cover.getLiked(), cover.getLikeCount());

            if (UserRepository.getInstance().getUser() != null && cover.getAuthor().getEmail().equals(UserRepository.getInstance().getUser().getEmail())) {
                // 내 글인경우
                fab.setVisibility(VISIBLE);

                // 2. Author 설정
                lineView.setVisibility(View.GONE);
                authorLayout.setVisibility(View.GONE);
            } else {
                // 남 글인경우
                fab.setVisibility(View.GONE);

                // 2. Author 설정
                lineView.setVisibility(View.VISIBLE);
                authorLayout.setVisibility(View.VISIBLE);

                textAuthor.setText(cover.getAuthor().getUsername());
                textEmail.setText(cover.getAuthor().getEmail());

                Glide.with(context)
                        .load(cover.getAuthor().getImg_profile())
                        .into(imgAuthorProfile);

            }
        }
    }

    @OnClick(R.id.fab)
    public void animateFAB() {
        postItemDialog = new PostItemDialog(context,
                view -> {
                    // 일반 글 클릭했을 경우
                    contentIntent = new Intent(context, PostTextActivity.class);
                    contentIntent.putExtra(Const.INTENT_EXTRA_TYPE, Const.CONTENT_TEXT_TYPE_BASIC);
                    ((Activity) context).startActivityForResult(contentIntent, Const.REQ_TEXT);
                    postItemDialog.dismiss();
                },
                view -> {
                    // 강조글 클릭했을 경우
                    contentIntent = new Intent(context, PostTextActivity.class);
                    contentIntent.putExtra(Const.INTENT_EXTRA_TYPE, Const.CONTENT_TEXT_TYPE_HIGHLIGHT);
                    ((Activity) context).startActivityForResult(contentIntent, Const.REQ_TEXT);
                    postItemDialog.dismiss();
                },
                view -> {
                    // 사진 클릭했을 경우
                    contentIntent = new Intent(context, GalleryActivity.class);
                    contentIntent.putExtra(Const.INTENT_EXTRA_TYPE, Const.CONTENT_TEXT_TYPE_HIGHLIGHT);
                    ((Activity) context).startActivityForResult(contentIntent, Const.REQ_GALLERY);
                    postItemDialog.dismiss();
                },
                view -> {
                    // 경로 클릭햇을 경우

                    /*
                     PostPath 를 작성하기 위해 Google Place Intent 사용
                     */
                    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                    try {
                        ((Activity) context).startActivityForResult(builder.build((Activity) context), Const.REQ_PATH);
                    } catch (GooglePlayServicesRepairableException e) {
                        e.printStackTrace();
                    } catch (GooglePlayServicesNotAvailableException e) {
                        e.printStackTrace();
                    }
                    postItemDialog.dismiss();
                },
                view -> {
                    // layout 클릭햇을 경우
                    postItemDialog.dismiss();
                });

        postItemDialog.setCanceledOnTouchOutside(true);
        postItemDialog.show();
    }


    private void setLiked(int[] liked, int likeCount) {
        /**
         *  좋아요 설정
         */
        if (UserRepository.getInstance().getUser() != null && isLiked(liked)) {
            // '좋아요'를 눌렀을 경우
            imgLike.setImageResource(R.drawable.ic_like_red_16dp);
            textLikeCount.setTextColor(context.getResources().getColor(R.color.colorRed));
            textLikeCount.setText(likeCount + "");
        } else {
            // '좋아요'를 누르지 않았을 경우
            imgLike.setImageResource(R.drawable.ic_like_gray_16dp);
            textLikeCount.setTextColor(context.getResources().getColor(R.color.colorGray));
            textLikeCount.setText(likeCount + "");
        }
    }

    private boolean isLiked(int[] liked) {
        if (liked != null && Arrays.binarySearch(liked, UserRepository.getInstance().getUser().getPk()) >= 0) {
            return true;
        } else {
            return false;
        }
    }
}
