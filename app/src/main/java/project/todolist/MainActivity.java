package project.todolist;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    String task;
    String dueDate;
    ArrayList<String> listItems = new ArrayList<String>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        readFromFile();
        updateListView();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.add_task, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == R.id.Russian){
            setLanguage("ru",1);
        }
        if (id == R.id.English){
            setLanguage("en",1);
        }
        if (id == R.id.Hebrew){
            setLanguage("he",1);
        }
        return super.onOptionsItemSelected(item);
    }

    public void AddQuickTask(View view){
        TextView textQuickTask = (TextView)findViewById(R.id.TextQuickTask);
        if(textQuickTask != null){
            listItems.add(textQuickTask.getText().toString());
            writeToFile(textQuickTask.getText().toString());
            updateListView();
        }
        textQuickTask.setText("");
    }

    public void updateListView(){
        //adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,listItems);
        adapter = new ArrayAdapter<String>(this,R.layout.rowlayout,R.id.check_box,listItems);
        ListView listOfTasks = (ListView)findViewById(R.id.tasksList);
        listOfTasks.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listOfTasks.setAdapter(adapter);
        listOfTasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = ((TextView) view).getText().toString();
            }
        });
        listOfTasks.setLongClickable(true);
        listOfTasks.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> av, View v, int pos, long id)
            {
                final View view = v;
                AlertDialog.Builder chooseDelete = new AlertDialog.Builder(MainActivity.this);
                chooseDelete.setMessage(MainActivity.this.getString(R.string.delete)).setCancelable(false)
                        .setPositiveButton(MainActivity.this.getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String selectedTask = ((TextView) view).getText().toString();
                                deleteFromFile(selectedTask);
                            }
                        })
                        .setNegativeButton(MainActivity.this.getString(R.string.no), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = chooseDelete.create();
                alertDialog.show();
                return true;
            }
        });
    }

    public void writeToFile(String taskToSave){
        File file = new File(getFilesDir(),"tasks.txt");
        if(!file.exists())
            file.mkdir();
        try{
            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file,true),"UTF-8");
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            bufferedWriter.write(taskToSave);
            bufferedWriter.newLine();
            bufferedWriter.close();
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void readFromFile(){
        try{
            FileInputStream fileInputStream = openFileInput("tasks.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String line;
            while ((line = bufferedReader.readLine()) != null)
                listItems.add(line);
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void deleteFromFile(String taskToDelete){
        try {
            File tempFile = new File(getFilesDir(), "temp.txt");
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(tempFile));

            File file = new File(getFilesDir(), "tasks.txt");
            FileInputStream fileInputStream = openFileInput("tasks.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String currentLine;
            while ((currentLine = bufferedReader.readLine()) != null) {
                if ((currentLine.trim()).equals(taskToDelete))
                    currentLine = "";
                else {
                    bufferedWriter.write(currentLine);
                    bufferedWriter.newLine();
                }
            }
            bufferedReader.close();
            bufferedWriter.close();
            file.delete();
            tempFile.renameTo(file);
        }catch (IOException e){
            e.printStackTrace();
        }
        listItems.remove(taskToDelete);
        updateListView();
    }


    private void setLanguage(String language,int num){
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getSharedPreferences("settings",MODE_PRIVATE).edit();
        editor.putString("My_Lang",language);
        editor.apply();
        if (num==1){
            finish();
            startActivity(getIntent());
        }
    }


    public void loadLocale(){
        SharedPreferences prefs = getSharedPreferences("settings", Activity.MODE_PRIVATE);
        String language = prefs.getString("My_Lang", "");
        setLanguage(language,0);
    }

}
