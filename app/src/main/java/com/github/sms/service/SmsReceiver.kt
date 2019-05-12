package com.github.sms.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.telephony.SmsMessage
import android.provider.Telephony
import android.os.Build
import android.support.v4.app.NotificationCompat
import com.github.sms.R
import com.github.sms.data.models.event.SmsUpdateAction
import com.github.sms.data.models.event.SmsUpdateEvent
import com.github.sms.data.models.local.SmsItem
import com.github.sms.ui.main.MainActivity
import com.github.sms.utils.AppConstants
import com.github.sms.utils.AppLogger
import com.github.sms.utils.rx.RxEventBus

@Suppress("DEPRECATION")
class SmsReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        if (intent.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            var smsTimestamp: Long? = -1L
            var smsSender: String? = ""
            var smsBody: String? = ""

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                AppLogger.d(TAG, "Using telephony api to get sms")
                for (msg in Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                    smsBody += msg.messageBody
                    smsSender = msg.displayOriginatingAddress
                    smsTimestamp = msg.timestampMillis
                }
            } else {
                AppLogger.d(TAG, "Using deprecated api to get sms")
                val bundle = intent.extras
                if (bundle != null) {
                    val pdus = bundle.get("pdus") as Array<*>
                    val msgs = arrayOfNulls<SmsMessage>(pdus.size)
                    for (i in pdus.indices) {
                        msgs[i] = SmsMessage.createFromPdu(pdus[i] as ByteArray)
                        smsBody += msgs[i]?.messageBody
                        smsSender = msgs[0]?.originatingAddress
                        smsTimestamp = msgs[0]?.timestampMillis
                    }
                }
            }

            //NOTE: SMS_ID/SMS ICC EF Record has not been created yet, so we initialized id to some value
            val sms = SmsItem(Integer.MAX_VALUE, smsSender, smsBody, 0, smsTimestamp ?: -1L)
            //display the notification
            showNotification(context, sms)

            //send Event broadcast
            if (smsTimestamp != null)
                RxEventBus.publish(SmsUpdateEvent(SmsUpdateAction.UPDATE))
        }
    }

    private fun showNotification(context: Context, sms: SmsItem) {

        //prepare the content
        val contentTitle: String = sms.address ?: context.getString(R.string.app_name)

        //create a call to action
        val pendingIntent: PendingIntent
        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP and Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra(AppConstants.INTENT_ARGS_SMS, sms)
        //use FLAG_UPDATE_CURRENT to update extras in case of multiple pending intents
        pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        //create a notification channel, for oreo and higher
        val mNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "SMS"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel(AppConstants.DEFAULT_NOTIFICATION_CHANNEL, name, importance)
            mNotificationManager.createNotificationChannel(mChannel)
        }

        //create the notification and show it
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(context, AppConstants.DEFAULT_NOTIFICATION_CHANNEL)
                .setContentTitle(contentTitle)
                .setSmallIcon(R.drawable.ic_small_notification)
                .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher))
                .setContentText(sms.message)
                .setStyle(NotificationCompat.BigTextStyle().bigText(sms.message))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setVibrate(longArrayOf(300, 300, 300))
                .setContentIntent(pendingIntent)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notificationBuilder.build())

    }

    companion object {
        const val TAG = "SmsReceiver"
    }
}

