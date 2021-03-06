package com.android.raionpunten;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class DaftarAdminActivity extends AppCompatActivity {
    private Button btnRegister;
    private EditText inputNama,inputEmail,inputPassword,inputDate,inPutDomisili,konfirPassword;
    private ProgressDialog loadingbar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);
        btnRegister = findViewById(R.id.btnDaftar);
        inputNama = findViewById(R.id.inputNama);
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        inputDate = findViewById(R.id.inputDate);
        inPutDomisili = findViewById(R.id.inputDomisili);
        konfirPassword = findViewById(R.id.konfirPassword);
        loadingbar = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });
    }

    public void createAccount(){
        String nama = inputNama.getText().toString();
        String tanggal = inputDate.getText().toString();
        String domisili = inPutDomisili.getText().toString();
        String konfirmasi = konfirPassword.getText().toString();
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();
        if(nama.isEmpty()){
            inputNama.setError("Nama Tidak Boleh Kosong");
            inputNama.setFocusable(true);
        }else if(tanggal.length() != 6){
            inputDate.setError("Tanggal Lahir Salah");
            inputDate.setFocusable(true);
        }else if(domisili.isEmpty()){
            inPutDomisili.setError("Domisili Tidak Boleh Kosong");
            inPutDomisili.setFocusable(true);
        } else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            inputEmail.setError("Email Salah");
            inputEmail.setFocusable(true);
        }else if (password.length()<6){
            inputPassword.setError("Password Minimal 6 Karakter");
            inputPassword.setFocusable(true);
        }else if(!konfirmasi.equals(password)){
            konfirPassword.setError("Password Tidak Sama");
            konfirPassword.setFocusable(true);
        } else {
            loadingbar.setTitle("Create Account");
            loadingbar.setMessage("Please wait!");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();
            registerUser(nama, email, password,tanggal,domisili);
        }


    }
    private void registerUser(final String nama, final String email, final String password,final String tanggal,
                              final String domisili) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            loadingbar.dismiss();
                            FirebaseUser user = mAuth.getCurrentUser();
                            ValidateUser(nama, email, password,tanggal,domisili);
                            Intent intent = new Intent(DaftarAdminActivity.this,LoginActivity.class);
                            startActivity(intent);
                            Toast.makeText(DaftarAdminActivity.this, "Account created", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(DaftarAdminActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loadingbar.dismiss();
                Toast.makeText(DaftarAdminActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void ValidateUser(final String nama, String emailAsli, final String password,final String tanggal,
                              final String domisili) {
        final DatabaseReference Rootref;
        Rootref = FirebaseDatabase.getInstance().getReference();
        final String email = emailAsli.replace("@","%1").replace(".","%2");

        Rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                HashMap<String, Object> userdataMap = new HashMap<>();
                userdataMap.put("nama",nama);
                userdataMap.put("tanggal",tanggal);
                userdataMap.put("domisili",domisili);
                userdataMap.put("email", email);
                userdataMap.put("password",password);

                Rootref.child("User").child(email).updateChildren(userdataMap);

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}