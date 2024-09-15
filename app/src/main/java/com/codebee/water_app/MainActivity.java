package com.codebee.water_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.codebee.water_app.dto.AuthDTO;
import com.codebee.water_app.dto.AuthResponseDTO;
import com.codebee.water_app.service.MessageService;
import com.codebee.water_app.service.RequestService;
import com.codebee.water_app.service.WaterAppService;
import com.squareup.picasso.Picasso;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imageView = findViewById(R.id.logInIcon);
        Picasso.get().load(R.drawable.purewater)
                .resize(300, 300)
                .into(imageView);


        findViewById(R.id.button_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText emailField = findViewById(R.id.log_email);
                EditText passwordField = findViewById(R.id.log_password);

                if (emailField.getText().toString().isEmpty()) {

                    new MessageService(MainActivity.this, "Email field is empty", false).setMessage();
                } else if (!Pattern.compile("(\\S.*\\S)(@)(\\S.*\\S)(.\\S[a-z]{2,3})").matcher(emailField.getText().toString()).matches()) {

                    new MessageService(MainActivity.this, "Invalid Email", false).setMessage();

                } else if (passwordField.getText().toString().isEmpty()) {

                    new MessageService(MainActivity.this, "password is Empty", false).setMessage();

                } else {
                    AuthDTO authDTO = new AuthDTO();
                    authDTO.setEmail(emailField.getText().toString());
                    authDTO.setPassword(passwordField.getText().toString());
                    WaterAppService requestService = RequestService.getRequestService();
                    Call<AuthResponseDTO> auth = requestService.auth(authDTO);
                    auth.enqueue(new Callback<AuthResponseDTO>() {
                        @Override
                        public void onResponse(Call<AuthResponseDTO> call, Response<AuthResponseDTO> response) {

                            if (response.isSuccessful()) {
                                System.out.println("success");

                            SharedPreferences data = getSharedPreferences("data", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = data.edit();
                                    editor.putString("accessToken", response.body().getAccessToken());
                                    editor.putString("email", authDTO.getEmail());
                                    editor.putString("password",authDTO.getPassword());
                                    editor.apply();


                                    Intent intent = new Intent(MainActivity.this, ClientHomeActivity.class);
                                    startActivity(intent);





                            }else{

                                new MessageService(MainActivity.this,"invalid email or password", false).setMessage();

                                System.out.println("unsuccess");
                            }
                        }

                        @Override
                        public void onFailure(Call<AuthResponseDTO> call, Throwable t) {

                            System.out.println("fail");
                        }
                    });
                }


            }
        });


        findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}