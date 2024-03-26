package com.example.lab5_and103;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lab5_and103.Adapter.DistributorAdapter;
import com.example.lab5_and103.Model.Distributor;
import com.example.lab5_and103.Model.Response;
import com.example.lab5_and103.Service.HttpRequest;
import com.example.lab5_and103.Service.Item_Distributor_Handle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private DistributorAdapter recycleViewProductAdapter;
    private RecyclerView rcvProduct;
    private HttpRequest httpRequest;
    private FloatingActionButton fltAdd;
    private Button btnAdd;
    private EditText etName;
    private Dialog dialogAddProduct;
    private boolean typeHandle = true;
    Distributor distributorUpdate = new Distributor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rcvProduct = findViewById(R.id.rcvProduct);
        fltAdd = findViewById(R.id.fltAdd);
        httpRequest = new HttpRequest();
        httpRequest.callAPI().getListDistributor().enqueue(getDistributorAPI);

        fltAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                typeHandle = true;
                OpenDialogAddCategory();
            }
        });
    }

    private void GetData(ArrayList<Distributor> list){
        recycleViewProductAdapter = new DistributorAdapter(this, list, new Item_Distributor_Handle() {
            @Override
            public void Delete(String id) {
                httpRequest.callAPI().deleteDistributor(id).enqueue(delDistributor);
            }

            @Override
            public void Update(Distributor distributor) {
                typeHandle = false;
                distributorUpdate = distributor;
                OpenDialogAddCategory();
            }
        });
        rcvProduct.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        rcvProduct.setAdapter(recycleViewProductAdapter);
    }

    Callback<Response<ArrayList<Distributor>>> getDistributorAPI = new Callback<Response<ArrayList<Distributor>>>() {
        @Override
        public void onResponse(Call<Response<ArrayList<Distributor>>> call, retrofit2.Response<Response<ArrayList<Distributor>>> response) {
            if(response.isSuccessful()){
                if (response.body().getStatus() == 200){
                    ArrayList<Distributor> listPro = response.body().getData();
                    GetData(listPro);
                }
            }
        }

        @Override
        public void onFailure(Call<Response<ArrayList<Distributor>>> call, Throwable t) {

        }
    };



    Callback<Response<Distributor>> delDistributor = new Callback<Response<Distributor>>() {
        @Override
        public void onResponse(Call<Response<Distributor>> call, retrofit2.Response<Response<Distributor>> response) {
            if(response.isSuccessful()){
                if (response.body().getStatus() == 200){
                    httpRequest.callAPI().getListDistributor().enqueue(getDistributorAPI);
                    Toast.makeText(MainActivity.this, "Delete successful" , Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onFailure(Call<Response<Distributor>> call, Throwable t) {

        }
    };

    Callback<Response<Distributor>> addDistributor = new Callback<Response<Distributor>>() {
        @Override
        public void onResponse(Call<Response<Distributor>> call, retrofit2.Response<Response<Distributor>> response) {
            if(response.isSuccessful()){
                if (response.body().getStatus() == 200){
                    httpRequest.callAPI().getListDistributor().enqueue(getDistributorAPI);
                    Toast.makeText(MainActivity.this, "Insert successful" , Toast.LENGTH_SHORT).show();
                    dialogAddProduct.dismiss();
                }
            }
        }

        @Override
        public void onFailure(Call<Response<Distributor>> call, Throwable t) {

        }
    };

    Callback<Response<Distributor>> updateDistributor = new Callback<Response<Distributor>>() {
        @Override
        public void onResponse(Call<Response<Distributor>> call, retrofit2.Response<Response<Distributor>> response) {
            if(response.isSuccessful()){
                if (response.body().getStatus() == 200){
                    httpRequest.callAPI().getListDistributor().enqueue(getDistributorAPI);
                    Toast.makeText(MainActivity.this, "Update successful" , Toast.LENGTH_SHORT).show();
                    dialogAddProduct.dismiss();
                }
            }
        }

        @Override
        public void onFailure(Call<Response<Distributor>> call, Throwable t) {

        }
    };




    private void OpenDialogAddCategory() {
        final View dialogView = View.inflate(this, R.layout.dialog_add, null);
        dialogAddProduct = new Dialog(this);

        dialogAddProduct.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogAddProduct.setContentView(dialogView);

        Window window = dialogAddProduct.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialogAddProduct.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.gravity = Gravity.CENTER;
        dialogAddProduct.getWindow().setAttributes(layoutParams);

        etName = dialogView.findViewById(R.id.et_name);
        btnAdd = dialogView.findViewById(R.id.btn_submit);
        if(typeHandle == false){
            etName.setText(distributorUpdate.getName());
            btnAdd.setText("Update distributor");
        }


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etName.getText().toString().trim();

                if (typeHandle){
                    httpRequest.callAPI().addDistributor(
                            new Distributor("", name, "", "")
                    ).enqueue(addDistributor);
                } else {
                    distributorUpdate.setName(name);
                        httpRequest.callAPI().updateDistributor(
                                distributorUpdate.getId(),
                                distributorUpdate
                        ).enqueue(updateDistributor);
                    }
            }
        });
        dialogAddProduct.show();
    }

}