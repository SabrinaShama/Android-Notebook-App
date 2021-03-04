package com.example.sabrina.samplenotebook;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.UTFDataFormatException;


public class WritingActivity extends ActionBarActivity {

    private EditText mEtTitle;
    private EditText mEtContent;

    private String mNoteFileName;
    private Note mLoadedNote;

    ImageView imageView;
    private static final int PICK_IMAGE = 100;
    Uri imageUri;

    FloatingActionButton butt_plus, butt_save, butt_delete, butt_addimage;
    Animation buttopen,buttclose, buttRclock, buttRanticlock;
    boolean isOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing);

        mEtTitle = (EditText)findViewById(R.id.note_et_title);
        mEtContent = (EditText)findViewById(R.id.note_et_content);

        mNoteFileName = getIntent().getStringExtra("NOTE_FILE");
        if(mNoteFileName != null && !mNoteFileName.isEmpty()){
            mLoadedNote = Utilities.getNoteByName(this, mNoteFileName);

            if(mLoadedNote != null){
                mEtTitle.setText(mLoadedNote.getmTitle());
                mEtContent.setText(mLoadedNote.getmContent());
            }
        }

        imageView = (ImageView)findViewById(R.id.imageView);

        butt_plus = (FloatingActionButton) findViewById(R.id.button_plus);
        butt_save = (FloatingActionButton)findViewById(R.id.button_save);
        butt_delete = (FloatingActionButton) findViewById(R.id.button_delete);
        butt_addimage = (FloatingActionButton)findViewById(R.id.button_addimage);
        buttopen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_open);
        buttclose = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.button_close);
        buttRclock = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_clockwise);
        buttRanticlock = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_anticlockwise);
        butt_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOpen){
                    butt_addimage.startAnimation(buttclose);
                    butt_save.startAnimation(buttclose);
                    butt_delete.startAnimation(buttclose);
                    butt_plus.startAnimation(buttRanticlock);
                    butt_delete.setClickable(false);
                    butt_save.setClickable(false);
                    butt_addimage.setClickable(false);
                    isOpen = false;
                }
                else{
                    butt_addimage.startAnimation(buttopen);
                    butt_save.startAnimation(buttopen);
                    butt_delete.startAnimation(buttopen);
                    butt_plus.startAnimation(buttRclock);
                    butt_delete.setClickable(true);
                    butt_save.setClickable(true);
                    butt_addimage.setClickable(true);
                    isOpen = true;
                }
            }
        });

        butt_addimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
    }

    private void openGallery(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE)
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
    }

    public void onSaveClick(View v) {
        if (v.getId() == R.id.button_save) {
            saveNote();
        }
    }

    private void saveNote(){
        Note note;

        if(mEtTitle.getText().toString().trim().isEmpty() || mEtContent.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "Please enter a title and a content", Toast.LENGTH_SHORT).show();
            return;
        }

        if(mLoadedNote == null){
            note = new Note(System.currentTimeMillis(), mEtTitle.getText().toString(),
                    mEtContent.getText().toString());
        } else {
            note = new Note(mLoadedNote.getmDateTime(), mEtTitle.getText().toString(),
                    mEtContent.getText().toString());
        }

        if(Utilities.saveNote(this, note)){
            Toast.makeText(this, "Your note is saved!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Can not save the note,please make sure you have enough space on your device", Toast.LENGTH_SHORT).show();
        }

        finish();
    }

    public void onDeleteClick(View v) {
        if (v.getId() == R.id.button_delete) {
            deleteNote();
        }
    }

    private void deleteNote(){
        if(mLoadedNote == null){
            finish();
        } else{
            AlertDialog.Builder dialog = new AlertDialog.Builder(this)
                    .setTitle("delete")
                    .setMessage("You are about to delete " + mEtTitle.getText().toString() + ", are you sure ?")
                    .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Utilities.deleteNote(getApplicationContext(), mLoadedNote.getmDateTime() + Utilities.FILE_EXTENSION);
                            Toast.makeText(getApplicationContext(), mEtTitle.getText().toString() + " is deleted", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    })
                    .setNegativeButton("no", null)
                    .setCancelable(false);

            dialog.show();

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_writing, menu);
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
