package edu.uga.cs.ridesharing;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // Find the login button
        Button loginButton = findViewById(R.id.login_button);

        // Set click listener for the login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the CustomersMapActivity
                Intent intent = new Intent(LoginActivity.this, Customer_Map.class);
                startActivity(intent);
            }
        });

        // Find the TextView representing the "Register now!" link
        TextView registerLink = findViewById(R.id.register_link);

        // Set click listener for the "Register now!" link
        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the RegisterActivity
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}