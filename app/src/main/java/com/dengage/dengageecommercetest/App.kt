package com.dengage.dengageecommercetest

import android.app.Application
import android.content.Context
import com.dengage.dengageecommercetest.data.CartManager
import com.dengage.geofence.DengageGeofence
//import com.dengage.hms.DengageHmsManager
import com.dengage.sdk.Dengage
import com.dengage.sdk.data.remote.api.DeviceConfigurationPreference
import com.dengage.sdk.data.remote.api.NotificationDisplayPriorityConfiguration
import com.dengage.sdk.util.DengageLifecycleTracker

// dev-app.dengage.com: hasnain new test app
const val FIREBASE_APP_INTEGRATION_KEY = "K2rRYB4Fh_s_l_lbQlmtPyZBglqHB4p0qvfSPVDdofNi_p_l_Axg6PSv_s_l_yIr4JEcGizPXFNUn9baevw6iCqqk9bKCUUqjNHi0_p_l_BImM1wlOfy_s_l_jPxZg9ILxPhTmpunjS4vQ9_s_l_2ep2"

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        CartManager.init(this)

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