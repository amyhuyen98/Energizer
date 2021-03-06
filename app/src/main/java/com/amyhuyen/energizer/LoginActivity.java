package com.amyhuyen.energizer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amyhuyen.energizer.models.Nonprofit;
import com.amyhuyen.energizer.models.Volunteer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    // the views
    @BindView (R.id.etEmail) EditText etEmail;
    @BindView (R.id.etPassword) EditText etPassword;
    @BindView (R.id.btnLogin) Button btnLogin;
    @BindView (R.id.tvSignUp) TextView tvSignUp;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private DatabaseReference mDBUserRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // bind the views
        ButterKnife.bind(this);
//        BitmapFactory.Options opts = new BitmapFactory.Options();
//        opts.inSampleSize = 4;
//        Bitmap newBitmap = BitmapFactory.decodeFile("/Users/acfoley/Desktop/Energizer/Energizer/app/src/main/res/drawable/skyline.jpg", opts);

        firebaseAuth = FirebaseAuth.getInstance();
        mDBUserRef = FirebaseDatabase.getInstance().getReference().child(DBKeys.KEY_USER);

        progressDialog = new ProgressDialog(this);
    }


    private void userLogin() {

        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // checking if email and password are empty
        if (TextUtils.isEmpty(email)) {
            // email is empty
            Toast.makeText(this, "Please enter email", Toast.LENGTH_LONG).show();
            return;
        } else if (TextUtils.isEmpty(password)) {
            // password is empty
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        // if email and password are not empty, login user
        progressDialog.setMessage("Logging in. Please Wait...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // display successful login message
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Logged In Successfully", Toast.LENGTH_SHORT).show();


                            // find user type then launch intent to landing activity
                            DatabaseReference dataUserRef = FirebaseDatabase.getInstance().getReference().child(DBKeys.KEY_USER).child(firebaseAuth.getCurrentUser().getUid());
                            dataUserRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    HashMap<String, String> userMapping = (HashMap<String, String>) dataSnapshot.getValue();
                                    String UserType = userMapping.get(DBKeys.KEY_USER_TYPE);

                                    // update user data provider
                                    UserDataProvider.getInstance().setCurrentUserType(UserType);
                                    if (UserType.equals(DBKeys.KEY_VOLUNTEER)){
                                        UserDataProvider.getInstance().setCurrentVolunteer(dataSnapshot.getValue(Volunteer.class));
                                    } else{
                                        UserDataProvider.getInstance().setCurrentNPO(dataSnapshot.getValue(Nonprofit.class));
                                    }

                                    // launch intent to landing activity
                                    Intent intent = new Intent(getApplicationContext(), LandingActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    finish();
                                    startActivity(intent);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Log.e("Login User", databaseError.toString());
                                }
                            });
                        } else{
                            progressDialog.dismiss();
                            Log.e("error", task.getException().toString());
                            Toast.makeText(LoginActivity.this, "Could not login, please try again", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                });
    }

    // on click listener for login button
    @OnClick(R.id.btnLogin)
    public void onLoginClick() {
        userLogin();
    }

    // on click listener for signup button
    @OnClick (R.id.tvSignUp)
    public void onSignUpClick(){
        // intent to signup activity
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        finish();
        startActivity(intent);
    }
}
