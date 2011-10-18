package org.jfsd.transformerbatterystatus;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

public class TransformerBatteryStatusActivity extends Activity {

    private static final String TAG = "TransformerBatteryStatus";
    private static final Boolean DEBUG = true;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

    }

    /*
     * (non-Javadoc)
     * @see android.app.Activity#onResume()
     */
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        Intent batteryStatusIntent = registerReceiver(null, new IntentFilter(
                Intent.ACTION_BATTERY_CHANGED));

        // Use level, scale and maybe status to display if device is charging
        int level = batteryStatusIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        if (DEBUG) Log.v(TAG, "Level:" + level);

        int scale = batteryStatusIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        int batteryLevel = (int) (100.0 * level / scale);
        ((TextView) findViewById(R.id.tablet_battery_level)).setText(batteryLevel + "%");

        int status = batteryStatusIntent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        if (DEBUG) Log.v(TAG, "Status:" + status);

        int dockLevel = batteryStatusIntent.getIntExtra("dock_level", -1);
        if (dockLevel == -1) {
            ((TextView) findViewById(R.id.dock_battery_level)).setText("-");
        } else {
            ((TextView) findViewById(R.id.dock_battery_level)).setText(dockLevel + "%");
        }

        int dockStatus = batteryStatusIntent.getIntExtra("dock_status", -1);
        if (DEBUG) Log.v(TAG, "Dock status:" + dockStatus);

        // To check for all the keys that the ACTION_BATTERY_CHANGED intent has
        /*
         * Set<String> keys = batteryStatusIntent.getExtras().keySet(); for
         * (String key: keys) { Log.v(TAG, "key: " + key); }
         */
    }

    /*
     * (non-Javadoc)
     * @see android.app.Activity#dispatchTouchEvent(android.view.MotionEvent)
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Rect dialogBounds = new Rect();
        getWindow().getDecorView().getHitRect(dialogBounds);

        if (!dialogBounds.contains((int) ev.getX(), (int) ev.getY())) {
            if (DEBUG) Log.v(TAG, "tap outside dialog");
            this.finish();
        }
        return super.dispatchTouchEvent(ev);
    }

}
