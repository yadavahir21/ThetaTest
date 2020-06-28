package com.raghu.thetapracticle.extra;


import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.raghu.thetapracticle.BuildConfig;
import com.raghu.thetapracticle.R;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.util.List;

import me.shaohui.advancedluban.Luban;
import me.shaohui.advancedluban.OnCompressListener;

import static androidx.core.content.FileProvider.getUriForFile;

public class ImagePickerActivity extends AppCompatActivity {
    private static final String TAG = ImagePickerActivity.class.getSimpleName();
    public static final String INTENT_IMAGE_PICKER_OPTION = "image_picker_option";
    public static final int REQUEST_IMAGE_CAPTURE = 0;
    public static final int REQUEST_GALLERY_IMAGE = 1;
    public static final int REQUEST_IMAGE = 100;
    public static String fileName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Intent intent = getIntent();
            if (intent == null) {
                Utils.showToastMassage(this, getString(R.string.txt_toast_image_intent_null));
                return;
            }
            int requestCode = intent.getIntExtra(INTENT_IMAGE_PICKER_OPTION, -1);
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                takeCameraImage();
            } else {
                chooseImageFromGallery();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void takeCameraImage() {
        try {
            Dexter.withActivity(this)
                    .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted()) {
                                fileName = System.currentTimeMillis() + ".jpg";
                                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, getCacheImagePath(fileName));
                                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                                }
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    }).check();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void chooseImageFromGallery() {
        try {
            Dexter.withActivity(this)
                    .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted()) {
                                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(pickPhoto, REQUEST_GALLERY_IMAGE);
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    }).check();
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        CropImage.ActivityResult result = CropImage.getActivityResult(data);
        switch (requestCode) {
            case REQUEST_IMAGE_CAPTURE:
                if (resultCode == RESULT_OK) {
                    Log.d(TAG, "onActivityResult: "+getCacheImagePath(fileName));
                    cropImage(getCacheImagePath(fileName));
                } else {
                    setResultCancelled();
                }
                break;
            case REQUEST_GALLERY_IMAGE:
                if (resultCode == RESULT_OK) {
                    Uri imageUri = data.getData();
                    cropImage(imageUri);
                } else {
                    setResultCancelled();
                }
                break;
            case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Uri resultUri = result.getUri();
                    if (resultUri != null) {
                        Log.d(TAG, "onActivityResult: " + resultUri.getPath());
                        File imageFile = new File(resultUri.getPath());
                        Luban.compress(imageFile, getFilesDir())
                                .setMaxSize(2000)
                                .putGear(Luban.CUSTOM_GEAR)
                                .launch(new OnCompressListener() {
                                    @Override
                                    public void onStart() {
                                    }

                                    @Override
                                    public void onSuccess(File file) {
                                        Log.d(TAG, "onSuccess: file: " + file);
                                        Uri compressUri = Uri.fromFile(file);
                                        if (compressUri != null) {
                                            setResultOk(compressUri);
                                        } else {
                                            setResultCancelled();
                                        }
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Log.d(TAG, "onError: ");
                                        setResultCancelled();
                                    }
                                });
                    } else {
                        setResultCancelled();
                    }
                } else {
                    setResultCancelled();
                }
                break;
            case CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE:
                setResultCancelled();
                break;
            default:
                setResultCancelled();
        }
    }

    private void cropImage(Uri sourceUri) {
        try {
            CropImage.activity(sourceUri)
                    .start(this);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void setResultOk(Uri imagePath) {
        Intent intent = new Intent();
        intent.putExtra("path", imagePath);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private void setResultCancelled() {
        Intent intent = new Intent();
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    }

    private Uri getCacheImagePath(String fileName) {
        File path = new File(getExternalCacheDir(), "camera");
        if (!path.exists()) path.mkdirs();
        File image = new File(path, fileName);
        return getUriForFile(ImagePickerActivity.this, BuildConfig.APPLICATION_ID + ".provider", image);
    }

    public static void clearCache(Context context) {
        try {
            File path = new File(context.getExternalCacheDir(), "camera");
            if (path.exists() && path.isDirectory()) {
                for (File child : path.listFiles()) {
                    child.delete();
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

}
