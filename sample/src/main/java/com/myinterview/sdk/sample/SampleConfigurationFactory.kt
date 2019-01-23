package com.myinterview.sdk.sample

import android.content.Context
import com.myinterview.sdk.Configuration
import com.myinterview.sdk.FlowConfiguration
import com.myinterview.sdk.PracticeFlowConfiguration
import com.myinterview.sdk.Question
import java.util.*

object SampleConfigurationFactory {

    private const val API_KEY = "nAjCYN1dtqTWat2oq9X0"

    fun createSampleConfiguration(
        context: Context,
        isShowQuestions: Boolean = false
    ) = Configuration.build(context) {
        normalFlowConfiguration = FlowConfiguration.build {
            preparationTimeSecs = 15
            showQuestions = isShowQuestions
            apiKey = API_KEY
            questions = Arrays.asList(
                Question.Builder().build {
                    title = "Introduce yourself"
                    text = "Example: Hello my name is [Your Name] and " +
                            "have been a [Profession] for [Number of] years.\n" +
                            "One more line.\n" +
                            "And this one too."
                    tips = "The introduction is your opportunity to make a good first impression. " +
                            "Here are some tips on how to ace this part of the cover video:\n" +
                            "\n" +
                            "1. Dress professionally\n" +
                            "2. Look directly at the webcam\n" +
                            "3. Speak clearly and at a steady pace.\n" +
                            "4. Make sure that the room you are in is well lit and quiet.\n" +
                            "5. Smile!\n" +
                            "6. State your name, profession and years of experience (If applicable).!\n" +
                            "\n" +
                            "This section may take less than 15 seconds to complete, " +
                            "please click stop when you are finished speaking."
                    durationSecs = 61
                    attempts = 10
                },
                Question.Builder().build {
                    title = "What is your experience?"
                    text = "Give some examples of your work/ study/ life experiences " +
                            "(During my time at... I was able to... meaning I can now... for you)."
                    durationSecs = 60
                    attempts = 2
                }
            )
        }
        practiceFlowConfiguration = PracticeFlowConfiguration(
            questions = Arrays.asList(
                Question.Builder().build {
                    title = "What is your experience? (Practice)"
                    text = "Give some examples of your work/ study/ life experiences " +
                            "(During my time at... I was able to... meaning I can now... for you)."
                    durationSecs = 60
                    attempts = 2
                }
            )
        )
    }
}