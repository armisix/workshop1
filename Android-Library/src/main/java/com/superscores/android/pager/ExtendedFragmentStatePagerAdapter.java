package com.superscores.android.pager;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import java.util.ArrayList;

public class ExtendedFragmentStatePagerAdapter extends FragmentStatePagerAdapter {

    private Context mContext;
    private ArrayList<MetaFragment> mMetaFragments;
    private SparseArray<Fragment> mRegisteredFragments = new SparseArray<>();

    public ExtendedFragmentStatePagerAdapter(Context context, FragmentManager fm) {
        this(context, fm, null);
    }

    public ExtendedFragmentStatePagerAdapter(Context context, FragmentManager fm,
            ArrayList<MetaFragment> metaFragments) {
        super(fm);
        mContext = context;
        mMetaFragments = metaFragments;
    }

    public void setMetaFragments(ArrayList<MetaFragment> metaFragments) {
        mMetaFragments = metaFragments;
    }

    public Context getContext() {
        return mContext;
    }

    @Override
    public Fragment getItem(int position) {
        return mMetaFragments.get(position)
                .getFragment();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mMetaFragments.get(position)
                .getTitle();
    }

    @Override
    public int getCount() {
        return mMetaFragments == null ? 0 : mMetaFragments.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        mRegisteredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        mRegisteredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getRegisteredFragment(int position) {
        return mRegisteredFragments.get(position);
    }

    public ArrayList<Fragment> getRegisteredFragment() {
        ArrayList<Fragment> registeredFragments = new ArrayList<>(mRegisteredFragments.size());
        for (int i = 0; i < mRegisteredFragments.size(); i++) {
            registeredFragments.add(mRegisteredFragments.valueAt(i));
        }
        return registeredFragments;
    }

}