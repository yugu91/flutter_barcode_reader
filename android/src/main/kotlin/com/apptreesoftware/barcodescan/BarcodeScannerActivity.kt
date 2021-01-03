package com.apptreesoftware.barcodescan

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.google.zxing.Result
import com.yourcompany.barcodescan.R
import me.dm7.barcodescanner.zxing.ZXingScannerView


class BarcodeScannerActivity : Activity(), ZXingScannerView.ResultHandler {

    lateinit var scannerView: me.dm7.barcodescanner.zxing.ZXingScannerView

    companion object {
        val REQUEST_TAKE_PHOTO_CAMERA_PERMISSION = 100
        val TOGGLE_FLASH = 200

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.scaner);

        title = ""
        scannerView = ZXingScannerView(this)
        scannerView.setAutoFocus(true)
//        flashOffTxt = this.intent.getStringExtra("flashOffTxt");
//        flashOnTxt = this.intent.getStringExtra("flashOnTxt");
        // this paramter will make your HUAWEI phone works great!
        scannerView.setAspectTolerance(0.5f)
        var fl_my_container = this.findViewById<FrameLayout>(R.id.fl_my_container);
        fl_my_container.addView(scannerView);
        var lightBT = this.findViewById<LinearLayout>(R.id.scan_light);
        lightBT.setOnClickListener(View.OnClickListener {
            scannerView.flash = !scannerView.flash
            if(scannerView.flash)
                lightBT.alpha = 1.0f;
            else
                lightBT.alpha = 0.5f;
        })

        lightBT.alpha = 0.5f;

            var back = this.findViewById<LinearLayout>(R.id.scan_back);
        back.setOnClickListener(View.OnClickListener {
            this.finish();
        })
    }

    // override fun onCreateOptionsMenu(menu: Menu, inflater:MenuInflater): Boolean {
    //     inflater.inflate(R.layout.menu, menu)
    //     var back = menu.findViewById<LinearLayout>(R.id.scan_back);
    //     back.setOnClickListener(View.OnClickListener {
    //         this.finish();
    //     })
    //     // return false
    //     // if (scannerView.flash) {
    //     //     val item = menu.add(0,
    //     //             TOGGLE_FLASH, 0, "Flash Off")
    //     //     item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
    //     // } else {
    //     //     val item = menu.add(0,
    //     //             TOGGLE_FLASH, 0, "Flash On")
    //     //     item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
    //     // }
    //     return super.onCreateOptionsMenu(menu,inflater)
    // }

    // override fun onOptionsItemSelected(item: MenuItem): Boolean {
    //     if (item.itemId == TOGGLE_FLASH) {
    //         scannerView.flash = !scannerView.flash
    //         this.invalidateOptionsMenu()
    //         return true
    //     }
    //     return super.onOptionsItemSelected(item)
    // }

    override fun onResume() {
        super.onResume()
        scannerView.setResultHandler(this)
        // start camera immediately if permission is already given
        if (!requestCameraAccessIfNecessary()) {
            scannerView.startCamera()
        }
    }

    override fun onPause() {
        super.onPause()
        scannerView.stopCamera()
    }

    override fun handleResult(result: Result?) {
        val intent = Intent()
        intent.putExtra("SCAN_RESULT", result.toString())
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    fun finishWithError(errorCode: String) {
        val intent = Intent()
        intent.putExtra("ERROR_CODE", errorCode)
        setResult(Activity.RESULT_CANCELED, intent)
        finish()
    }

    private fun requestCameraAccessIfNecessary(): Boolean {
        val array = arrayOf(Manifest.permission.CAMERA)
        if (ContextCompat
                .checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, array,
                    REQUEST_TAKE_PHOTO_CAMERA_PERMISSION)
            return true
        }
        return false
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,grantResults: IntArray) {
        when (requestCode) {
            REQUEST_TAKE_PHOTO_CAMERA_PERMISSION -> {
                if (PermissionUtil.verifyPermissions(grantResults)) {
                    scannerView.startCamera()
                } else {
                    finishWithError("PERMISSION_NOT_GRANTED")
                }
            }
            else -> {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
        }
    }
}

object PermissionUtil {

    /**
     * Check that all given permissions have been granted by verifying that each entry in the
     * given array is of the value [PackageManager.PERMISSION_GRANTED].

     * @see Activity.onRequestPermissionsResult
     */
    fun verifyPermissions(grantResults: IntArray): Boolean {
        // At least one result must be checked.
        if (grantResults.size < 1) {
            return false
        }

        // Verify that each required permission has been granted, otherwise return false.
        for (result in grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }
}
