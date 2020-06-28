package com.raghu.thetapracticle.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.raghu.thetapracticle.AppMain;
import com.raghu.thetapracticle.MainActivity;
import com.raghu.thetapracticle.R;
import com.raghu.thetapracticle.database.UserTable;
import com.raghu.thetapracticle.extra.Constant;
import com.raghu.thetapracticle.extra.Utils;

import stream.customalert.CustomAlertDialogue;

public class ProfileFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = ProfileFragment.class.getSimpleName();
    private View view;
    private ImageView imgUserProfile;
    private TextView txtUserNameProfile;
    private TextView txtEmailIDProfile;
    private Button btnLogout;
    private Button btnEditProfile;
    private String loginEMAIL = AppMain.getInstance().getPreferences().getString(Constant.LOGIN_EMAIL, "");

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view != null)
            return view;
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        getViewRoot();
        initViews();
        return view;
    }

    private void initViews() {
        imgUserProfile = view.findViewById(R.id.imgUserProfile);
        txtUserNameProfile = view.findViewById(R.id.txtUserNameProfile);
        txtEmailIDProfile = view.findViewById(R.id.txtEmailIDProfile);
        btnLogout = view.findViewById(R.id.btnLogout);
        btnEditProfile = view.findViewById(R.id.btnEditProfile);

        UserTable userTable = AppMain.getRepository().findEmail(loginEMAIL);
        if (userTable != null) {
            txtUserNameProfile.setText(userTable.username);
            txtEmailIDProfile.setText(userTable.emailID);
            if (!Utils.isEmpty(userTable.photoPath)) {
                Glide.with(getActivity())
                        .load(userTable.photoPath)
                        .into(imgUserProfile);
            }
        }
        btnLogout.setOnClickListener(this);
        btnEditProfile.setOnClickListener(this);
    }

    @Override
    public void getViewRoot() {
        setViewRoot(view);
    }

    @Override
    public void onClick(View view) {
        if (view == btnLogout) {
            showLogoutDialog();
        } else if (view == btnEditProfile) {
            Bundle bundle = new Bundle();
            bundle.putBoolean("isUpdate",true);
            Navigation.findNavController(view).navigate(R.id.registerFragment,bundle);
        }
    }

    private void showLogoutDialog() {
        CustomAlertDialogue.Builder alert = new CustomAlertDialogue.Builder(getActivity())
                .setStyle(CustomAlertDialogue.Style.DIALOGUE)
                .setTitle(getString(R.string.txt_logout))
                .setMessage(getString(R.string.txt_logout_msg))
                .setPositiveText(getString(R.string.txt_yes))
                .setNegativeText(getString(R.string.txt_no))
                .setOnPositiveClicked(new CustomAlertDialogue.OnPositiveClicked() {
                    @Override
                    public void OnClick(View view, Dialog dialog) {
                        dialog.dismiss();
                        logout();
                    }
                })
                .setOnNegativeClicked(new CustomAlertDialogue.OnNegativeClicked() {
                    @Override
                    public void OnClick(View view, Dialog dialog) {
                        dialog.dismiss();
                    }
                })
                .setDecorView(getActivity().getWindow().getDecorView())
                .build();
        alert.show();
    }

    private void logout() {
        AppMain.getInstance().getPreferences().edit().putString(Constant.LOGIN_EMAIL, "").apply();
        AppMain.getInstance().getPreferences().edit().putBoolean(Constant.IS_LOGIN, false).apply();
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}
