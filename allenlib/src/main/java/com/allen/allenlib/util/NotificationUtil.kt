package com.allen.allenlib.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

object NotificationUtil {

    fun createNotificationChannel(
        context: Context,
        notificationData: BaseNotificationData
    ): String? {
        // NotificationChannels are required for Notifications on O (API 26) and above.
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // The id of the channel.
            val channelId: String = notificationData.channelId
            // The user-visible name of the channel.
            val channelName: CharSequence = notificationData.channelName
            // The user-visible description of the channel.
            val channelDescription: String = notificationData.channelDescription
            val channelImportance: Int = notificationData.channelImportance
            val channelEnableVibrate: Boolean = notificationData.channelEnableVibrate
            val channelLockScreenVisibility: Int =
                notificationData.channelLockScreenVisibility

            // Initializes NotificationChannel.
            val notificationChannel =
                NotificationChannel(channelId, channelName, channelImportance)
            notificationChannel.description = channelDescription
            notificationChannel.enableVibration(channelEnableVibrate)
            notificationChannel.lockscreenVisibility = channelLockScreenVisibility
            // Adds NotificationChannel to system. Attempting to create an existing notification
            // channel with its original values performs no operation, so it's safe to perform the
            // below sequence.
            val notificationManager =
                context.applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)

            channelId
        } else { // Returns null for pre-O (26) devices.
            null
        }
    }
}

abstract class BaseNotificationData {

    // Standard notification values:
    var contentTitle: String = String()
        protected set
    var contentText: String = String()
        protected set
    var priority: Int = 0
        protected set

    // Notification channel values (O and above):
    var channelId: String = String()
        protected set
    var channelName: CharSequence = String()
        protected set
    var channelDescription: String = String()
        protected set
    var channelImportance = 0
        protected set
    var channelEnableVibrate = false
        protected set
    var channelLockScreenVisibility = 0
        protected set


}