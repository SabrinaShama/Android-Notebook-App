package com.example.sabrina.samplenotebook;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;


public class SignUpActivity extends ActionBarActivity {

    private EditText name, email, uname, pass1, pass2;
    private String namestr, emailstr, unamestr, pass1str, pass2str;
    DatabaseHelper helper = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

    }

    public void onSignBtnClick(View v){
        if (v.getId() == R.id.SignBtn){
            register();
        }
    }

    public void register(){
        if(!validate()){
            Toast.makeText(this,"Sign up has failed",Toast.LENGTH_SHORT).show();
        }
        else{
            Intent log = new Intent(SignUpActivity.this,MainActivity.class);

            startActivity(log);

            Contact c = new Contact();
            c.setName(namestr);
            c.setEmail(emailstr);
            c.setUname(unamestr);
            c.setPass(pass1str);

            helper.insertContact(c);
        }
    }
    public boolean validate(){
        boolean valid = true;
        name = (EditText)findViewById(R.id.InName);
        email = (EditText)findViewById(R.id.InEmail);
        uname = (EditText)findViewById(R.id.InUser);
        pass1 = (EditText)findViewById(R.id.InPass);
        pass2 = (EditText)findViewById(R.id.ConPass);

        namestr = name.getText().toString().trim();
        emailstr = email.getText().toString().trim();
        unamestr = uname.getText().toString().trim();
        pass1str = pass1.getText().toString().trim();
        pass2str = pass2.getText().toString().trim();

        if(namestr.isEmpty()||namestr.length()>32){
            name.setError("Please enter valid name!");
            valid = false;
        }
        if(emailstr.isEmpty()||!Patterns.EMAIL_ADDRESS.matcher(emailstr).matches()){
            email.setError("Please enter valid Email address!");
            valid = false;
        }
        if(unamestr.isEmpty()){
            uname.setError("Please enter valid username!");
            valid = false;
        }
        if(pass1str.isEmpty()){
            pass1.setError("Please enter valid password!");
            valid = false;
        }
        if(pass2str.isEmpty()){
            pass2.setError("Please enter matching password!");
            valid = false;
        }
        if(!pass1str.equals(pass2str)){
            Toast pass = Toast.makeText(SignUpActivity.this,"Passwords don't match",Toast.LENGTH_SHORT);
            pass.show();
            valid = false;
        }
        return valid;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
