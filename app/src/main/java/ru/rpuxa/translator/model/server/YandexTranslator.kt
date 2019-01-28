package ru.rpuxa.translator.model.server

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import ru.rpuxa.translator.await
import ru.rpuxa.translator.model.data.Language
import ru.rpuxa.translator.model.data.Phrase
import ru.rpuxa.translator.model.data.TranslatedPhrase

object YandexTranslator : ServerTranslator {
    private const val API_KEY = "trnsl.1.1.20190124T145116Z.4dacc51ed78c5230.b65671914c1c9236f6c2884574c856d67d2a9ab8"
    private const val SITE = "https://translate.yandex.net/api/v1.5/tr.json/"
    private const val ENGLISH_LANGUAGE = "en"
    private val impl: YandexRetrofitInterface = create()

    private fun create(): YandexRetrofitInterface {
        val retrofit = Retrofit.Builder()
                .baseUrl(SITE)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        return retrofit.create(YandexRetrofitInterface::class.java)
    }

    override suspend fun getLanguages(): List<Language>? {
        try {
            val result = impl.getLanguages(ENGLISH_LANGUAGE, API_KEY).await()
            if (result.isSuccessful)
                return result.body().toLanguageList()
        } catch (e: Exception) {
        }
        return null
    }

    override suspend fun translate(from: Phrase, to: Language): TranslatedPhrase? {
        try {
            val response = impl.getTranslate(
                    from.text,
                    "${from.language.code}-${to.code}",
                    API_KEY
            ).await()
            if (response.isSuccessful) {
                return TranslatedPhrase(from, Phrase(to, response.body().text))
            }
        } catch (e: Exception) {
        }
        return null
    }

    private interface YandexRetrofitInterface {

        @GET("getLangs")
        fun getLanguages(
                @Query("ui") ui: String,
                @Query("key") apiKey: String
        ): Call<Languages>


        @GET("translate")
        fun getTranslate(
                @Query("text") text: String,
                @Query("lang") language: String,
                @Query("key") apiKey: String
        ): Call<Translate>
    }


    private class Languages {

        @Expose
        @SerializedName("dirs")
        private lateinit var codes: Array<String>

        @Expose
        @SerializedName("langs")
        private lateinit var languages: HashMap<String, String>

        fun toLanguageList(): List<Language> {
            val codeSet = HashSet<String>()
            for (code in codes) {
                codeSet += code.split('-')
            }
            return codeSet.map { Language(it, languages[it]!!) }
        }
    }

    private class Translate {

        @Expose
        @SerializedName("text")
        private lateinit var _text: Array<String>

        val text: String by lazy {
            val result = StringBuilder()
            for ((i, line) in _text.withIndex()) {
                result.append(line)
                if (i != _text.lastIndex)
                    result.append('\n')
            }

            result.toString()
        }
    }

}








