package com.codebee.water_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.codebee.water_app.service.MessageService;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


//        LayoutInflater from = LayoutInflater.from(RegisterActivity.this);
//        View inflate = from.inflate(R.layout.message_dialog_custom, null);
//
//
//        TextView viewById = inflate.findViewById(R.id.messageText);
//        setContentView(R.layout.activity_register);
//
//        viewById.setText("wada");


//        View view = findViewById(R.layout.message_dialog_custom);

        EditText fname = findViewById(R.id.fn);
        EditText lname = findViewById(R.id.ln);
        EditText mobile = findViewById(R.id.mob);
        EditText email = findViewById(R.id.email);
        EditText password = findViewById(R.id.pass);


        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (fname.getText().toString().isEmpty()) {

                    new MessageService(RegisterActivity.this, "First Name Is empty", false).setMessage();
                } else if (lname.getText().toString().isEmpty()) {
                    new MessageService(RegisterActivity.this, "Last Name Is empty", false).setMessage();

                } else if (mobile.getText().toString().isEmpty()) {
                    new MessageService(RegisterActivity.this, "mobile Is empty", false).setMessage();

                } else if (!Pattern.compile("0[7][0|1|2|4|5|6|7|8][0-9]{7}").matcher(mobile.getText().toString()).matches()) {
                    new MessageService(RegisterActivity.this, "Invalid mobile", false).setMessage();

                } else if (email.getText().toString().isEmpty()) {
                    new MessageService(RegisterActivity.this, "email Is empty", false).setMessage();

                } else if (!Pattern.compile("(\\S.*\\S)(@)(\\S.*\\S)(.\\S[a-z]{2,3})").matcher(email.getText().toString()).matches()) {
                    new MessageService(RegisterActivity.this, "Invalid email", false).setMessage();

                } else if (password.getText().toString().isEmpty()) {

                    new MessageService(RegisterActivity.this, "password is empty", false).setMessage();

                } else if (password.getText().toString().length() < 8 || password.getText().toString().length() > 12) {
                    new MessageService(RegisterActivity.this, "password should be 8 to 12 characters", false).setMessage();

                } else {

                    Intent intent = new Intent(RegisterActivity.this, LocationRegisterActivity.class);

                    intent.putExtra("fname", fname.getText().toString());
                    intent.putExtra("lname", lname.getText().toString());
                    intent.putExtra("mobile", mobile.getText().toString());
                    intent.putExtra("email", email.getText().toString());
                    intent.putExtra("password", password.getText().toString());
                    startActivity(intent);


                }


            }
        });
    }
}

