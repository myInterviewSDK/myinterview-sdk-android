package com.myinterview.sdk.sample

import com.myinterview.sdk.Configuration
import com.myinterview.sdk.Question
import java.util.*

object SampleConfigurationFactory {

    private const val API_KEY = "qDp2egprFa"

    fun createSampleConfiguration(isShowQuestions: Boolean = false): Configuration = Configuration.Builder().build {
        preparationTimeSecs = 15
        showQuestions = isShowQuestions
        apiKey = API_KEY
        questions = Arrays.asList(Question.Builder().build {
            title = "Introduce yourself"
            text = "Example: Hello my name is [Your Name] and have been a [Profession] for [Number of] years.\n" +
                    "One more line.\n" +
                    "And this one too."
            durationSecs = 61
            attempts = 10
        }, Question.Builder().build {
            title = "What is your experience?"
            text = "Give some examples of your work/ study/ life experiences " +
                    "(During my time at... I was able to... meaning I can now... for you)."
            durationSecs = 60
            attempts = 10
        })
    }
}