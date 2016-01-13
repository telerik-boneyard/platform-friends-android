
# Telerik Friends Sample App for Android

<a id="top"></a>
* [Overview](#overview)
* [Screenshots](#screenshots)
* [Requirements](#requirements)
* [Configuration](#configuration)
* [Running the Sample](#running-the-sample)

# Overview

This repository contains the Telerik Friends app for Android. It is a sample mobile app demonstrating how to integrate a wide range of Telerik Platform services into a native Android mobile application.

The Telerik Friends sample app showcases these features and SDKs:

- [Cloud data](http://docs.telerik.com/platform/backend-services/android/data/introduction) access (Telerik Backend Services)
- Working with [files](http://docs.telerik.com/platform/backend-services/android/files/introduction) (Telerik Backend Services)
- User [registration](http://docs.telerik.com/platform/backend-services/android/users/users-register) and [authentication](http://docs.telerik.com/platform/backend-services/android/users/users-authenticate) (Telerik Backend Services)
- Authentication with [social login](http://docs.telerik.com/platform/backend-services/android/users/social-login/introduction) providers (Facebook, Google, etc.) (Telerik Backend Services)
- Authentication with [AD FS](http://docs.telerik.com/platform/backend-services/android/users/adfs-login/introduction) (Telerik Backend Services)
- Using custom user account fields (Telerik Backend Services)
- Basic [app analytics](http://docs.telerik.com/platform/analytics/getting-started/introduction) (Telerik Analytics)
- Tracking [feature use](http://docs.telerik.com/platform/analytics/client/reports/feature-use) (Telerik Analytics)

To implement all the features listed above, the sample app utilizes the following products, components, and SDKs:

- [Telerik Backend Services Android SDK](http://docs.telerik.com/platform/backend-services/android/getting-started-android-sdk)&mdash;to connect the app to Telerik Platform
- [Telerik Analytics Java SDK](http://docs.telerik.com/platform/analytics/sdk/java/)&mdash;to connect the app to Telerik Platform
- [google-play-services.jar from the Android SDK by Google](http://developer.android.com/sdk/index.html)&mdash;used for signing in to Telerik Backend Services using a Google account
- [LiveSDK-for-Android-master.jar](https://github.com/liveservices/LiveSDK-for-Android)&mdash;used for signing in to Telerik Backend Services using a Windows Account
- [Facebook Android library](https://developers.facebook.com/docs/android/getting-started)&mdash;used for signing in to Telerik Backend Services using a Facebook account

# Screenshots

Login Screen|Activity Stream|Activity Details
---|---|---
![Login Screen](https://raw.githubusercontent.com/telerik/platform-friends-android/master/screenshots/android-login-screen.png)|![Activities stream view](https://raw.githubusercontent.com/telerik/platform-friends-android/master/screenshots/android-activities-stream.png)|![Activity details view](https://raw.githubusercontent.com/telerik/platform-friends-android/master/screenshots/android-activity-details.png)


# Requirements

Before you begin, you need to ensure that you have the following:

- **An active Telerik Platform account**
Ensure that you can log in to a Telerik Platform account. This can be a free trial account. Depending on your license you may not be able to use all app features. For more information on what is included in the different editions, check out the pricing page. All features included in the sample app work during the free trial period.

- **Android Studio** The sample project is created with Android Studio version 0.4.2 and gradle-1.9. If you want to use another version you may need to modify the `build.gradle` file to reflect the gradle version you are using.

- **Android SDK** You need API level 8 or later to build the Facebook library project from the Facebook SDK. The Friends sample app itself requires API level 11 or later. The default project is set up to build for API level 19 (Android 4.4.2) but if you want to change that, edit `app/build.gradle` and set `compileSdkVersion`, `minSdkVersion`, and `targetVersion` to match you setup.

# Configuration

The Friends sample app comes fully functional, but to see it in action you must link it to your own Telerik Platform account.

1. Log in to the Telerik Platform portal.
2. Create a new Hybrid or NativeScript app.<br>
	You will only use the backend of the app.
3. Click the **Data** tab and then click **Enable and populate with sample data**.<br>
	Sample content types with data required for the app to run is automatically created for you. The button also enables the **Users** service where user accounts for the app are precreated.
3. Click the **Settings** tab.
4. Take note of your **App ID**.

> If you happen to break the structure of the automatically generated Friends data, you can delete the app and start over.

## App ID for Telerik Platform

This is a unique string that links the sample mobile app to your Telerik Platform account where all the data is read from/saved. To set it in the app code:

1. Open the `Friends/app/src/main/res/values/settings.xml` file.
2. Find the `telerik_app_id` string resource and set its value to the App ID of your Telerik Platform app.

## (Optional) Project Key for Telerik Analytics

This is a unique string that links the sample mobile app to the Analytics part of your Telerik Platform app. If you do not set this the sample will still work, but no analytics data will be collected.
	
1. In the Telerik Platform portal, go to your app.
2. Click the **Analytics** tab and then click **Enable**.
3. Go to **Analytics > Settings > Options** and take note of your **Project Key**.
4. Open the `Friends/app/src/main/res/values/settings.xml` file.
5. Find the `analytics_app_id` string resource and set its value to the Project Key that you acquired earlier.

## (Optional) Facebook App ID

To demonstrate social login, we have preinitialized the sample to use a purpose-built Facebook app by Telerik. However, you still need to enable Facebook integration in the Telerik Platform portal:

1. In the Telerik Platform portal, go to your app.
2. Navigate to **Users > Authentication**.
3. Ensure that the Facebook box is checked.

> Note that if you intend to use the code for a production app you need to set up your own Facebook application and adjust the Facebook app ID as follows:
	
1. Open the `Friends/app/src/main/res/values/settings.xml` file.
2. Find the `facebook_app_id` string resource and set its value to your Facebook app ID.

## (Optional) Google Client ID

The sample app allows users to register using their Google ID.

To try this integration, make the following configurations:

1. In the Telerik Platform portal, go to your app.
2. Navigate to **Users > Authentication**.
3. Ensure that the **Google** box is checked.
4. Fill in your Google Account details on the device that you are using for testing. In case you have more than one Google account, the app prompt you to select the one to use.

## (Optional) Microsoft Account

To demonstrate social login, we have preinitialized the sample to use a  Microsoft Account Client ID owned by Telerik. However, you still need to enable Microsoft Account integration in the Telerik Platform portal:

1. In the Telerik Platform portal, go to your app.
2. Navigate to **Users > Authentication**.
3. Ensure that the **Windows Live** box is checked.

> Note that if you intend to use the code for a production app you need to set up your own Microsoft Account Client ID and adjust the code as follows:

1. Open the `Friends/app/src/main/res/values/settings.xml` file.
2. Find the `live_id_client_id` string resource and set its value to your Microsoft Account app ID.

## (Optional) Active Directory Federation Services (AD FS)

The sample app allows users to [register using AD FS](http://docs.telerik.com/platform/backend-services/javascript/users/adfs-login/introduction). To try this integration, configure the following:

1. In the Telerik Platform portal, go to your app.
2. Navigate to **Users > Authentication**.
5. Ensure that the **Active Directory** box is checked.
6. Fill in **ADFS metadata URL** with your AD FS server's metadata URL.

# Running the Sample

Once the app is configured, click **Run** in Android Studio or Eclipse to run it either on a real device or in an emulator.

> Ensure that the emulator or the device that you are using has Internet connectivity when running the sample.


