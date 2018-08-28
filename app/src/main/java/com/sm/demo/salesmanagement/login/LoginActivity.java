package com.sm.demo.salesmanagement.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sm.demo.salesmanagement.HomeActivity;
import com.sm.demo.salesmanagement.R;
import com.sm.demo.salesmanagement.users.UsersActivity;
import com.sm.demo.salesmanagement.users.UsersModel;

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "LoginActivity";
    private Button button;
    private EditText userName, passWord;
    private ImageView imageView;
    private TextView textView;
    private LoginService loginService;


    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.loginService = new LoginService(LoginActivity.this);

        this.textView = (TextView)findViewById(R.id.for_registration);
        this.imageView = (ImageView) this.findViewById(R.id.login_photo);
        this.userName = (EditText) this.findViewById(R.id.login_username);
        this.passWord = (EditText) this.findViewById(R.id.login_password);
        this.button = (Button) this.findViewById(R.id.login_button);


        //===============================================| Create SharedPreferences & Check it |===========================================
        preferences = getSharedPreferences("loginInfo", Context.MODE_PRIVATE); //Create login.xml file in private data folder in your mobile apps
        //Logging time access
        boolean isLoggedIn = preferences.getBoolean("isLoggedIn",false); //Retrieve value by key="isLoggedIn" from login.xml file. False coz if do not get find any value.
        if(isLoggedIn){

            goToHome();
        //===================================================================================================================================

        } else {

            //Do not get true value login.xml file then login using database
            passWord.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) { }
                @Override
                public void afterTextChanged(Editable s) {
                    UsersModel data = loginService.loginByUserPass(userName.getText().toString(), passWord.getText().toString());
                    if (data != null) {
                        try {
                            Log.d(TAG, data.getUsername()+"/"+data.getPassword());
                            imageView.setImageBitmap(BitmapFactory.decodeFile(data.getPhotoPath()+"/"+data.getPhotoName() ));
                            if(data.getUsername().equals("admin")){
                                imageView.setImageResource(getResources().getIdentifier("admin", "drawable", "com.sm.demo.salesmanagement"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                //===============================================| Writes SharedPreferences |===========================================
                                SharedPreferences.Editor editor = preferences.edit(); //Write
                                editor.putBoolean("isLoggedIn", true); //key = isLoggedIn and value="true"
                                editor.putString("userName", userName.getText().toString());
                                editor.putString("userPass", passWord.getText().toString());
                                editor.apply();
                                editor.commit();
                                //======================================================================================================================

                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(intent);
                                Toast.makeText(getApplicationContext(), "Login successfully", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });


        }






        //Need to registration in text-view then display progress bar
        textView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                ProgressDialog progressBar = ProgressDialog.show(LoginActivity.this, "Title", "Be patient!");
                progressBar.setCancelable(true);
                Intent i = new Intent(getApplicationContext(), UsersActivity.class);
                startActivity(i);
                return false;
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    //============================================================| SharedPreferences
    private void goToHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
    //Editor to edit value in xml file at the logging time
    /*public void login(View view) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_LOGGED_IN,true); //key = isLoggedIn and value="true"
        editor.apply();
        //editor. remove();//Remove value using key
        ///editor.clear(); //Remove from login.xml file
        editor.commit();

        boolean isLoggedIn = sharedPreferences.getBoolean(IS_LOGGED_IN,false);
        Toast.makeText(this, "is xml file created? "+isLoggedIn, Toast.LENGTH_SHORT).show();
        login();
    }*/

}
