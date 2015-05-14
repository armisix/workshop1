##---------------Begin: proguard configuration for Support Lib  ----------

-keep class android.support.v4.** { *; }
-keep interface android.support.v4.** { *; }
-dontwarn android.support.v4.**

-keep class !android.support.v7.internal.view.menu.MenuBuilder, !android.support.v7.internal.view.menu.SubMenuBuilder, android.support.v7.** { *; }
-keep interface android.support.v7.** { *; }
-dontwarn android.support.v7.**

-keep class android.support.v13.** { *; }
-keep interface android.support.v13.** { *; }
-dontwarn android.support.v13.**

##---------------End: proguard configuration for Support Lib  ----------



##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
# Gson specific classes
-keep class sun.misc.Unsafe { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.superscores.android.io.model.json.** { *; }

##---------------End: proguard configuration for Gson  ----------