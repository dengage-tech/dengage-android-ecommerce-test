package com.dengage.dengageecommercetest

import android.app.Application
import android.content.Context
import com.dengage.geofence.DengageGeofence
//import com.dengage.hms.DengageHmsManager
import com.dengage.sdk.Dengage
import com.dengage.sdk.data.remote.api.DeviceConfigurationPreference
import com.dengage.sdk.data.remote.api.NotificationDisplayPriorityConfiguration
import com.dengage.sdk.util.DengageLifecycleTracker

// app.dengage.com: hasnain new test app
const val FIREBASE_APP_INTEGRATION_KEY = "9MBNB5X2IWf8oBfxaNjs5kWcmObGwc8g6bmJcqS2rprtPSJgAThZL_s_l_n1nypZLOoQApPKMzfFMoJpU_s_l_BQk9YpJMk3mn05bpF3_p_l_1XjtNC1jvrEkEZ3D8h5VmUz0U4xmiI0ycs7_s_l_BJ20fOwTQsOq5OXRA_e_q__e_q_"

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        registerActivityLifecycleCallbacks(DengageLifecycleTracker())

        val context : Context = this

        //val dengageHmsManager = DengageHmsManager()

        Dengage.init(
            context = context,
            firebaseIntegrationKey = FIREBASE_APP_INTEGRATION_KEY,
            deviceConfigurationPreference = DeviceConfigurationPreference.Google,
            disableOpenWebUrl = false,
            notificationDisplayPriorityConfiguration = NotificationDisplayPriorityConfiguration.SHOW_WITH_HIGH_PRIORITY
        )

        Dengage.setLogStatus(true)
        Dengage.setDevelopmentStatus(true)
        Dengage.inAppLinkConfiguration("www.dengage.com")

        //DengageGeofence.startGeofence()
    }


}