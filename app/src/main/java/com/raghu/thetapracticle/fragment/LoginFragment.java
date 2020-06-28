package com.raghu.thetapracticle.fragment;

import android.os.Bundle;

import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.raghu.thetapracticle.AppMain;
import com.raghu.thetapracticle.R;
import com.raghu.thetapracticle.database.UserTable;
import com.raghu.thetapracticle.extra.Constant;
import com.raghu.thetapracticle.extra.Utils;


public class LoginFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = LoginFragment.class.getSimpleName();
    private View view;
    private EditText edtEmailLogin;
    private EditText edtPasswordLogin;
    private Button btnLogin;
    private TextView txtCreateNewAccount;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view != null)
            return view;
        view = inflater.inflate(R.layout.fragment_login, container, false);
        getViewRoot();

        initViews();
        return view;
    }

    private void initViews() {
        edtEmailLogin = view.findViewById(R.id.edtEmailLogin);
        edtPasswordLogin = view.findViewById(R.id.edtPasswordLogin);
        btnLogin = view.findViewById(R.id.btnLogin);
        txtCreateNewAccount = view.findViewById(R.id.txtCreateNewAccount);

        btnLogin.setOnClickListener(this);
        txtCreateNewAccount.setOnClickListener(this);
    }

    @Override
    public void getViewRoot() {
        setViewRoot(view);
    }

    @Override
    public void onClick(View view) {
        if (view == btnLogin) {
            String emailID = edtEmailLogin.getText().toString().trim();
            String password = edtPasswordLogin.getText().toString().trim();

            if (Utils.isEmpty(emailID)){
                Utils.showToastMassage(getActivity(),getString(R.string.txt_enter_email_id));
                return;
            }

            if (Utils.isEmpty(password)){
                Utils.showToastMassage(getActivity(),getString(R.string.txt_enter_password));
                return;
            }

            UserTable userTable = AppMain.getRepository().loginValidation(emailID, password);
            Log.d(TAG, "onClick: userTable: "+userTable);
            if (userTable != null) {
                Utils.showToastMassage(getActivity(),getString(R.string.txt_login_success));
                AppMain.getInstance().getPreferences().edit().putBoolean(Constant.IS_LOGIN, true).apply();
                AppMain.getInstance().getPreferences().edit().putString(Constant.LOGIN_EMAIL,emailID).apply();
                Navigation.findNavController(view).popBackStack();
                Navigation.findNavController(view).navigate(R.id.pagerFragment);
            } else {
                Utils.showToastMassage(getActivity(),getString(R.string.txt_check_emailid_or_password));
            }
        } else if (view == txtCreateNewAccount) {
            Navigation.findNavController(view).navigate(R.id.registerFragment);
        }
    }
}
