package cervi.dev.skipity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Toast;

public class Core extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_core);
        startSkipity();
    }

    private void startSkipity() {
        final Intent intentService = new Intent(this, Skipity.class);
        Toast.makeText(getBaseContext(), "Comienza el servicio!", Toast.LENGTH_LONG).show();
        startService(intentService);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}
