package com.company.automaticfishfeederapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import io.github.muddz.styleabletoast.StyleableToast;

public class CreateAccountActivity extends AppCompatActivity {

    private Button btn_createAccount;
    private TextInputLayout txt_firstNameLayout, txt_lastNameLayout,txt_emailLayout, txt_passwordLayout,txt_deviceIdLayout;
    private TextInputEditText txt_firstName, txt_lastName,txt_email,txt_password,txt_deviceId;
    private String userId,firstName,lastName,email,password,profilePicture,deviceId;
    private DatabaseReference databaseReference,databaseReferenceDefaultData;
    private FirebaseAuth firebaseAuth;

    private String image = "";
    private StorageTask uploadTask;
    private StorageReference storageReference;

    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z ]+$"); //any letter

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
        setContentView(R.layout.activity_create_account);

        storageReference = FirebaseStorage.getInstance().getReference().child("Profile Picture");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("User");
        databaseReferenceDefaultData = FirebaseDatabase.getInstance().getReference();
        firebaseAuth=FirebaseAuth.getInstance();

        btn_createAccount = (Button) findViewById(R.id.buttonCreateAccount);
        txt_firstNameLayout = (TextInputLayout) findViewById(R.id.textInputLayoutFirstName);
        txt_lastNameLayout = (TextInputLayout) findViewById(R.id.textInputLayoutLastName);
        txt_emailLayout = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        txt_passwordLayout = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);
        txt_deviceIdLayout = (TextInputLayout) findViewById(R.id.textInputLayoutDeviceId);

        txt_firstName=(TextInputEditText)findViewById(R.id.textInputEditTextFirstName);
        txt_lastName=(TextInputEditText)findViewById(R.id.textInputEditTextLastName);
        txt_email=(TextInputEditText)findViewById(R.id.textInputEditTextEmail);
        txt_password=(TextInputEditText)findViewById(R.id.textInputEditTextPassword);
        txt_deviceId=(TextInputEditText)findViewById(R.id.textInputEditTextDeviceId);

        txt_firstNameLayout.setErrorTextAppearance(R.style.error_style);
        txt_lastNameLayout.setErrorTextAppearance(R.style.error_style);
        txt_emailLayout.setErrorTextAppearance(R.style.error_style);
        txt_passwordLayout.setErrorTextAppearance(R.style.error_style);
        txt_deviceIdLayout.setErrorTextAppearance(R.style.error_style);
        btn_createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                firstName = txt_firstName.getText().toString().trim();
                lastName = txt_lastName.getText().toString().trim();
                email = txt_email.getText().toString().trim();
                password = txt_password.getText().toString().trim();
                deviceId = txt_deviceId.getText().toString().trim();

                if (!validateFirstName() | !validateLastName() | !validateEmail() | !validatePassword()|!validateDeviceId()) {
                    return;
                }else {
                    firebaseAuth.fetchSignInMethodsForEmail(email)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    List<String> signInMethods = task.getResult().getSignInMethods();
                                    if (signInMethods != null && !signInMethods.isEmpty()) {
                                        int blue = Color.parseColor("#1CD3EC");
                                        new StyleableToast.Builder(getApplicationContext())
                                                .text("User already exists")
                                                .backgroundColor(blue)
                                                .solidBackground()
                                                .textColor(Color.WHITE)
                                                .gravity(Gravity.TOP)
                                                .cornerRadius(50)
                                                .textSize(12)
                                                .show();

                                    } else {
                                        createAccount(firstName, lastName, email, password, deviceId);

                                    }
                                } else {
                                    // An error occurred while checking if the user exists

                                }
                            });
                }
            }
        });


        txt_firstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence searchText, int start, int before, int count) {
                txt_firstNameLayout.setError(null);
                txt_firstName.setBackground(getDrawable(R.drawable.textinputedittext_background));

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        txt_lastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence searchText, int start, int before, int count) {
                txt_lastNameLayout.setError(null);
                txt_lastName.setBackground(getDrawable(R.drawable.textinputedittext_background));

            }

            @Override
            public void afterTextChanged(Editable s) {

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
        txt_deviceId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence searchText, int start, int before, int count) {
                txt_deviceIdLayout.setError(null);
                txt_deviceId.setBackground(getDrawable(R.drawable.textinputedittext_background));

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void createAccount(String firstName, String lastName, String email,String password,String deviceId)
    {
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(CreateAccountActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            FirebaseUser refUser = firebaseAuth.getCurrentUser();
                            userId = refUser.getUid();
                            Uri imageUrl = Uri.parse("android.resource://com.company.automaticfishfeederapp/"+R.drawable.common_profile_picture);
                            final StorageReference fileRef = storageReference.child(userId + ".jpg");

                            uploadTask = fileRef.putFile(imageUrl);
                            uploadTask.continueWithTask(new Continuation() {
                                @Override
                                public Object then(@NonNull Task task) throws Exception
                                {
                                    if (!task.isSuccessful())
                                    {
                                        throw task.getException();
                                    }

                                    return fileRef.getDownloadUrl();
                                }
                            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task)
                                {
                                    Uri downloadUrl = task.getResult();
                                    image = downloadUrl.toString();
                                    HashMap<String, Object> hashMap = new HashMap<>();
                                    hashMap.put("firstName", firstName);
                                    hashMap.put("lastName", lastName);
                                    hashMap.put("email", email);
                                    hashMap.put("deviceId", deviceId);
                                    hashMap.put("userId", userId);
                                    hashMap.put("mobileNumber", "");
                                    hashMap.put("profilePicture", image);

                                    databaseReference.child(userId).setValue(hashMap);
                                    defaultDataSeed(deviceId);
                                    Intent intent = new Intent(CreateAccountActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                }
                            });


                        }
                        else {
                                //error message
                        }
                    }
                });

    }

    private void defaultDataSeed(String deviceId)
    {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("deviceId", deviceId);
        hashMap.put("triggerValue", 0);

        databaseReferenceDefaultData.child("WaterChange").child(deviceId).setValue(hashMap);
        databaseReferenceDefaultData.child("FishFeeding").child(deviceId).setValue(hashMap);

        HashMap<String, Object> sensorData = new HashMap<>();
        sensorData.put("deviceId", deviceId);
        sensorData.put("phValue", "0.0");
        sensorData.put("temp", 0);
        sensorData.put("waterLevel", 0);

        databaseReferenceDefaultData.child("SensorData").child(deviceId).setValue(sensorData);

    }
    private boolean validateFirstName() {

        if (TextUtils.isEmpty(txt_firstName.getText())) {
            txt_firstNameLayout.setError("First name cannot be empty");
            return false;
        } else if (txt_firstName.length()>10) {
            txt_firstNameLayout.setError("You have exceeded the maximum character limit of 10");
            return false;
        } else if (!NAME_PATTERN.matcher(txt_firstName.getText()).matches()) {
            txt_firstNameLayout.setError("Invalid first name");
            return false;
        } else {
            txt_firstNameLayout.setError(null);
            return true;
        }

    }
    private boolean validateLastName() {

        if (TextUtils.isEmpty(txt_lastName.getText())) {
            txt_lastNameLayout.setError("Last name cannot be empty");
            return false;
        }else if (txt_lastName.length()>20) {
            txt_lastNameLayout.setError("You have exceeded the maximum character limit of 20");
            return false;
        } else if (!NAME_PATTERN.matcher(txt_lastName.getText()).matches()) {
            txt_lastNameLayout.setError("Invalid first name");
            return false;
        } else {
            txt_lastNameLayout.setError(null);
            return true;
        }

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
    private boolean validateDeviceId() {

        if (TextUtils.isEmpty(txt_deviceId.getText())) {
            txt_deviceIdLayout.setError("Device Id cannot be empty");
            return false;
        } else {
            txt_deviceIdLayout.setError(null);
            return true;
        }

    }
}