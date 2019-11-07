package com.zonar.zonarapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.zonar.zonarapp.data.model.GetToken;
import com.zonar.zonarapp.data.remote.APIService;
import com.zonar.zonarapp.data.remote.ApiUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginRegisterActivity extends AppCompatActivity {


    private static final String TAG = LoginRegisterActivity.class.getSimpleName();
    private TextView mResponseTv;

    private APIService mAPIService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText usernameET = findViewById(R.id.usernameET);
        final EditText passwordEt = findViewById(R.id.passwordET);
        Button loginBtn = findViewById(R.id.btn_login);
        Button registerBtn = findViewById(R.id.btn_register);
        mResponseTv = findViewById(R.id.tv_response);

        mAPIService = ApiUtils.getAPIService();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameET.getText().toString().trim();
                String password = passwordEt.getText().toString().trim();
                if(!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
                    login(username, password);
                }
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameET.getText().toString().trim();
                String password1 = passwordEt.getText().toString().trim();
                String password2 = passwordEt.getText().toString().trim();
                if(!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password1)) {
                    register(username, password1, password2);
                }
            }
        });
    }

    public void login(String username, String password) {
        mAPIService.login(username, password).enqueue(new Callback<GetToken>() {
            @Override
            public void onResponse(Call<GetToken> call, Response<GetToken> response) {

                if (response.isSuccessful()) {
                    String token = response.body().toString();
                    showResponse(token);
                    Log.i(TAG, "post submitted to API." + response.body().toString());
                    Intent intent = new Intent(LoginRegisterActivity.this, DataActivity.class);
                    intent.putExtra("token", token);
                    startActivity(intent);
                } else {
                    try {
                        String jObjError = new String(response.errorBody().string());
                        showResponse(jObjError);
                        Log.i(TAG, jObjError);
                    } catch (Exception e) {
                        Log.i(TAG, e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<GetToken> call, Throwable t) {

                showErrorMessage();
                Log.e(TAG, "Unable to submit post to API. Throwable :" + t.toString());
            }
        });
    }

    public void register(String username, String password1, String password2) {
        mAPIService.register(username, password1, password2).enqueue(new Callback<GetToken>() {
            @Override
            public void onResponse(Call<GetToken> call, Response<GetToken> response) {

                if (response.isSuccessful()) {
                    showResponse(response.body().toString());
                    mResponseTv.setText("Register success! You can now login");
                    Log.i(TAG, "post submitted to API." + response.body().toString());
                } else {
                    try {
                        String jObjError = new String(response.errorBody().string());
                        showResponse(jObjError);
                        Log.i(TAG, jObjError);
                    } catch (Exception e) {
                        Log.i(TAG, e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<GetToken> call, Throwable t) {

                showErrorMessage();
                Log.e(TAG, "Unable to submit post to API. Throwable :" + t.toString());
            }
        });
    }

    public void showResponse(String response) {
        if(mResponseTv.getVisibility() == View.GONE) {
            mResponseTv.setVisibility(View.VISIBLE);
        }
        mResponseTv.setText(response);
    }

    public void showErrorMessage() {
        Toast.makeText(this, "Error submitting post", Toast.LENGTH_SHORT).show();
    }
}
