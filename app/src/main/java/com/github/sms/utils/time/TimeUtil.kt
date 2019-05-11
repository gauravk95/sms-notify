package com.github.sms.utils.time

import com.github.sms.R
import com.github.sms.utils.AppConstants
import java.util.*

object TimeUtil {

    fun isBeyondTimeDifference(time: Long, timeDiffInMilli: Long): Boolean = (Date().time - time) > timeDiffInMilli

    fun getTimeGroupFromTime(time: Long): TimeGroup {
        when {
            isBeyondTimeDifference(time, 48 * AppConstants.HOUR_IN_MILLI) -> return TimeGroup.OTHERS
            isBeyondTimeDifference(time, 24 * AppConstants.HOUR_IN_MILLI) -> return TimeGroup.ONE_DAY
            isBeyondTimeDifference(time, 12 * AppConstants.HOUR_IN_MILLI) -> return TimeGroup.TWELVE_HOUR
            isBeyondTimeDifference(time, 6 * AppConstants.HOUR_IN_MILLI) -> return TimeGroup.SIX_HOUR
            isBeyondTimeDifference(time, 3 * AppConstants.HOUR_IN_MILLI) -> return TimeGroup.THREE_HOUR
            isBeyondTimeDifference(time, 2 * AppConstants.HOUR_IN_MILLI) -> return TimeGroup.TWO_HOUR
            isBeyondTimeDifference(time, AppConstants.HOUR_IN_MILLI) -> return TimeGroup.HOUR
        }
        return TimeGroup.ZERO
    }

    fun getTextIdFromTimeGroup(timeGroup: TimeGroup): Int {
        return when (timeGroup) {
            TimeGroup.ZERO -> R.string.time_zero_hour_ago
            TimeGroup.HOUR -> R.string.time_one_hour_ago
            TimeGroup.TWO_HOUR -> R.string.time_two_hour_ago
            TimeGroup.THREE_HOUR -> R.string.time_three_hour_ago
            TimeGroup.SIX_HOUR -> R.string.time_six_hour_ago
            TimeGroup.TWELVE_HOUR -> R.string.time_twelve_hour_ago
            TimeGroup.ONE_DAY -> R.string.time_one_day_ago
            TimeGroup.OTHERS -> R.string.time_other_ago
        }
    }
}