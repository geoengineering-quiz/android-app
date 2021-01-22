package app.geoengineering.quiz

import com.beust.klaxon.Json

data class Questions(
        val questions: Array<Question>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Questions

        if (!questions.contentEquals(other.questions)) return false

        return true
    }

    override fun hashCode(): Int {
        return questions.contentHashCode()
    }
}

data class Question(
        @Json(name = "q")
        val question: String,
        @Json(name = "o")
        val options: Array<String>,
        @Json(name = "a")
        val answer: Int,
        @Json(name = "i")
        val info: String,
        val info_url: String? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Question

        if (question != other.question) return false
        if (!options.contentEquals(other.options)) return false
        if (answer != other.answer) return false
        if (info != other.info) return false

        return true
    }

    override fun hashCode(): Int {
        var result = question.hashCode()
        result = 31 * result + options.contentHashCode()
        result = 31 * result + answer
        result = 31 * result + info.hashCode()
        return result
    }
}
