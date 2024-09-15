package com.codebee.water_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.codebee.water_app.dto.MessageDto;
import com.codebee.water_app.dto.RegisterDto;
import com.codebee.water_app.model.City;
import com.codebee.water_app.model.Customer;
import com.codebee.water_app.service.MessageService;
import com.codebee.water_app.service.RequestService;
import com.codebee.water_app.service.WaterAppService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationRegisterActivity extends AppCompatActivity {

    ArrayAdapter<String> cityArrayAdapter;
    List<String> cities = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_register);
        Spinner spinner = findViewById(R.id.addressCity);

        WaterAppService requestService = RequestService.getRequestService();
        Call<List<City>> city = requestService.getCity();
        city.enqueue(new Callback<List<City>>() {
            @Override
            public void onResponse(Call<List<City>> call, Response<List<City>> response) {
                if (response.isSuccessful()) {
                    cities.add("city");
                    List<City> body = response.body();

                    if (body.size() != 0) {
                        body.forEach(city1 -> {
                            cities.add(city1.getName());
                        });
                        cityArrayAdapter = new ArrayAdapter<String>(LocationRegisterActivity.this, android.R.layout.simple_spinner_item, cities);
                        cityArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(cityArrayAdapter);
                        spinner.setSelection(0);
                    }


                }


            }

            @Override
            public void onFailure(Call<List<City>> call, Throwable t) {
                Log.i("error", t.toString());
            }
        });


        Intent intent = getIntent();
        String fname = intent.getStringExtra("fname");
        String lname = intent.getStringExtra("lname");
        String mobile = intent.getStringExtra("mobile");
        String email = intent.getStringExtra("email");
        String password = intent.getStringExtra("password");
        EditText no = findViewById(R.id.addressNo);
        EditText line1 = findViewById(R.id.addressLine1);
        EditText line2 = findViewById(R.id.addressLine2);
        Spinner cit = findViewById(R.id.addressCity);
        findViewById(R.id.button5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (line1.getText().toString().isEmpty()) {

                    new MessageService(LocationRegisterActivity.this, "address line 1 is empty", false).setMessage();
                } else if (cit.getSelectedItemPosition() == 0) {
                    new MessageService(LocationRegisterActivity.this, "Please select a city", false).setMessage();

                } else {

                    RegisterDto registerDto = new RegisterDto(fname, lname, mobile, email, password, no.getText().toString(), line1.getText().toString(), line2.getText().toString(), cit.getSelectedItem().toString());


                    WaterAppService service = RequestService.getRequestService();
                    Call<MessageDto> register = service.register(registerDto);
                    register.enqueue(new Callback<MessageDto>() {
                        @Override
                        public void onResponse(Call<MessageDto> call, Response<MessageDto> response) {
                            System.out.println("success");
                            if (response.isSuccessful()) {
                                String message = response.body().getMessage();
                                if (message.equals("success")) {

                                    SharedPreferences data = getSharedPreferences("data", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = data.edit();
                                    editor.putString("chat","no");
                                    editor.putInt("num",1);
//                                    new MessageService(LocationRegisterActivity.this, "Success", true).setMessage();

                                    Intent intent1 = new Intent(LocationRegisterActivity.this, SplashActivity.class);
                                    startActivity(intent1);

                                } else {
                                    Thread thread = new MessageService(LocationRegisterActivity.this, message, false).setMessage();


                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<MessageDto> call, Throwable t) {
                            System.out.println("fail " + t.toString());
                        }
                    });
                }

            }
        });


        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationRegisterActivity.this.finish();
            }
        });

    }


}