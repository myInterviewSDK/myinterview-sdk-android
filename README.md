[ ![Download](https://api.bintray.com/packages/myintsdk/myinterview-sdk/myinterview-sdk-android/images/download.svg) ](https://bintray.com/myintsdk/myinterview-sdk/myinterview-sdk-android/_latestVersion) [![License](https://img.shields.io/badge/license-Apache--2.0-blue.svg)](LICENSE) ![Platform](https://img.shields.io/badge/platform-Android-lightgrey.svg)

# MyInterview Android SDK
Integrate video into your recruitment solution and enhance the decision making process for employers.
To obtain required credentials please visit [myInterview.com].

## Supported version
The SDK supports all Android versions starting from API 19 (KitKat).

## Installation
Just put inside your application level build.gradle:
```groovy
dependencies {
  implementation 'com.myinterview:myinterview-sdk-android:0.9.0'
}
```

## **Configuration**
There are two main classes that are responsible for configuration: `Configuration` and `Question`.
`Configuration` targets on configuration for the whole widget. `Question` contains required info about target question.

### **Question**
The example of creating instance `Question` class:
```kotlin
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

### **Configuration**
The example of creating instance of `Configuration` class:
```kotlin
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
 - **apiKey** - API key to connect with MyInterview services. To obtain **apiKey** please visit [myInterview.com].
 
 - **jobId** - if company has several open vacancies, this one could be vacancy id. *Optional field*.
 
 - **username** - username of the user that is going to answer interview questions. *Optional field*.
 
 - **email** - email of the user that is going to answer interview questions. *Optional field*.
 
 - **questions** - List of questions
 
 - **preparationTimeSec** - time in seconds to allow user prepare for answering the question before recording will start.
 
 - **showQuestions** - switch between interview and normal mode. In interview mode user has _preparationTimeSec_ to prepare for answering before recording will start. In normal mode this parameter _preparationTimeSec_ is ignored.

### Example
The full example of creating configuration for MyInterview widget:
```kotlin
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
There are two options to integrate MyInterview Widget into the app: `MyInterviewActivity` and `MyInterviewView`.

### **MyInterviewActivity** integration
The most easiest way start using MyInterview widget is to use `MyInterviewActivity`.
It could be started using `context.startActivity(Intent)` and `context.startActivityForResult(Intent, Int)`.
```kotlin
val configuration = createMyInteviewWidgetConfiguration()
context.startActivity(MyInterviewActivity.createIntent(this, sampleConfiguration), MY_INTERVIEW_REQUEST_CODE)
```
or 
```kotlin
val configuration = createMyInteviewWidgetConfiguration()
context.startActivityForResult(MyInterviewActivity.createIntent(this, sampleConfiguration), MY_INTERVIEW_REQUEST_CODE)
```

##### Handling activity result
The app could handle the result of interview process.
In `Activity` or `Fragment` app should override `onActivityResult(Int,Int,Intent)` method. Example:
```kotlin
override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
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

### **MyInterviewView** integration
`MyInterviewView` integration option is more flexible but requires some additional coding.

##### Layout
The example of using `MyInterviewView` in layout file:
```xml
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
This is the minimum amount of code that is required to integrate `MyInterviewView` in Activity or Fragment.
```kotlin
class UseViewSampleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle) {
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

- set `listener` to `MyInterviewView` - implementation of `Listener` interface. It's used for listening results of interview process.

- set `permissionCallback` to `MyInterview` - implementation of `RequestPermissionCallback` interface. It's used for requesting permissions.

- call `MyInterviewView#onStart()` and `MyInterviewView#onStop()` in appropriate lifecycle callbacks for correct lifecycle handling

- call `MyInterviewView#onRequestPermissionResult(Int,Array<String>,IntArray)` inside `onRequestPermissionResult(Int,Array<String>,IntArray)` in target Activity or Fragment to handle request permission results

- **To start interview process** the app should invoke `MyInterviewView#startSurvey(Configuration)`

##### Activity or Fragment: handling results
For handling results is using `Listener`:
```kotlin
myinterview_view.listener = object : Listener {
   override fun onError(throwable: Throwable) {
        // Place for error handling
   }
                 
    override fun onCanceled() {
        // Would be invoked in case when user canceled interview process
    }
                                 
    override fun onCompleted() {
        // Would be invoked in case when user 
        // successfully finished interview process
    }
}
```

## License
```
Copyright 2018 Myinterview Solutions Pty Ltd.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

[myInterview.com]: https://www.myinterview.com/?utm_source=Github-android&utm_medium=web&utm_campaign=Github-SDK&utm_content=SDK-Android-Github