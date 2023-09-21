package com.example.kakaoimagesearch_btype

import com.google.gson.*
import java.lang.reflect.Type

class KakaoCommonDataTypeAdapter : JsonSerializer<KakaoCommonData>, JsonDeserializer<KakaoCommonData> {

    private val gson = Gson()

    override fun serialize(src: KakaoCommonData, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        val json = gson.toJsonTree(src).asJsonObject
        when (src) {
            is ImageData.Document -> {
                json.addProperty("type", "ImageData.Document")
            }
            is VideoData.Document -> {
                json.addProperty("type", "VideoData.Document")
            }
        }
        return json
    }

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): KakaoCommonData {
        val jsonObject = json.asJsonObject
        val type = jsonObject["type"].asString
        return when (type) {
            "ImageData.Document" -> gson.fromJson(json, ImageData.Document::class.java)
            "VideoData.Document" -> gson.fromJson(json, VideoData.Document::class.java)
            else -> throw IllegalArgumentException("Unknown type: $type")
        }
    }
}
