package edu.uga.cs.ridesharing;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword;
    private Button loginButton;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        mAuth = FirebaseAuth.getInstance();

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        loginButton = findViewById(R.id.login_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        // Find the TextView representing the "Register now!" link
        findViewById(R.id.register_link).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the RegisterActivity
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    private void loginUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            showErrorDialog("Please enter your email");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            showErrorDialog("Please enter your password");
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Login success, start the Customer_Map activity
                            startActivity(new Intent(LoginActivity.this, Customer_Map.class));
                            finish();
                        } else {
                            // Login failed, display appropriate error message
                            Exception exception = task.getException();
                            if (exception instanceof FirebaseAuthInvalidUserException) {
                                String errorCode = ((FirebaseAuthInvalidUserException) exception).getErrorCode();
                                if (errorCode.equals("ERROR_USER_NOT_FOUND")) {
                                    showErrorDialog("User not found. Please check your email.");
                                } else {
                                    showErrorDialog("Invalid email address. Please check your email.");
                                }
                            } else if (exception instanceof FirebaseAuthInvalidCredentialsException) {
                                String errorCode = ((FirebaseAuthInvalidCredentialsException) exception).getErrorCode();
                                if (errorCode.equals("ERROR_WRONG_PASSWORD")) {
                                    showErrorDialog("Invalid password. Please check your password.");
                                } else {
                                    showErrorDialog("Invalid credentials. Please check your email and password.");
                                }
                            } else {
                                showErrorDialog("Login failed. Please try again later.");
                            }
                        }
                    }
                });
    }

    // Helper method to display custom error dialog
    private void showErrorDialog(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing, just close the dialog
                    }
                })
                .show();
    }
}
