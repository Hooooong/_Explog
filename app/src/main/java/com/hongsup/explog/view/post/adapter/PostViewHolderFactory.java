package com.hongsup.explog.view.post.adapter;

import android.view.View;

import com.hongsup.explog.R;
import com.hongsup.explog.data.Const;
import com.hongsup.explog.view.post.adapter.viewholder.PathViewHolder;
import com.hongsup.explog.view.post.adapter.viewholder.PhotoViewHolder;
import com.hongsup.explog.view.post.adapter.viewholder.PostViewHolder;
import com.hongsup.explog.view.post.adapter.viewholder.TextViewHolder;

/**
 * Created by Android Hong on 2017-12-12.
 */

public class PostViewHolderFactory {
    /**
     * Type 에 맞게 Layout Id 를 반환하는 메소드
     *
     * @param type
     * @return
     */
    public static int getPostItemLayoutId(int type) {
        int layoutId;
        switch (type) {
            case Const.VIEW_TYPE_TEXT:
                layoutId = R.layout.item_post_text;
                break;
            case Const.VIEW_TYPE_PHOTO:
                layoutId = R.layout.item_post_photo;
                break;
            case Const.VIEW_TYPE_PATH:
                layoutId = R.layout.item_post_path;
                break;
            default:
                throw new RuntimeException(type + "에 맞는 Layout 이 없습니다.");
        }
        return layoutId;
    }

    /**
     * Type 에 맞는 ViewHolder 를 반환하는 메소드
     * @param type
     * @param itemView
     * @return
     */
    public static PostViewHolder getViewHolder(int type, View itemView) {

        PostViewHolder viewHolder;
        switch (type) {
            case Const.VIEW_TYPE_TEXT:
                viewHolder = new TextViewHolder(itemView);
                break;
            case Const.VIEW_TYPE_PHOTO:
                viewHolder = new PhotoViewHolder(itemView);
                break;
            case Const.VIEW_TYPE_PATH:
                viewHolder = new PathViewHolder(itemView);
                break;
            default:
                throw new RuntimeException(type + "에 맞는 ViewHolder 가 없습니다.");
        }
        return viewHolder;
    }

    public static int getViewType(String contentType) {
        if (Const.CONTENT_TYPE_TEXT.equals(contentType)) {
            return Const.VIEW_TYPE_TEXT;
        } else if (Const.CONTENT_TYPE_PHOTO.equals(contentType)) {
            return Const.VIEW_TYPE_PHOTO;
        } else if (Const.CONTENT_TYPE_PATH.equals(contentType)) {
            return Const.VIEW_TYPE_PATH;
        }
        throw new RuntimeException("there is no type that matches the type " + contentType + " + make sure your using types correctly");
    }
}
