package com.persona5dex.views

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.persona5dex.R
import com.persona5dex.models.GameType

class PersonaListItemView(context: Context, attrs: AttributeSet?, defStyleAttr: Int): LinearLayout(context, attrs, defStyleAttr) {
    init {
        inflate(context, R.layout.view_persona_item, this)
    }
}

//fun getPersonaListItemView(gameType: GameType) =
//        when(gameType){
//            GameType.BASE -> R.style.PersonaListItem
//            GameType.ROYAL -> TODO()
//        }