# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

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

# Firebase Authentication
-keepattributes *Annotation*

# Firebase Realtime database
-keepattributes Signature

# 소스 파일 변수 명 바꾸는 옵션
-renamesourcefileattribute SourceFile

# Error Log의 Line & ClassName
-keepattributes SourceFile,LineNumberTable

# Exception
-keep public class * extends java.lang.Exception

# 사용하지 않는 메소드 유지
-dontshrink

# Firebase Crashlytics
-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**

# 없애면 난독화 하지 않음
#-dontobfuscate

# 없애면 최적화 하지 않음
#-dontoptimize

# 없애면 manifest 난독화 하지 않음
#-keepresourcexmlattributenames manifest/**

# 빌드 후 mapping seed usage cofing 파일을 만들어주는 옵션
#-printmapping map.txt
#-printseeds seed.txt
#-printusage usage.txt
#-printconfiguration config.txt
