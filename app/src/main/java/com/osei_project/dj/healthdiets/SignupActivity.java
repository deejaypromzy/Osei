package com.osei_project.dj.healthdiets;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SignupActivity extends AppCompatActivity {
    private EditText etEmail;
    private EditText cpassword,password;
    final int PERMS_REQUEST_CODE = 123;
    private String nam1,nam2,nam3;
    public SQLiteDatabase db;
    int flag=0;


    // [START declare_auth]
    private FirebaseAuth mAuth;
    private DatabaseReference mref;

    // [END declare_auth]


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        // Views

        etEmail = findViewById(R.id.email);

        password = findViewById(R.id.password);
        cpassword = findViewById(R.id.cpassword);

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        mref = FirebaseDatabase.getInstance().getReference();
        // [END initialize_auth]*/



    }


    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
    // [END on_start_check_user]

    private void createAccount(String email, String password) {
        if (!validateForm()) {
            return;
        }

        showProgressDialog();

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SignupActivity.this, "Failed : Email exist or Check Internet Connections.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END create_user_with_email]
    }

    private boolean validateForm() {
       String myemail = etEmail.getText().toString();
        if (TextUtils.isEmpty(myemail)) {
            etEmail.setError("Email can't be empty..");
            etEmail.requestFocus();
            return false;

        } else {
            etEmail.setError(null);
        }  String mypassword = password.getText().toString();
        if (TextUtils.isEmpty(mypassword)) {
            password.setError("Password can't be empty..");
            password.requestFocus();
            return false;
        } else if(mypassword.length()<6) {
            password.setError("Password must be more than 4 characters");
            password.requestFocus();
            return false;
        } else {
            password.setError(null);
        }
        String mycpassword = cpassword.getText().toString();
        if (TextUtils.isEmpty(mycpassword)) {
            cpassword.setError("Cornfirm password");
            cpassword.requestFocus();
            return false;
        } else if (!mypassword.equals(mycpassword)) {
            cpassword.setError("Password do not match");
            cpassword.requestFocus();
            return false;
        } else {
            cpassword.setError(null);
        }
        return true;
    }

    private void updateUI(FirebaseUser user) {
        hideProgressDialog();
        if (user != null) {
            startActivity(new Intent(SignupActivity.this,Diet.class));
            onBackPressed();

        }
    }
    @VisibleForTesting
    public ProgressDialog mProgressDialog;

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(("Loading..."));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
    }

    public void CreateAccount(View view) {
        createAccount(etEmail.getText().toString(), password.getText().toString());

    }

    public void GoToLogin(View view) {
        finish();
    }

}
