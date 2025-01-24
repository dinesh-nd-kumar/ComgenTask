package com.example.comgentask;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);



        // Root layout
        LinearLayout rootLayout = new LinearLayout(this);
        rootLayout.setOrientation(LinearLayout.VERTICAL);
        rootLayout.setBackgroundColor(Color.parseColor("#FCE4EC")); // Light pink background
        rootLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        rootLayout.setPadding(20, 20, 20, 20);

        // Logo
        TextView logo = new TextView(this);
        logo.setText("simplr LOTTE");
        logo.setTextSize(24);
        logo.setTypeface(Typeface.DEFAULT_BOLD);
        logo.setGravity(Gravity.CENTER);
        logo.setTextColor(Color.parseColor("#D32F2F"));
        LinearLayout.LayoutParams logoParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        logoParams.setMargins(0, 20, 0, 20);
        rootLayout.addView(logo, logoParams);

        // Time and Date
        TextView timeDate = new TextView(this);
        timeDate.setText("11:32 AM | Wed, 8 Jan 2025");
        timeDate.setGravity(Gravity.CENTER);
        timeDate.setTextSize(16);
        timeDate.setTextColor(Color.BLACK);
        LinearLayout.LayoutParams timeDateParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        timeDateParams.setMargins(0, 20, 0, 20);
        rootLayout.addView(timeDate, timeDateParams);

//        Shape shape =

        // Username field
        EditText username = new EditText(this);
        username.setHint("THL001");
        username.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_menu_myplaces, 0, 0, 0);
        username.setPadding(20, 20, 20, 20);
        username.setBackgroundColor(Color.WHITE);
        LinearLayout.LayoutParams usernameParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        usernameParams.setMargins(0, 20, 0, 20);
        rootLayout.addView(username, usernameParams);

        // Password field
        EditText password = new EditText(this);
        password.setHint("Password");
        password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        password.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_lock_lock, 0, 0, 0);
        password.setPadding(20, 20, 20, 20);
        password.setBackgroundColor(Color.WHITE);
        LinearLayout.LayoutParams passwordParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        passwordParams.setMargins(0, 20, 0, 20);
        rootLayout.addView(password, passwordParams);

        // Forgot Password link
        TextView forgotPassword = new TextView(this);
        forgotPassword.setText("Forgot Password?");
        forgotPassword.setTextColor(Color.BLUE);
        forgotPassword.setGravity(Gravity.END);
        forgotPassword.setPadding(0, 10, 0, 20);
        LinearLayout.LayoutParams forgotPasswordParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        forgotPasswordParams.setMargins(0, 20, 0, 20);
        rootLayout.addView(forgotPassword, forgotPasswordParams);

        // Login button
        Button loginButton = new Button(this);
        loginButton.setText("GO");
        loginButton.setBackgroundColor(Color.parseColor("#D32F2F"));
        loginButton.setTextColor(Color.WHITE);
        LinearLayout.LayoutParams loginButtonParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        loginButtonParams.setMargins(0, 20, 0, 20);
        rootLayout.addView(loginButton, loginButtonParams);

        // Last Sync info
        TextView lastSync = new TextView(this);
        lastSync.setText("Last Sync: 08-01-2025 12:26:45");
        lastSync.setTextSize(14);
        lastSync.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams lastSyncParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lastSyncParams.setMargins(0, 20, 0, 20);
        rootLayout.addView(lastSync, lastSyncParams);

        // Sync buttons
        LinearLayout syncLayout = new LinearLayout(this);
        syncLayout.setOrientation(LinearLayout.HORIZONTAL);
        syncLayout.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams syncLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        syncLayoutParams.setMargins(0, 20, 0, 20);

        Button syncButton1 = new Button(this);
        syncButton1.setText("Sync");
        syncLayout.addView(syncButton1);

        Button syncButton2 = new Button(this);
        syncButton2.setText("Refresh");
        syncLayout.addView(syncButton2);

        rootLayout.addView(syncLayout, syncLayoutParams);

        // Footer info
        TextView footerInfo = new TextView(this);
        footerInfo.setText("MDT No: THL01 | Version: 4.2.4.28\nConfig Version: 3.2.15\nSimplr Solution \u00A9 2019");
        footerInfo.setGravity(Gravity.CENTER);
        footerInfo.setTextSize(12);
        LinearLayout.LayoutParams footerInfoParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        footerInfoParams.setMargins(0, 20, 0, 20);
        rootLayout.addView(footerInfo, footerInfoParams);

        // Product image
        ImageView productImage = new ImageView(this);
        productImage.setImageResource(R.drawable.lotte_chocopie);
        productImage.setAdjustViewBounds(true);
        LinearLayout.LayoutParams productImageParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        productImageParams.setMargins(0, 20, 0, 20);
        rootLayout.addView(productImage, productImageParams);

        setContentView(rootLayout);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity2.class));
            }
        });


    }
}