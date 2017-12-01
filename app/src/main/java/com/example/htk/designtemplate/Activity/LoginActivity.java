package com.example.htk.designtemplate.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.htk.designtemplate.R;
import com.example.htk.designtemplate.Service.AccountService;
import com.example.htk.designtemplate.Service.ApiUtils;
import com.example.htk.designtemplate.Utils.Encryption;
import com.example.htk.designtemplate.Utils.MultipleToast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private TextView signup;
    private EditText userNameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private AccountService mService;
    private Activity context = this;
    private String lengthPasswordToast;
    private String validUserNameToast;
    private String failedLoginToast;
    private String successfullyloginToast;
    private String completedToast;
    private String incorrectLoginToast;
    private boolean lengthPassword = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences sharedPreferences= getSharedPreferences("user", Context.MODE_PRIVATE);
        if(!sharedPreferences.getString("userName","").equals(""))
        {
            Intent intent = new Intent(context,MainActivity.class);
            startActivity(intent);
        }

        // set retrofit account service
        mService = ApiUtils.getAccountService();

        // set context for toasts
        MultipleToast.context = this;

        //Inital UI
        signup = (TextView) findViewById(R.id.signupTextView_Login);
        userNameEditText = (EditText) findViewById(R.id.userName_Login);
        passwordEditText = (EditText) findViewById(R.id.password_Login);
        loginButton = (Button) findViewById(R.id.loginButton);
        completedToast = getResources().getString(R.string.completed_signup_error);
        successfullyloginToast = getResources().getString(R.string.successful_login);
        failedLoginToast = getResources().getString(R.string.fail_login);
        incorrectLoginToast = getResources().getString(R.string.incorrect_login);
        lengthPasswordToast = getResources().getString(R.string.length_password_error);
        validUserNameToast = getResources().getString(R.string.invalid_username_error);

        //set action for clicking signup textview
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,SignUpActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        userNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                    String userNameText = userNameEditText.getText().toString();
                    checkValidUserName(userNameText);

            }
        });

        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkLengthPassWord();

            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(check()){

                    String userName = userNameEditText.getText().toString();
                    String password = Encryption.md5(passwordEditText.getText().toString());
                    checkLogin(userName, password);
                }

            }
        });
    }
    public boolean checkLengthPassWord(){
        String passWordText = passwordEditText.getText().toString();
        if(passWordText.length()>=6){
            lengthPassword = true;
            return true;
        }
        else {
            lengthPassword = false;
            MultipleToast.showToast(lengthPasswordToast);
            return false;
        }
    }
    public void checkValidUserName(String userName){
        if(!userName.matches("[a-zA-Z0-9]*"))
        {
            userNameEditText.setText(userName.substring(0,userName.length() - 1));
            userNameEditText.setSelection(userName.length()-1);
            MultipleToast.showToast(validUserNameToast);
        }
    }
    public boolean check() {
        boolean completedUserName = userNameEditText.getText().toString().length() == 0;
        boolean completedPassword = passwordEditText.getText().toString().length() == 0;
        if(completedUserName || completedPassword){
            MultipleToast.showToast(completedToast);
            return false;
        }
        if (!lengthPassword) {
                MultipleToast.showToast(lengthPasswordToast);
                return false;
        }
        return true;
    }
    public void checkLogin(final String userName, String password){

        mService.login(userName,password).enqueue(new Callback<SignUpActivity.existsUser>() {
            @Override
            public void onResponse(Call<SignUpActivity.existsUser> call, Response<SignUpActivity.existsUser> response) {
                if(response.isSuccessful()) {
                    if(response.body().exists.equals("1")) {
                        Intent intent = new Intent(context,MainActivity.class);
                        // set current user
                        SharedPreferences sharedPreferences= getSharedPreferences("user", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor=sharedPreferences.edit();
                        editor.putString("userName",userName);
                        editor.commit();
                        MultipleToast.showToast(successfullyloginToast);
                        startActivity(intent);
                    }
                    else{
                        MultipleToast.showToast(incorrectLoginToast);
                    }
                    Log.d("LoginActivity", "respone from API"+response.body().exists);
                }else {
                    MultipleToast.showToast(failedLoginToast);
                    int statusCode  = response.code();
                    Log.d("LoginActivity", "fail loaded from API");
                    // handle request errors depending on status code
                }
            }
            @Override
            public void onFailure(Call<SignUpActivity.existsUser> call, Throwable t) {
                MultipleToast.showToast(failedLoginToast);
                Log.d("LoginActivity", "fail");
            }
        });
    }
}
