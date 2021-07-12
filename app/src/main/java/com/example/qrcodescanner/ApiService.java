package com.example.qrcodescanner;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {
    @PUT("/code")
    Call<QRCode> putQRCode(@Body QRCode qrCode);
}
