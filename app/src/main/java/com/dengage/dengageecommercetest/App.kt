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
const val FIREBASE_APP_INTEGRATION_KEY = "o5r8xJa8eyBUGSmwXdskiIx0Um1oacEGoowX1_p_l_chY5_p_l_cVGTBC4m4jWgohN6vS3X3FG1cUbmToIAyVQfXAFDShKgqChrmLTMwnae_s_l_mqgXMNgkIG2gsAkOidM1znE1lIypsr_p_l_p9VrPR6OKSDdvF_s_l_3krw_e_q__e_q_"

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