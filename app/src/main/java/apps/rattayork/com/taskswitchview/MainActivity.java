package apps.rattayork.com.taskswitchview;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import apps.rattayork.com.taskswitchview.widget.TaskSwitchView;


public class MainActivity extends Activity {

    Switch materialSwitch;
    TaskSwitchView taskSwitchView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        materialSwitch = findViewById(R.id.material_switch);
        taskSwitchView = findViewById(R.id.task_switch);

        taskSwitchView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Toast.makeText(getApplicationContext(), "Switch is ON", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "Switch is OFF", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


}
