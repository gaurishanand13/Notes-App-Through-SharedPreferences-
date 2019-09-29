package com.example.notes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

public class NotesDescription extends AppCompatActivity {

    EditText editText;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_description);
        editText=(EditText)findViewById(R.id.editText);
        final SharedPreferences sharedPreferences=this.getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);;
        Intent intent=getIntent();
        final int position=intent.getIntExtra("position",-1);
        if(position!=-1)
        {
            editText.setText(MainActivity.notes.get(position));
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    MainActivity.notes.set(position,String.valueOf(s));
                    MainActivity.arrayAdapter.notifyDataSetChanged();
                    try {
                        sharedPreferences.edit().putString("notes",ObjectSerializer.serialize(MainActivity.notes)).apply();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Something went Wrong",Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
