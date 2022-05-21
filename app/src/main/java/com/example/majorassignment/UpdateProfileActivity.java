package com.example.majorassignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class UpdateProfileActivity extends AppCompatActivity {
    EditText txtFullName, txtPhoneNum, txtEmail;
    Button btnSave;
    ProgressBar progressBar;

    FirebaseFirestore db;
    FirebaseAuth mAuth;
    String name, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        txtEmail = findViewById(R.id.profileEmail);
        txtFullName = findViewById(R.id.profileFullName);
        txtPhoneNum = findViewById(R.id.profilePhoneNum);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        btnSave = findViewById(R.id.btnSave);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        name = getIntent().getStringExtra("fullname");
        phone = getIntent().getStringExtra("phone");
        FirebaseUser currentUser = mAuth.getCurrentUser();

        txtEmail.setText(currentUser.getEmail());
        txtFullName.setText(name);
        txtPhoneNum.setText(phone);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String fullName = txtFullName.getText().toString();
                String phoneNum = txtPhoneNum.getText().toString();
                Map<String, Object> user = new HashMap<>();
                user.put("email", currentUser.getEmail());
                user.put("fullName", fullName);
                user.put("phoneNum", phoneNum);
                if (!name.isEmpty() || !phone.isEmpty()) {
                    db.collection("user").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    if (document.get("email").toString().equals(currentUser.getEmail())) {
                                        db.collection("user").document(document.getId()).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> innerTask) {
                                                if (innerTask.isSuccessful()) {
                                                    Toast.makeText(UpdateProfileActivity.this, "Update profile successfully", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(UpdateProfileActivity.this, MainActivity.class));
                                                } else {
                                                    Toast.makeText(UpdateProfileActivity.this, "Update profile failed" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                                progressBar.setVisibility(View.GONE);
                                            }
                                        });
                                    }
                                }
                            }
                        }
                    });
                } else {
                    db.collection("user").add(user).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(UpdateProfileActivity.this, "Update profile successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(UpdateProfileActivity.this, MainActivity.class));
                            } else {
                                Toast.makeText(UpdateProfileActivity.this, "Update profile failed" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }

            }
        });

    }
}