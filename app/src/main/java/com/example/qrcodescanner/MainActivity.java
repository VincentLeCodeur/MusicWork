package com.example.qrcodescanner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONObject;

import java.util.EventListener;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {
    private Button BtnScan;
    private TextView QRCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        QRCode = findViewById(R.id.QRCode);
        BtnScan = findViewById(R.id.BtnScanner);
        BtnScan.setOnClickListener(v -> {
            IntentIntegrator intentIntegrator = new IntentIntegrator(MainActivity.this);
            intentIntegrator.setBeepEnabled(true);
            intentIntegrator.setOrientationLocked(true);
            intentIntegrator.setCaptureActivity(Capture.class);
            intentIntegrator.initiateScan();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult.getContents() != null){
            QRCode.setText(intentResult.getContents());
        }
        else {
            Toast.makeText(getApplicationContext(), "No QR code found", Toast.LENGTH_SHORT).show();
        }
    }

    public void OnSendButtonClicked(View view){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.3.63:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService service = retrofit.create(ApiService.class);
        Call<QRCode> repos = service.putQRCode(new QRCode(QRCode.getText().toString()));
        new Thread(() -> {
            repos.enqueue(new Callback<com.example.qrcodescanner.QRCode>() {
                @Override
                public void onResponse(Call<com.example.qrcodescanner.QRCode> call, Response<com.example.qrcodescanner.QRCode> response) {
                    System.out.println("Fonctionnel");
                }

                @Override
                public void onFailure(Call<com.example.qrcodescanner.QRCode> call, Throwable t) {
                    System.out.println("Non Fonctionnel " + t.getMessage() + " " + t.getCause());
                }
            });
        }).start();

    }
}

