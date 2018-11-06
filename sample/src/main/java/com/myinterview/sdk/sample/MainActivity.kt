package com.myinterview.sdk.sample

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.myinterview.sdk.MyInterviewActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val myInterviewRequestCode = 12

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        use_activity_sample_interview_btn.setOnClickListener {
            startActivityForResult(createMyInterviewActivityIntent(false), myInterviewRequestCode)
        }
        use_view_sample_interview_btn.setOnClickListener {
            startActivity(UseViewSampleActivity.createIntent(this, false))
        }
        use_activity_sample_normal_btn.setOnClickListener {
            startActivityForResult(createMyInterviewActivityIntent(true), myInterviewRequestCode)
        }
        use_view_sample_normal_btn.setOnClickListener {
            startActivity(UseViewSampleActivity.createIntent(this, true))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode != myInterviewRequestCode) {
            return
        }
        when (resultCode) {
            MyInterviewActivity.RESULT_SUCCESS -> showToast("Success")
            MyInterviewActivity.RESULT_CANCELED -> showToast("Canceled")
            MyInterviewActivity.RESULT_ERROR -> {
                val throwable = data!!.getSerializableExtra(MyInterviewActivity.EXTRA_ERROR_KEY) as Throwable
                throwable.printStackTrace()
                showToast(throwable.message)
            }
        }
    }

    private fun showToast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun createMyInterviewActivityIntent(isShowQuestions: Boolean): Intent {
        val sampleConfiguration = SampleConfigurationFactory.createSampleConfiguration(isShowQuestions)
        return MyInterviewActivity.createIntent(this, sampleConfiguration)
    }
}