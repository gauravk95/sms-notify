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
import com.github.sms.data.models.local.SmsItem
import com.github.sms.ui.main.MainActivity
import com.github.sms.utils.AppConstants
import com.github.sms.utils.AppLogger
import com.github.sms.utils.rx.RxEventBus


@Suppress("DEPRECATION")
class SmsReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        if (intent.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            var smsTimeStamp: Long? = 0
            var smsSender: String? = ""
            var smsBody: String? = ""

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                AppLogger.d(TAG, "Using telephony api to get sms")
                for (msg in Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                    smsBody += msg.messageBody
                    smsSender = msg.displayOriginatingAddress
                    smsTimeStamp = msg.timestampMillis
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
                        smsTimeStamp = msgs[0]?.timestampMillis
                    }
                }
            }

            //display the notification
            showNotification(context, smsTimeStamp, smsSender, smsBody)

            //send Event broadcast
            if (smsTimeStamp != null)
                RxEventBus.publish(SmsItem(smsTimeStamp.toString(), smsSender, smsBody, 0, smsTimeStamp))
        }
    }

    private fun showNotification(context: Context, smsId: Long?, smsSender: String?, smsBody: String?) {

        //prepare the content
        val contentTitle: String = smsSender ?: context.getString(R.string.app_name)

        //create a call to action
        val pendingIntent: PendingIntent
        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra(AppConstants.INTENT_ARGS_SMS_ID, smsId)
        pendingIntent = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_ONE_SHOT)

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
                .setContentText(smsBody)
                .setStyle(NotificationCompat.BigTextStyle().bigText(smsBody))
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

