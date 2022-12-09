package com.example.finalexamapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {
    Button loadBtn, searchBtn;
    String data;
    TextView output;

    ArrayList<product> products = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadBtn = findViewById(R.id.loadBtn);
        output = findViewById(R.id.output);


        loadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String line;
                DBHandler dbHandler = new DBHandler(MainActivity.this);
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open("Products-1.txt")));
                    while ((line = reader.readLine()) != null){
                        product currentProduct = new product();
                        StringTokenizer tokens = new StringTokenizer(line, ",");
                        currentProduct.id = tokens.nextToken();
                        currentProduct.name = tokens.nextToken();
                        currentProduct.quantity = tokens.nextToken();
                        currentProduct.price = tokens.nextToken();
                        products.add(currentProduct);
                        dbHandler.addProduct(currentProduct);
                        Toast.makeText(MainActivity.this, "Succesfully Loaded Data", Toast.LENGTH_LONG).show();
                    }
                }catch (IOException ex ){

                }
            }


        });
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });
    }

}