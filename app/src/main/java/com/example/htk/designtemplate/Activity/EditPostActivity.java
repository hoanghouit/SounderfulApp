package com.example.htk.designtemplate.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.htk.designtemplate.Model.Account;
import com.example.htk.designtemplate.Model.Post;
import com.example.htk.designtemplate.R;
import com.example.htk.designtemplate.Service.AccountService;
import com.example.htk.designtemplate.Service.ApiUtils;
import com.example.htk.designtemplate.Service.PostService;
import com.example.htk.designtemplate.Utils.FileUtils;
import com.example.htk.designtemplate.Utils.MultipleToast;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditPostActivity extends AppCompatActivity {
    private static final String tag ="EditPostActivity";
    private ImageView backButton;
    private String userName;
    private AccountService accountService;
    private TextView userNameTextView;
    private ImageView avatarImage;
    private ImageView addImage;
    private ImageView trackImage;
    private EditText trackNameTextView;
    private EditText titleEditText;
    private EditText descriptionEditText;
    private TextView updateButton;
    private Activity context = this;
    private final static int REQ_CODE_PICK_IMAGE = 2;
    private Uri imageUriGlobal;
    private PostService postService;
    private ProgressDialog progressDialog;
    private String successful_update;
    private String fail_update;
    private String title_error;
    private String track_error;
    private int postId;
    private boolean imageUpdate=false;
    private boolean infoUpdate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);
        // set postId
        postId = getIntent().getIntExtra("postId",-1);

        // set context for toast
        MultipleToast.context = this;
        // set user name
        SharedPreferences sharedPreferences= getSharedPreferences("user", Context.MODE_PRIVATE);
        userName = sharedPreferences.getString("userName","");
        // set retrofit accountGlobal service;
        accountService = ApiUtils.getAccountService();
        postService = ApiUtils.getPostService();

        // inital UI
        backButton = (ImageView) findViewById(R.id.backButton_EditPostActivity);
        trackNameTextView = (EditText) findViewById(R.id.trackEditTextView_EditPostActivity);
        addImage = (ImageView) findViewById(R.id.addTrackImage_EditPostActivity);
        trackImage = (ImageView) findViewById(R.id.trackImage_EditPostActivity);
        updateButton = (TextView) findViewById(R.id.updateButton_EditPostActivity);
        titleEditText = (EditText) findViewById(R.id.title_EditPostActivity);
        avatarImage = (ImageView) findViewById(R.id.avatarImage_EditPostActivity);
        descriptionEditText = (EditText) findViewById(R.id.descriptionTextView_EditPostActivity);
        title_error = getResources().getString(R.string.title_newpost_error);
        successful_update = getResources().getString(R.string.successful_upload);
        fail_update = getResources().getString(R.string.fail_upload);


        //set user name
        userNameTextView = (TextView) findViewById(R.id.userName_EditPostActivity);
        userNameTextView.setText(userName);

        // load user info
        loadUserInfo();
        //
        loadPostInfo();

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,REQ_CODE_PICK_IMAGE);
            }
        });
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePost();
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.finish();
            }
        });
    }
    public void updatePost(){
            updatePostInfo();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data != null && data.getData() != null) {
                imageUpdate = true;
                imageUriGlobal = data.getData();
                trackImage.setImageURI(imageUriGlobal);
            }
        }
    }
    public String getFileExtension(Uri fileUri){
        File file = FileUtils.getFile(this, fileUri);
        return android.webkit.MimeTypeMap.getFileExtensionFromUrl(file.getName());
    }
    private void updateImage() {
        // use the FileUtils to get the actual file by uri
        File file = FileUtils.getFile(this, imageUriGlobal);

        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse(getContentResolver().getType(imageUriGlobal)),
                        file
                );
        // MultipartBody.Part is used to send also the actual file name
        String postid = Integer.toString(postId);
        String imageName = "imageTrack_" + postid + '.' + getFileExtension(imageUriGlobal);
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file",imageName, requestFile);

        // finally, execute the request
        Call<ResponseBody> call = postService.updateImage(body, postId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    context.finish();
                    Log.v(tag, "success");
                }
                else {
                    Log.v(tag, "fail update image");
                    MultipleToast.showToast(MainActivity.fail_request);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(tag, "fail date image error");
                Log.e(tag, t.getMessage());
                MultipleToast.showToast(MainActivity.fail_request);
            }
        });
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

        // Set avatar image
        String url = ApiUtils.getImageUrl(account.getUrlAvatar());
        Glide.with(this).load(url).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL).override(500, 500).circleCrop().error(R.mipmap.ic_avatar_error)).into(avatarImage);

    }
    public void loadPostInfo() {
        postService.getOnePost(postId).enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {

                if (response.isSuccessful()) {
                    setPostInfo(response.body());
                    Log.d(tag, "get post info from API");
                } else {
                    int statusCode = response.code();
                    Log.d(tag, "fail get post info from API");
                    Log.d(tag, ((Integer) statusCode).toString());
                    MultipleToast.showToast(MainActivity.fail_request);
                    // handle request errors depending on status code
                }
            }
            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Log.d(tag, "fail get post info");
                MultipleToast.showToast(MainActivity.fail_request);
            }
        });
    }

    public void setPostInfo(Post post) {
        // Set track image
        String url = ApiUtils.getImageUrl(post.getUrlImage());
        Glide.with(this).load(url).apply(RequestOptions.overrideOf(800, 400).error(R.color.colorLittleGray)).into(trackImage);
        titleEditText.setText(post.getTitle());
        trackNameTextView.setText(post.getUrlTrack());
        descriptionEditText.setText(post.getDescription());
    }
    public void updatePostInfo(){
        String title = titleEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        postService.modifyPost(postId,title,description).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    if(imageUpdate){
                        updateImage();
                    }
                    else {
                        context.finish();
                    }
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
}
