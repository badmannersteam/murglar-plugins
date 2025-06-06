# https://www.guardsquare.com/en/products/proguard/manual/usage
-dontobfuscate

-keepattributes Exceptions, Signature, InnerClasses, *Annotation*, SourceFile, LineNumberTable, EnclosingMethod

-keepnames public class * extends java.lang.Exception

# for names equality in plugin and host app
-keepnames class com.badmanners.murglar.lib.core.** {*;}
# for serialization
-keep class * implements com.badmanners.murglar.lib.core.model.node.Node {*;}
# for instantiation from host app
-keep class * implements com.badmanners.murglar.lib.core.service.Murglar {
    <init>(...);
    public *;
}

# Keep `Companion` object fields of serializable classes.
# This avoids serializer lookup through `getDeclaredClasses` as done for named companion objects.
-if @kotlinx.serialization.Serializable class **
-keepclassmembers class <1> {
    static <1>$Companion Companion;
}

# Keep `serializer()` on companion objects (both default and named) of serializable classes.
-if @kotlinx.serialization.Serializable class ** {
    static **$* *;
}
-keepclassmembers class <2>$<3> {
    kotlinx.serialization.KSerializer serializer(...);
}

# Keep `INSTANCE.serializer()` of serializable objects.
-if @kotlinx.serialization.Serializable class ** {
    public static ** INSTANCE;
}
-keepclassmembers class <1> {
    public static <1> INSTANCE;
    kotlinx.serialization.KSerializer serializer(...);
}

# @Serializable and @Polymorphic are used at runtime for polymorphic serialization.
-keepattributes RuntimeVisibleAnnotations, AnnotationDefault

# Serialization core uses `Class.forName("java.lang.ClassValue")` for caching in JVM-only, so it is an expected situation that this class is not in Android
-dontwarn java.lang.ClassValue