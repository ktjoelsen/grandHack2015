package ktjolsen.grandhackdemo;

import android.app.Activity;

import com.microsoft.band.BandClient;
import com.microsoft.band.BandClientManager;
import com.microsoft.band.BandDeviceInfo;
import com.microsoft.band.BandException;
import com.microsoft.band.BandIOException;
import com.microsoft.band.BandPendingResult;
import com.microsoft.band.ConnectionResult;
import com.microsoft.band.sensors.BandHeartRateEvent;
import com.microsoft.band.sensors.BandHeartRateEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 *
 */
public class Band {

    private Activity activity;
    private BandClient bandClient;

    private List<Integer> heartRates = new ArrayList<Integer>();

    public Band(Activity activity) {

        this.activity = activity;

        // create bandClient
        final BandDeviceInfo[] pairedBands = BandClientManager.getInstance().getPairedBands();
        BandClient bandClient = BandClientManager.getInstance().create(activity, pairedBands[0]);
        this.bandClient = bandClient;
        setupBand();

    }

    /**
     * Connect phone with band and start getHeartRateData
     */
    public void setupBand() {

        new Thread(new Runnable() {
            public void run() {
                try {
                    BandPendingResult<ConnectionResult> pendingResult = bandClient.connect();
                    try {
                        ConnectionResult result = pendingResult.await();
                        if (result == ConnectionResult.OK) {
                            // do work on success
                            getHeartRateData();
                        } else {
                            // do work on failure
                        }
                    } catch(InterruptedException ex) {
                        // handle InterruptedException
                    } catch(BandException ex) {
                        // handle BandException
                    }
                } catch (BandIOException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }

    private int getAvailableSpace() {
        try {
            // determine the number of available tile slots on the Band
            int tileCapacity = this.bandClient.getTileManager().getRemainingTileCapacity().await();
            return tileCapacity;
        } catch (BandException e) {
            // handle BandException
        } catch (InterruptedException e) {
            // handle InterruptedException
        }
        return -1;
    }

    /**
     * Continuously read heart rate data
     */
    private void getHeartRateData() {
        // create a heart rate event listener
        BandHeartRateEventListener heartRateListener = new BandHeartRateEventListener() {
            @Override
            public void onBandHeartRateChanged(BandHeartRateEvent event) {
                // do work on heart rate changed (i.e. update UI)
                heartRates.add(event.getHeartRate());
            }
        };

        try {
            // register the listener
            bandClient.getSensorManager().registerHeartRateEventListener(heartRateListener);
        } catch (BandException ex) {

        }
    }
}



