package com.example.verboversa

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import android.widget.EditText
import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.google.cloud.translate.Translate
import com.google.cloud.translate.TranslateOptions
import com.google.cloud.translate.Translation
import android.speech.tts.TextToSpeech
import androidx.core.content.ContextCompat.startActivity
import java.util.Locale

class LoggedInActivity : AppCompatActivity(), TextToSpeech.OnInitListener, TabLayout.OnTabSelectedListener {


    // Declare sourceLanguageCode and targetLanguageCode as class-level properties
    private var sourceLanguageCode = "en" // Default source language code (English)
    private var targetLanguageCode = "es" // Default target language code (Spanish)

    private lateinit var tts: TextToSpeech
    private lateinit var listenButton2: Button
    private lateinit var listenButton1: Button
    private lateinit var textInput: EditText
    private lateinit var translationResult: TextView


    // A map of language names to their ISO-639-1 codes
    private val languageMap = mapOf(
        "Afrikaans" to "af",
        "Albanian" to "sq",
        "Amharic" to "am",
        "Akan" to "aka", // Assuming "aka" is accepted by your application or API
        "Arabic" to "ar",
        "Armenian" to "hy",
        "Azerbaijani" to "az",
        "Basque" to "eu",
        "Belarusian" to "be",
        "Bengali" to "bn",
        "Bosnian" to "bs",
        "Bulgarian" to "bg",
        "Catalan" to "ca",
        "Cebuano" to "ceb",
        "Chichewa" to "ny",
        "Chinese (Simplified)" to "zh-CN",
        "Chinese (Traditional)" to "zh-TW",
        "Corsican" to "co",
        "Croatian" to "hr",
        "Czech" to "cs",
        "Danish" to "da",
        "Dutch" to "nl",
        "English" to "en",
        "Esperanto" to "eo",
        "Estonian" to "et",
        "Filipino" to "tl",
        "Finnish" to "fi",
        "French" to "fr",
        "Frisian" to "fy",
        "Galician" to "gl",
        "Georgian" to "ka",
        "German" to "de",
        "Greek" to "el",
        "Gujarati" to "gu",
        "Haitian Creole" to "ht",
        "Hausa" to "ha",
        "Hawaiian" to "haw",
        "Hebrew" to "he",
        "Hindi" to "hi",
        "Hmong" to "hmn",
        "Hungarian" to "hu",
        "Icelandic" to "is",
        "Igbo" to "ig",
        "Indonesian" to "id",
        "Irish" to "ga",
        "Italian" to "it",
        "Japanese" to "ja",
        "Javanese" to "jv",
        "Kannada" to "kn",
        "Kazakh" to "kk",
        "Khmer" to "km",
        "Kinyarwanda" to "rw",
        "Korean" to "ko",
        "Kurdish (Kurmanji)" to "ku",
        "Kyrgyz" to "ky",
        "Lao" to "lo",
        "Latin" to "la",
        "Latvian" to "lv",
        "Lithuanian" to "lt",
        "Luxembourgish" to "lb",
        "Macedonian" to "mk",
        "Malagasy" to "mg",
        "Malay" to "ms",
        "Malayalam" to "ml",
        "Maltese" to "mt",
        "Maori" to "mi",
        "Marathi" to "mr",
        "Mongolian" to "mn",
        "Myanmar (Burmese)" to "my",
        "Nepali" to "ne",
        "Norwegian" to "no",
        "Odia (Oriya)" to "or",
        "Pashto" to "ps",
        "Persian" to "fa",
        "Polish" to "pl",
        "Portuguese" to "pt",
        "Punjabi" to "pa",
        "Romanian" to "ro",
        "Russian" to "ru",
        "Samoan" to "sm",
        "Scots Gaelic" to "gd",
        "Serbian" to "sr",
        "Sesotho" to "st",
        "Shona" to "sn",
        "Sindhi" to "sd",
        "Sinhala" to "si",
        "Slovak" to "sk",
        "Slovenian" to "sl",
        "Somali" to "so",
        "Spanish" to "es",
        "Sundanese" to "su",
        "Swahili" to "sw",
        "Swedish" to "sv",
        "Tajik" to "tg",
        "Tamil" to "ta",
        "Tatar" to "tt",
        "Telugu" to "te",
        "Thai" to "th",
        "Turkish" to "tr",
        "Turkmen" to "tk",
        "Ukrainian" to "uk",
        "Urdu" to "ur",
        "Uyghur" to "ug",
        "Uzbek" to "uz",
        "Vietnamese" to "vi",
        "Welsh" to "cy",
        "Xhosa" to "xh",
        "Yiddish" to "yi",
        "Yoruba" to "yo",
        "Zulu" to "zu"
        // This is a comprehensive example list, but make sure to verify and update based on current API support.
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logged_in) // Make sure to use your actual layout file name here

        val translateFromButton = findViewById<Button>(R.id.translate_from)
        val translateToButton = findViewById<Button>(R.id.translate_to)
        val textInput = findViewById<EditText>(R.id.text_input)
        val translateButton = findViewById<Button>(R.id.translate_button)
        val translationResult = findViewById<TextView>(R.id.translation_result)
        listenButton2 = findViewById(R.id.listen_button_to)
        listenButton1 = findViewById(R.id.listen_button_from)
        val tabLayout = findViewById<TabLayout>(R.id.translation_tabs)
        tabLayout.addOnTabSelectedListener(this)


        // Setting up click listeners for both buttons
        translateFromButton.setOnClickListener { view ->
            showLanguageMenu(view, true) // true for source language
        }

        translateToButton.setOnClickListener { view ->
            showLanguageMenu(view, false) // false for target language
        }

        translateButton.setOnClickListener {
            val text = textInput.text.toString()
            if (text.isNotEmpty()) {
                translateText(text, translationResult)
            }
        }


        // Initialize TextToSpeech
        tts = TextToSpeech(this@LoggedInActivity, this@LoggedInActivity as TextToSpeech.OnInitListener)


        listenButton1.setOnClickListener {
            // Check if TextToSpeech is successfully initialized
            listenButton1.setOnClickListener {
                if (textInput.text.isNotEmpty() || translationResult.text.isNotEmpty()) {
                    speakOut(textInput.text.toString() )
                }
            }
        }

        listenButton2.setOnClickListener {
            // Check if TextToSpeech is successfully initialized
            listenButton2.setOnClickListener {
                if (textInput.text.isNotEmpty() || translationResult.text.isNotEmpty()) {
                    speakOut(translationResult.text.toString())
                }
            }
        }
    }
        private fun speakOut(text: String) {
            // Speak out the text
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
        }


        override fun onInit(status: Int) {
            if (status == TextToSpeech.SUCCESS) {
                // Set language for TTS
                val result = tts.setLanguage(Locale.getDefault())
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    // Handle case where language data is missing or language is not supported
                }
            } else {
                // Initialization failed
            }
        }


        override fun onDestroy() {
            if (::tts.isInitialized) {
                tts.stop()
                tts.shutdown()
            }
            super.onDestroy()
        }

        private fun showLanguageMenu(view: View, isSourceLanguage: Boolean) {
            val popup = PopupMenu(this, view)
            languageMap.forEach { (name, _) ->
                popup.menu.add(name)
            }

            popup.setOnMenuItemClickListener { menuItem ->
                val selectedLanguageName = menuItem.title.toString()
                val selectedLanguageCode = languageMap[selectedLanguageName]

                if (isSourceLanguage) {
                    sourceLanguageCode = selectedLanguageCode ?: "en"
                    (view as Button).text = selectedLanguageName
                } else {
                    targetLanguageCode = selectedLanguageCode ?: "es"
                    (view as Button).text = selectedLanguageName
                }

                true
            }

            popup.show()
        }






    private fun translateText(inputText: String, resultView: TextView) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Initialize the Google Cloud Translation client
                val translateOptions = TranslateOptions.newBuilder().setApiKey("AIzaSyDdzrZq7OjjREuNpDUPOUNcRJOMx55M1TY").build()
                val translate = translateOptions.service

                // Perform the translation
                val translation: Translation = translate.translate(
                    inputText,
                    Translate.TranslateOption.sourceLanguage(sourceLanguageCode),
                    Translate.TranslateOption.targetLanguage(targetLanguageCode),
                    // Use the model for your specific needs
                    Translate.TranslateOption.model("base")
                )

                withContext(Dispatchers.Main) {
                    // Update the UI with the translation result
                    resultView.text = translation.translatedText
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    // Handle any errors in a way suitable for your app
                    resultView.text = "Translation failed: ${e.localizedMessage}"
                }
            }
        }
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        when (tab?.position) {
            0 -> {
                // The "Write" tab is selected, do nothing or refresh LoggedInActivity if needed
                if (this !is LoggedInActivity) {
                    val intent = Intent(this, LoggedInActivity::class.java)
                    startActivity(intent)}
            }
            1 -> {
                // The "Speech" tab is selected, start SpeechActivity2
                val intent = Intent(this, SpeechActivity2::class.java)
                startActivity(intent)
            }
            // Add more cases if there are more tabs
            2 -> {
                // The "Speech" tab is selected, start SpeechActivity2
                val intent = Intent(this, Image::class.java)
                startActivity(intent)
            }
            else -> {
                // Handle unknown tab
                val intent = Intent(this, LoggedInActivity::class.java)
                startActivity(intent)
            }

        }

    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
        // Handle tab unselected state if needed
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {
        // Handle tab reselected state if needed
    }

    // other overridden methods



}