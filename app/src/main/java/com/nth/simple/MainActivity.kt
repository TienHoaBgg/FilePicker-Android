package com.nth.simple

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.nth.filepicker.NTHFilePicker
import com.nth.filepicker.MimeTypeManager
import com.nth.filepicker.engine.impl.GlideEngine
import com.nth.filepicker.internal.entity.CaptureStrategy
import com.nth.filepicker.internal.entity.ConstValue
import com.nth.imagespicker.utils.Platform
import com.tbruyelle.rxpermissions2.RxPermissions

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<AppCompatButton>(R.id.bottomClick).setOnClickListener {
            RxPermissions(this@MainActivity)
                .request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .subscribe {
                    if (!it) {
                        Toast.makeText(
                            this@MainActivity, "Not Access", Toast.LENGTH_LONG
                        ).show()
                        return@subscribe
                    }
                    showFileChooser()
                }
        }

    }

    private fun showFileChooser() {
        NTHFilePicker.from(this@MainActivity)
            .choose(MimeTypeManager.ofAll())
            .countable(false)
            .capture(true)
            .captureStrategy(
                CaptureStrategy(
                    true,
                    "${Platform.getPackageName(this@MainActivity)}.fileprovider"
                )
            )
            .thumbnailScale(0.85f)
            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            .imageEngine(GlideEngine())
            .forResult(ConstValue.REQUEST_CODE_CHOOSE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data == null) return

        if (requestCode == ConstValue.REQUEST_CODE_CHOOSE && resultCode == Activity.RESULT_OK) {
            var string = ""
            val uriList = NTHFilePicker.obtainResult(data) ?: return
            uriList.forEach {
                string += it.toString() + "\n"
                Log.i("NTHDebug",string)
            }

        }
    }




}