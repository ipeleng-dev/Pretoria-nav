package com.poe.preotrianav;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class sign_up extends AppCompatActivity {
    //firebase authentication
    private FirebaseAuth firebaseAuth;
    //Progress
    private ProgressDialog progressDialog;
    //
    Button button_sign_in;
    private EditText name_editText,email_editText,cPassword_editText,password_editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
//init firebase auth
        firebaseAuth = FirebaseAuth.getInstance();
        //setup progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Hold on a minute");
        progressDialog.setCanceledOnTouchOutside(false);
        button_sign_in = findViewById(R.id.button_sign_in);

        name_editText = findViewById(R.id.name_editText);
        email_editText = findViewById(R.id.email_editText);
        cPassword_editText = findViewById(R.id.cPassword_editText);
        password_editText = findViewById(R.id.password_editText);

        //click event for button register
        button_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
            //
            private String  username="" , eEmail ="", ePassword ="";
            private void validateData() {


                //getting the data
                username= name_editText.getText().toString().trim();
                eEmail = email_editText.getText().toString().trim();
                ePassword = password_editText.getText().toString().trim();
                String confirmPassword = cPassword_editText.getText().toString().trim();

                //performing validation of the data
                if (TextUtils.isEmpty(username)){
                    Toast.makeText(sign_up.this,"Full names needed", Toast.LENGTH_SHORT).show();
                }

				/*else if (TextUtils.isEmpty(eEmail)){
					Toast.makeText(signup_activity.this,"Enter Email", Toast.LENGTH_SHORT).show();
				}*/
                else if(!Patterns.EMAIL_ADDRESS.matcher(eEmail).matches()){
                    Toast.makeText(sign_up.this, "Fill/check your email format", Toast.LENGTH_SHORT).show();

                }
                else if (TextUtils.isEmpty(ePassword)){
                    Toast.makeText(sign_up.this, "Enter your password", Toast.LENGTH_SHORT).show();

                }
                else if (TextUtils.isEmpty(confirmPassword)){
                    Toast.makeText(sign_up.this, "Confirm Password", Toast.LENGTH_SHORT).show();

                }
                else if (!ePassword.equals(confirmPassword)){
                    Toast.makeText(sign_up.this, "Oops,passwords don't match",Toast.LENGTH_SHORT).show();
                }
                else {
                    createUserAccount();
                }


            }

            private void createUserAccount() {
                //display progress
                progressDialog.setMessage("Getting your account ready");
                progressDialog.show();

                //create the user in db
                firebaseAuth.createUserWithEmailAndPassword(eEmail,ePassword)

                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {

                                //account creation success
                                //progressDialog.dismiss();
                                //account update
                                updateUserInfo();
                            }

                            private void updateUserInfo() {
                                progressDialog.setMessage("putting your eggs in a basket");
                                long timestamp = System.currentTimeMillis();
                                //get current user

                                String uid = firebaseAuth.getUid(); //get current user id

                                //setup data to add in db
                                HashMap<String,Object> hashMap= new HashMap<>();
                                hashMap.put("uid",uid);
                                hashMap.put("email", eEmail);
                                hashMap.put("UserType","user"); //Will make admin in firebase
                                hashMap.put("timestamp", timestamp);


                                //set data to db
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
                                ref.child(uid)
                                        .setValue(hashMap)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {

                                                //data added db
                                                progressDialog.dismiss();
                                                Toast.makeText(sign_up.this,"Account created. Good eggs", Toast.LENGTH_SHORT).show();
                                                //now have an account, start the first window
                                                startActivity(new Intent(sign_up.this, log_in.class));
                                                finish();


                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(Exception e) {
                                                //data failed adding db
                                                progressDialog.dismiss();
                                                Toast.makeText(sign_up.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                                            }
                                        });


                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(Exception e) {
                                //Account creating failed
                                progressDialog.dismiss();
                                Toast.makeText(sign_up.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });


            }
        });
    }
}