package com.raghu.thetapracticle.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.raghu.thetapracticle.R;
import com.raghu.thetapracticle.fragment.HomeFragment;
import com.raghu.thetapracticle.fragment.MapFragment;
import com.raghu.thetapracticle.fragment.ProfileFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {
    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.home, R.string.map, R.string.profile};
    private final Context mContext;

    public PagerAdapter(Context context, @NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.mContext = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new HomeFragment();
            case 1:
                return new MapFragment();
            case 2:
                return new ProfileFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }
}