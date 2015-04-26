package ktjolsen.grandhackdemo;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

    private int heartRate;
    private TextView tv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Band band = new Band(this);
        band.setupBand();

//        Intent mServiceIntent = new Intent(this, HeartRateService.class);
//        this.startService(mServiceIntent);

        tv1 = (TextView)findViewById(R.id.textView);
        tv1.setText("Time to take your medicine.");

        new Thread(new Runnable() {
            public void run() {

                while (true) {
                    try {
                        Thread.sleep(500);
                        if (band.getRecentHeartRate() > 90) {
                            break;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Runnable updateRate = new Runnable() {
                        @Override
                        public void run() {
                            tv1.setText(Integer.toString(band.getRecentHeartRate()));
                        }
                    };
                    tv1.post(updateRate);
                }
                Runnable updateRate = new Runnable() {
                    @Override
                    public void run() {
                        tv1.setText("Well done!");
                    }
                };
                tv1.post(updateRate);

            }
        }).start();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showHeartRates(final Band band) {
        final TextView tv1 = (TextView)findViewById(R.id.textView);
        tv1.setText(Integer.toString(band.getRecentHeartRate()));
    }

}
