package com.example.htk.designtemplate.Activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.htk.designtemplate.Model.Account;
import com.example.htk.designtemplate.R;
import com.example.htk.designtemplate.Service.AccountService;
import com.example.htk.designtemplate.Service.ApiUtils;
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

public class UpdateBackgroundActivity extends AppCompatActivity {
    private static final String tag = "UpdateBackgroundAct";
    private String userName;
    private AccountService accountService;
    private ImageView avatarImage;
    private ImageView backgroundImage;
    private ImageView addImage;
    private ImageView backButton;
    private final static int REQ_CODE_PICK_IMAGE = 2;
    private Uri imageUriGlobal;
    private boolean imageUpdate=false;
    private Button updateButton;
    private Activity context =this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_background);
        // get userName
        userName = getIntent().getStringExtra("userName");
        // set retrofit account service
        accountService = ApiUtils.getAccountService();
        // initial UI
        avatarImage = (ImageView) findViewById(R.id.avatarImage_UpdateBackgroundActivity);
        addImage = (ImageView) findViewById(R.id.addBackgroundImage_UpdateBackgroundActivity);
        backgroundImage = (ImageView) findViewById(R.id.backGroundImage_UpdateBackgroundActivity);
        updateButton= (Button) findViewById(R.id.updateButton_UpdateBackgroundActivity);
        backButton = (ImageView) findViewById(R.id.backButton_UpdateBackgroundActivity);
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
                if(imageUpdate){
                    updateImage();
                }
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.finish();
            }
        });

        loadUserInfo();


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data != null && data.getData() != null) {
                imageUpdate = true;
                imageUriGlobal = data.getData();
                Glide.with(this).load(imageUriGlobal).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE).centerCrop().error(R.mipmap.ic_avatar_error)).into(backgroundImage);
            }
        }
    }
    public void loadUserInfo(){
        accountService.getAccountDetail(userName).enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {

                if(response.isSuccessful()) {
                    setUserInfo(response.body());
                    Log.d(tag, "get account detail from API");
                }else {
                    int statusCode  = response.code();
                    Log.d(tag, "fail get account detail from API");
                    Log.d(tag, ((Integer)statusCode).toString());
                    MultipleToast.showToast(MainActivity.fail_request);
                }
            }
            @Override
            public void onFailure(Call<Account> call, Throwable t) {
                Log.d(tag, "fail");
                MultipleToast.showToast(MainActivity.fail_request);
            }
        });
    }
    public void setUserInfo(Account account){
        // Set avatar image
        String url= ApiUtils.getImageUrl(account.getUrlAvatar());
        Glide.with(this).load(url).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE).skipMemoryCache(true).circleCrop().error(R.mipmap.ic_avatar_error)).into(avatarImage);
        // Set avatar image
        String url_background= ApiUtils.getImageUrl(account.getUrlBackground());
        Glide.with(this).load(url_background).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE).skipMemoryCache(true).centerCrop().error(R.mipmap.ic_avatar_error)).into(backgroundImage);
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
        String imageName = "background_" + userName + '.' + getFileExtension(imageUriGlobal);
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file",imageName, requestFile);
        // finally, execute the request
        Call<ResponseBody> call = accountService.updateBackground(body, userName );
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
}
