package com.dengage.dengageecommercetest


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

import com.dengage.sdk.Dengage
import com.dengage.sdk.ui.story.StoriesListView
import com.dengage.sdk.ui.inappmessage.InAppInlineElement

class HomeFragment : Fragment() {

    private lateinit var storiesListView: StoriesListView
    private lateinit var webview: InAppInlineElement


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onResume() {
        super.onResume()

        Dengage.showStoriesList(
            screenName = "home",
            storiesListView = storiesListView,
            storyPropertyId = "story",
            activity = requireActivity()
        )

        Dengage.showInlineInApp(
            screenName = "home",
            inAppInlineElement = webview,
            propertyId = "inline",
            activity = requireActivity()
        )

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        storiesListView = view.findViewById(R.id.storiesListView)
        webview = view.findViewById(R.id.webview)
    }

}
