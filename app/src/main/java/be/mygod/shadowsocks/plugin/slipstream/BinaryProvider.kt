package be.mygod.shadowsocks.plugin.slipstream

import android.net.Uri
import android.os.ParcelFileDescriptor
import com.github.shadowsocks.plugin.NativePluginProvider
import com.github.shadowsocks.plugin.PathProvider
import java.io.File
import java.io.FileNotFoundException

class BinaryProvider : NativePluginProvider() {
    override fun populateFiles(provider: PathProvider) {
        provider.addPath("slipstream", 0b111101101)
        // Expose the library for NekoBox
        provider.addPath("libslipstream.so", 0b111101101)
    }
    
    override fun getExecutable() = context!!.applicationInfo.nativeLibraryDir + "/libslipstream.so"
    
    override fun openFile(uri: Uri): ParcelFileDescriptor = when (uri.path) {
        "/slipstream" -> ParcelFileDescriptor.open(File(getExecutable()), ParcelFileDescriptor.MODE_READ_ONLY)
        "/libslipstream.so" -> ParcelFileDescriptor.open(File(getExecutable()), ParcelFileDescriptor.MODE_READ_ONLY)
        else -> throw FileNotFoundException()
    }
}
