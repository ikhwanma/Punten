package com.android.raionpunten;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class BuatKerjaActivity extends AppCompatActivity {
    private EditText inputNama,inputBidang,inputDomisili,inputPemilik,inputKode;
    private Button btnSetuju;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buat_kerja);
        inputNama = findViewById(R.id.inputNama);
        inputBidang = findViewById(R.id.inputBidang);
        btnSetuju = findViewById(R.id.btnSetuju);
        inputDomisili = findViewById(R.id.inputDomisili);
        inputPemilik = findViewById(R.id.inputPemilik);
        inputKode = findViewById(R.id.inputKode);
        btnSetuju.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuatKerja();

            }
        });
    }

    public void BuatKerja(){
        final String nama = inputNama.getText().toString();
        final String bidang = inputBidang.getText().toString();
        final String domisili = inputDomisili.getText().toString();
        final String pemilik = inputPemilik.getText().toString();
        final String kode = inputKode.getText().toString();
        if(nama.isEmpty()){
            inputNama.setError("Nama Tidak Boleh Kosong");
            inputNama.setFocusable(true);
        }else if(bidang.isEmpty()){
            inputBidang.setError("Bidang Tidak Boleh Kosong");
            inputBidang.setFocusable(true);
        }else if(domisili.isEmpty()){
            inputDomisili.setError("Domisili Tidak Boleh Kosong");
            inputDomisili.setFocusable(true);
        }else if(pemilik.isEmpty()){
            inputPemilik.setError("Domisili Tidak Boleh Kosong");
            inputPemilik.setFocusable(true);
        }else if(kode.isEmpty()){
            inputKode.setError("Kode Tidak Boleh Kosong");
            inputKode.setFocusable(true);
        }else{
            final DatabaseReference Rootref;
            Rootref = FirebaseDatabase.getInstance().getReference();
            Rootref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("Nama",nama);
                    userdataMap.put("Domisili",domisili);
                    userdataMap.put("Bidang",bidang);
                    userdataMap.put("Pemilik",pemilik);
                    userdataMap.put("Kode",kode);
                    Rootref.child("Kantor").child(kode).updateChildren(userdataMap);
                    Intent intent = new Intent(BuatKerjaActivity.this,BerandaActivity.class);
                    startActivity(intent);
                    Toast.makeText(BuatKerjaActivity.this, "Kantor Dibuat", Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

    }
}