/*
 * Copyright (C) 2010 Niall 'Rivernile' Scott
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

package uk.org.rivernile.edinburghbustracker.android;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SimpleAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NewsUpdatesActivity extends ListActivity {

    /* Error constants */
    public static final int ERROR_NODATA = 0;
    public static final int ERROR_PARSEERR = 1;
    public static final int ERROR_IOERR = 2;
    public static final int ERROR_URLERR = 3;

    /* Constants for menus */
    private static final int MENU_REFRESH = Menu.FIRST;

    /* Constants for dialogs */
    private static final int PROGRESS_DIALOG = 0;

    private boolean progressDialogShown = false;
    private FetchNewsUpdatesTask fetchTask;
    private String jsonString;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newsupdates);
        setTitle(R.string.newsupdates_title);

        fetchTask = FetchNewsUpdatesTask.getInstance(mHandler);

        if(savedInstanceState != null)
            jsonString = savedInstanceState.getString("jsonString");

        String temp = fetchTask.getJSONString();
        if(jsonString != null && jsonString.length() > 0) {
            handleJSONString(jsonString);
        } else if(temp != null && temp.length() > 0) {
            handleJSONString(temp);
        } else if(!fetchTask.isExecuting()) {
            showDialog(PROGRESS_DIALOG);
            fetchTask.doTask();
        }
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("jsonString", jsonString);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        fetchTask.setHandler(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        super.onCreateOptionsMenu(menu);

        menu.add(0, MENU_REFRESH, 1, R.string.displaystopdata_menu_refresh)
                .setIcon(R.drawable.ic_menu_refresh);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch(item.getItemId()) {
            case MENU_REFRESH:
                showDialog(PROGRESS_DIALOG);
                fetchTask.doTask();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected Dialog onCreateDialog(final int id) {
        switch(id) {
            case PROGRESS_DIALOG:
                ProgressDialog prog = new ProgressDialog(this);
                prog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                prog.setCancelable(true);
                prog.setMessage(getString(
                        R.string.displaystopdata_gettingdata));
                prog.setOnCancelListener(new DialogInterface
                        .OnCancelListener() {
                    public void onCancel(DialogInterface di) {
                        finish();
                    }
                });
                progressDialogShown = true;
                return prog;
            default:
                return null;
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(final Message msg) {
            if(msg.getData().containsKey("errorCode")) {
                handleError(msg.getData().getInt("errorCode"));
            } else if(msg.getData().containsKey("jsonString")) {
                handleJSONString(msg.getData().getString("jsonString"));
            } else if(msg.getData().containsKey("refresh")) {
                showDialog(PROGRESS_DIALOG);
                fetchTask.doTask();
            }
        }
    };

    private void handleError(final int errorCode) {
        if(progressDialogShown) dismissDialog(PROGRESS_DIALOG);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        switch(errorCode) {
            case ERROR_NODATA:
                builder.setMessage(R.string.newsupdates_err_nodata);
                break;
            case ERROR_PARSEERR:
                builder.setMessage(R.string.newsupdates_err_parseerr);
                break;
            case ERROR_IOERR:
                builder.setMessage(R.string.newsupdates_err_ioerr);
                break;
            case ERROR_URLERR:
                builder.setMessage(R.string.newsupdates_err_urlerr);
                break;
            default:
                break;
        }
        builder.setCancelable(false).setTitle(R.string.error)
                .setPositiveButton(R.string.retry,
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface di, final int i) {
                showDialog(PROGRESS_DIALOG);
                fetchTask.doTask();
            }
        }).setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface di, final int i) {
                di.dismiss();
            }
        });
        builder.create().show();
    }

    private void handleJSONString(final String jsonString) {
        if(jsonString == null || jsonString.length() == 0) {
            handleError(ERROR_NODATA);
            return;
        }
        System.out.println(jsonString.length());
        JSONArray ja;
        try {
            ja = new JSONArray(jsonString);
            ArrayList<HashMap<String, String>> list =
                    new ArrayList<HashMap<String, String>>();
            JSONObject currentObj, user;
            String[] splitted;

            for(int i = 0; i < ja.length(); i++) {
                HashMap<String, String> map = new HashMap<String, String>();
                currentObj = ja.getJSONObject(i);
                map.put("TEXT", currentObj.getString("text"));
                user = currentObj.getJSONObject("user");
                splitted = currentObj.getString("created_at").split("\\s+");
                if(splitted.length == 6) {
                    map.put("INFO", user.getString("name") + " - " +
                            splitted[0] + " " + splitted[2] + " " + splitted[1]
                            + " " + splitted[5] + " " + splitted[3]);
                } else {
                    map.put("INFO", user.getString("name") + " - " +
                            currentObj.getString("created_at"));
                }
                list.add(map);
            }
            SimpleAdapter adapter = new SimpleAdapter(this, list,
                    R.layout.newsupdateslist, new String[] { "TEXT",
                    "INFO" }, new int[] { R.id.twitText, R.id.twitInfo });
            setListAdapter(adapter);
        } catch(JSONException e) {
            handleError(ERROR_PARSEERR);
        }

        if(progressDialogShown) dismissDialog(PROGRESS_DIALOG);
    }
}