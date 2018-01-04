package com.hongsup.explog.view.search.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hongsup.explog.R;
import com.hongsup.explog.data.Const;
import com.hongsup.explog.data.post.PostCover;
import com.hongsup.explog.view.post.PostActivity;
import com.hongsup.explog.view.search.adapter.HistorySearchAdapter;
import com.hongsup.explog.view.search.adapter.SearchResultAdapter;
import com.hongsup.explog.view.search.contract.SearchContract;
import com.hongsup.explog.view.search.presenter.SearchPresenter;
import com.jakewharton.rxbinding2.widget.RxTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 정인섭 on 2017-12-08.
 * <p>
 * Modified by Android Hong on 2017-12-20
 * - MVP Pattern 으로 변경하였습니다.
 * - 변수면 변경 및 구조 변경
 */

public class SearchView extends FrameLayout implements SearchContract.iView {

    private Context context;
    private InputMethodManager inputMethodManager;

    private HistorySearchAdapter historySearchAdapter;
    private SearchResultAdapter searchResultAdapter;

    private SearchContract.iPresenter searchPresenter;

    @BindView(R.id.editSearch)
    EditText editSearch;
    @BindView(R.id.recyclerSearchHistory)
    RecyclerView recyclerView;
    @BindView(R.id.imgDeleteTextSearch)
    ImageView imgDeleteTextSearch;

    @BindView(R.id.relativeSearching)
    RelativeLayout relativeSearching;
    @BindView(R.id.textSearching)
    TextView textSearching;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    public SearchView(@NonNull Context context) {
        super(context);
        this.context = context;

        initView();
        setPresenter(new SearchPresenter(context));
    }

    // View 초기화
    private void initView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_search, null);
        ButterKnife.bind(this, view);
        // 키보드 관련
        inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);

        initAdapter();
        initListener();
        addView(view);
    }

    // Presenter Setting
    @Override
    public void setPresenter(SearchContract.iPresenter presenter) {
        this.searchPresenter = presenter;

        // View 설정
        this.searchPresenter.attachView(this);

        // Adapter 설정
        this.searchPresenter.setHistorySearchAdapterModel(historySearchAdapter);
        this.searchPresenter.setHistorySearchAdapterView(historySearchAdapter);
        this.searchPresenter.setSearchResultAdapterModel(searchResultAdapter);
        this.searchPresenter.setSearchResultAdapterView(searchResultAdapter);

        // Item 불러오기
        this.searchPresenter.loadHistorySearchWord();
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(GONE);
    }

    @Override
    public void setHistoryToSearchEditText(String word) {
        editSearch.setText(word);
        //editSearch.setSelection(word.length());
    }

    @Override
    public void showNoData() {
        relativeSearching.setVisibility(VISIBLE);
    }

    /**
     * Listener 설정
     */
    private void initListener() {

        // EditText Listener
        editSearch.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                /**
                 * 비어있지 않고, ENTER Key 를 눌렀을 때만 word 를 Insert 한다.
                 */
                if (!"".equals(editSearch.getText().toString()) && keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {

                    // 기존에 있던 word 지우고, 새로 넣어주는 작업
                    // 다 하였으면 새로 Load 함
                    String word = editSearch.getText().toString();
                    searchPresenter.deleteSearchWord(word);
                    searchPresenter.insertSearchWord(word);
                    searchPresenter.loadHistorySearchWord();

                    // KeyBoard 숨기는 작업
                    inputMethodManager.hideSoftInputFromWindow(editSearch.getWindowToken(), 0);
                }
                return false;
            }
        });

        // Delete ImageButton Click Listener
        imgDeleteTextSearch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!"".equals(editSearch.getText().toString())) {
                    editSearch.setText("");
                }
            }
        });

        // EditSearch Binding Listener
        RxTextView.textChanges(editSearch)
                .subscribe(ch -> {
                    if (ch.length() > 0) {
                        // 검색할 때
                        relativeSearching.setVisibility(GONE);
                        recyclerView.setAdapter(searchResultAdapter);

                        searchPresenter.loadSearchResult(ch.toString());

                        //결과가 검색이 되면 스크롤 움직였을 때 소프트 키보드 사라짐 12/20
                        recyclerView.setOnTouchListener(new OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                                    inputMethodManager.hideSoftInputFromWindow(recyclerView.getWindowToken(), 0);
                                }
                                return false;
                            }
                        });
                    } else {
                        progressBar.setVisibility(GONE);
                        relativeSearching.setVisibility(GONE);
                        recyclerView.setAdapter(historySearchAdapter);
                    }
                });
    }

    private void initAdapter() {
        historySearchAdapter = new HistorySearchAdapter(context);
        searchResultAdapter = new SearchResultAdapter(context);

        // recyclerView 에 `최근검색어` 관련 Adapter 설정
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(historySearchAdapter);
    }

    @Override
    public void goToPost(PostCover cover) {
        Intent intent = new Intent(context, PostActivity.class);
        intent.putExtra(Const.INTENT_EXTRA_COVER, cover);
        context.startActivity(intent);
    }
}
