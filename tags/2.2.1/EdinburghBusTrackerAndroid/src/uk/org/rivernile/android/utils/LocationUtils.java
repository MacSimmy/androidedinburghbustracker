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

import android.location.Location;
import android.location.LocationManager;
import java.util.List;

/**
 * This class contains various methods which help with Location services. Some
 * of the code is taken from the Android Developer website. Any bits of code
 * which have been taken from elsewhere will be marked in their method Javadoc.
 *
 * @author Niall Scott
 */
public class LocationUtils {

    private static final int TWO_MINUTES = 120000;

    /**
     * Prevent instantiation of this class.
     */
    private LocationUtils() {
        // Nothing to do here.
    }

    /**
     * Get the best initial fix on a Location. It will loop through all known
     * system location providers and get the location from each provider. They
     * will all be compared to return the best Location.
     *
     * @param locMan An instance of the LocationManager, so that this method can
     * obtain the last location for each provider.
     * @return An instance of Location which contains the best Location, or null
     * if a location could not be determined.
     */
    public static Location getBestInitialLocation(
            final LocationManager locMan) {
        if (locMan == null) {
            return null;
        }

        final List<String> matchingProviders = locMan.getAllProviders();
        Location location, bestLocation = null;

        for (String provder : matchingProviders) {
            location = locMan.getLastKnownLocation(provder);

            if (location != null && isBetterLocation(location, bestLocation)) {
                bestLocation = location;
            }
        }

        return bestLocation;
    }

    /**
     * Determines whether one Location reading is better than the current
     * Location fix.
     *
     * This was taken from
     * http://d.android.com/training/basics/location/currentlocation.html
     *
     * @param location The new Location fix.
     * @param currentBestLocation The currently held Location fix.
     * @return true if the new Location is better than the old one, false if
     * not.
     */
    public static boolean isBetterLocation(final Location location,
            final Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location.
            return true;
        }

        // Check whether the new location fix is newer or older.
        final long timeDelta = location.getTime()
                - currentBestLocation.getTime();
        final boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use
        // the new location because the user has likely moved.
        if (timeDelta > TWO_MINUTES) {
            return true;
        } else if (timeDelta < -TWO_MINUTES) {
            // If the new location is more than two minutes older, it must be
            // worse.
            return false;
        }

        // Check whether the new location fix is more or less accurate.
        final int accuracyDelta = (int) (location.getAccuracy()
                - currentBestLocation.getAccuracy());
        final boolean isLessAccurate = accuracyDelta > 0;
        final boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider.
        final boolean isFromSameProvider = isSameProvider(
                location.getProvider(), currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and
        // accuracy.
        if (accuracyDelta < 0) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate
                && isFromSameProvider) {
            return true;
        }

        return false;
    }

    /**
     * Checks to see if two providers are the same.
     *
     * @param provider1 The first provider String to compare.
     * @param provider2 The second provider String to compare.
     * @return true if they are the same, false if not.
     */
    public static boolean isSameProvider(final String provider1,
            final String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }

        return provider1.equals(provider2);
    }
}