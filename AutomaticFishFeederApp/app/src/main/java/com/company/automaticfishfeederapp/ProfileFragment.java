package com.company.automaticfishfeederapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button btn_logout;
    private FloatingActionButton btn_editMyProfile;
    private TextView txt_deviceId,txt_mobileNumber,txt_email,txt_fullName;
    private CircleImageView img_profilePicture;
    private String userId,firstName,lastName,email,profilePicture,deviceId,mobileNumber;
    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment profile.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile, container, false);

        img_profilePicture = (CircleImageView) view.findViewById(R.id.myProfileUserProfilePicture);
        txt_fullName = (TextView) view.findViewById(R.id.myProfileUserFullName);
        txt_email = (TextView) view.findViewById(R.id.myProfileUserEmail);
        txt_mobileNumber = (TextView) view.findViewById(R.id.myProfileUserMobileNumber);
        txt_deviceId = (TextView) view.findViewById(R.id.myProfileUserDeviceId);

        btn_editMyProfile = (FloatingActionButton) view.findViewById(R.id.buttonEditMyProfile);
        btn_logout = (Button) view.findViewById(R.id.buttonLogOut);


        LoginSession sessionManagement =new LoginSession(getContext());
        HashMap<String, String> user = sessionManagement.readLoginSession();
        firstName = user.get(LoginSession.KEY_FIRSTNAME);
        lastName = user.get(LoginSession.KEY_LASTNAME);
        email = user.get(LoginSession.KEY_EMAIL);
        deviceId = user.get(LoginSession.KEY_DEVICEID);
        mobileNumber = user.get(LoginSession.KEY_MOBILENUMBER);
        profilePicture = user.get(LoginSession.KEY_PROFILEPICTURE);

        txt_fullName.setText(firstName+" "+lastName);
        txt_email.setText(email);
        if(mobileNumber=="")
        {
            txt_mobileNumber.setText("N/A");
        }else {
            txt_mobileNumber.setText(mobileNumber);
        }
        txt_deviceId.setText(deviceId);

        btn_editMyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
        return view;
    }

    private void logout() {

        //logout user and clear all session data
        LoginSession loginSession=new LoginSession(getContext());
        loginSession.logoutUser();

    }

}