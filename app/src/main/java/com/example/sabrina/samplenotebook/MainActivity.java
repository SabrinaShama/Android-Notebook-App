package com.example.sabrina.samplenotebook;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    DatabaseHelper helper = new DatabaseHelper(this);

    public void onLogClick(View v) {
         if (v.getId() == R.id.LogBtn) {
             EditText a = (EditText)findViewById(R.id.editUser);
             String str = a.getText().toString();
             EditText b = (EditText)findViewById(R.id.editPass);
             String pass = b.getText().toString();

             String password = helper.searchPass(str);
             if(pass.equals(password)){
                 Intent note = new Intent(MainActivity.this, NotebookActivity.class);
                 note.putExtra("Username",str);
                 startActivity(note);
             }
             else {
                 Toast temp = Toast.makeText(MainActivity.this,"Username and Password don't match",Toast.LENGTH_SHORT);
                 temp.show();
             }
         }
    }

    public void onSignClick(View v) {
        if (v.getId() == R.id.SignText) {
            Intent sign = new Intent(MainActivity.this, SignUpActivity.class);
            startActivity(sign);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String uname = getIntent().getStringExtra("Uname");
        EditText et = (EditText)findViewById(R.id.editUser);
        et.setText(uname);
    }

    public void onBackPressed(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Are you sure you want to do this ?");
        builder.setCancelable(true);
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
