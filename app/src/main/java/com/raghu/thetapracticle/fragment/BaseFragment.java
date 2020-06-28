package com.raghu.thetapracticle.fragment;


import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public abstract class BaseFragment extends Fragment {
    private static final String TAG = BaseFragment.class.getSimpleName();
    private View view;

    public abstract void getViewRoot();

    public void setViewRoot(View view) {
        this.view = view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (view.getParent() != null)
            ((ViewGroup) view.getParent()).removeView(view);
    }
}
