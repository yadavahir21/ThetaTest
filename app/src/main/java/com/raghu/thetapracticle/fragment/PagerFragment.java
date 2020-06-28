package com.raghu.thetapracticle.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.raghu.thetapracticle.R;
import com.raghu.thetapracticle.adapter.PagerAdapter;


public class PagerFragment extends BaseFragment {
    private static final String TAG = PagerFragment.class.getSimpleName();
    private View view;
    private PagerAdapter pageAdapter;

    public PagerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view != null)
            return view;
        view = inflater.inflate(R.layout.fragment_pager, container, false);
        getViewRoot();
        if (pageAdapter == null && isAdded()) {
            pageAdapter = new PagerAdapter(getActivity(), getChildFragmentManager());
            ViewPager viewPager = view.findViewById(R.id.pager);
            viewPager.setAdapter(pageAdapter);
            TabLayout tabLayout = view.findViewById(R.id.tabLayout);
            tabLayout.setupWithViewPager(viewPager);
            tabLayout.getTabAt(0).setIcon(R.drawable.ic_home);
            tabLayout.getTabAt(1).setIcon(R.drawable.ic_map);
            tabLayout.getTabAt(2).setIcon(R.drawable.ic_user_name);
        }
        return view;
    }

    @Override
    public void getViewRoot() {
        setViewRoot(view);
    }
}
