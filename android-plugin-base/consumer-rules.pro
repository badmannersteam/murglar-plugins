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
