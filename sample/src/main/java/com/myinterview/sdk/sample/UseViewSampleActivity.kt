package com.myinterview.sdk.sample

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.myinterview.sdk.Listener
import com.myinterview.sdk.RequestPermissionCallback
import kotlinx.android.synthetic.main.activity_use_view_sample.*

class UseViewSampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_use_view_sample)
        myinterview_view.run {
            listener = object : Listener {
                override fun onError(throwable: Throwable) {
                    Toast.makeText(this@UseViewSampleActivity, "Error: ${throwable.message}", Toast.LENGTH_SHORT).show()
                    finish()
                }

                override fun onCanceled() {
                    Toast.makeText(this@UseViewSampleActivity, "Canceled", Toast.LENGTH_SHORT).show()
                    finish()
                }

                override fun onCompleted() {
                    Toast.makeText(this@UseViewSampleActivity, "Completed", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
            permissionCallback = object : RequestPermissionCallback {
                override fun onRequestPermissionsRequired(requestCode: Int, permissions: Array<out String>) {
                    ActivityCompat.requestPermissions(this@UseViewSampleActivity, permissions, requestCode)
                }
            }
        }
        if (savedInstanceState == null) {
            val showQuestions = intent?.getBooleanExtra(EXTRA_SHOW_QUESTIONS_KEY, false)!!
            myinterview_view.startSurvey(SampleConfigurationFactory.createSampleConfiguration(showQuestions))
        }
    }

    override fun onStart() {
        super.onStart()
        myinterview_view.onStart()
    }

    override fun onStop() {
        myinterview_view.onStop()
        super.onStop()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        myinterview_view.onRequestPermissionResult(requestCode, permissions, grantResults)
    }

    companion object {
        private const val EXTRA_SHOW_QUESTIONS_KEY = "EXTRA_SHOW_QUESTIONS_KEY"

        fun createIntent(context: Context, showQuestions: Boolean) =
                Intent(context, UseViewSampleActivity::class.java).apply {
                    putExtra(EXTRA_SHOW_QUESTIONS_KEY, showQuestions)
                }
    }
}
