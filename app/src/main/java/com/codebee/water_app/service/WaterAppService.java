package com.codebee.water_app.service;

import com.codebee.water_app.dto.AuthDTO;
import com.codebee.water_app.dto.AuthResponseDTO;
import com.codebee.water_app.dto.CartDto;
import com.codebee.water_app.dto.CartItemsDto;
import com.codebee.water_app.dto.DriverDtoChat;
import com.codebee.water_app.dto.LocationDto;
import com.codebee.water_app.dto.MessageDto;
import com.codebee.water_app.dto.OrderDto;
import com.codebee.water_app.dto.OrderReceiveDto;
import com.codebee.water_app.dto.ProductDTO;
import com.codebee.water_app.dto.RegisterDto;
import com.codebee.water_app.model.City;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface WaterAppService {


    @POST("customer/register")
    Call<MessageDto> register(@Body RegisterDto customer);


    @POST("city")
    Call<List<City>> getCity();

    @POST("customer/login")
    Call<AuthResponseDTO> auth(@Body AuthDTO authDTO);


    @POST("customer/product")
    Call<List<ProductDTO>> getProducts();


    @POST("customer/cart/add")
    Call<MessageDto> addToCart(@Body CartDto cartDto, @Header("Authorization") String token);

    @POST("customer/cart/getCount")
    Call<MessageDto> getItemCounts(@Body AuthDTO email, @Header("Authorization") String token);

    @POST("customer/cart/getAllItems")
    Call<List<CartItemsDto>> getCartItem(@Body AuthDTO email, @Header("Authorization") String token);

    @POST("customer/order/add")
    Call<MessageDto> addOrder(@Body LocationDto location, @Header("Authorization") String token);




    @POST("customer/order/getOrder")
    Call <List<OrderDto>> getOrders(@Body AuthDTO email, @Header("Authorization") String token);


    @POST("driver/order/getdriverforchat")
    Call <DriverDtoChat> getDriver(@Body AuthDTO email, @Header("Authorization") String token);
}
