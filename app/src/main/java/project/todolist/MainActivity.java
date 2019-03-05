package project.todolist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    String task;
    String dueDate;
    ArrayList<String> listItems = new ArrayList<String>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        Log.i("tag2","Getting intent");
        if (intent != null){
            Bundle bundle = intent.getExtras();
            if(bundle != null){
                task = (String)bundle.get("TASK");
                listItems.add(task);
            }
        }
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,listItems);
        ListView listOfTasks = (ListView)findViewById(R.id.tasksList);
        listOfTasks.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.add_task, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == R.id.addTaskButton){
           Intent intent = new Intent(this,NewTask.class);
           startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


}
