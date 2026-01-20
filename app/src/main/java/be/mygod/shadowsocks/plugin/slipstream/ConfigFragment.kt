package be.mygod.shadowsocks.plugin.slipstream

import android.os.Bundle
import android.text.InputType
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.github.shadowsocks.plugin.PluginOptions

class ConfigFragment : PreferenceFragmentCompat() {
    private val domain by lazy { findPreference<EditTextPreference>("domain")!! }
    private val authoritative by lazy { findPreference<SwitchPreferenceCompat>("authoritative")!! }
    private val cert by lazy { findPreference<EditTextPreference>("cert")!! }
    private val keepAliveInterval by lazy { findPreference<EditTextPreference>("keep_alive_interval")!! }

    val options get() = PluginOptions().apply {
        val domainValue = domain.text?.trim().takeIf { !it.isNullOrEmpty() }
        putWithDefault("domain", domainValue)
        if (authoritative.isChecked) {
            putWithDefault("authoritative", "")
        }
        val certValue = cert.text?.trim().takeIf { !it.isNullOrEmpty() }
        putWithDefault("cert", certValue)
        val keepAliveValue = keepAliveInterval.text?.trim().takeIf { !it.isNullOrEmpty() }
        putWithDefault("keep-alive-interval", keepAliveValue, DEFAULT_KEEP_ALIVE)
    }

    fun onInitializePluginOptions(options: PluginOptions) {
        domain.text = options["domain"]
        authoritative.isChecked = options.containsKey("authoritative")
        cert.text = options["cert"]
        keepAliveInterval.text = options["keep-alive-interval"]
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.config)
        domain.setOnBindEditTextListener { it.inputType = InputType.TYPE_TEXT_VARIATION_URI }
        cert.setOnBindEditTextListener { it.inputType = InputType.TYPE_TEXT_VARIATION_URI }
        keepAliveInterval.setOnBindEditTextListener {
            it.inputType = InputType.TYPE_CLASS_NUMBER
            it.hint = DEFAULT_KEEP_ALIVE
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.setOnApplyWindowInsetsListener(listView) { v, insets ->
            insets.apply {
                v.updatePadding(bottom = getInsets(WindowInsetsCompat.Type.navigationBars()).bottom)
            }
        }
    }

    private companion object {
        private const val DEFAULT_KEEP_ALIVE = "400"
    }
}
