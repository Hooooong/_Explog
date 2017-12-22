package com.hongsup.explog.view.newspeed.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.hongsup.explog.R;
import com.hongsup.explog.view.newspeeditem.contract.NewsPeedItemContract;
import com.hongsup.explog.view.newspeeditem.presenter.NewsPeedItemPresenter;
import com.hongsup.explog.view.newspeeditem.view.NewsPeedItemView;

/**
 * Created by Android Hong on 2017-11-30.
 */

public class NewsPeedViewPagerAdapter extends PagerAdapter {

    private static final int COUNT = 6;
    private Context context;

    private NewsPeedItemContract.iPresenter newsPeedItemPresenter;
    private NewsPeedItemContract.iView newsPeedItemView;

    private SparseArray<View> views = new SparseArray<>(COUNT);

    public NewsPeedViewPagerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return COUNT;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        newsPeedItemPresenter = new NewsPeedItemPresenter();
        newsPeedItemView = new NewsPeedItemView(context, position+1);

        newsPeedItemPresenter.attachView(newsPeedItemView);
        newsPeedItemView.setPresenter(newsPeedItemPresenter);

        View view = (View)newsPeedItemView;
        container.addView(view);
        views.put(position, view);
        return view;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View)object;
        container.removeView(view);
        views.remove(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getResources().getString(R.string.asia);
            case 1:
                return context.getResources().getString(R.string.europe);
            case 2:
                return context.getResources().getString(R.string.north_americas);
            case 3:
                return context.getResources().getString(R.string.south_americas);
            case 4:
                return context.getResources().getString(R.string.africa);
            case 5:
                return context.getResources().getString(R.string.oceania);
            default:
                return "띠용";
        }
    }
}