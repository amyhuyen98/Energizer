package com.amyhuyen.energizer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.amyhuyen.energizer.R.id;
import static com.amyhuyen.energizer.R.layout;

public class RegistrationActivity extends AppCompatActivity {

    // the views
    @BindView (id.etEmail) EditText etEmail;
    @BindView (id.etPassword) EditText etPassword;
    @BindView (id.etConfirmPassword) EditText etConfirmPassword;
    @BindView (id.btnRegister) Button btnRegister;
    @BindView (id.tvLogin) TextView tvLogin;
    @BindView (id.etName) EditText etName;
    @BindView (id.tvSignUp) TextView tvSignUp;

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference firebaseData;
    private String userID;
    private CallbackManager callbackManager;
    private String userType;

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            checkFieldsForEmptyValues();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_registration);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseData = FirebaseDatabase.getInstance().getReference();
        callbackManager = CallbackManager.Factory.create();

        progressDialog = new ProgressDialog(this);

        // bind the views
        ButterKnife.bind(this);

        userType = getIntent().getStringExtra(DBKeys.KEY_USER_TYPE);
        if (userType.equals(DBKeys.KEY_VOLUNTEER)){
            tvSignUp.setText(R.string.volunteer_registration);
        } else {
            tvSignUp.setText(R.string.non_profit_registration);
            etName.setHint(R.string.organization_name);
            etEmail.setHint(R.string.organization_email);
        }

        checkFieldsForEmptyValues();

        // set text change listeners for all fields
        etName.addTextChangedListener(mTextWatcher);
        etEmail.addTextChangedListener(mTextWatcher);
        etPassword.addTextChangedListener(mTextWatcher);
        etConfirmPassword.addTextChangedListener(mTextWatcher);
    }

    void checkFieldsForEmptyValues() {
        // get the contents of the edit texts
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)){
            btnRegister.setEnabled(false);
            btnRegister.setClickable(false);
        } else {
            btnRegister.setEnabled(true);
            btnRegister.setClickable(true);
        }

    }


    private void registerUser() {
        final String name = etName.getText().toString().trim();
        final String email = etEmail.getText().toString().trim();
        final String password = etPassword.getText().toString().trim();
        final String confirmPassword = etConfirmPassword.getText().toString().trim();

        // proceed to registering user if passwords match
        if (password.equals(confirmPassword)) {

            // intent to the SetSkills activity
            if (userType.equals(DBKeys.KEY_VOLUNTEER)){
                Intent continueRegistrationIntent = new Intent(getApplicationContext(), VolRegContActivity.class);
                continueRegistrationIntent.putExtra(DBKeys.KEY_NAME, name);
                continueRegistrationIntent.putExtra(DBKeys.KEY_EMAIL, email);
                continueRegistrationIntent.putExtra("Password", password);
                startActivity(continueRegistrationIntent);
                finish();
            } else {
                Intent continueRegistrationIntent = new Intent(getApplicationContext(), NpoRegContActivity.class);
                continueRegistrationIntent.putExtra(DBKeys.KEY_NAME, name);
                continueRegistrationIntent.putExtra(DBKeys.KEY_EMAIL, email);
                continueRegistrationIntent.putExtra("Password", password);
                startActivity(continueRegistrationIntent);
                finish();
            }

        } else {
            // if passwords don't match, alert user using toast
            Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
        }

    }

 

    // on click listener for register button
    @OnClick(id.btnRegister)
    public void onRegisterClick(){
        // register the user
        registerUser();
    }


    // on click listener for login text
    @OnClick (id.tvLogin)
    public void onLoginClick(){
        // intent to login activity
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

            // onActivityResult for Facebook Login
            callbackManager.onActivityResult(requestCode, resultCode, data);

    }

}
