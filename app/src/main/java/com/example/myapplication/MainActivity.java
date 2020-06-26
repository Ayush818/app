package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.icu.text.Transliterator;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    final List<String> list = new ArrayList<>();
    int[] backgroundColors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ListView listView = findViewById(R.id.listView); //these is how we access the list view
        final TextAdapter adapter = new TextAdapter();

        int maxItems = 100;
        backgroundColors = new int[maxItems];

        for (int i=0;i<maxItems;i++){
            if (i%3==0){
                backgroundColors[i]=Color.LTGRAY;
            }else if (i%3==1){
                backgroundColors[i]=Color.WHITE;
            }else {
                backgroundColors[i]=Color.CYAN;
            }
        }

        readInfo();

        adapter.setData(list, backgroundColors);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Delete this task?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                list.remove(position);
                                adapter.setData(list, backgroundColors);
                                saveInfo();


                            }
            })
                        .setNegativeButton("No", null)
                        .create();
                        dialog.show();

            }
        });

        final Button newTaskButton = findViewById(R.id.newTaskButton);

        newTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText taskInput = new EditText(MainActivity.this);
                taskInput.setSingleLine();
                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Add a new task")
                        .setMessage("what is your new task")
                        .setView(taskInput)
                        .setPositiveButton("Add task", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                list.add(taskInput.getText().toString());
                                adapter.setData(list,backgroundColors);
                                saveInfo();
                            }
                        })
                        .setNegativeButton("cancel",null)
                        .create();
                dialog.show();
            }
        });

        final Button deleteAllTasksButton = findViewById(R.id.deleteAllTasksButton);

        deleteAllTasksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Delete All Task?")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                list.clear();
                                adapter.setData(list,backgroundColors);
                                saveInfo();

                            }
                        })
                        .setNegativeButton("No", null)
                        .create();
                dialog.show();


            }
        });

    }


    private void saveInfo(){
        try {
            File file = new File(this.getFilesDir(),"saved");
            FileOutputStream fout = new FileOutputStream(file);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fout));
            for(int i = 0; i < list.size();i++){
                bw.write(list.get(i));
                bw.newLine();
            }
            bw.close();
            fout.close();
             } catch (Exception e){
            e.printStackTrace();
        }

    }
    private void readInfo(){
        File file = new File(this.getFilesDir(),"saved");
        if(!file.exists()){
            return;
        }
        try{
            FileInputStream is = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line = reader.readLine();
            while (line != null){
                list.add(line);
                line = reader.readLine();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    class TextAdapter extends BaseAdapter{

        List<String> list = new ArrayList<>();

        int[] backgroundColors ;

        void setData(List<String> mList, int[] mBackgroundColors){
            list.clear();
            list.addAll(mList);
            backgroundColors = new int[list.size()];
            for (int i=0;i<list.size();i++){
                backgroundColors[i]=mBackgroundColors[i];
            }
            notifyDataSetChanged();
        }

        @Override
        public int getCount(){
            return list.size();
        } // return the amount of items that listview have

        @Override
        public  Object getItem(int position){
            return null;
        }
         @Override
        public  long getItemId(int position){
            return  0;

         }
        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            if(convertView==null){
                LayoutInflater inflater = (LayoutInflater)
                        MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.item, parent, false);

            }


            final TextView textView = convertView.findViewById(R.id.task);

            textView.setBackgroundColor(backgroundColors[position]);
            textView.setText(list.get(position));
            return convertView;
        }
    }

}