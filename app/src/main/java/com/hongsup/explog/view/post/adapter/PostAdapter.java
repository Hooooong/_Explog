package com.hongsup.explog.view.post.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hongsup.explog.data.post.PostContent;
import com.hongsup.explog.view.post.adapter.contract.PostAdapterContract;
import com.hongsup.explog.view.post.adapter.viewholder.PostViewHolder;
import com.hongsup.explog.view.post.listener.OnPostContentClickListener;
import com.hongsup.explog.view.post.listener.OnPostLikeClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android Hong on 2017-12-11.
 */

public class PostAdapter extends RecyclerView.Adapter<PostViewHolder> implements PostAdapterContract.iModel, PostAdapterContract.iView {

    private static final String TAG = "PostAdapter";

    private Context context;
    private List<PostContent> postContentList;
    private OnPostContentClickListener postContentClickListener;
    private OnPostLikeClickListener postLikeClickListener;
    private boolean checkMyPost;

    public PostAdapter(Context context, boolean checkMyPost) {
        postContentList = new ArrayList<>();
        this.context = context;
        this.checkMyPost = checkMyPost;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = PostViewHolderFactory.getPostItemLayoutId(viewType);
        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        return PostViewHolderFactory.getViewHolder(viewType, view);
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        PostContent postContent = postContentList.get(position);
        holder.setContext(context);
        holder.setPosition(position);
        holder.setCheckMyPost(checkMyPost);
        holder.setContentClickListener(postContentClickListener);
        holder.bind(postContent.getContent());
    }

    @Override
    public int getItemCount() {
        return postContentList.size();
    }

    @Override
    public int getItemViewType(int position) {
        PostContent postContent = postContentList.get(position);
        return PostViewHolderFactory.getViewType(postContent.getContentType());
    }

    @Override
    public void setOnPostContentClickListener(OnPostContentClickListener postContentClickListener) {
        this.postContentClickListener = postContentClickListener;
    }

    @Override
    public void notifyAllAdapter() {
        notifyItemRangeChanged(0, postContentList.size());
    }

    @Override
    public void setItems(List<PostContent> postContentList) {
        this.postContentList.clear();
        this.postContentList = postContentList;
    }

    @Override
    public void addItems(PostContent postContent) {
        /*
        if (postContentList.get(0).getContentType().equals(Const.CONTENT_TYPE_INIT)) {
            // 첫번째 아이템이 init 인 경우
            PostContent footerContent = createContent(postContentList.get(0).getContent().getLiked(), postContentList.get(0).getContent().getLikeCount(), postContentList.get(0).getContent().getAuthor(), Const.CONTENT_TYPE_FOOTER);
            this.postContentList.clear();
            this.postContentList.add(postContent);
            this.postContentList.add(footerContent);
            notifyItemRangeChanged(0, postContentList.size());
        } else {
            // 아닌 경우
            this.postContentList.add(postContentList.size() - 1, postContent);
            notifyItemInserted(postContentList.size() - 1);
        }
        */
    }

    /**
     * initViewHolder Item OR FooterViewHolder Item 생성기
     *
     * @param author
     * @param type
     * @return
     */

    /*private PostContent createContent(User author, String type) {
        PostContent postContent = new PostContent();
        *//**
         * Like 와 Author 설정
         *//*
        Content content = new Content();
        content.setAuthor(author);
        postContent.setContent(content);

        postContent.setContentType(type);

        return postContent;
    }*/

}
