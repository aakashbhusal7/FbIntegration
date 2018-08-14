package com.example.aakash.samplefblogin;

import android.content.Intent;
import android.os.Bundle;
import android.se.omapi.Session;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private TextView txt_name, txt_email;
    private ImageView imageView;
    String name, email;
    Button logoutButton;
    LoginButton loginButton;
    private AccessToken mAccesssToken;
    private ProfileTracker profileTracker;

    private CallbackManager callbackManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.photoImageView);
        txt_name = findViewById(R.id.nameTextView);
        txt_email = findViewById(R.id.emailTextView);
        logoutButton=findViewById(R.id.logout_button);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout(view);
            }
        });

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                if (currentProfile != null) {
                    displayProfileInfo(currentProfile);
                }
            }
        };
        if (AccessToken.getCurrentAccessToken() == null) {
            switchLoginScreen();
        } else {
            requestEmail(AccessToken.getCurrentAccessToken());
            Profile profile = Profile.getCurrentProfile();
            if (profile != null) {
                displayProfileInfo(profile);
            } else {
                Profile.fetchProfileForCurrentAccessToken();
            }
        }
    }

    private void requestEmail(AccessToken currentAccessToken) {

        GraphRequest request = GraphRequest.newMeRequest(currentAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                if (response.getError() != null) {
                    Toast.makeText(getApplicationContext(), response.getError().getErrorMessage(), Toast.LENGTH_LONG).show();
                    return;
                }
                try {
                    String email = object.getString("email");
                    setEmail(email);
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,first_name, last_name, email");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void setEmail(String email) {
        txt_email.setText(email);
    }

    private void switchLoginScreen() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void logout(View view) {
        if (AccessToken.getCurrentAccessToken() == null) {
            return;
        }
        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse response) {
                LoginManager.getInstance().logOut();
                switchLoginScreen();
            }
        }).executeAsync();
    }
//        LoginManager.getInstance().logOut();
//        Session
//
//        switchLoginScreen();
//    }

    private void displayProfileInfo(Profile profile) {
        String id = profile.getId();
        String name = profile.getName();
        String photo = profile.getProfilePictureUri(100, 100).toString();
        txt_name.setText(name);
        Glide.with(getApplicationContext()).load(photo).into(imageView);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        profileTracker.stopTracking();
    }

    //    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        callbackManager.onActivityResult(requestCode,resultCode,data);
//}
}