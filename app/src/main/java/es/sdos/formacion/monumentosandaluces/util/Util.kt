package es.sdos.formacion.monumentosandaluces.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.navigation.navOptions
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.snackbar.Snackbar
import es.sdos.formacion.monumentosandaluces.R


class Util {
    companion object {

        //Note: Logs
        const val TAG = ":::LOG"
        //Note: MonumentDTO Detail Fragment Bundles
        const val MONUMENT_BUNDLE = "MONUMENT_EXTRA_BUNDLE"
        //Note: Database tag
        const val DATABASE_TAG = ":::DATABASE"
        const val WEBVIEW_TAG = "WEB_VIEW_TARGET_:::"
        //Note: Database name
        const val DATABASE_NAME = "monuments_databas"
        //Note: Request for location
        const val LOCATION_PERMISSION_REQUEST_CODE = 1
        //Note: Request Camera
        const val SELECT_ACTIVITY_IMAGE = 50
        //Note: Picture result
        const val TAKE_ACTIVITY_IMAGE = 100
        //Note: Map const
        const val ZOOM_DURATION = 1000
        const val ZOOM_ON_MARKER = 18f
        val DEFAULT_LOCATION = LatLng(0.0, 0.0)
        const val ZOOM_DEFAULT = 1f

        val NAVIGATE_OPTIONS_RIGHT = navOptions {
            anim {
                enter = R.anim.slide_in_right
                exit = R.anim.slide_out_left
                popEnter = R.anim.slide_in_left
                popExit = R.anim.slide_out_right
            }
        }

        val NAVIGATE_OPTIONS_LEFT = navOptions {
            anim {
                enter = R.anim.slide_in_left
                exit = R.anim.slide_out_right
                popEnter = R.anim.slide_in_right
                popExit = R.anim.slide_out_left
            }
        }

        fun stringToNull(cadena: String?): String?{
            return if (cadena == "") {
                null
            } else {
                cadena
            }
        }

        fun showSnack(view: View, message: String, length: Int){
            Snackbar.make(
                view,
                message,
                if (length == 0) Snackbar.LENGTH_SHORT
                else Snackbar.LENGTH_LONG)
                .show()
        }
    }
}