package br.com.hellodev.domain.model.sheet

import br.com.hellodev.core.enums.sheet.BottomSheetType

data class DefaultSheetModel(
    val type: BottomSheetType? = null,
    val message: String? = "",
    val title: String? = null,
    val firstButtonText: String? = null,
    val secondButtonText: String? = null
)