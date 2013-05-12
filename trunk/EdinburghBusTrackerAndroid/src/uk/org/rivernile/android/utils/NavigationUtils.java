/*
 * Copyright (C) 2013 Niall 'Rivernile' Scott
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

package uk.org.rivernile.android.utils;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;

/**
 * This class contains various code related to navigation that may be reused in
 * multiple places in the same application. It makes more sense to implement it
 * once rather than multiple times across the same application.
 * 
 * @author Niall Scott
 */
public class NavigationUtils {
    
    /**
     * Prevent instantiation of this class.
     */
    private NavigationUtils() {
        // Nothing to do here.
    }
    
    /**
     * Call this method when it is necessary to navigate 'up' from an Activity
     * that only has a single known entry point. The parent Activity that is
     * used is defined in the AndroidManifest.xml, as described in 
     * {@link android.support.v4.app.NavUtils}.
     * 
     * @param activity The Activity on which 'up' was pressed.
     * @return true if the navigation operation succeeded, false if it didn't.
     * This may happen if activity is null, or the parent Activity was not
     * defined in AndroidManifest.xml.
     * @see android.support.v4.app.NavUtils
     */
    public static boolean navigateUpOnActivityWithSingleEntryPoint(
            final Activity activity) {
        if(activity == null) {
            return false;
        }
        
        final Intent upIntent = NavUtils.getParentActivityIntent(activity);
        
        if(upIntent == null) {
            return false;
        }

        if(NavUtils.shouldUpRecreateTask(activity, upIntent)) {
            TaskStackBuilder.create(activity).addNextIntent(upIntent)
                    .startActivities();
            activity.finish();
        } else {
            NavUtils.navigateUpTo(activity, upIntent);
        }
        
        return true;
    }
    
    /**
     * Call this method when it is necessary to navigate 'up' from an Activity
     * that has multiple entry points. A parent Activity should be defined in
     * AndroidManifest.xml, most likely the home Activity of the application,
     * that will be navigated to if
     * {@link android.support.v4.app.NavUtils#shouldUpRecreateTask(
     * android.app.Activity, android.content.Intent)} returns true.
     * 
     * @param activity The Activity on which 'up' was pressed.
     * @return true if the navigation operation succeeded, false if it didn't.
     * @see android.support.v4.app.NavUtils
     */
    public static boolean navigateUpOnActivityWithMultipleEntryPoints(
            final Activity activity) {
        if(activity == null) {
            return false;
        }
        
        final Intent upIntent = NavUtils.getParentActivityIntent(activity);
        
        if(upIntent == null) {
            return false;
        }
        
        if(NavUtils.shouldUpRecreateTask(activity, upIntent)) {
            TaskStackBuilder.create(activity).addNextIntent(upIntent)
                    .startActivities();
        }

        activity.finish();
        
        return true;
    }
}