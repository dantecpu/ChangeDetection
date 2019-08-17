package com.bernaferrari.changedetection.settings

import android.os.Bundle
import android.view.View
import com.airbnb.epoxy.EpoxyController
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.activityViewModel
import com.bernaferrari.base.mvrx.simpleController
import com.bernaferrari.changedetection.*
import com.bernaferrari.ui.extras.BaseRecyclerFragment
import kotlinx.android.synthetic.main.recyclerview.*

class SettingsFragment : BaseRecyclerFragment() {

    private val viewModel: SettingsViewModel by activityViewModel()

    override fun epoxyController(): EpoxyController = simpleController(viewModel) { state ->

        marquee {
            id("header")
            title("Settings")
            subtitle("Version ${BuildConfig.VERSION_NAME}")
        }

        if (state.data is Loading) {
            loadingRow { id("loading") }
        } else if (!state.data.complete) {
            return@simpleController
        }

        val lightMode = state.data()?.lightMode ?: true

        SettingsSwitchBindingModel_()
            .id("light mode")
            .title("Light mode")
            .icon(R.drawable.ic_sunny)
            .switchIsVisible(true)
            .switchIsOn(lightMode)
            .clickListener { v ->
                Injector.get().isLightTheme().set(!lightMode)
                activity?.recreate()
            }
            .addTo(this)

        val backgroundSync = state.data()?.backgroundSync ?: false

        SettingsSwitchBindingModel_()
            .id("background sync")
            .title("Background Sync")
            .icon(R.drawable.ic_sync)
            .subtitle(if (backgroundSync) "Enabled" else "Disabled")
            .clickListener { v ->
                DialogBackgroundSync.show(requireActivity())
            }
            .addTo(this)

        val vibrateWhenSync = state.data()?.showSystemApps ?: true

        SettingsSwitchBindingModel_()
            .id("vibrate on sync")
            .title("Vibrate when a change is detected")
            .icon(R.drawable.ic_twotone_vibration)
            .switchIsVisible(true)
            .switchIsOn(vibrateWhenSync)
            .subtitle("Make sure it won't vibrate while you are sleeping.")
            .clickListener { v ->
                Injector.get().showSystemApps().set(!vibrateWhenSync)
            }
            .addTo(this)

        SettingsSwitchBindingModel_()
            .id("about")
            .title("About")
            .icon(R.drawable.ic_info)
            .clickListener { v ->

            }
            .addTo(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val decoration = InsetDecoration(1, 0, 0x40FFFFFF)
        recycler.addItemDecoration(decoration)
    }
}
