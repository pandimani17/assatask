package com.assaabloy.task.domain.lockconfig.model

sealed class ParamDefinition {
    abstract val key: String
    abstract val displayName: String
    abstract val defaultValue: String
}


data class ChoiceParams(
    override val key: String,
    override val displayName: String,
    val values: List<String>,
    override val defaultValue: String
) : ParamDefinition()


data class RangeParams(
    override val key: String,
    override val displayName: String,
    val min: Float,
    val max: Float,
    val unit: String,
    override val defaultValue: String
) : ParamDefinition()