package com.company.automaticfishfeederapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import io.github.muddz.styleabletoast.StyleableToast;

public class LoginActivity extends AppCompatActivity {

    private Button btn_login;
    private TextInputLayout  txt_emailLayout, txt_passwordLayout;
    private TextInputEditText txt_email,txt_password;
    private String userId,firstName,lastName,email,password,profilePicture,mobileNumber,deviceId;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^" +
            "(?=.*[0-9])" +         //at least 1 digit
            // "(?=.*[a-z])" +         //at least 1 lower case letter
            // "(?=.*[A-Z])" +         //at least 1 upper case letter
            "(?=.*[a-zA-Z])" +      //any letter
            //"(?=.*[@#$%^&+=])" +    //at least 1 special character
            //"(?=S+$)" +           //no white spaces
            ".{6,}" +               //at least 6 characters
            "$");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_login = (Button) findViewById(R.id.buttonLogin);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("User");
        firebaseAuth=FirebaseAuth.getInstance();

        txt_emailLayout = (TextInputLayout) findViewById(R.id.textInputLayoutLoginEmail);
        txt_passwordLayout = (TextInputLayout) findViewById(R.id.textInputLayoutLoginPassword);

        txt_email=(TextInputEditText)findViewById(R.id.textInputEditTextLoginEmail);
        txt_password=(TextInputEditText)findViewById(R.id.textInputEditTextLoginPassword);

        txt_emailLayout.setErrorTextAppearance(R.style.error_style);
        txt_passwordLayout.setErrorTextAppearance(R.style.error_style);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = txt_email.getText().toString().trim();
                password = txt_password.getText().toString().trim();
                if (!validateEmail() | !validatePassword()) {
                    return;
                }else {
                    firebaseAuth.fetchSignInMethodsForEmail(email)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    List<String> signInMethods = task.getResult().getSignInMethods();
                                    if (signInMethods != null && !signInMethods.isEmpty()) {
                                        getUserData(email);
                                        login(email, password);
                                    } else {
                                        int blue = Color.parseColor("#1CD3EC");
                                        new StyleableToast.Builder(getApplicationContext())
                                                .text("User not found, Please check your email or password")
                                                .backgroundColor(blue)
                                                .solidBackground()
                                                .textColor(Color.WHITE)
                                                .gravity(Gravity.TOP)
                                                .cornerRadius(50)
                                                .textSize(12)
                                                .show();
                                    }
                                } else {
                                    int blue = Color.parseColor("#1CD3EC");
                                    new StyleableToast.Builder(getApplicationContext())
                                            .text("User not found, Please check your email or password")
                                            .backgroundColor(blue)
                                            .solidBackground()
                                            .textColor(Color.WHITE)
                                            .gravity(Gravity.TOP)
                                            .cornerRadius(50)
                                            .textSize(12)
                                            .show();

                                }
                            });
                }
            }
        });

        txt_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence searchText, int start, int before, int count) {
                txt_emailLayout.setError(null);
                txt_email.setBackground(getDrawable(R.drawable.textinputedittext_background));

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        txt_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence searchText, int start, int before, int count) {
                txt_passwordLayout.setError(null);
                txt_password.setBackground(getDrawable(R.drawable.textinputedittext_background));

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void getUserData(String loginEmail)
    {
        databaseReference.orderByChild("email").equalTo(loginEmail).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshotUser) {

                for (DataSnapshot child: dataSnapshotUser.getChildren()) {

                    userId = child.getKey();
                    databaseReference.child(userId).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if (dataSnapshot.child("userId").exists()) {

                                firstName = dataSnapshot.child("firstName").getValue().toString();
                                lastName = dataSnapshot.child("lastName").getValue().toString();
                                mobileNumber = dataSnapshot.child("mobileNumber").getValue().toString();
                                profilePicture = dataSnapshot.child("profilePicture").getValue().toString();
                                email = dataSnapshot.child("email").getValue().toString();
                                deviceId = dataSnapshot.child("deviceId").getValue().toString();

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }

            }
            @Override
            public void onCancelled(DatabaseError databaseErrorUser) {

            }
        });


    }
    private void login(String loginEmail,String loginPassword)
    {
        firebaseAuth.signInWithEmailAndPassword(loginEmail,loginPassword).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    LoginSession sessionManagement =new LoginSession(getApplicationContext());
                    sessionManagement.writeLoginSession(userId,firstName,lastName,email,profilePicture,mobileNumber,deviceId);
                    startActivity(intent);

                }
                else {
                    int blue = Color.parseColor("#1CD3EC");
                    new StyleableToast.Builder(getApplicationContext())
                            .text("Please check your email or password")
                            .backgroundColor(blue)
                            .solidBackground()
                            .textColor(Color.WHITE)
                            .gravity(Gravity.TOP)
                            .cornerRadius(50)
                            .textSize(12)
                            .show();
                }
            }
        });

    }
    private boolean validateEmail() {

        if (TextUtils.isEmpty(txt_email.getText())) {
            txt_emailLayout.setError("Email cannot be empty");
            return false;
        } else {
            txt_emailLayout.setError(null);
            return true;
        }

    }
    private boolean validatePassword() {

        if (TextUtils.isEmpty(txt_password.getText())) {
            txt_passwordLayout.setError("Password cannot be empty");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(txt_password.getText()).matches()) {
            txt_passwordLayout.setError("Password must be minimum of six characters, with atleast a letter and a number");
            return false;
        } else {
            txt_passwordLayout.setError(null);
            return true;
        }

    }
}