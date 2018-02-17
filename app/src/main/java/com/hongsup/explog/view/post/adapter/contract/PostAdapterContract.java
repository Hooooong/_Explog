package com.hongsup.explog.view.post.adapter.contract;

import com.hongsup.explog.data.post.PostContent;
import com.hongsup.explog.view.post.listener.OnPostContentClickListener;

import java.util.List;

/**
 * Created by Android Hong on 2017-12-14.
 */

public interface PostAdapterContract {

    interface iView {
        void setOnPostContentClickListener(OnPostContentClickListener postContentClickListener);

        void notifyAllAdapter();
    }

    interface iModel {
        void setItems(List<PostContent> postContentList);

        void addItems(PostContent postContent);
    }
}
