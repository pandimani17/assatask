package com.assaabloy.task.data.lockconfig.remote.dto

data class LockConfigurationDto(
    val lockVoltage: ChoiceDto,
    val lockType: ChoiceDto,
    val lockKick: ChoiceDto,
    val lockRelease: ChoiceDto,
    val lockReleaseTime: RangeDto,
    val lockAngle: RangeDto
)