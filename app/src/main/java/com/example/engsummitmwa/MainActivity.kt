package com.example.engsummitmwa

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.example.engsummitmwa.ui.theme.EngSummitMwaTheme
import io.ionic.liveupdates.LiveUpdate
import io.ionic.portals.PortalManager
import io.ionic.portals.PortalView
import io.ionic.portals.PortalsPlugin
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private var refId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupPortal()
        setContent {
            EngSummitMwaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MicroWebAppPortal(portalId = PORTAL_ID)
                }
            }
        }
    }

    private fun setupPortal() {
        if (!PortalManager.isRegistered()) {
            PortalManager.register(PORTAL_KEY)
        }
        PortalManager.newPortal(PORTAL_ID)
            .setStartDir(PORTAL_ID)
            .setInitialContext(
                mapOf(
                    "startingRoute" to "/main",
                    "language" to Locale.getDefault().toLanguageTag(),
                    "greetingMessage" to "Hello from Android!"
                )
            )
            .setLiveUpdateConfig(
                applicationContext,
                LiveUpdate(
                    appId = "2598bc73",
                    channelName = "develop"
                )
            )
            .create().also {
                PortalManager.addPortal(it)
            }
        PortalsPlugin.subscribe(PORTAL_SUBSCRIPTION) {
            when (it.toJSObject().getJSObject("data")?.getString("type")) {
                "dismiss" -> {
                    Toast.makeText(this, "Dismiss button has clicked!", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }
}

@Composable
fun MicroWebAppPortal(portalId: String, modifier: Modifier = Modifier) {
    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = {
            PortalView(it, portalId)
        })
}

const val PORTAL_SUBSCRIPTION = "subscription"
const val PORTAL_ID = "eng-summit-mwa"
const val PORTAL_KEY =
    "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiI3ZmNiMTg5OC0wMDNlLTQwNmMtOWQyOC03ZTEyNjcyMzc5MGUifQ.UOZMzh-BJ3FqXNUWs8se2jJjxqOCcn7NnAqq3hOrKzH_kMhtSlIBwUAMqtbOYhIuZwQejK5lTvc6Akjv-esX7oMHkx4T_QwS8hHk7bxkN5JO_cFN_O9xn8mvaVz2pRdstMKFutPqO4oHe4SF-L2675cOPPHpDTTAfsP72Xs4O4ZD0htAqO4WHuc1RdQSXFjqIJx8a-zp6Vf9Ysz4pbiDNS2KrUeARz-86A92xgdlMzhhxNwbxxM-ZjKJo9Lm28gLTyTbmEHZInjFmpLjvmqIykhAWJeohFSzBZLjdjTe4EQcrydw0lpk9ZWnwRGnYWF6HeFFT1o8Ms4SXZpvoTKzrg"