package edu.uga.cs.ridesharing;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextName, editTextEmail, editTextPassword;
    private Button registerButton;
    private ProgressDialog loadingBar;

    private FirebaseAuth mAuth;
    private DatabaseReference usersDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        mAuth = FirebaseAuth.getInstance();
        usersDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users");

        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        registerButton = findViewById(R.id.register_button);
        loadingBar = new ProgressDialog(this);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });
    }

    private void createAccount() {
        final String name = editTextName.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            showErrorDialog("Please enter your name");
            return;
        }

        if (TextUtils.isEmpty(email)) {
            showErrorDialog("Please enter your email");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            showErrorDialog("Please enter your password");
            return;
        }

        // Validate email format using a regular expression
        if (!isValidEmail(email)) {
            showErrorDialog("Please enter a valid email address");
            return;
        }

        loadingBar.setTitle("Creating Account");
        loadingBar.setMessage("Please wait while we create your account");
        loadingBar.setCanceledOnTouchOutside(true);
        loadingBar.show();

        // Check if the email is already registered
        mAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                if (task.isSuccessful()) {
                    SignInMethodQueryResult result = task.getResult();
                    if (result != null && result.getSignInMethods() != null && result.getSignInMethods().size() > 0) {
                        // Email is already registered
                        loadingBar.dismiss();
                        showErrorDialog("This email is already registered");
                    } else {
                        // Email is not registered, proceed with account creation
                        mAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        loadingBar.dismiss();
                                        if (task.isSuccessful()) {
                                            String currentUserID = mAuth.getCurrentUser().getUid();
                                            DatabaseReference currentUserRef = usersDatabaseRef.child(currentUserID);
                                            currentUserRef.child("name").setValue(name);

                                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            showErrorDialog("Error: " + task.getException().getMessage());
                                        }
                                    }
                                });
                    }
                } else {
                    loadingBar.dismiss();
                    showErrorDialog("Error: " + task.getException().getMessage());
                }
            }
        });
    }

    // Method to validate email format using regular expression
    private boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
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
