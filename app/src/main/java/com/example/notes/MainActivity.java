package com.example.notes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    SharedPreferences sharedPreferences;
    static ArrayList<String> notes;
    static ArrayAdapter<String> arrayAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId()==R.id.newId)
        {
                notes.add("");
                Intent intent=new Intent(getApplicationContext(),NotesDescription.class);
                intent.putExtra("position",notes.size()-1);
                startActivity(intent);
                return true;
        }
        return false;
    }

    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView=(ListView)findViewById(R.id.listView);
        notes=new ArrayList<String>();
        sharedPreferences=this.getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);
        try {
            notes=(ArrayList<String>)ObjectSerializer.deserialize(sharedPreferences.getString("notes",new ArrayList<String>().toString()));
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        if(notes.size()==0)
        {
            notes.add("Example note");
        }
        //notes.add("Example note");
        arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,notes);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getApplicationContext(),NotesDescription.class);
                intent.putExtra("position",position);
                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.star_on)
                        .setTitle("Are you sure?")
                        .setMessage("Do you want to delete this")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                notes.remove(position);
                                arrayAdapter.notifyDataSetChanged();
                                try {
                                    sharedPreferences.edit().putString("notes",ObjectSerializer.serialize(MainActivity.notes)).apply();
                                }
                                catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .setNegativeButton("NO",null)
                        .show();
                return true;
            }
        });
    }
}
