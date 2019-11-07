package com.zonar.zonarapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.zonar.zonarapp.data.remote.APIService;
import com.zonar.zonarapp.data.remote.ApiUtils;
import com.zonar.zonarapp.data.model.GetData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataActivity extends AppCompatActivity {

    private static final String TAG = LoginRegisterActivity.class.getSimpleName();
    private APIService mAPIService;
    private EditText nameET, genderET, birthdayET, emailET, data1ET;
    private Button editBtn, saveBtn;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        mAPIService = ApiUtils.getAPIService();
        Intent intent = getIntent();
        token = intent.getStringExtra("token");

        nameET = findViewById(R.id.nameET);
        genderET = findViewById(R.id.genderET);
        birthdayET = findViewById(R.id.birthdayET);
        emailET = findViewById(R.id.emailET);
        data1ET = findViewById(R.id.data1ET);
        getData();
        editToggle(false);

        editBtn = findViewById(R.id.btn_editData);
        saveBtn = findViewById(R.id.btn_saveData);
        saveBtn.setVisibility(View.INVISIBLE);

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (genderET.isEnabled()) {
                    getData();
                    editBtn.setText("Edit");
                    editToggle(false);
                    saveBtn.setVisibility(View.INVISIBLE);
                }else {
                    editBtn.setText("Cancel");
                    editToggle(true);
                    saveBtn.setVisibility(View.VISIBLE);
                }

            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameET.getText().toString().trim();
                String gender = genderET.getText().toString().trim();
                String birthday = birthdayET.getText().toString().trim();
                String email = emailET.getText().toString().trim();
                String data1 = data1ET.getText().toString().trim();
                editData(name, gender, birthday, email, data1);
                editToggle(false);
                editBtn.setText("Edit");
                saveBtn.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void getData() {
        //"Token ec2ae74c03543f8e2f70d288f9b87297f888c319"
        mAPIService.get(token).enqueue(new Callback<GetData>() {
            @Override
            public void onResponse(Call<GetData> call, Response<GetData> response) {

                if (response.isSuccessful()) {
                    nameET.setText(response.body().getName());
                    genderET.setText(response.body().getGender());
                    birthdayET.setText(response.body().getBirthday());
                    emailET.setText(response.body().getE_mail());
                    data1ET.setText(response.body().getData1());
                    Log.i(TAG, "post submitted to API." + response.body().toString());
                } else {
                    try {
                        String jObjError = new String(response.errorBody().string());
                        Log.i(TAG, jObjError);
                    } catch (Exception e) {
                        Log.i(TAG, e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<GetData> call, Throwable t) {

                Log.e(TAG, "Unable to submit post to API. Throwable :" + t.toString());
            }
        });
    }

    public void editData(String name, String gender, String birthday, String email, String data1) {
        mAPIService.modify(token, name, gender, birthday, email, data1).enqueue(new Callback<GetData>() {
            @Override
            public void onResponse(Call<GetData> call, Response<GetData> response) {

                if (response.isSuccessful()) {
                    nameET.setText(response.body().getName());
                    genderET.setText(response.body().getGender());
                    birthdayET.setText(response.body().getBirthday());
                    emailET.setText(response.body().getE_mail());
                    data1ET.setText(response.body().getData1());
                    Log.i(TAG, "post submitted to API." + response.body().toString());
                } else {
                    try {
                        String jObjError = new String(response.errorBody().string());
                        Log.i(TAG, jObjError);
                    } catch (Exception e) {
                        Log.i(TAG, e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<GetData> call, Throwable t) {

                Log.e(TAG, "Unable to submit post to API. Throwable :" + t.toString());
            }
        });
    }

    private void editToggle(boolean enable){

        genderET.setEnabled(enable);
        birthdayET.setEnabled(enable);
        emailET.setEnabled(enable);
        data1ET.setEnabled(enable);
    }
}
