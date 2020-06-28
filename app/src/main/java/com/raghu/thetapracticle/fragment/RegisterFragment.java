package com.raghu.thetapracticle.fragment;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.loader.content.CursorLoader;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.raghu.thetapracticle.AppMain;
import com.raghu.thetapracticle.R;
import com.raghu.thetapracticle.database.UserTable;
import com.raghu.thetapracticle.extra.Constant;
import com.raghu.thetapracticle.extra.ImagePickerActivity;
import com.raghu.thetapracticle.extra.Utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import stream.customalert.CustomAlertDialogue;

import static android.app.Activity.RESULT_OK;
import static com.raghu.thetapracticle.extra.ImagePickerActivity.REQUEST_IMAGE;

public class RegisterFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = RegisterFragment.class.getSimpleName();
    private View view;
    private MaterialCardView cardUserRegister;
    private TextView txtRegisterTitle;
    private ImageView imgUserRegister;
    private EditText edtUserNameRegister;
    private EditText edtEmailRegister;
    private EditText edtPasswordRegister;
    private Button btnRegister;
    private String photoPath = "";
    private boolean isUpdate = false;
    private String loginEmail = AppMain.getInstance().getPreferences().getString(Constant.LOGIN_EMAIL, "");
    public static final int REQUEST_IMAGE_CAPTURE = 0;
    public static final int REQUEST_GALLERY_IMAGE = 1;
    private String fileName;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isUpdate = getArguments().getBoolean("isUpdate");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view != null)
            return view;
        view = inflater.inflate(R.layout.fragment_register, container, false);
        getViewRoot();
        initViews();
        return view;
    }

    private void initViews() {
        cardUserRegister = view.findViewById(R.id.cardUserRegister);
        txtRegisterTitle = view.findViewById(R.id.txtRegisterTitle);
        imgUserRegister = view.findViewById(R.id.imgUserRegister);
        edtUserNameRegister = view.findViewById(R.id.edtUserNameRegister);
        edtEmailRegister = view.findViewById(R.id.edtEmailRegister);
        edtPasswordRegister = view.findViewById(R.id.edtPasswordRegister);
        btnRegister = view.findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(this);
        cardUserRegister.setOnClickListener(this);

        if (isUpdate) {
            txtRegisterTitle.setText(R.string.txt_update);
            btnRegister.setText(getString(R.string.txt_update));
            UserTable userTable = AppMain.getRepository().findEmail(loginEmail);
            if (userTable != null) {
                edtUserNameRegister.setText(userTable.username);
                edtEmailRegister.setText(userTable.emailID);
                edtPasswordRegister.setText(userTable.password);
                if (!Utils.isEmpty(userTable.photoPath)) {
                    photoPath = userTable.photoPath;
                    setImage(userTable.photoPath);
                }
            }
        }
    }

    private void setImage(String photoPath) {
        Glide.with(getActivity())
                .load(photoPath)
                .into(imgUserRegister);
    }

    @Override
    public void getViewRoot() {
        setViewRoot(view);
    }

    @Override
    public void onClick(View view) {
        if (view == btnRegister) {
            String userName = edtUserNameRegister.getText().toString().trim();
            String emailID = edtEmailRegister.getText().toString().trim();
            String password = edtPasswordRegister.getText().toString().trim();

            if (Utils.isEmpty(userName)) {
                Utils.showToastMassage(getActivity(), getString(R.string.txt_enter_user_name));
                return;
            }
            if (Utils.isEmpty(emailID)) {
                Utils.showToastMassage(getActivity(), getString(R.string.txt_enter_email_id));
                return;
            }
            if (Utils.isEmpty(password)) {
                Utils.showToastMassage(getActivity(), getString(R.string.txt_enter_password));
                return;
            }

            UserTable userTable = AppMain.getRepository().findEmail(emailID);
            Log.d(TAG, "onClick: userTable: " + userTable);
            if (userTable == null) {
                UserTable newUserTable = new UserTable();
                newUserTable.emailID = emailID;
                newUserTable.username = userName;
                newUserTable.photoPath = photoPath;
                newUserTable.password = password;
                AppMain.getRepository().insertUser(newUserTable);
                Utils.showToastMassage(getActivity(), getString(R.string.txt_register_success));
                getActivity().onBackPressed();
            } else {
                if (isUpdate) {
                    AppMain.getRepository().updateUser(userName, password, emailID, photoPath, userTable.userID);
                    Utils.showToastMassage(getActivity(), getString(R.string.txt_profile_update_success));
                } else {
                    Utils.showToastMassage(getActivity(), getString(R.string.txt_email_already_exists));
                }
            }
        } else if (view == cardUserRegister) {
            Log.d(TAG, "onClick: cardUserRegister");
            pickImage();
        }
    }

    private void pickImage() {
        Log.d(TAG, "pickImage: ");
        Dexter.withActivity(getActivity())
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            openPhotoPicOption();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void openPhotoPicOption() {
        ArrayList<String> other = new ArrayList<String>();
        other.add(getString(R.string.txt_take_a_picture));
        other.add(getString(R.string.txt_choose_from_gallery));

        CustomAlertDialogue.Builder alert = new CustomAlertDialogue.Builder(getActivity())
                .setStyle(CustomAlertDialogue.Style.ACTIONSHEET)
                .setTitle(getString(R.string.txt_select_photo_from))
                .setTitleColor(R.color.black)
                .setOthers(other)
                .setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        int selection = i;
                        switch (selection) {
                            case 0:
                                CustomAlertDialogue.getInstance().dismiss();
                                launchCameraIntent();
                                break;
                            case 1:
                                CustomAlertDialogue.getInstance().dismiss();
                                launchGalleryIntent();
                                break;
                            default:
                                CustomAlertDialogue.getInstance().dismiss();
                                break;
                        }
                    }
                })
                .setDecorView(getActivity().getWindow().getDecorView())
                .build();
        alert.show();
    }

    private void launchCameraIntent() {
        Intent intent = new Intent(getActivity(), ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    public void launchGalleryIntent() {
        Intent intent = new Intent(getActivity(), ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: ");
        if (getActivity() == null)
            return;
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getParcelableExtra("path");
                Log.d(TAG, "onActivityResult: " + uri);
                File file = new File(uri.getPath());
                photoPath = file.getAbsolutePath();
                setImage(photoPath);
            }
        }
    }
}
