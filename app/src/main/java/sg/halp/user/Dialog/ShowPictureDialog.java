package sg.halp.user.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import sg.halp.user.Interface.DialogFragmentCallback;
import sg.halp.user.R;
import sg.halp.user.Utils.Commonutils;
import sg.halp.user.Utils.Const;
import sg.halp.user.Utils.EbizworldUtils;

import static android.app.Activity.RESULT_OK;

public class ShowPictureDialog extends DialogFragment implements View.OnClickListener {

    @BindView(R.id.custom_simple_title)
    TextView custom_simple_title;

    @BindView(R.id.custom_simple_content)
    TextView custom_simple_content;

    @BindView(R.id.btn_yes)
    TextView btn_yes;

    @BindView(R.id.btn_no)
    TextView btn_no;

    private Activity activity;
    private File cameraFile;
    private Uri uri = null;
    private DialogFragmentCallback.CropPictureListener cropPictureListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = getActivity();

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        Dialog dialog = new Dialog(getActivity(), R.style.DialogSlideAnim_leftright_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_custom_simple);

        ButterKnife.bind(this, dialog);

        custom_simple_title.setText(getResources().getString(R.string.txt_select_option));
        btn_yes.setText(getResources().getString(R.string.txt_gellery));
        btn_no.setText(getResources().getString(R.string.txt_camera));
        custom_simple_content.setVisibility(View.GONE);

        btn_yes.setOnClickListener(this);
        btn_no.setOnClickListener(this);

        return dialog;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        EbizworldUtils.appLogDebug("HaoLS", "request code: " + requestCode);
        EbizworldUtils.appLogDebug("HaoLS", "Result code: " + resultCode);

        switch (requestCode) {

            case Const.CHOOSE_PHOTO:{

                if (data != null) {

                    uri = data.getData();

                    if (uri != null) {

                        beginCrop(uri);

                    } else {
                        EbizworldUtils.showShortToast(getResources().getString(R.string.txt_img_error), activity);
                    }
                }

            }
            break;

            case Const.TAKE_PHOTO:{

                if (uri != null) {

                    beginCrop(uri);

                } else {

                    EbizworldUtils.showShortToast(getResources().getString(R.string.txt_img_error), activity);

                }

            }
            break;

            case Crop.REQUEST_CROP:{

                if (data != null){

                    handleCrop(resultCode, data);

                }

            }
            break;
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btn_yes:{

                choosePhotoFromGallery();

            }
            break;

            case R.id.btn_no:{

                takePhotoFromCamera();

            }
            break;
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        if (cameraFile != null) {

            cameraFile.getAbsoluteFile().delete();

        }
    }

    private void choosePhotoFromGallery() {
        try {

            Intent i = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            startActivityForResult(i, Const.CHOOSE_PHOTO);

        } catch (Exception e) {

            e.printStackTrace();
            Commonutils.showtoast("Gallery not found!", activity);

        }

    }

    private void takePhotoFromCamera() {

        Calendar cal = Calendar.getInstance();

        cameraFile = new File(Environment.getExternalStorageDirectory(),
                (cal.getTimeInMillis() + ".jpg"));


        if (!cameraFile.exists()) {

            try {

                cameraFile.createNewFile();

            } catch (IOException e) {

                // TODO Auto-generated catch block
                e.printStackTrace();

            }

        } else {

            cameraFile.delete();

            try {

                cameraFile.createNewFile();

            } catch (IOException e) {

                // TODO Auto-generated catch block
                e.printStackTrace();

            }

        }

        uri = Uri.fromFile(cameraFile);

        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        i.putExtra(MediaStore.EXTRA_OUTPUT, uri);

        startActivityForResult(i, Const.TAKE_PHOTO);

    }

    private void beginCrop(Uri source) {

        Uri outputUri = Uri.fromFile(new File(Environment
                .getExternalStorageDirectory(), (Calendar.getInstance().getTimeInMillis() + ".jpg")));
        Crop.of(source, outputUri).asSquare().start(activity, ShowPictureDialog.this);

    }

    private void handleCrop(int resultCode, Intent result) {

        if (resultCode == RESULT_OK) {

            String filePath = getRealPathFromURI(Crop.getOutput(result));

            if (cropPictureListener != null){

                cropPictureListener.onHandleCrop(getRealPathFromURI(Crop.getOutput(result)));

                EbizworldUtils.appLogDebug("HaoLS", "File path: " + filePath);

            }

            dismiss();

        } else if (resultCode == Crop.RESULT_ERROR) {

            EbizworldUtils.showShortToast(Crop.getError(result).getMessage(), activity);

        }

    }

    private String getRealPathFromURI(Uri contentURI) {

        String result;
        Cursor cursor = activity.getContentResolver()
                .query(
                        contentURI,
                        null,
                        null,
                        null,
                        null
                );

        if (cursor == null) {
            // Source is Dropbox or other similar local file
            // path
            result = contentURI.getPath();

        } else {

            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();

        }

        return result;
    }

    public void setCropPictureListener(DialogFragmentCallback.CropPictureListener cropPictureListener) {
        this.cropPictureListener = cropPictureListener;
    }
}
