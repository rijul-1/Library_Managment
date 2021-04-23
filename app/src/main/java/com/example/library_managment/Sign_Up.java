package com.example.library_managment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Sign_Up extends AppCompatActivity {

   FirebaseAuth mfirebaseauth;
   FirebaseFirestore mfirestore;
    EditText email,password,name,phone_number;
    Button Sign_Up;
    String userid;
    public void Sign_In(View view){
        Intent i = new Intent(Sign_Up.this, MainActivity.class);
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign__up);

        mfirebaseauth = FirebaseAuth.getInstance();
        mfirestore = FirebaseFirestore.getInstance();
        email = findViewById(R.id.email_id);
        password = findViewById(R.id.password);
        Sign_Up = findViewById(R.id.Sign_Up_button);
        name = findViewById(R.id.uname);
        phone_number = findViewById(R.id.uphonenumber);

        Sign_Up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email_id = email.getText().toString();
                String pwd = password.getText().toString();
                String uname = name.getText().toString();
                String uphonenumber = phone_number.getText().toString();

                if(email_id.isEmpty() && pwd.isEmpty()){
                    Toast.makeText(Sign_Up.this , "Enter Email Id and Password",Toast.LENGTH_SHORT).show();
                }
                else if(email_id.isEmpty()){
                    email.setError("Please Enter Email Address");
                    email.requestFocus();
                }
                else if(pwd.isEmpty()){
                    password.setError("Please Enter Your Password");
                    password.requestFocus();
                }
                else {
                    mfirebaseauth.createUserWithEmailAndPassword(email_id,pwd).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Toast.makeText(com.example.library_managment.Sign_Up.this , "Successfully Registered!!" ,Toast.LENGTH_SHORT).show();
                            userid = mfirebaseauth.getCurrentUser().getUid();
                            DocumentReference documentreference = mfirestore.collection("Users").document(userid);
                            Map<String,Object> user = new HashMap<>();
                            user.put("uname",uname);
                            user.put("uemail",email_id);
                            user.put("uphone",uphonenumber);
                            documentreference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(Sign_Up.this , " User Profile Created Successfully!!",Toast.LENGTH_SHORT).show();
                                 }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Sign_Up.this , "Some Error Occurred, Please Try Again!!",Toast.LENGTH_SHORT).show();
                                }
                            });
                            Intent i = new Intent(Sign_Up.this, home_page.class);
                            i.putExtra("uname","Welcome " + uname.toUpperCase() + "!!");
                            startActivity(i);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Sign_Up.this , e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }


}