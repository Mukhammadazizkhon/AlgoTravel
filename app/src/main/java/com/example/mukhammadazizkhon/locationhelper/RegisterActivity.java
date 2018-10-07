package com.example.mukhammadazizkhon.locationhelper;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity
{

    Button directToLoginButton,registerButton;
    EditText userEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initializeComponents();
        // if the user has already the application account
        directToLoginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(loginIntent);
            }
        });
        // requesting the registration process to the server here
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(isUserConnectedToNetwork()){
                    String mail = userEmail.getText().toString();
                    boolean isCorrectEmail = isValidEmail(mail);
                    if(isCorrectEmail){
                        Toast.makeText(RegisterActivity.this, "Correct EMail", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(RegisterActivity.this, "No Correct EMail", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(RegisterActivity.this, "Network is required for Registration", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void initializeComponents()
    {
        directToLoginButton = (Button)findViewById(R.id.directToLoginButton);
        registerButton = (Button)findViewById(R.id.buttonSignUp);
        userEmail = (EditText)findViewById(R.id.email);
    }

    private boolean isValidEmail(String userEmail)
    {
        String regexExpression = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";
        CharSequence inputString = userEmail;
        Pattern pattern = Pattern.compile(regexExpression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputString);
        if(matcher.matches()){
            return true;
        }
        else{
            return false;
        }
    }

    private boolean isUserConnectedToNetwork(){
        ConnectivityManager connectivityManager =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo!=null && activeNetworkInfo.isConnected();
    }
}
