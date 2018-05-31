package cervi.dev.skipity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;
import android.view.View;

public class Core extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_core);
    }

    public void startSkipity(View view) {
        final Intent intentService = new Intent(this, Skipity.class);
        Toast.makeText(getBaseContext(), "Service started!", Toast.LENGTH_LONG).show();
        startService(intentService);
    }

    public void stopService(View view){
        final Intent intentService = new Intent(this, Skipity.class);
        Toast.makeText(getBaseContext(), "Service stopped!", Toast.LENGTH_LONG).show();
        stopService(intentService);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode==KeyEvent.KEYCODE_VOLUME_DOWN && event.getEventTime()>2000){
            //Log.w("CERV1", "VOLUME DOWN");
            return true;

        }

        if(keyCode==KeyEvent.KEYCODE_VOLUME_UP && event.getEventTime()>2000){
            //Log.w("CERV1", "VOLUME UP");
            return true;
        }


        return super.onKeyDown(keyCode, event);
    }

}
