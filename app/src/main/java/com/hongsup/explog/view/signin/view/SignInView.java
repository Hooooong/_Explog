package com.hongsup.explog.view.signin.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.text.TextUtils;
import android.transition.TransitionManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hongsup.explog.R;
import com.hongsup.explog.data.sign.SignIn;
import com.hongsup.explog.util.VerificationUtil;
import com.hongsup.explog.view.main.MainActivity;
import com.hongsup.explog.view.signin.contract.SignInContract;
import com.hongsup.explog.view.signup.SignUpActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * Created by Android Hong on 2017-11-30.
 */

public class SignInView implements SignInContract.iView {

    private static final String TAG = "SignIn";

    @BindView(R.id.signLayout)
    ConstraintLayout signLayout;
    @BindView(R.id.imgSign)
    ImageView imgSign;
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;
    @BindView(R.id.startLayout)
    LinearLayout startLayout;
    @BindView(R.id.inputLayout)
    LinearLayout inputLayout;
    @BindView(R.id.editEmail)
    EditText editEmail;
    @BindView(R.id.editPassword)
    EditText editPassword;
    @BindView(R.id.btnFacebook)
    Button btnFacebook;
    @BindView(R.id.line)
    View line;
    @BindView(R.id.progressBarLayout)
    RelativeLayout progressBarLayout;

    InputMethodManager imm;

    private boolean startFlag;

    private View view;
    private Context context;
    private SignInContract.iPresenter presenter;

    public SignInView(Context context) {
        this.context = context;
        view = LayoutInflater.from(context).inflate(R.layout.activity_sign_in, null);

        // 키보드 관련 객체
        imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        /**
         * ButterKnife Binding
         */
        ButterKnife.bind(this, view);
        setImageView();
        setListener();
    }

    private void setListener() {
        signLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // 입력필드들이 있는 레이아웃 가져오기
                int keyboard_height;
                Rect outGlobalRect = new Rect();
                Rect outWindowRect = new Rect();
                DisplayMetrics metrics = new DisplayMetrics();
                ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
                // 실제 레이아웃 사이즈 가져오기
                signLayout.getGlobalVisibleRect(outGlobalRect);
                // 보여지는 (키보드가 가린부분 제외) 레이아웃 사이즈 가져오기
                signLayout.getWindowVisibleDisplayFrame(outWindowRect);
                // 실제 레이아웃에서 보여지는 부분(키보드 크기)만 빼면 키보드의 높이가 나온다
                keyboard_height = outGlobalRect.bottom - outWindowRect.bottom;

                if (keyboard_height == 0) {
                    changeEditTextTransY(false, 0);
                    //changeEditTextBias(false);
                } else {
                    /**
                     * keyboard - line 위치 + StatusBar + ?
                     * 계산식의 오류인지 적용이 잘 되지 않는다.
                     */
                    float height = keyboard_height - line.getY() + outWindowRect.top + 32;
                    changeEditTextTransY(true, height);
                    //changeEditTextBias(true);
                }
            }
        });
    }

    @Override
    public View getView() {
        return view;
    }

    @Override
    public void setPresenter(SignInContract.iPresenter presenter) {
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
    public void goMain() {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
        ((Activity) context).finish();
    }

    @Override
    public boolean isInputEmail() {
        return startFlag;
    }

    @Override
    public void layoutReset() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TransitionManager.beginDelayedTransition(signLayout);
        }
        startLayout.setVisibility(View.VISIBLE);
        inputLayout.setVisibility(View.GONE);
        startFlag = false;
    }

    @OnTextChanged(value = R.id.editEmail, callback = OnTextChanged.Callback.TEXT_CHANGED)
    public void detectEmailChange(CharSequence charSequence, int i, int i1, int i2) {
        if (VerificationUtil.isValidEmail(charSequence.toString())) {
        }
    }

    @OnTextChanged(value = R.id.editPassword, callback = OnTextChanged.Callback.TEXT_CHANGED)
    public void detectPasswordChange(CharSequence charSequence, int i, int i1, int i2) {
        if (VerificationUtil.isValidPassword(charSequence.toString())) {
        }
    }

    @OnClick(R.id.btnFacebook)
    public void goToMain() {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
        ((Activity) context).finish();
    }

    @OnClick(R.id.txtSignUp)
    public void goSignUp() {
        Intent intent = new Intent(context, SignUpActivity.class);
        context.startActivity(intent);
    }

    @OnClick(R.id.btnEmail)
    public void inputEmail() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TransitionManager.beginDelayedTransition(signLayout);
        }
        startLayout.setVisibility(View.GONE);
        inputLayout.setVisibility(View.VISIBLE);
        startFlag = true;
    }

    @OnClick(R.id.btnStart)
    public void singIn() {
        if (TextUtils.isEmpty(editEmail.getText().toString())) {
            editEmail.setError("Email 을 입력하세요.");
            return;
        }

        if (TextUtils.isEmpty(editPassword.getText().toString())) {
            editPassword.setError("Password 를 입력하세요.");
            return;
        }

        if (VerificationUtil.isValidEmail(editEmail.getText().toString())) {
            editEmail.setError("Email 형식이 맞지 않습니다.");
            return;
        }

        if (VerificationUtil.isValidPassword(editPassword.getText().toString())) {
            editPassword.setError("Password 형식이 맞지 않습니다.");
            return;
        }

        imm.hideSoftInputFromWindow(editEmail.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(editPassword.getWindowToken(), 0);

        SignIn user = new SignIn();
        user.setEmail(editEmail.getText().toString());
        user.setPassword(editPassword.getText().toString());
        presenter.getSignIn(user);
    }

    /**
     * SingIn ImageView Setting
     */
    private void setImageView() {
        Glide.with(context)
                .load(R.drawable.sign)
                .fitCenter()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imgSign);
    }

    /**
     * EditText Layout 애니메이션 효과 주기
     *
     * @param flag : true 이면 위로, false 이면 아래로
     */
    private void changeEditTextTransY(boolean flag, float height) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TransitionManager.beginDelayedTransition(signLayout);
        }
        if (flag) {
            ConstraintSet set = new ConstraintSet();
            set.clone(signLayout);
            set.setTranslationY(R.id.linearLayout, -height);
            set.applyTo(signLayout);
        } else {
            ConstraintSet set = new ConstraintSet();
            set.clone(signLayout);
            set.setTranslationY(R.id.linearLayout, height);
            set.applyTo(signLayout);
        }
    }

    /**
     * EditText Layout 애니메이션 효과 주기
     *
     * @param flag : true 이면 위로, false 이면 아래로
     */
    private void changeEditTextBias(boolean flag) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TransitionManager.beginDelayedTransition(signLayout);
        }

        if (flag) {
            ConstraintSet set = new ConstraintSet();
            set.clone(signLayout);
            set.setVerticalBias(R.id.linearLayout, 0.5F);
            set.applyTo(signLayout);
        } else {
            ConstraintSet set = new ConstraintSet();
            set.clone(signLayout);
            set.setVerticalBias(R.id.linearLayout, 0.8F);
            set.applyTo(signLayout);
        }
    }
}
