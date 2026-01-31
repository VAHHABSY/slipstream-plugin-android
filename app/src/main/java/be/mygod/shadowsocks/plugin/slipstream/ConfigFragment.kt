package be.mygod.shadowsocks.plugin.slipstream

import android.os.Bundle
import android.text.InputType
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.preference.EditTextPreference
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.github.shadowsocks.plugin.PluginOptions

class ConfigFragment : PreferenceFragmentCompat() {
    private val domain by lazy { findPreference<EditTextPreference>("domain")!! }
    private val resolver by lazy { findPreference<ListPreference>("resolver")!! }
    private val customResolver by lazy { findPreference<EditTextPreference>("custom_resolver")!! }
    private val authoritative by lazy { findPreference<SwitchPreferenceCompat>("authoritative")!! }
    private val cert by lazy { findPreference<EditTextPreference>("cert")!! }
    private val keepAliveInterval by lazy { findPreference<EditTextPreference>("keep_alive_interval")!! }

    val options get() = PluginOptions().apply {
        val domainValue = domain.text?.trim().takeIf { !it.isNullOrEmpty() }
        putWithDefault("domain", domainValue)
        
        // Add resolver option
        val resolverValue = resolver.value?.trim().takeIf { !it.isNullOrEmpty() } ?: "cloudflare"
        
        // If custom resolver is selected, use the custom IP, otherwise use the preset
        if (resolverValue == "custom") {
            val customResolverValue = customResolver.text?.trim().takeIf { !it.isNullOrEmpty() }
            if (customResolverValue != null) {
                putWithDefault("resolver", customResolverValue)
            } else {
                putWithDefault("resolver", "cloudflare") // Fallback if custom is empty
            }
        } else {
            putWithDefault("resolver", resolverValue, "cloudflare")
        }
        
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
        
        // Check if resolver is a preset or custom IP
        val resolverOption = options["resolver"] ?: "cloudflare"
        val knownResolvers = listOf(
            "cloudflare", "cloudflare-family", "google", "quad9", "quad9-secured",
            "opendns", "adguard", "adguard-family", "cleanbrowsing", "nextdns", "system"
        )
        
        if (knownResolvers.contains(resolverOption)) {
            resolver.value = resolverOption
            customResolver.text = ""
        } else {
            // It's a custom IP
            resolver.value = "custom"
            customResolver.text = resolverOption
        }
        
        authoritative.isChecked = options.containsKey("authoritative")
        cert.text = options["cert"]
        keepAliveInterval.text = options["keep-alive-interval"]
        
        // Update custom resolver visibility
        updateCustomResolverVisibility()
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.config)
        
        domain.setOnBindEditTextListener { it.inputType = InputType.TYPE_TEXT_VARIATION_URI }
        cert.setOnBindEditTextListener { it.inputType = InputType.TYPE_TEXT_VARIATION_URI }
        keepAliveInterval.setOnBindEditTextListener {
            it.inputType = InputType.TYPE_CLASS_NUMBER
            it.hint = DEFAULT_KEEP_ALIVE
        }
        
        // Configure custom resolver input
        customResolver.setOnBindEditTextListener { 
            it.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_URI
            it.hint = "1.1.1.1"
        }
        
        // Show/hide custom resolver input based on selection
        resolver.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, newValue ->
            customResolver.isVisible = (newValue == "custom")
            true
        }
        
        // Set initial visibility
        updateCustomResolverVisibility()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.setOnApplyWindowInsetsListener(listView) { v, insets ->
            insets.apply {
                v.updatePadding(bottom = getInsets(WindowInsetsCompat.Type.navigationBars()).bottom)
            }
        }
    }
    
    private fun updateCustomResolverVisibility() {
        customResolver.isVisible = (resolver.value == "custom")
    }

    private companion object {
        private const val DEFAULT_KEEP_ALIVE = "400"
    }
}
