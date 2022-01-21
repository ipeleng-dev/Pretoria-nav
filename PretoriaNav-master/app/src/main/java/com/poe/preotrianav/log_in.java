package com.poe.preotrianav;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class log_in extends AppCompatActivity {
    //firebase authentication
    private FirebaseAuth firebaseAuth;

    //progress dialog--show loading
    private ProgressDialog progressDialog;

    private Button btnLOGin; //button_sign_in
    private EditText username;//email
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
//
        btnLOGin = findViewById(R.id.button_sign_in);
        username = findViewById(R.id.email_editText);
        password =  findViewById(R.id.password_editText);
        TextView registerOption =findViewById(R.id.tv_redirect_register);


        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance();
        //setup progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);


        //click event to register
        registerOption.setOnClickListener(v -> startActivity(new Intent(log_in.this,sign_up.class)));

        //click event to login
        btnLOGin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }

            private String eEmail ="", ePassword ="";
            private void validateData() {

                //getting the data
                eEmail = username.getText().toString().trim();
                ePassword = password.getText().toString().trim();

                //Validate data
                if(!Patterns.EMAIL_ADDRESS.matcher(eEmail).matches()){
                    Toast.makeText(log_in.this, "Fill/check email format", Toast.LENGTH_SHORT).show();

                }
                else if (TextUtils.isEmpty(ePassword)){
                    Toast.makeText(log_in.this, "Password is needed", Toast.LENGTH_SHORT).show();

                }
                else{
                    //data is validated, begin login
                    loginUser();

                }

            }

            //function to handle user login
            private void loginUser() {
                progressDialog.setMessage("Logging user in..");
                progressDialog.show();

                //logging the user in
                firebaseAuth.signInWithEmailAndPassword(eEmail,ePassword)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                checkUser();
                            }

                            private void checkUser() {
                                //check if user is user from realtime database
                                //get current user
                                progressDialog.setMessage("Checking If You've Registered ");
                                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                                //Check in db
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
                                ref.child(firebaseUser.getUid())
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot snapshot) {
                                                progressDialog.dismiss();
                                                //get user type
                                                String userType = "" + snapshot.child("UserType").getValue();
                                                //check user type
                                                if (userType.equals("user")) {
                                                    //this  user, open user splash to first window
                                                    startActivity(new Intent(log_in.this, navigation_screen.class));
                                                    finish();

                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }


                                        });

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(Exception e) {
                                //login failed
                                progressDialog.dismiss();
                                Toast.makeText(log_in.this, ""+e.getMessage(),Toast.LENGTH_SHORT).show();

                            }
                        });
            }
        });
    }



    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

    }
    public void sign_up(View v){

        // intent to open the sign up page
        Intent open = new Intent(log_in.this,sign_up.class);
        startActivity(open);
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
    }

    public void skip(View v){

        // intent to open the sign up page
        Intent open = new Intent(log_in.this,navigation_screen.class);
        startActivity(open);
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
    }
}