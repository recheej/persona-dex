# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\Rechee\AppData\Local\Android\Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

#crashlytics
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
#end crashlytics

-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }

-keepclassmembers class com.persona5dex.models**{ <fields>; }

-keep class android.support.v7.widget.SearchView { *; }

#picasso}
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
-dontwarn org.conscrypt.**
# A resource is loaded with a relative path so the package of this class must be preserved.
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase
-dontwarn retrofit.**
-keep class retrofit.** { *; }
-keepclassmembers,allowobfuscation interface * {
    @retrofit.http.** <methods>;
}
-dontwarn com.squareup.okhttp.**


-keep class com.facebook.** {
   *;
}
-dontwarn com.facebook.stetho.**
-keep class com.facebook.stetho.** { *; }