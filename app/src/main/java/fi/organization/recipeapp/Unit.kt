package fi.organization.recipeapp

import android.app.Activity
import android.content.Context
import android.util.Log
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread


fun downloadUrlAsync(context : Context, url: String, callback: (str: String) -> Unit) {
    thread {
        var data : String? = null
        val myUrl = URL(url)
        val conn = myUrl.openConnection() as HttpURLConnection
        conn.setRequestProperty("Accept", "application/json")
        try {
            data = conn.inputStream.use { it.reader().use{reader -> reader.readText()} }
            (context as Activity).runOnUiThread {
                callback(data)
            }

        } catch (ex: Exception) {
            Log.d("Exception", ex.toString())
        }
    }
}
