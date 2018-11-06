# MyInterview Android SDK
Integrate video into your recruitment solution and enhance the decision making process for employers.

## Configuration
There are two main classes that are responsible for configuration: [Configuration] and [Question].
[Configuration] targets on configuration for the whole widget. [Question] contains required info about target question.

### [Question]
The example of creating instance [Question] class:
```
Question.Builder().build {
    title = String
    text = String
    durationSecs = Int
    attempts = Int
}
```
Fields:

- **title** - question subject

- **text** - the full question text

- **duration** - the maximum amount of time in seconds that given for user to record the answer

- **attempts** - number of attempts to record the answer

### [Configuration]
The example of creating instance of [Configuration] class:
```
Configuration.build {
    apiKey = String
    jobId = String
    username = String
    email = String
    questions = List<Question>
    preparationTimeSecs = Int
    showQuestions = Boolean
}
```
Configuration fields:
 - **apiKey** - API key to connect with MyInterview services
 
 - **jobId** - if company has several open vacancies, this one could be vacancy id. *Optional field*.
 
 - **username** - username of the user that is going to answer interview questions. *Optional field*.
 
 - **email** - email of the user that is going to answer interview questions. *Optional field*.
 
 - **questions** - List of questions
 
 - **preparationTimeSec** - time in seconds to allow user prepare for answering the question before recording will start.
 
 - **showQuestions** - switch between interview and normal mode. In interview mode user has _preparationTimeSec_ to prepare for answering before recording will start. In normal mode this parameter _preparationTimeSec_ is ignored.

### Example
The full example of creating configuration for MyInterview widget:
```
Configuration.build {
    preparationTimeSecs = 15
    showQuestions = true /* switch to Interview mode */
    apiKey = API_KEY
    questions = Arrays.asList(Question.Builder().build {
        title = "Introduce yourself"
        text = "Example: Hello my name is [Your Name] and have been a [Profession] for [Number of] years.\n" +
            "One more line.\n" +
            "And this one too."
        durationSecs = 61
        attempts = 10
    },
    Question.Builder().build {
        title = "What is your experience?"
        text = "Give some examples of your work/ study/ life experiences " +
            "(During my time at... I was able to... meaning I can now... for you)."
        durationSecs = 60
        attempts = 10
    })
}
```
## Integration
There are two options to integrate MyInterview Widget into the app: [MyInterviewActivity] and [MyInterviewView].

### [MyInterviewActivity] integration
The most easiest way start using MyInterview widget is to use [MyInterviewActivity].
It could be started using `context.startActivity(Intent)` and `context.startActivityForResult(Intent, Int)`.
```
val configuration = createMyInteviewWidgetConfiguration()
context.startActivity(MyInterviewActivity.createIntent(this, sampleConfiguration), MY_INTERVIEW_REQUEST_CODE)
```
or 
```
val configuration = createMyInteviewWidgetConfiguration()
context.startActivityForResult(MyInterviewActivity.createIntent(this, sampleConfiguration), MY_INTERVIEW_REQUEST_CODE)
```

##### Handling activity result
The app could handle the result of interview process.
In `Activity` or `Fragment` app should override `onActivityResult(Int,Int,Intent)` method. Example:
```
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
```

### [MyInterviewView] integration
[MyInterviewView] integration option is more flexible but requires some additional coding.

##### Layout
The example of using [MyInterviewView] in layout file:
```
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".UseViewSampleActivity">

    <com.myinterview.sdk.MyInterviewView
        android:id="@+id/myinterview_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />

</FrameLayout>
```
##### Activity or Fragment
This is the minimum amount of code that is required to integrate [MyInterviewView] in Activity or Fragment.
```
class UseViewSampleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_use_view_sample)
        myinterview_view.run {
            listener = object : Listener {
                override fun onError(throwable: Throwable) {
                    ...
                }

                override fun onCanceled() {
                    ...
                }

                override fun onCompleted() {
                    ...
                }
            }
            permissionCallback = object : RequestPermissionCallback {
                override fun onRequestPermissionsRequired(requestCode: Int, permissions: Array<out String>) {
                    ActivityCompat.requestPermissions(this@UseViewSampleActivity, permissions, requestCode)
                }
            }
        }
        if (savedInstanceState == null) {
            myinterview_view.startSurvey(createConfiguration())
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
}
```
Important parts:

- call `setContentView(Int)` is required because app works with View that is specified in layout xml

- set `listener` to [MyInterviewView] - implementation of [Listener] interface. It's used for listening results of interview process.

- set `permissionCallback` to [MyInterview] - implementation of [RequestPermissionCallback] interface. It's used for requesting permissions.

- call `MyInterviewView#onStart()` and `MyInterviewView#onStop()` in appropriate lifecycle callbacks for correct lifecycle handling

- call `MyInterviewView#onRequestPermissionResult(Int,Array<String>,IntArray)` inside `onRequestPermissionResult(Int,Array<String>,IntArray)` in target Activity or Fragment to handle request permission results

- **To start interview process** the app should invoke `MyInterviewView#startSurvey(Configuration)`

## Sample 
The samples are available in the [sample] module.

[Configuration]: </myinterview-android-sdk/src/develop/library/src/main/java/com/myinterview/sdk/Configuration.kt>
[Question]: </myinterview-android-sdk/src/develop/library/src/main/java/com/myinterview/sdk/Question.kt>
[MyInterviewActivity]: </myinterview-android-sdk/src/develop/library/src/main/java/com/myinterview/sdk/MyInterviewActivity.kt>
[MyInterviewView]: </myinterview-android-sdk/src/develop/library/src/main/java/com/myinterview/sdk/MyInterviewView.kt>
[Listener]: </myinterview-android-sdk/src/develop/library/src/main/java/com/myinterview/sdk/Listener.kt>
[RequestPermissionCallback]: </myinterview-android-sdk/src/develop/library/src/main/java/com/myinterview/sdk/RequestPermissionCallback.kt>
[sample]: </myinterview-android-sdk/src/develop/sample/>