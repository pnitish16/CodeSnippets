package com.foodapp.foodbuddies.ui;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.foodapp.foodbuddies.R;
import com.foodapp.foodbuddies.utils.Constants;
import com.foodapp.foodbuddies.utils.Utilities;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CreateEvent extends AppCompatActivity {

    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    // LogCat tag
    private static final String TAG = CreateEvent.class.getSimpleName();

    // Camera activity request codes
    private static final int CAMERA_GALLERY_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 101;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    private static final int CAMERA_GALLERY_VIDEO_REQUEST_CODE = 201;

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    private Uri fileUri;
    private Dialog commonDialog;
    private int intFromCamera, intFromGallery;
    private String type;

    private ImageView ivUploadImage;
    private ImageView ivUploadVideo,ivPlayVideo;
    private VideoView videoView;
    private Context context;
    private boolean allPermissionsGranted = false;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        initView();
    }

    //Initializing the views
    private void initView() {
        activity = CreateEvent.this;
        context = CreateEvent.this;
        ivUploadImage = (ImageView) findViewById(R.id.ivUploadImage);
        ivUploadVideo = (ImageView) findViewById(R.id.ivUploadVideo);
        videoView = (VideoView) findViewById(R.id.videoPreview);
        ivPlayVideo = (ImageView) findViewById(R.id.ivPlayVideo);

        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getProfilePick(true,0,"video");
            }
        });
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                ivPlayVideo.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Marshmallow+
            activity = CreateEvent.this;
            allPermissionsGranted = checkPermissionsFromUtils();
        }
    }


    //invoking the generic function from utilites
    public boolean checkPermissionsFromUtils(){

        final List<String> permissionsList = new ArrayList<String>();
        List<String> permissionsNeeded = new ArrayList<>();
        if (!Utilities.addPermission(activity,permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE))
            permissionsNeeded.add("Read External Storage");
        if (!Utilities.addPermission(activity,permissionsList, Manifest.permission.CAMERA))
            permissionsNeeded.add("Camera");
        return Utilities.checkPermissions(activity,permissionsList,permissionsNeeded,REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionResult");

        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);

                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for ACCESS_FINE_LOCATION
                if (perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    // All Permissions Granted

                    if(type !=null) {
                        if (type.equals("image"))
                            getProfilePick(false, 0, "image");
                        else
                            getProfilePick(false, 0, "video");
                    }else{
                        break;
                    }
                } else {
                    // Permission Denied
                    Toast.makeText(CreateEvent.this, "Some Permission is Denied", Toast.LENGTH_SHORT)
                            .show();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void uploadImage(View view) {
        type = "image";
        if (checkPermissionsFromUtils())
            getProfilePick(false, 0, "image");
    }

    public void uploadVideo(View view) {
        type = "video";
        if (checkPermissionsFromUtils())
            getProfilePick(false, 0, "video");
    }

    //dialog for the handling the upload image click
    public void getProfilePick(boolean isReplace, int position, final String type) {
        if (commonDialog == null) {
            commonDialog = new Dialog(this);

            final Dialog dialog = commonDialog;
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.dialog_select_image);
            dialog.show();

            TextView tvGallery, tvPhoto, tvTitle;
            ImageView ivCancel;
            tvGallery = (TextView) dialog.findViewById(R.id.tvGallereySelect);
            tvPhoto = (TextView) dialog.findViewById(R.id.tvCameraCpture);
            ivCancel = (ImageView) dialog.findViewById(R.id.imgCancel);
            ((TextView) dialog.findViewById(R.id.tvDialogTitle)).setText("Select Option");

            ivCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    commonDialog = null;
                    dialog.dismiss();
                }
            });

            tvPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Through camera

                    if (type.equals("image")) {
                        getImage(1, type);
                    } else {
                        recordVideo();
                    }
                    commonDialog = null;
                    dialog.dismiss();
                }
            });

            tvGallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // From Gallery
                    getImage(2, type);
                    commonDialog = null;
                    dialog.dismiss();
                }
            });

            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(final DialogInterface arg0) {
                    commonDialog = null;
                }
            });
        }
    }


    public void getImage(int intOption, String type) {
        switch (intOption) {
            case 1: // Capture from Camera
                intFromCamera = 1;

                captureImage();

                break;

            case 2: // Pick From Gallery.
                intFromCamera = 0;

                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {

                    if (type.equals("image")) {
                        startActivityForResult(Intent.createChooser(new Intent(Intent.ACTION_GET_CONTENT).setType("image/*"), "Choose an image"), CAMERA_GALLERY_IMAGE_REQUEST_CODE);
                    } else {
                        startActivityForResult(Intent.createChooser(new Intent(Intent.ACTION_GET_CONTENT).setType("video/*"), "Choose an video"), CAMERA_GALLERY_VIDEO_REQUEST_CODE);
                    }
                } else {
                    Intent pickPhoto = null;
                    if (type.equals("image")) {
                        pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(pickPhoto, CAMERA_GALLERY_IMAGE_REQUEST_CODE);
                    } else {
                        pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(pickPhoto, CAMERA_GALLERY_VIDEO_REQUEST_CODE);

                    }

                }

                break;

            default:
                break;
        }
    }

    /**
     * Launching camera app to capture image
     */
    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    /**
     * Launching camera app to record video
     */
    private void recordVideo() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);

        // set video quality
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file
        // name

        // start the video capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_VIDEO_REQUEST_CODE);
    }

    /**
     * Here we store the file url as it will be null after returning from camera
     * app
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on screen orientation
        // changes
        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }

    /**
     * ------------ Helper Methods ----------------------
     * */

    /**
     * Creating file uri to store image/video
     */
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * returning image / video
     */
    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                Constants.IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "Oops! Failed create "
                        + Constants.IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    private void launchUploadActivity(boolean isImage) {
        Log.d("File Path", fileUri.getPath());
    }

    /**
     * Receiving activity result method will be called after closing the camera
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // if the result is capturing Image
        if (requestCode == CAMERA_GALLERY_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                // successfully captured the image
                // launching upload activity
//                    launchUploadActivity(true);
                Uri selectedImageUri = data == null ? null : data.getData();
                String Image_path = null;

                if (selectedImageUri != null) {
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT)
                        Image_path = Utilities.getPath_fromlolipop(CreateEvent.this, selectedImageUri);
                    else
                        Image_path = Utilities.getPath_below_lolipop(selectedImageUri, CreateEvent.this);
                }

                Log.d("imagepath", Image_path);

                final Bitmap bitmap = Utilities.decodeSampledBitmapFromFile(Image_path, 200, 200);

                ivUploadImage.setImageBitmap(bitmap);


            } else if (resultCode == RESULT_CANCELED) {

                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();

            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }

        }

        //image is picked from the gallery
        else if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {
                String strProfileImgPath = fileUri.getPath();
                Bitmap bitmap = Utilities.decodeSampledBitmapFromFile(strProfileImgPath, 800, 800);
                Log.d("imagepath", strProfileImgPath);
                ivUploadImage.setImageBitmap(bitmap);
            }
        }
        //video is recorded from the camera
        else if (requestCode == CAMERA_CAPTURE_VIDEO_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                // video successfully recorded
//                launchUploadActivity(false);
                ivPlayVideo.setVisibility(View.VISIBLE);
                videoView.setVisibility(View.VISIBLE);
                videoView.setVideoPath(fileUri.getPath());
                videoView.seekTo(2000);

            } else if (resultCode == RESULT_CANCELED) {

                // user cancelled recording
                Toast.makeText(getApplicationContext(),
                        "User cancelled video recording", Toast.LENGTH_SHORT)
                        .show();

            } else {
                // failed to record video
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to record video", Toast.LENGTH_SHORT)
                        .show();
            }
        }
        //video is picked from the gallery
        else if (requestCode == CAMERA_GALLERY_VIDEO_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {
                Uri selectedImageUri = data == null ? null : data.getData();
                String video_path = null;

                if (selectedImageUri != null) {
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT)
                        video_path = Utilities.getPath_fromlolipop(CreateEvent.this, selectedImageUri);
                    else
                        video_path = Utilities.getPath_below_lolipop(selectedImageUri, CreateEvent.this);
                }

                Log.d("videopath", video_path);

                ivPlayVideo.setVisibility(View.VISIBLE);
                videoView.setVisibility(View.VISIBLE);
                videoView.setVideoPath(video_path);
                videoView.seekTo(2000);
            }
        }
    }

    public void playVideo(View view) {

        view.setVisibility(View.INVISIBLE);
        videoView.start();
    }
}
