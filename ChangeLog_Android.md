# ChangeLog for the Android application #

### 2.2.3 ###
  * Added a darker grey star for adding favourites as some users reported difficulty seeing the old graphic.
  * Added the support ActionBar to the project.
  * Added new xxhdpi app launcher icons.
  * Removed support for ldpi density icons.
  * Updated to the latest compatibility library.
  * Updated to a new version of the Google Play Services library.
  * Updated to the latest BugSense SDK.
  * Changed SDK target to API level 19.
  * Removed code that was targeted towards older platform versions than the minimum supported.
  * Fixed a crash where references to IDs in the Android framework were being referenced incorrectly.
  * Fixed a crash when the user had set a proximity alert and Google Play Services was not on the device, a crash would occur when the proximity notification is displayed.
  * Fixed a crash where the progress DialogFragment being dismissed could cause an IllegalStateException warning about state loss.
  * Fixed an OutOfMemoryException in BusStopDetailsFragment where the compass needle would continue to eat RAM as it was manipulated.
  * Fixed an issue where the data that comes back from the external barcode scanning application could be null.
  * Fixed an issue on platform versions prior to Ice Cream Sandwich in the map search suggestions where internal data could be exposed in the suggestions UI.
  * The "My Location" ActionItem in the bus stop map has been replaced with the map-provided location button.
  * The map search UI has been replaced with SearchView as an ActionView.
  * The services filter on the map has been made more prominent.
  * Added more rigorous checking around opening the bus stop database to prevent some crashes.
  * Re-wrote the services chooser DialogFragment so that more than 1 service could be selected by default, and removed dependency on hosting Fragments to retain their instance.
  * Removed code that was targeted towards older versions of Android than this app will run on.
  * Cleaned up code in DialogFragments.
  * Refactored Activities and Fragments so that all navigation code is hosted in Activities. This will potentially fix a few crashes seen in the wild. It will also mean for a future tablet version being easier to write.
  * Moved some source code in to XML where possible to let the Android system handle certain tasks.
  * If an Activity hosts a Fragment which does not require parameters, then the layout for that Activity has been moved out to a XML file rather than being done in code.

### 2.2.2 ###
  * Split the bus stop database in to 900KB chunks. This is because Android 2.2 cannot deal with files in the application assets that are greater than 1MB in size. Written code to handle this and append these files in order on the device.
  * Fixed NullPointerException in BusStopMapFragment and NearestStopsFragment where it was attempting to reference the ServicesChooserDialogFragment when it is null.
  * If the bus times completes loading when the Fragment is no longer added to the Activity, then it is prevented from updating the UI. This is to prevent NullPointerExceptions when trying to reference the underlying Activity.
  * Fixed an issue in FavouriteStopsActivity where the up navigation would take the user in an endless navigation loop if they had selected to show favourites when the application starts.

### 2.2.1 ###
  * Cached the hit box size required for the star button as in certain cases, it would attempt to get this from an Activity that didn't exist, causing a NullPointerException.
  * In some cases, the bus stop database cannot be extracted properly (for example, insufficient disk space) and this means many items return null or empty sets, for example a null service listing. This causes problems later. Code has been added to disable access to the ServicesChooserDialogFragment if a list of services cannot be obtained.
  * Fixed an issue where an IllegalStateException was being thrown when the auto refresh handler was being fired just as it was about to be cancelled.

### 2.2 ###
  * Much of the app has been re-written with a move to using Fragments via the compatibility library. This re-write offered the chance of refactoring a lot of the code in the app, to make it cleaner and less prone to problems.
  * strings.xml was modified to make use of proper String formatting and plural Strings.
  * The ExpandableListView in DisplayStopDataFragment now remembers what groups have been expanded and contracted between configuration changes (such as screen rotation).
  * The Google Maps for Android v2 library replaces the v1 library via the Google Play Services library. This brings a new Maps interface to the application, including vector maps.
  * When a bus stop marker is tapped on the map, it no longer shows a Dialog. It now shows a bubble which which shows the stop name and services. Tapping on this bubble will show the user a bus stop details view.
  * The bus stop search on the map view has been revamped, showing results in a suggestion list and on the map itself. The new search mechanism has also been made available to the system-wide search. Stop heading icons are shown next to the results in the suggestion list.
  * The code has had much more documentation applied to it.
  * URL request mismatches are now detected, for example if the user is unauthenticated on a public Wi-Fi hotspot. The application will show the user the appropriate error in this case.
  * Added new database schema. This includes route lines for showing on the map and service colours. Some parts of the database inflation have had to be changed as an index needs to be created on the route lines table after it has been extracted from the application assets.
  * Fixed an issue where old database journal files were not being deleted.
  * The bus stop map will now show a coloured route line for a selected service, as well as the bus stops it stops at.
  * DisplayStopDataFragment now shows service colours.
  * Added new BusStopDetails view, which shows data on a particular bus stop, as well as showing a map and the distance and the direction from the device to the bus stop.
  * Removed unused graphical assets, source code files and XML files from the project.
  * The bus stop map now persists the centre point of the map so that location is returned to when the map is resumed.
  * Star icons have been added to the favourite stops, the new bus stop details and DisplayStopDataActivity which allow the user to add or remove the stop as a favourite.
  * The app checks for the availability of Google Play Service when it starts up. If it is not available, then it disables the map button and alerts the user that they cannot use map features. This is because the map depends on Google Play Services.
  * Added new graphics assets - thanks to Anthony Totton.
  * Reorganised application strings to make it easier to manage in the future and easier to localise.
  * Some text labels in the application are not selectable so that text can be copied from them.
  * Stop heading icons are shown in the nearest bus stops list.
  * Better compliance with lint.
  * Fixed an issue where the bus parser was being too strict and if unexpected data was received, the app would crash. The parsing code has now been made more robust.
  * Fixed a bug where the time String formatter for the API key was not using UTC.
  * Auto-refresh has been rewritten in BusStopDetailsFragment so that it is more in line with user expectations.
  * Added Open Source licence DialogFragment inside the AboutFragment, as per the requirements of using the new Maps API.
  * Updated to the latest version of the BugSense library.
  * Removed text around progress indicators, as per the Android Design guidelines.
  * Removed unnecessary items from context menus.
  * Added locality information to DisplayStopDetailsFragment.
  * Shortened titles of confirmation Dialogs so that the text is not truncated on some devices.
  * Implemented proper ‘up’ navigation inside the application. This only applies to devices running Honeycomb and above.
  * Added Intent actions on DisplayStopDataActivity and BusStopMapActivity so that other applications can use the features in those Activities. This is essentially the app providing an API to other applications on the device.
  * Detecting the device location has now been improved.

### 2.1.1 ###
  * Changed the data source of the Twitter News feed because of the upcoming switch-off of the Twitter v1.0 API.
  * Fixed a bug in the generation of the API key.

### 2.1 ###
  * New feature: on certain devices on Android 2.2 (Froyo) or above, the application will automatically backup user settings and saved bus stops to Google Backup.
  * New feature: added a user preference where the user can specify how many departures per service is shown at each bus stop.
  * New feature: replace the bus stop marker icons on the map with new icons which show the direction of the bus stop.
  * New feature: where possible, show locality information for the bus stop.
  * New feature: Italian localisation - thanks Matteo Doni!
  * Added intent filter for DisplayStopDataActivity so that other application can load bus times using this activity.
  * Fixed an issue in the map search results list on certain devices where the row background colour was the same as the text colour making it unreadable.
  * Made the ActionBar semi-transparent in the bus stop map for devices running Android 3.0 (Honeycomb) or later, like in the Google Maps app.
  * Added a clickable link to the project Twitter feed in the About dialog.
  * Fixed an issue where loading the location settings activity on Android 4.0 (Ice Cream Sandwich) showed the wrong settings activity.
  * Added performance improvements in navigating the bus stop map.
  * Fixed an issue which crashed the app when trying to load the QR code scanning activity.
  * Re-write the Twitter parser which now looks cleaner and is abstracted away from the UI code, and it now deals with HTML encoded characters properly. The new API URL is used as the current one being used is about to be deprecated.
  * The ActionBar is now a consistent theme throughout all activities on Android 4.0 (Ice Cream Sandwich) and later.
  * Cleaned up resources and a small amount of code based on recommendations from the new lint tool.
  * Cleaned up the application preferences (renamed to "Settings") based on recommendations from the Android Design website.
  * Database handling code has been cleaned up. Performance enhancements are possible as a result.
  * Custom Ant build rules have been moved out to custom\_rules.xml, as per recommendations in API level 15 build tools.
  * Fixed an issue where the "Distance" text where the nearest bus stops are shown can wrap on certain devices - thanks Ross McClymont.
  * When the user sets a time alert and the user taps on the resulting notification, the bus times are now force refreshed (causes old data to be refreshed if it exists) - thanks Ross McClymont.


### 2.0.2 ###
  * Fixed sorting of bus services in the times display (for example, 3 and 3A were not together in the list).
  * Fixed a UI bug on some devices (such as HTC Sense) where the list view in the bus stop dialog was shown as white text on a white background.
  * Added thread locking around database methods as this could be the cause of many of the database problems.
  * Reverted back to putting a star ( `*` ) next to estimated, not real bus times.
  * Speeded up scrolling of favourite bus stops if the user had enough elements in the list.

### 2.0.1 ###
  * Added better exception handling around database code to attempt to deal with problems more gracefully.
  * Fixed a crash in the bus stop map activity where the location point is null.
  * MD5 checksum the bus stop database to make sure it's not corrupt after downloading an update.
  * Fixed a crash after using the map search function and scrolling the resulting list.

### 2.0 ###
  * Projected migrated away from old NBAndroid format to standard Android project format.
  * The bus times are now retrieved from the new My Bus Tracker API.
  * New feature: nearest bus stops listing.
  * New feature: bus service filtering has been added to the bus stop map and the new nearest bus stops listing.
  * Fetchers have been re-written to use AsyncTask so that Android handles threading.
  * The bus stop map can be told to start up with a focus on a particular bus stop.
  * New feature: the bus times display now shows how long it has been since the last refresh.
  * New feature: favourite bus stops also show which services stop at that bus stop.
  * Preferences have been reordered to make it more logical.
  * New feature: added a user preference to let users show the favourite bus stops upon application start-up.
  * More/better use of convertView on list adapters for faster loading.
  * New feature: StreetView integration.
  * Redesigned the bus stop dialog to fit in more with the Android look and feel.
  * Menu declarations are no longer in code, they have been pulled out to XML.
  * New feature: bus arrival alerts (experimental feature).
  * New feature: bus stop proximity alerts (experimental feature).
  * New feature: QR code support. Supported externally to application by URL interception, or inside the application by starting the zxing scanner and getting the activity result.
  * Partial re-write of the bus stop map for reliability.
  * Improved searching on the bus stop map.
  * Favourite stop backups has changed to use JSON rather than just copying the database. Should improve future integration with Google Backup.
  * Removed old artwork and added new artwork.
  * Redesigned user interface and adding Honeycomb/Ice Cream Sandwich support.
  * Database updating has changed.
  * Added BugSense for better error tracking.

### 0.0.4 ###
  * Added the ability to get the latest news and travel updates within the application. This grabs the feeds from a Twitter list. This can be accessed from the main screen, then press the menu key on the device, then select 'News and Updates'. This data can be refreshed from the menu.
  * Increased the padding on the expandable list where the bus stop times are shown to improve the look of the cells.
  * Moved the 'About' screen from an Activity to a Dialog. It is now more pleasing to the eye and is laid out better.
  * Rewrote the map activity. It now no longer crashes when attempting to draw the user's location on the map (this was solved by working around a bug in the Google Maps API). The map activity now no longer crashes when the user rotates the device when the bus stop dialog is open.
  * Added the ability to search for locations on the map. The user can search for places and bus stops. Geocoding is used to search for places and the database is searched for bus stops. The Google search framework is used to make this process as native as possible, and also gives the added benefit of allowing voice recognition search terms. Search results are shown in the order of distance from the detected device location. This can be accessed by pressing the device's search key if it has one, or by pressing the menu key then 'Search' in the map activity.
  * Fixed an issue where the user would be shown the times for the wrong bus stop, or not shown times at all if they try to load the times for two or more stops at the same time. For example, the user may flick through stops in their favourites list, select stop A followed by stop B and be shown the times or stop A, or no times at all.
  * Made better use of the 

&lt;uses-feature&gt;

 tags in AndroidManifest.xml to allow the application to be shown to a wider range of devices in the Android Market.
  * When the application starts, if an older version of the database is in use than what is bundled with the application, the application will bring this database up to the bundled version. This means that the stop database will always be compatible with the latest versions of the application and the user will not have a very old version of the database loaded.
  * Added the ability, from the application preferences, for the user to force check for bus stop database updates. This disregards the stop database update setting. If an update is found, a new version of the database is downloaded. This does not affect the application's ability to automatically update the database periodically.
  * The user can now hit the enter key after typing in a stop code to submit that code, rather than having to close the keyboard to find the submit button.
  * General house keeping.

### 0.0.3 ###
  * The user can now backup and restore their favourite bus stops from the Preferences section of the application. This will save a copy of the favourites database to the SD card and when the user wishes to restore the database, this same file will be used.
  * The user can now sort the bus services in order of time (with the first to arrive being the first in the list). The default ordering, in order of service name, can still be selected and will remain the default ordering. If the user wishes to order by time, they can enable it from the Preferences or in the menu when displaying bus times. Their selection will be remembered for subsequent views of departure times.
  * The display of departure times has now been rewritten to reflect a cleaner way of displaying bus times. All bus services will be unexpanded but will show the service number, it's destination and the arrival time for the next bus in that service. If the user wishes to view subsequent departures, they can do so by expanding the list for that bus service.
  * Fixed an issue which prevented the application being displayed in the Android Market by QVGA devices (HTC Tattoo, HTC Wildfire, ...). The application is now compiled for Android 2.1 rather than 1.5, and now contains QVGA compatible graphics to support lower resolution devices. At the same time, higher resolution images were added to support WVGA. Thanks to all who reported this issue.

### 0.0.2 ###
  * DisplayStopDataActivity has now been rewritten so that it behaves correctly when the device has been rotated.
  * DisplayStopDataActivity will no longer randomly crash while auto-refreshing.
  * The dialog displayed when viewing a bus stop in BusStopMapActivity now lets you add and remove that stop from the favourites database.
  * Fixed an issue where the application would crash upon start up if the database is corrupt - Thanks Nick Forsyth.
  * Various other minor bug fixes.
  * Some code commenting.

### 0.0.1 ###
Initial release.