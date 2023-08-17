package com.company.automaticfishfeederapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditMyProfileActivity extends AppCompatActivity {
    private Button btn_updateMyProfile;
    private FloatingActionButton btn_back,btn_changeImage;
    private TextInputLayout txt_firstNameLayout, txt_lastNameLayout,txt_mobileNumberLayout;
    private TextInputEditText txt_firstName, txt_lastName,txt_mobileNumber;
    private String userId,firstName,lastName,mobileNumber,profilePicture,email,deviceId;
    private DatabaseReference databaseReference;
    private Uri imageUrl;
    private String image = "";
    private StorageTask uploadTask;
    private StorageReference storageReference;
    private CircleImageView img_profilePicture;
    private String checker = "";

    private static final int GalleryPick = 1;
    private static final int SELECT_PICTURE = 200;
    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z ]+$"); //any letter
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_my_profile);

        storageReference = FirebaseStorage.getInstance().getReference().child("Profile Picture");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("User");

        LoginSession sessionManagement =new LoginSession(getApplicationContext());
        HashMap<String, String> user = sessionManagement.readLoginSession();
        userId = user.get(LoginSession.KEY_USERID);
        profilePicture = user.get(LoginSession.KEY_PROFILEPICTURE);
        firstName = user.get(LoginSession.KEY_FIRSTNAME);
        lastName = user.get(LoginSession.KEY_LASTNAME);
        mobileNumber= user.get(LoginSession.KEY_MOBILENUMBER);
        email = user.get(LoginSession.KEY_EMAIL);
        deviceId = user.get(LoginSession.KEY_DEVICEID);

        btn_updateMyProfile = (Button) findViewById(R.id.buttonUpdateEditMyProfile);
        btn_back = (FloatingActionButton) findViewById(R.id.buttonEditMyProfileBack);
        btn_changeImage = (FloatingActionButton) findViewById(R.id.buttonEditMyProfilePicture);

        txt_firstNameLayout = (TextInputLayout) findViewById(R.id.textInputLayoutFirstName);
        txt_lastNameLayout = (TextInputLayout) findViewById(R.id.textInputLayoutEditLastName);
        txt_mobileNumberLayout = (TextInputLayout) findViewById(R.id.textInputLayoutEditMobileNumber);

        txt_firstName=(TextInputEditText)findViewById(R.id.textInputEditTextEditFirstName);
        txt_lastName=(TextInputEditText)findViewById(R.id.textInputEditTextEditLastName);
        txt_mobileNumber=(TextInputEditText)findViewById(R.id.textInputEditTextEditMobileNumber);
        img_profilePicture = (CircleImageView) findViewById(R.id.imageEditMyProfilePicture);

        txt_firstNameLayout.setErrorTextAppearance(R.style.error_style);
        txt_lastNameLayout.setErrorTextAppearance(R.style.error_style);
        txt_mobileNumberLayout.setErrorTextAppearance(R.style.error_style);

        txt_firstName.setText(firstName);
        txt_lastName.setText(lastName);
        txt_mobileNumber.setText(mobileNumber);
        Picasso.get().load(profilePicture).into(img_profilePicture);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditMyProfileActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });

        btn_changeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checker = "clicked";

                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);

            }
        });
        btn_updateMyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                firstName = txt_firstName.getText().toString().trim();
                lastName = txt_lastName.getText().toString().trim();
                mobileNumber = txt_mobileNumber.getText().toString().trim();

                if (!validateFirstName() | !validateLastName() | !validateMobileNumber() ) {
                    return;
                }else {
                    if (checker.equals("clicked")) {
                        uploadProfilePicture(firstName, lastName, mobileNumber, userId);
                    } else {

                        updateOnlyUserInfo(firstName, lastName, mobileNumber, userId);
                    }
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
        txt_mobileNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence searchText, int start, int before, int count) {
                txt_mobileNumberLayout.setError(null);
                txt_mobileNumber.setBackground(getDrawable(R.drawable.textinputedittext_background));

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void updateOnlyUserInfo(String firstName, String lastName, String mobileNumber, String userId) {

        HashMap<String, Object> userMap = new HashMap<>();
        userMap. put("firstName",firstName);
        userMap. put("lastName",lastName);
        userMap. put("mobileNumber",mobileNumber);
        databaseReference.child(userId).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                //Data insert Successfully
                Intent intent = new Intent(EditMyProfileActivity.this, HomeActivity.class);

                LoginSession loginSession =new LoginSession(getApplicationContext());
                loginSession.writeLoginSession(userId,firstName,lastName,email,profilePicture,mobileNumber,deviceId);

                startActivity(intent);

            }
        });



    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                imageUrl = data.getData();
                if (null != imageUrl) {
                    // update the preview image in the layout
                    img_profilePicture.setImageURI(imageUrl);
                }
            }
        }
    }

    private void uploadProfilePicture(String firstName, String lastName, String mobileNumber, String userId)
    {
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
                })
                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task)
                    {
                        Uri downloadUrl = task.getResult();
                        image = downloadUrl.toString();

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("profilePicture", image);
                        hashMap. put("firstName",firstName);
                        hashMap. put("lastName",lastName);
                        hashMap. put("mobileNumber",mobileNumber);

                        databaseReference.child(userId).updateChildren(hashMap);

                        Intent intent = new Intent(EditMyProfileActivity.this, HomeActivity.class);

                        LoginSession loginSession =new LoginSession(getApplicationContext());
                        loginSession.writeLoginSession(userId,firstName,lastName,email,image,mobileNumber,deviceId);

                        startActivity(intent);
                    }
                });

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

    private boolean validateMobileNumber() {

        if (TextUtils.isEmpty(txt_mobileNumber.getText())) {
            txt_mobileNumberLayout.setError("Mobile number cannot be empty");
            return false;
        } else {
            txt_mobileNumberLayout.setError(null);
            return true;
        }

    }
}