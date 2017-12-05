package com.example.htk.designtemplate.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.htk.designtemplate.Adapter.CommentAdapter;
import com.example.htk.designtemplate.Model.Comment;
import com.example.htk.designtemplate.Model.CommentModel;
import com.example.htk.designtemplate.R;
import com.example.htk.designtemplate.Service.ApiUtils;
import com.example.htk.designtemplate.Service.PostService;
import com.example.htk.designtemplate.Utils.MultipleToast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentActivity extends AppCompatActivity {
    private static final String tag = "CommentActivity";
    private List<Comment> commentArrayList = new ArrayList<Comment>() ;
    private CommentAdapter commentAdapter;
    private ListView listView;
    private Toolbar myToolbar;
    private ImageView backImage;
    private ImageView sendButton;
    private EditText commentEditText;
    private PostService mService = ApiUtils.getPostService();
    private int postId;
    private String currentUser = MainActivity.userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        // set post id
        postId = getIntent().getIntExtra("postId", -1);

        // inital UI
        listView = (ListView) findViewById(R.id.listView_commentActivity);
        backImage = (ImageView) findViewById(R.id.backImage_commentActivity);
        sendButton = (ImageView) findViewById(R.id.sendIcon_commentActivity);
        commentEditText = (EditText) findViewById(R.id.commentEditText_commentActivity);
        MultipleToast.context= this;
        // set up action bar
        setActionBar();

        //set adapter for list view
        commentAdapter = new CommentAdapter(this, R.layout.item_comment_listview, commentArrayList);
        listView.setAdapter(commentAdapter);

        // set action for clicking back icon
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = commentEditText.getText().toString().trim();
                if(!content.equals("")){
                    createComment(content);
                }
            }
        });
        //load comments
        loadComments();

    }
    public void setActionBar(){
        myToolbar = (Toolbar) findViewById(R.id.toolbar_commentActivity);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
    public void loadComments(){
        mService.getCommentsOfPost(postId).enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                if(response.isSuccessful()) {
                    commentAdapter.clear();
                    commentAdapter.addAll(response.body());
                    commentAdapter.notifyDataSetChanged();
                    Log.d(tag, "get comments from API");
                }else {
                    int statusCode  = response.code();
                    Log.d(tag, "fail get comments from API");
                    Log.d(tag, ((Integer)statusCode).toString());
                    MultipleToast.showToast(MainActivity.fail_request);
                }
            }
            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                Log.d(tag,"fail");
                //progressDialog.dismiss();
                MultipleToast.showToast(MainActivity.fail_request);
            }
        });
    }
    public void createComment(String content){
        mService.createComment(currentUser,postId,content).enqueue(new Callback<CommentModel>() {
            @Override
            public void onResponse(Call<CommentModel> call, Response<CommentModel> response) {
                if(response.isSuccessful()) {
                    MultipleToast.showToast("Bình luận thành công");
                    commentEditText.setText("");
                    loadComments();
                    Log.d(tag, "create a comment from API");
                }else {
                    int statusCode  = response.code();
                    Log.d(tag, "fail create a comment from API");
                    Log.d(tag, ((Integer)statusCode).toString());
                    MultipleToast.showToast(MainActivity.fail_request);
                }
            }
            @Override
            public void onFailure(Call<CommentModel> call, Throwable t) {
                Log.d(tag,"fail create a comment");
                //progressDialog.dismiss();
                MultipleToast.showToast(MainActivity.fail_request);
            }
        });
    }

}
