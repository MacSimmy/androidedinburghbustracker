/*
 * Copyright (C) 2011 - 2012 Niall 'Rivernile' Scott
 *
 * This software is provided 'as-is', without any express or implied
 * warranty.  In no event will the authors or contributors be held liable for
 * any damages arising from the use of this software.
 *
 * The aforementioned copyright holder(s) hereby grant you a
 * non-transferrable right to use this software for any purpose (including
 * commercial applications), and to modify it and redistribute it, subject to
 * the following conditions:
 *
 *  1. This notice may not be removed or altered from any file it appears in.
 *
 *  2. Any modifications made to this software, except those defined in
 *     clause 3 of this agreement, must be released under this license, and
 *     the source code of any modifications must be made available on a
 *     publically accessible (and locateable) website, or sent to the
 *     original author of this software.
 *
 *  3. Software modifications that do not alter the functionality of the
 *     software but are simply adaptations to a specific environment are
 *     exempt from clause 2.
 */
package uk.org.rivernile.edinburghbustracker.android.alerts;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import java.util.HashMap;
import uk.org.rivernile.android.bustracker.parser.livetimes.BusParser;
import uk.org.rivernile.android.bustracker.parser.livetimes.BusParserException;
import uk.org.rivernile.android.bustracker.parser.livetimes.BusService;
import uk.org.rivernile.android.bustracker.parser.livetimes.BusStop;
import uk.org.rivernile.edinburghbustracker.android.BusStopDatabase;
import uk.org.rivernile.edinburghbustracker.android.DisplayStopDataActivity;
import uk.org.rivernile.edinburghbustracker.android.PreferencesActivity;
import uk.org.rivernile.edinburghbustracker.android.R;
import uk.org.rivernile.edinburghbustracker.android.SettingsDatabase;
import uk.org.rivernile.edinburghbustracker.android.livetimes.parser.EdinburghBus;
import uk.org.rivernile.edinburghbustracker.android.livetimes.parser
        .EdinburghParser;

/**
 * The purpose of the TimeAlertService is run on a once-per-minute basis to load
 * bus times from the server to see if any of the services the user has filtered
 * on have arrived at the bus stop within the time trigger time, also set by the
 * user. If the criteria is not met then it schedules to run again in the next
 * minute. If the criteria is met, the user is greeted with a notification.
 * 
 * As this is an IntentService, it runs in a separate thread and does not block
 * the UI thread.
 * 
 * @author Niall Scott
 */
public class TimeAlertService extends IntentService {
    
    private static final int ALERT_ID = 2;
    
    private SettingsDatabase sd;
    private BusStopDatabase bsd;
    private NotificationManager notifMan;
    private AlertManager alertMan;
    private AlarmManager alarmMan;
    private SharedPreferences sp;
    
    /**
     * Create a new instance of the TimeAlertService. This simply calls its
     * super constructor.
     */
    public TimeAlertService() {
        super(TimeAlertService.class.getSimpleName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate() {
        super.onCreate();
        
        sd = SettingsDatabase.getInstance(getApplicationContext());
        bsd = BusStopDatabase.getInstance(getApplicationContext());
        notifMan = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        alertMan = AlertManager.getInstance(this);
        alarmMan = (AlarmManager)getSystemService(ALARM_SERVICE);
        sp = getSharedPreferences(PreferencesActivity.PREF_FILE, 0);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void onHandleIntent(final Intent intent) {
        final String stopCode = intent.getStringExtra("stopCode");
        final String[] services = intent.getStringArrayExtra("services");
        final int timeTrigger = intent.getIntExtra("timeTrigger", 5);
        final BusParser parser = new EdinburghParser();
        
        HashMap<String, BusStop> result;
        try {
            // Get the bus times. Only get 1 bus per service.
            result = parser.getBusStopData(new String[] { stopCode }, 1);
        } catch(BusParserException e) {
            // There was an error. No point continuing. Reschedule.
            reschedule(intent);
            return;
        }
        
        // Get the bus stop we are interested in. It should be the only one in
        // the HashMap anyway.
        final BusStop busStop = result.get(stopCode);
        int time;
        EdinburghBus edinBs;
        
        // Loop through all the bus services at this stop.
        for(BusService bs : busStop.getBusServices()) {
            // We are only interested in the next departure. Also get the time.
            edinBs = (EdinburghBus)bs.getFirstBus();
            time = edinBs.getArrivalMinutes();
            
            // Loop through all of the services we are interested in.
            for(String service : services) {
                // The service matches and meets the time criteria.
                if(service.equals(bs.getServiceName()) && time <= timeTrigger) {
                    // The alert may have been cancelled by the user recently,
                    // check it's still active to stay relevant. Cancel the
                    // alert if we're continuing.
                    if(!sd.isActiveTimeAlert(stopCode)) return;
                    alertMan.removeTimeAlert();
                    
                    // Create the intent that's fired when the notification is
                    // tapped. It shows the bus times view for that stop.
                    final Intent launchIntent = new Intent(this,
                            DisplayStopDataActivity.class);
                    launchIntent.setAction(DisplayStopDataActivity
                            .ACTION_VIEW_STOP_DATA);
                    launchIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    launchIntent.putExtra("stopCode", stopCode);
                    launchIntent.putExtra("forceLoad", true);
                    
                    final String stopName = bsd.getNameForBusStop(stopCode);
                    
                    final String title = getString(R.string.alert_time_title);
                    
                    final String summary = getResources().getQuantityString(
                            R.plurals.alert_time_summary, time == 0 ? 1 : time,
                            service, time, stopName);
                    
                    // Build the notification.
                    final NotificationCompat.Builder notifBuilder =
                            new NotificationCompat.Builder(this);
                    notifBuilder.setAutoCancel(true);
                    notifBuilder.setSmallIcon(R.drawable.ic_status_bus);
                    notifBuilder.setTicker(summary);
                    notifBuilder.setContentTitle(title);
                    notifBuilder.setContentText(summary);
                    // Support for Jelly Bean notifications.
                    notifBuilder.setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(summary));
                    notifBuilder.setContentIntent(
                            PendingIntent.getActivity(this, 0, launchIntent,
                                PendingIntent.FLAG_ONE_SHOT));

                    final Notification n = notifBuilder.build();
                    if(sp.getBoolean(PreferencesActivity.PREF_ALERT_SOUND,
                            true))
                        n.defaults |= Notification.DEFAULT_SOUND;

                    if(sp.getBoolean(PreferencesActivity.PREF_ALERT_VIBRATE,
                            true))
                        n.defaults |= Notification.DEFAULT_VIBRATE;

                    if(sp.getBoolean(PreferencesActivity.PREF_ALERT_LED,
                            true)) {
                        n.defaults |= Notification.DEFAULT_LIGHTS;
                        n.flags |= Notification.FLAG_SHOW_LIGHTS;
                    }
                    
                    // Send the notification.
                    notifMan.notify(ALERT_ID, n);
                    return;
                }
            }
        }
        
        // All the services have been looped through and the criteria didn't
        // match. This means a reschedule should be attempted.
        reschedule(intent);
    }
    
    /**
     * Reschedule the retrieval of bus times from the server because there was
     * an error loading them or the service/time criteria has not been met.
     * 
     * If the rescheduling goes on for an hour, then cancel the checking and
     * remove the alert otherwise the user's battery will be drained and data
     * used.
     * 
     * @param intent The intent that started this service. This is to be reused
     * to start the next service at the appropriate time.
     */
    private void reschedule(final Intent intent) {
        final long timeSet = intent.getLongExtra("timeSet", 0);
        
        // Checks to see if the alert has been active for the last hour or more.
        // If so, it gets cancelled.
        if((SystemClock.elapsedRealtime() - timeSet) >= 3600000) {
            alertMan.removeTimeAlert();
            return;
        }
        
        final PendingIntent pi = PendingIntent.getService(this, 0, intent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        // Reschedule ourself to run again in 60 seconds.
        alarmMan.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + 60000, pi);
    }
}