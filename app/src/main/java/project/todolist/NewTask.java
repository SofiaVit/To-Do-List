package project.todolist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class NewTask extends AppCompatActivity {

    String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        Toolbar toolbar = (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.done, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == R.id.doneButton){
            EditText taskText = (EditText) findViewById(R.id.UserTask);
            EditText dateText = (EditText) findViewById(R.id.UserDueDate);
            Intent intent = new Intent(this,MainActivity.class);
            intent.putExtra("TASK",taskText.getText().toString());
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

}
