package com.example.majorassignment.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.majorassignment.LoginActivity;
import com.example.majorassignment.MainActivity;
import com.example.majorassignment.R;
import com.example.majorassignment.UpdateProfileActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class FragmentProfile extends Fragment {

    private TextView textView, txtFullName, txtPhoneNum;
    private FirebaseAuth mAuth;
    private Button buttonLogout, buttonUpdateProfile;
    String fullName, phoneNum;
    FirebaseFirestore db;
    public FragmentProfile() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textView = view.findViewById(R.id.txtProfile);
        txtFullName = view.findViewById(R.id.txtFullName);
        txtPhoneNum = view.findViewById(R.id.txtPhoneNum);
        mAuth = FirebaseAuth.getInstance();
        buttonLogout = view.findViewById(R.id.btnLogout);
        buttonUpdateProfile = view.findViewById(R.id.btnUpdateProfile);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        textView.setText("Hi " + currentUser.getEmail());
        db.collection("user").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT);
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document.get("email").toString().equals(currentUser.getEmail())) {
                            fullName = document.get("fullName").toString();
                            txtFullName.setText("Full Name: " + fullName);
                            phoneNum = document.get("phoneNum").toString();
                            txtPhoneNum.setText("Phone Number: " + phoneNum);
                        }
                    }
                } else {

                }
            }
        });
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
        buttonUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), UpdateProfileActivity.class)
                        .putExtra("fullname", fullName)
                        .putExtra("phone", phoneNum));
            }
        });
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getActivity(), LoginActivity.class));
    }
}
