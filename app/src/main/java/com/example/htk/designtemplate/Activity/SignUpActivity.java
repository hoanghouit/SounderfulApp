package com.example.htk.designtemplate.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.htk.designtemplate.Model.Account;
import com.example.htk.designtemplate.R;
import com.example.htk.designtemplate.Service.AccountService;
import com.example.htk.designtemplate.Service.ApiUtils;
import com.example.htk.designtemplate.Utils.Encryption;
import com.example.htk.designtemplate.Utils.MultipleToast;
import com.google.gson.annotations.SerializedName;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    private AccountService mService;
    private Activity context= this;
    private Button signupButton;
    private EditText userNameEditText;
    private EditText passWordEditText;
    private EditText passWordAgainEditText;
    private String lengthPasswordToast;
    private String validUserNameToast;
    private String availableUserNameToast;
    private String confirmPasswordToast;
    private String completedToast;
    private String successfulSignupToast;
    private String failSignupToast;
    private boolean lengthPassword = false;
    private boolean availableUserName = false;
    private boolean confirmPassword = false;
    private boolean invalidUserName = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // set retrofit account service
        mService = ApiUtils.getAccountService();
        // set context for toast
        MultipleToast.context= this;

        // Initial UI
        signupButton = (Button) findViewById(R.id.signUpButton);
        userNameEditText = (EditText) findViewById(R.id.userName_SignUp);
        passWordEditText = (EditText) findViewById(R.id.password_SignUp);
        passWordAgainEditText = (EditText) findViewById(R.id.passwordAgain_SignUp);
        lengthPasswordToast = getResources().getString(R.string.length_password_error);
        validUserNameToast = getResources().getString(R.string.invalid_username_error);
        availableUserNameToast = getResources().getString(R.string.available_username_error);
        confirmPasswordToast = getResources().getString(R.string.confirm_password_error);
        completedToast = getResources().getString(R.string.completed_signup_error);
        successfulSignupToast = getResources().getString(R.string.successul_signup);
        failSignupToast = getResources().getString(R.string.fail_signup);

        userNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(invalidUserName){
                    String userNameText = userNameEditText.getText().toString();
                    if(checkValidUserName(userNameText))
                    { checkAvailableUserName(userNameEditText.getText().toString());}
                }
            }
        });

        passWordEditText.addTextChangedListener(new TextWatcher() {
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

        passWordAgainEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkConfirmPassword();
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(check()){
                    Account account = new Account();
                    account.setUserName(userNameEditText.getText().toString());
                    account.setPassWord(Encryption.md5(passWordEditText.getText().toString()));
                    createAccount(account);
                }
            }
        });

    }
    public void createAccount(Account account){
        mService.createAccount(account).enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {

                if(response.isSuccessful()) {
                    MultipleToast.showToast(successfulSignupToast);
                    Log.d("SignUpActivity", "create account from API");
                    Intent intent = new Intent(context,LoginActivity.class);
                    startActivity(intent);
                }else {
                    MultipleToast.showToast(failSignupToast);
                    int statusCode  = response.code();
                    Log.d("SignUpActivity", "fail loaded from API");
                    Log.d("SignUpActivity", ((Integer)statusCode).toString());
                    // handle request errors depending on status code
                }
            }
            @Override
            public void onFailure(Call<Account> call, Throwable t) {
                MultipleToast.showToast(failSignupToast);
                Log.d("MainActivity", t.getMessage());
            }
        });
    }

    public boolean checkLengthPassWord(){
        String passWordText = passWordEditText.getText().toString();
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
    public boolean checkValidUserName(String userName){
        if(!userName.matches("[a-zA-Z0-9]*"))
        {
            invalidUserName = false;
            userNameEditText.setText(userName.substring(0,userName.length() - 1));
            userNameEditText.setSelection(userName.length()-1);
            invalidUserName = true;
            MultipleToast.showToast(validUserNameToast);
            return false;
        }
        return true;
    }

    public void checkAvailableUserName(String userNameCheck){
        mService.checkUserName(userNameCheck).enqueue(new Callback<existsUser>() {
            @Override
            public void onResponse(Call<existsUser> call,Response<existsUser> response) {
                if(response.isSuccessful()) {
                    if(response.body().exists.equals("0")) {
                        availableUserName = true;
                    }
                    else{
                        availableUserName = false;
                        MultipleToast.showToast(availableUserNameToast);
                    }
                    Log.d("SignUpActivity", "respone from API");
                }else {
                    int statusCode  = response.code();
                    Log.d("SignUpActivity", "fail loaded from API");
                    // handle request errors depending on status code
                }
            }
            @Override
            public void onFailure(Call<existsUser> call,Throwable t) {
                Log.d("SignUpActivity", t.getMessage());
            }
        });
    }
    public  boolean checkConfirmPassword(){
        if(passWordEditText.getText().toString().equals(passWordAgainEditText.getText().toString())){
            confirmPassword = true;
            return true;
        }
        else {
            confirmPassword = false;
            MultipleToast.showToast(confirmPasswordToast);
            return false;
        }
    }
    public boolean check() {
        boolean completedUserName = userNameEditText.getText().toString().length() == 0;
        boolean completedPassword = passWordEditText.getText().toString().length() == 0;
        if(completedUserName || completedPassword){
            MultipleToast.showToast(completedToast);
            return false;
        }
        if (!availableUserName) {
            MultipleToast.showToast(availableUserNameToast);
        } else {
            if (!lengthPassword) {
                MultipleToast.showToast(lengthPasswordToast);
            } else {
                if (checkConfirmPassword()) {
                    confirmPassword = true;
                    return true;
                }
            }
        }
        return false;
    }


    public class existsUser{
        @SerializedName("exists")
        public String exists;
    }
}
