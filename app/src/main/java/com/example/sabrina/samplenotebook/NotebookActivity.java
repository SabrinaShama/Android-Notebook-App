package com.example.sabrina.samplenotebook;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class NotebookActivity extends ActionBarActivity {

    private ListView mListViewNotes;
    FloatingActionButton butt_plus, butt_logout, butt_addnote;
    Animation buttopen, buttclose, buttRclock, buttRanticlock;
    boolean isOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notebook);
        String username = getIntent().getStringExtra("Username");

        TextView tv = (TextView)findViewById(R.id.User);
        tv.setText(username);

        mListViewNotes = (ListView)findViewById(R.id.main_listView_notes);
        butt_plus = (FloatingActionButton) findViewById(R.id.button_plus);
        butt_logout = (FloatingActionButton)findViewById(R.id.button_logout);
        butt_addnote = (FloatingActionButton) findViewById(R.id.button_addnote);
        buttopen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_open);
        buttclose = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.button_close);
        buttRclock = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_clockwise);
        buttRanticlock = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_anticlockwise);
        butt_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOpen){
                    butt_addnote.startAnimation(buttclose);
                    butt_logout.startAnimation(buttclose);
                    butt_plus.startAnimation(buttRanticlock);
                    butt_logout.setClickable(false);
                    butt_addnote.setClickable(false);
                    isOpen = false;
                }
                else{
                    butt_addnote.startAnimation(buttopen);
                    butt_logout.startAnimation(buttopen);
                    butt_plus.startAnimation(buttRclock);
                    butt_logout.setClickable(true);
                    butt_addnote.setClickable(true);
                    isOpen = true;
                }
            }
        });
    }

    public void onNoteClick(View v) {
        if (v.getId() == R.id.button_addnote) {
            Intent note = new Intent(NotebookActivity.this, WritingActivity.class);
            startActivity(note);
        }
    }

    public void onLogoutClick(View v) {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mListViewNotes.setAdapter(null);

        ArrayList<Note> notes = Utilities.getAllSavedNotes(this);

        if(notes == null || notes.size() == 0){
            Toast.makeText(this, "You have no saved notes!", Toast.LENGTH_SHORT).show();
            return;
        } else {
            NoteAdapter na = new NoteAdapter(this, R.layout.item_note, notes);
            mListViewNotes.setAdapter(na);

            mListViewNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String fileName = ((Note)mListViewNotes.getItemAtPosition(position)).getmDateTime()
                            + Utilities.FILE_EXTENSION;

                    Intent viewNoteIntent = new Intent(getApplicationContext(), WritingActivity.class);
                    viewNoteIntent.putExtra("NOTE_FILE", fileName);
                    startActivity(viewNoteIntent);
                }
            });
        }
    }

    @Override
    public void onBackPressed(){

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_notebook, menu);
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
