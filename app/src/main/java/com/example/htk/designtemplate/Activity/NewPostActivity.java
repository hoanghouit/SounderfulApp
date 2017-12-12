package com.example.htk.designtemplate.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.htk.designtemplate.Model.Account;
import com.example.htk.designtemplate.Model.PostModel;
import com.example.htk.designtemplate.R;
import com.example.htk.designtemplate.Service.AccountService;
import com.example.htk.designtemplate.Service.ApiUtils;
import com.example.htk.designtemplate.Service.PostService;
import com.example.htk.designtemplate.Utils.FileUtils;
import com.example.htk.designtemplate.Utils.MultipleToast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewPostActivity extends AppCompatActivity {
    private static final String tag ="NewPostActivity";
    private ImageView backButton;
    private String userName;
    private AccountService accountService;
    private TextView userNameTextView;
    private ImageView avatarImage;
    private TextView timeTextView;
    private ImageView addImage;
    private ImageView trackImage;
    private Button recordButton;
    private Button browseFileButton;
    private EditText trackNameTextView;
    private EditText titleEditText;
    private EditText descriptionEditText;
    private TextView uploadButton;
    private Activity context = this;
    private final static int REQ_CODE_PICK_SOUNDFILE = 1;
    private final static int REQ_CODE_PICK_IMAGE = 2;
    private final static int REQ_CODE_RECORD_AUDIO = 3;
    private Uri imageUriGlobal;
    private Uri trackUriGlobal;
    private PostService postService;
    private ProgressDialog progressDialog;
    private String successful_upload;
    private String fail_upload;
    private String title_error;
    private String track_error;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        // set context for toast
        MultipleToast.context = this;
        // set user name
        SharedPreferences sharedPreferences= getSharedPreferences("user", Context.MODE_PRIVATE);
        userName = sharedPreferences.getString("userName","");
        // set retrofit accountGlobal service;
        accountService = ApiUtils.getAccountService();
        postService = ApiUtils.getPostService();

        // inital UI
        backButton = (ImageView) findViewById(R.id.backButton_NewPostActivity);
        timeTextView = (TextView) findViewById(R.id.timeTextView_newPost);
        browseFileButton = (Button) findViewById(R.id.loadFromMemory_newPost);
        trackNameTextView = (EditText) findViewById(R.id.trackEditTextView_newPost);
        addImage = (ImageView) findViewById(R.id.addTrackImage_newPost);
        trackImage = (ImageView) findViewById(R.id.trackImage_newPost);
        recordButton = (Button) findViewById(R.id.record_newPost);
        uploadButton = (TextView) findViewById(R.id.uploadButton_newPost);
        titleEditText = (EditText) findViewById(R.id.title_newPost);
        descriptionEditText = (EditText) findViewById(R.id.descriptionTextView_newPost);
        title_error = getResources().getString(R.string.title_newpost_error);
        track_error = getResources().getString(R.string.track_newpost_error);
        successful_upload = getResources().getString(R.string.successful_upload);
        fail_upload = getResources().getString(R.string.fail_upload);

        // set time
        Calendar c = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy");
        timeTextView.setText(simpleDateFormat.format(c.getTime()));

        //set user name
        userNameTextView = (TextView) findViewById(R.id.userName_newPost);
        userNameTextView.setText(userName);
        // set user infor
        loadUserInfo();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,REQ_CODE_PICK_IMAGE);
            }
        });
        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
                startActivityForResult(intent,REQ_CODE_RECORD_AUDIO);
            }
        });
        browseFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //initPermission();
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("audio/mpeg");
                Intent chooser = Intent.createChooser(intent,"Select an audio file");
                startActivityForResult(chooser, REQ_CODE_PICK_SOUNDFILE);
            }
        });
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = titleEditText.getText().toString();
                String description = descriptionEditText.getText().toString();
                if(title.trim().equals("")){
                    MultipleToast.showToast(title_error);
                    return;
                }
                if(trackUriGlobal == null){
                    MultipleToast.showToast(track_error);
                    return;
                }
                if(imageUriGlobal == null){
                    MultipleToast.showToast("Vui lòng chọn hình ảnh");
                    return;
                }
                createPost(title,description);
            }
        });
        initPermission();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE_PICK_SOUNDFILE && resultCode == Activity.RESULT_OK) {
            if (data != null && data.getData() != null) {
                trackUriGlobal = data.getData();
                Cursor cursor = context.getContentResolver().query(trackUriGlobal, null, null, null, null);
                cursor.moveToFirst();
                trackNameTextView.setText(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
                trackNameTextView.setVisibility(View.VISIBLE);
            }
        }
        if (requestCode == REQ_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data != null && data.getData() != null) {
                imageUriGlobal = data.getData();
                trackImage.setImageURI(imageUriGlobal);

            }
        }
        if (requestCode == REQ_CODE_RECORD_AUDIO && resultCode == Activity.RESULT_OK) {
            if (data != null && data.getData() != null) {
                trackUriGlobal = data.getData();
                Cursor cursor = context.getContentResolver().query(trackUriGlobal, null, null, null, null);
                cursor.moveToFirst();
                trackNameTextView.setText(cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)));
                trackNameTextView.setVisibility(View.VISIBLE);

            }
        }
    }

    public void loadUserInfo() {
        accountService.getAccountDetail(userName).enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {

                if (response.isSuccessful()) {
                    setUserInfo(response.body());
                    Log.d(tag, "get uer info from API");
                } else {
                    int statusCode = response.code();
                    Log.d(tag, "fail get user info from API");
                    Log.d(tag, ((Integer) statusCode).toString());
                    MultipleToast.showToast(MainActivity.fail_request);
                    // handle request errors depending on status code
                }
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {
                Log.d(tag, "fail get user info");
                MultipleToast.showToast(MainActivity.fail_request);
            }
        });
    }

    public void setUserInfo(Account account) {
        avatarImage = (ImageView) findViewById(R.id.avatarImage_newPost);

        // Set avatar image
        String url = ApiUtils.getImageUrl(account.getUrlAvatar());
        Glide.with(this).load(url).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL).override(500, 500).circleCrop().error(R.mipmap.ic_avatar_error)).into(avatarImage);

    }
    private void uploadImage(Uri fileUri, String fileName) {
        // use the FileUtils to get the actual file by uri
        File file = FileUtils.getFile(this, fileUri);

        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse(getContentResolver().getType(fileUri)),
                        file
                );
        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file",fileName , requestFile);

        // finally, execute the request
        Call<ResponseBody> call = postService.uploadImage(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Log.v(tag, "success");
                }
                else {
                    Log.v(tag, "fail upload image");
                    MultipleToast.showToast(MainActivity.fail_request);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(tag, "fail upload image error");
                Log.e(tag, t.getMessage());
                MultipleToast.showToast(MainActivity.fail_request);
            }
        });
    }
    private void uploadTrack(Uri fileUri, String fileName, final PostModel postModel) {
        // use the FileUtils to get the actual file by uri
        File file = FileUtils.getFile(this, fileUri);

        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse(getContentResolver().getType(fileUri)),
                        file
                );

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", fileName, requestFile);

        // finally, execute the request
        Call<ResponseBody> call = postService.uploadTrack(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    completePost(postModel);
                    Log.v(tag, "success upload track");
                }
                else {
                    deletePost(postModel);
                    MultipleToast.showToast(fail_upload);
                    Log.v(tag, "fail upload track");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(tag, "upload track error");
                Log.e(tag, t.getMessage());
            }
        });
    }
    public void createPost(String title, String description){
        postService.createPost(userName,title,description).enqueue(new Callback<PostModel>() {
            @Override
            public void onResponse(Call<PostModel> call, Response<PostModel> response) {
                if(response.isSuccessful()) {
                    updatePost(response.body());
                    Log.d(tag, "create post from API");
                }else {
                    int statusCode  = response.code();
                    Log.d(tag, "fail create post from API");
                    Log.d(tag, ((Integer)statusCode).toString());
                    MultipleToast.showToast(MainActivity.fail_request);
                }
            }
            @Override
            public void onFailure(Call<PostModel> call, Throwable t) {
                Log.d(tag, "fail create post");
                MultipleToast.showToast(MainActivity.fail_request);
            }
        });
    }
    public void updatePost(PostModel post){
        createProgressDialog();
       // progressDialog.show();
        String id = Integer.toString(post.getPostId());
        String imageName= null;
        if(imageUriGlobal != null) {
            imageName = "imageTrack_" + id + '.' + getFileExtension(imageUriGlobal);
            uploadImage(imageUriGlobal, imageName);
        }
        String trackName = "track_"+id+'.'+getFileExtension(trackUriGlobal);
        post.setUrlImage(imageName);
        post.setUrlTrack(trackName);
        uploadTrack(trackUriGlobal,trackName, post);
    }
    public String getFileExtension(Uri fileUri){
        File file = FileUtils.getFile(this, fileUri);
        return android.webkit.MimeTypeMap.getFileExtensionFromUrl(file.getName());
    }
    public void completePost(final PostModel postModel){
        postService.updatePost(postModel.getPostId(),postModel).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    startIntent();
                    Log.d(tag, "complete post from API");
                }else {
                    MultipleToast.showToast("Đăng tải không thành công");
                    int statusCode  = response.code();
                    Log.d(tag, "fail complete post from API");
                    Log.d(tag, ((Integer)statusCode).toString());
                    MultipleToast.showToast(MainActivity.fail_request);
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                MultipleToast.showToast(MainActivity.fail_request);
                Log.d(tag, "fail complete post");
            }
        });
    }
    public void deletePost(PostModel postModel){
        postService.deletePost(postModel.getPostId()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    Log.d(tag, "delete post from API");
                }else {
                    // MultipleToast.showToast(failSignupToast);
                    int statusCode  = response.code();
                    Log.d(tag, "fail delete post from API");
                    Log.d(tag, ((Integer)statusCode).toString());
                    MultipleToast.showToast(MainActivity.fail_request);
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                MultipleToast.showToast(MainActivity.fail_request);
                Log.d(tag, "fail delete post");
            }
        });
    }
    public void createProgressDialog(){
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Uploading ...");
        progressDialog.setProgress(0);
    }
    public void startIntent(){
        //progressDialog.dismiss();
        MultipleToast.showToast(successful_upload);
        Intent intent = new Intent(context,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    public boolean checkPermissions(String permission){
        Log.d(tag, "checkPermissions: checking permission: " + permission);

        int permissionRequest = ActivityCompat.checkSelfPermission(NewPostActivity.this, permission);

        if(permissionRequest != PackageManager.PERMISSION_GRANTED){
            Log.d(tag, "checkPermissions: \n Permission was not granted for: " + permission);
            return false;
        }
        else{
            Log.d(tag, "checkPermissions: \n Permission was granted for: " + permission);
            return true;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(NewPostActivity.this, "Permision Write File is Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(NewPostActivity.this, "Permision Write File is Denied", Toast.LENGTH_SHORT).show();

            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void initPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                //Permisson don't granted
                if (shouldShowRequestPermissionRationale(
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Toast.makeText(NewPostActivity.this, "Permission isn't granted ", Toast.LENGTH_SHORT).show();
                }
                // Permisson don't granted and dont show dialog again.
                else {
                    Toast.makeText(NewPostActivity.this, "Permisson don't granted and dont show dialog again ", Toast.LENGTH_SHORT).show();
                }
                //Register permission
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

            }
        }
    }





}
