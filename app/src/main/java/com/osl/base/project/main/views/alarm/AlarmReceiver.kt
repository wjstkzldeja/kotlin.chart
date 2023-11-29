package com.osl.base.project.main.views.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.osl.base.project.main.utils.date.getCalendarTimestamp
import com.osl.swrnd.data.pref.PrefManager
import com.osl.swrnd.entity.local.res.TimeListVo
import timber.log.Timber.Forest.d
import java.text.SimpleDateFormat
import java.util.*

/** 알람 매니저 리시버*/
class AlarmReceiver : BroadcastReceiver() {

  override fun onReceive(context: Context, intent: Intent) {
    val message = intent.getStringExtra("alarmMessage")
    d("AlarmReceiver intent message : ${message}")
    timeTestCode()

    sendBroadcastMsg(context)
  }

  private fun sendBroadcastMsg(context: Context) {
    val intent = Intent("LOCAL_BROADCAST_ACTION")
    intent.putExtra("LOCAL_BROADCAST_KEY", "reciver test")
    LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
  }

  private fun timeTestCode() {
    val currentDate =  getCalendarTimestamp(System.currentTimeMillis())

    d("AlarmReceiver currentDate : $currentDate")
    val timeList = ArrayList<TimeListVo>()
    val prefGetTimeList = PrefManager.testTimeList
    prefGetTimeList?.let {
      timeList.addAll(prefGetTimeList)
    }
    val currentTimeVo = TimeListVo(currentDate)
    timeList.add(currentTimeVo)
    PrefManager.testTimeList = timeList

    d("AlarmReceiver PrefManager.testTimeList : ${PrefManager.testTimeList}")
  }
}