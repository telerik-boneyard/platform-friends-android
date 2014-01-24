Friends Sample App for Android
=============================
This repository contains the [Friends sample app](http://docs.telerik.com/platform/backend-services/samples/friends/friends-sample) for Android. The Friends app is a sample mobile app showcasing the integration of Telerik Platform services into an Android appllication. To download the source code, just click on the "Download ZIP" button.


## Showcased features and SDKs

To implement all the features listed above, the sample app utilizes the following products and SDKs:

- Telerik Backend Services Android SDK - used to work with Telerik Backend Services.
- Telerik Analytics Android SDK - used to work with Telerik Analytics.
- google-play-services.jar - used to sign-in Telerik Backend Services using a Google account.
- LiveSDK-for-Android-master.jar - used to sign-in Telerik Backend Services using a LiveID account.
- Facebook Android library (as a different module) - used to sign-in Telerik Backend Services using a Facebook account.

## Requirements

The following is a list of requirements for the sample app:

- **Active Telerik Platform account**  
To use this sample app you must have an active Telerik Platform account. Depending on your license you may not be able to use all features of the app. For more information on what is included in the different editions, please check out the pricing page for the respective product. All features included in the sample app will work in the free trial period.

- **Android Studio**  
The sample project is created with the latest version of Android Studio (0.4.2) however it could be opened with any version of Android Studio. However you should modify build.gradle file in order to use a different version of gradle, since the project is created with gradle-1.9 (which is supported by latest Android Studio version).


- **Android SDK API level 8**  
You need Android SDK for API level 8 in order to build Facebook library project (which is imported as is from Facebook SDK). Friends sample for Android require any API version above API level 11 (Android 3.0), because of using ActionBar class. The sample requires API level 19 (4.4.2) in order to build it, or you can edit app/build.gradle file and set appropriate to your setup values for compileSdkVersion, minSdkVersion and targetVersion.

## Configuring the sample app
The Friends sample app comes fully functional, but to see it in action you must link it to your own Telerik Platform account.

What you need to set:

- **API key for Telerik Backend Services**  
This links the sample mobile app to a project in Telerik Backend Services. When you activate Telerik Backend Services a Friends sample project is created for you automatically. It has necessary structure defined and some data pre-filled. You must use its API key.  
To set the API key in the code, go to Friends/app/src/main/res/values/settings.xml and place your API key as a value of the "everlive_api_key" string resource.
> If you happen to break the structure of the automatically generated Friends sample project, you can delete it and a fresh instance will be created again for you automatically. Alternatively, you could create a new project and choose to start from a Friends template, instead of starting from a blank project.

- [optional] **API key for Telerik Analytics**  
This step is optional, it links the sample mobile app to a Telerik Analytics project in your account. If you do not set this the sample will still work, but no analytics data will collected. To set your Telerik Analytics app id go to Friends/app/src/main/res/values/settings.xml and place your Telerik Analytics app id obtained from [Telerik Analytics](http://www.telerik.com/analytics) 

- [optional] **Facebook app ID**  
The sample app allows users to register using their Facebook account. We've pre-initialized the sample to use a Facebook app created by Telerik for the purpose. If you want, you can set it to use your own Facebook application by adjusting the Facebook app ID.
You can set Facebook app ID by editing Friends/app/src/main/res/values/settings.xml file replacing string value named "facebook_app_id" with your Facebook App key. For more information about creating your own Facebook App you can refer to the official [Facebook documentation](https://developers.facebook.com/docs/android/getting-started/).

- [optional] **Google**  
Since Android version 2.1 all accounts are managed by AccountManager, so in order to be able to use a different Google account to sign-in Telerik Backend Services this account should be added on the Android device via Settings->Accounts->Add new account. In case of having more than one Google account, sample app will provide a chooser dialog when sign-in with Google.
> Note that Google account login cannot be used on an emulator, since there is no option to install Google-Play-Services and also no option to add a Google account.

- [optional] **Windows Live**  
The sample app allows users to register using their LiveID account. We've pre-initialized the sample to use a LiveID app created by Telerik for the purpose. If you want, you can set it to use your own LiveID application by adjusting the LiveID app ID.
You can set LiveID app ID by editing Friends/app/src/main/res/values/settings.xml file replacing string value named "live_id_client_id" with your LiveID client ID.

- [optional] **Active Directory Federation Services(ADFS)**  
The sample app allows users to register using ADFS active federation (by providing username and password). In order to use ADFS login, you should configure your Telerik Backend Services account and your Active Directory service. For more information you can refer to [this help article](http://docs.telerik.com/platform/backend-services/development/android-sdk/users/adfs-provider).

## Running the sample app
Once the app is configured as described in the previous section, you can run it either on a real device or on an emulator. Just click "Run" in Android Studio or Eclipse.

> Make sure the emulator or the device you use have working Internet connection when running the sample. Internet connection is necessary in order to connect to the cloud.
