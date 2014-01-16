This folder contains the files needed to try out Telerik Mobile Testing with the
sample Everlive Friends app.

The most up-to-date instructions for preparing your Android app for testing with
Telerik Mobile Testing are available at:

http://docs.telerik.com/platform/mobile-testing/documentation/configure-your-app/android

---

1. Copy AndroidManifest.xml over Friends/app/src/main/AndroidManifest.xml
2. Copy MobileTestingExtension.jar into Friends/app/libs/
3. Copy build.gradle over Friends/app/build.gradle

Now you should be ready to build the Everlive Friends sample app in Android Studio.
After building and deploying it, you can use Telerik Mobile Testing tools to run
automated tests against the app. Try out the tests in specs/friendsNativeSpec.js
