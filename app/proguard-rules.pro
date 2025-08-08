# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If you use reflection, typically to load classes dynamically, you
# will need to include keep options here specifying the classes that
# are used.
-keep class com.google.android.gms.common.api.internal.IStatusCallback

# This is required by the Wearable Support Library
-keep class android.support.v4.app.NotificationCompat$WearableExtender {
    <init>();
}
