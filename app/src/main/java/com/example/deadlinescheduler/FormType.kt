package com.example.deadlinescheduler

import java.io.Serializable

sealed class FormType : Serializable {
    data object New : FormType(){
        private fun readResolve(): Any = New
    }
    data class Edit(val itemId: Int) : FormType()
}