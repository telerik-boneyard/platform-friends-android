
# Friends Sample App for Android

<a href="https://github.com/telerik/platform-friends-android" target="_blank"><img style="padding-left:20px" src="http://docs.telerik.com/platform/appbuilder/sample-apps/images/get-github.png" alt="Get from GitHub" title="Get from GitHub"></a>

* [Overview](#overview)
* [Requirements](#requirements)
* [Configuration](#configuration)
* [Running the Sample](#running-the-sample)

## Overview

This repository contains the Telerik Friends app for iOS. It is a sample mobile app demonstrating how to integrate a large gamut of Telerik Platform services into a native iOS mobile application.

The Telerik Friends sample app showcases these features and SDKs:

- Cloud data access (Telerik Backend Services)
- Working with files (Telerik Backend Services)
- User registration and authentication (Telerik Backend Services)
- Authentication with social login providers (Facebook, Google, etc.) (Telerik Backend Services)
- Authentication with AD FS (Telerik Backend Services)
- Using custom user account fields (Telerik Backend Services)
- Basic app analytics (Telerik Analytics)
- Tracking feature usage (Telerik Analytics)

To implement all the features listed above, the sample app utilizes the following products, components, and SDKs:

- Telerik Backend Services - this is where all data, files, and user accounts are stored in the cloud
- Telerik Backend Services Android SDK - to connect the app to Telerik Backend Services
- Telerik Analytics - used to store analytics data in the cloud
- Telerik Analytics Android SDK - to connect the app with Telerik Analytics
- google-play-services.jar - used for signing in to Telerik Backend Services using a Google account
- LiveSDK-for-Android-master.jar - used for signing in to Telerik Backend Services using a Windows Account
- Facebook Android library - used for signing in to Telerik Backend Services using a Facebook account

## Requirements

Before you begin, you need to ensure that you have the following:

- **An active Telerik Platform account**
Ensure that you can log in to a Telerik Platform account. This can be a free trial account. Depending on your license you may not be able to use all app features. For more information on what is included in the different editions, check out the pricing page. All features included in the sample app work during the free trial period.

- **Android Studio** The sample project is created with Android Studio version 0.4.2 and gradle-1.9. If you want to use another version you may need to modify the `build.gradle` file to reflect the gradle version you are using.

- **Android SDK** You need API level 8 or later to build the Facebook library project from the Facebook SDK. The Friends sample app itself requires API level 11 or later. The default project is set up to build for API level 19 (Android 4.4.2) but if you want to change that, edit `app/build.gradle` and set `compileSdkVersion`, `minSdkVersion`, and `targetVersion` to match you setup.

## Configuration

The Friends sample app comes fully functional, but to see it in action you must link it to your own Telerik Platform account.

What you need to set:

### API Key for Telerik Backend Services

This is a unique string that links the sample mobile app to a project in Telerik Backend Services where all the data is read from/saved. When creating the project, you must base it on the Friends sample Backend Services project that has all the necessary data prepopulated.

You must use this project's API key. To set it in the app:

1. Open the `Friends/app/src/main/res/values/settings.xml` file.
2. Find the `backend_services_api_key` string resource and set its value to the API Key of your Friends Backend Services project.

> If you happen to break the structure of the automatically generated Friends sample project, you can delete it and a fresh instance will be created again for you automatically. Alternatively, you could create a new project and choose to start from a Friends template, instead of starting from a blank project.

### (Optional) Project Key for Telerik Analytics

This is a unique string that links the sample mobile app to a Telerik Analytics project in your account. If you do not set this the sample will still work, but no analytics data will be collected.
	
1. Open the `Friends/app/src/main/res/values/settings.xml` file.
2. Find the `analytics_app_id` string resource and set its value to the Project Key of your Friends Analytics project.

### (Optional) Facebook App ID
To demonstrate social login, we've pre-initialized the sample to use a purpose-built Facebook app by Telerik. However, you still need to enable Facebook integration in the Telerik Platform portal:

1. Go to your app.
2. Click the Backend Services project that you are using.
3. Navigate to **Users > Authentication**.
4. Ensure that the Facebook box is checked.

> Note that if you intend to use the code for a production app you need to set up your own Facebook application and adjust the Facebook app ID as follows:
	
1. Open the `Friends/app/src/main/res/values/settings.xml` file.
2. Find the `facebook_app_id` string resource and set its value to your Facebook app ID.

### (Optional) Google Client ID

The sample app allows users to register using their Google ID.

To try this integration, make the following configurations:

1. Log in to the Telerik Platform Portal.
2. Go to your app.
3. Click the Backend Services project that you are using.
4. Navigate to **Users > Authentication**.
5. Ensure that the Google box is checked.
6. Fill in your Google Account details on the device that you are using for testing. In case you have more than one Google account, the app prompt you to select the one to use.

### (Optional) Microsoft Account

To demonstrate social login, we've pre-initialized the sample to use a  Microsoft Account Client ID owned by Telerik. However, you still need to enable Microsoft Account integration in the Telerik Platform portal:

1. Go to your app.
2. Click the Backend Services project that you are using.
3. Navigate to **Users > Authentication**.
4. Ensure that the Windows Live box is checked.

> Note that if you intend to use the code for a production app you need to set up your own Microsoft Account Client ID and adjust the code as follows:

1. Open the `Friends/app/src/main/res/values/settings.xml` file.
2. Find the `live_id_client_id` string resource and set its value to your Microsoft Account app ID.

### (Optional) Active Directory Federation Services (AD FS)

The sample app allows users to [register using AD FS](http://docs.telerik.com/platform/backend-services/javascript/users/adfs-login/introduction). To try this integration, configure the following:

1. Log in to the Telerik Platform portal.
2. Go to your app.
3. Click the Backend Services project that you are using.
4. Navigate to **Users > Authentication**.
5. Ensure that the Active Directory box is checked.
6. Fill in **ADFS metadata URL** with your AD FS server's metadata URL.

## Running the Sample

Once the app is configured, click **Run** in Android Studio or Eclipse to run it either on a real device or in an emulator.

> Ensure that the emulator or the device that you are using has Internet connectivity when running the sample.


