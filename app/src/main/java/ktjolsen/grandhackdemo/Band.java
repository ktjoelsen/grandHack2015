package ktjolsen.grandhackdemo;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.microsoft.band.BandClient;
import com.microsoft.band.BandClientManager;
import com.microsoft.band.BandDeviceInfo;
import com.microsoft.band.BandException;
import com.microsoft.band.ConnectionResult;


/**
 * Created by ktjolsen on 4/25/15.
 */
public class Band {

    private Activity activity;

    public Band(Activity activity) {
        this.activity = activity;
    }

    public void setupBand() {
        BandDeviceInfo[] pairedBands = BandClientManager.getInstance().getPairedBands();
        BandClient bandClient = BandClientManager.getInstance().create(activity, pairedBands[0]);
        Log.d("setupBand", bandClient.toString());
        new ConnectTask().execute(bandClient);
        Log.d("setupBand", "connected");
    }


}

class ConnectTask extends AsyncTask<BandClient, Void, ConnectionResult> {
    @Override
    protected ConnectionResult doInBackground(BandClient... clientParams) {
        try {
            return clientParams[0].connect().await();
        } catch (InterruptedException e) {
            Log.d("ConnectBand", ConnectionResult.TIMEOUT.toString());
            return ConnectionResult.TIMEOUT;
        } catch (BandException e) {
            Log.d("ConnectBand", ConnectionResult.INTERNAL_ERROR.toString());
            return ConnectionResult.INTERNAL_ERROR;
        }
    }

    protected void onPostExecute(ConnectionResult result) {
        if (result == ConnectionResult.OK) {

            Log.d("ConnectBand", "is connected YESS!");
        }

        else {
            Log.d("ConnectBand", "not yet.......");
        }

    }
}

