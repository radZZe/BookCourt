package com.example.bookcourt.models.library

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable
@Serializable
data class CatalogResponse(
    val bookBlocks: List<BookBlock>,
    @SerializedName("infoBlock")val infoBlocks: List<InfoBlock>
)
