package com.osl.base.project.main.views.alarm

import android.app.AlarmManager
import android.app.AlarmManager.AlarmClockInfo
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.provider.AlarmClock
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.gun0912.tedpermission.provider.TedPermissionProvider
import com.osl.base.project.main.R
import com.osl.base.project.main.databinding.FragmentAlarmBinding
import com.osl.base.project.main.utils.date.getCalendarTimestamp
import com.osl.base.project.main.views.container.main.ActMainViewModel
import com.osl.base.project.osl.views.OslFragment
import timber.log.Timber.Forest.d
import java.util.*


class AlarmFragment : OslFragment<FragmentAlarmBinding, AlarmViewModel>() {
    override val layoutRes = R.layout.fragment_alarm
    override val destinationId = R.id.calendarFragment
    override val viewModelClass = AlarmViewModel::class.java
    override val actViewModelClass = ActMainViewModel::class.java
    private var alarmManager: AlarmManager? = null
    private var br: BroadcastReceiver? = null

    override fun initViews(savedInstanceState: Bundle?) {
        ui.viewDataBinding.viewModel = viewModel

        ui.viewDataBinding.localAlarm.setOnClickListener {
            setAlarmManager()
        }

        ui.viewDataBinding.showAlarmApp.setOnClickListener {
            setAlarmManagerTwo()
        }

        registerReceiver()
    }

    override fun onDestroy() {
        super.onDestroy()
        unRegisterReceiver()
    }

    override fun addObservers() {
    }

    /** setAlarmClock 사용시 하루 다음날 시간으로 지정 할때 사용
     * 나중에 기획서 알람 시간 정확히 알면 수정해서 사용이 필요*/
    fun covertAddDate(time: Long): Long {
        return GregorianCalendar.getInstance().apply {
            timeInMillis = time
            add(Calendar.DATE,1)
//            set(Calendar.HOUR_OF_DAY, 0)
//            set(Calendar.MINUTE, 0)
//            set(Calendar.SECOND, 0)
        }.timeInMillis
    }

    /** 기본 알람 사용(setExactAndAllowWhileIdle 사용 예정)
     * set, setWindow, setExact(정확한 시간에 가지 않는다)
     * setAndAllowWhileIdle() : 해당 앱만 도즈 모드에서 잠깐 깨어난다.
     * setExactAndAllowWhileIdle() :  해당 앱반 도즈 모드에서 잠깐 깨어난다, setAndAllowWhileIdle 보다 정확한 시간에 알람 동작, 시간에 오차가 있을 수 있음
     * setAlarmClock() : 해당 앱 뿐만 아니라 다른 앱까지 도즈 상태에서 벗어난다. 제일 정확한 시간에 동작 시킬 수 있음(배터리 소모 증가),<uses-permission android:name="android.permission.SCHEDULE_EXACT_ALARM" /> 사용필요 */
    /** 반복 알람 사용
     * setAlarmClock()을 사용하는게 정확하다 생각함
     * setReating : 정확하지 않음
     * setInexactRepeating : 마찬가지로 정확하지 않음
     * */
    private fun setAlarmManager() {
        if (alarmManager == null) {
            alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val triggerAddTime: Long = (1 * 60 * 1000)//1분 //(5 * 60 * 1000) //5 min
            /** 시간값 사용
             * System.currentTimeMillis() : 현재 시간(ex:2023-04-12 10:09:38)
             * SystemClock.elapsedRealtime() : 부팅후 시간(ex:1970-01-06 09:05:15)
             * */
            val triggerTime = System.currentTimeMillis() + triggerAddTime//covertAddDate(triggerTime)
//            val triggerTime = SystemClock.elapsedRealtime() + triggerAddTime
            /**
             * setInexactRepeating 반복에 사용
             * AlarmManager.INTERVAL_DAY (하루마다 반복)
             * */
            val repeatInterval: Long = AlarmManager.INTERVAL_DAY

            d("AlarmReceiver setAlarm currentTime: ${getCalendarTimestamp(System.currentTimeMillis())}")
            d("AlarmReceiver setAlarm elapsedRealtime: ${getCalendarTimestamp(SystemClock.elapsedRealtime())}")
            d("AlarmReceiver setAlarm triggerTime : ${getCalendarTimestamp(triggerTime)}")
            d("AlarmReceiver setAlarm triggerTime add +1 : ${getCalendarTimestamp(covertAddDate(triggerTime))}")
            d("AlarmReceiver setAlarm INTERVAL_DAY : ${repeatInterval}")

            val intent = Intent(context, AlarmReceiver::class.java)
            intent.putExtra("alarmMessage","basic")
//            val alarmIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
            val alarmIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE)
            } else {
                PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            }

//            alarmManager?.setAlarmClock(AlarmManager.AlarmClockInfo(triggerTime, alarmIntent), alarmIntent)
            /** setExactAndAllowWhileIdle 시간값 사용
             * ELAPSED_REALTIME_WAKEUP : SystemClock.elapsedRealtime() 사용
             * RTC_WAKEUP : System.currentTimeMillis() 사용
             * */
            alarmManager?.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, alarmIntent)
//            alarmManager?.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, alarmIntent)
            /** 반복 */
            //alarmManager?.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, repeatInterval, alarmIntent)
        }
    }

    /** REQUEST_CODE와 intent가 일치 하는 알람을 제거 한다*/
    private fun alarmManagerCancel(){
        val alarmManager =context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra("alarmMessage","basic")
        val alarmIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager.cancel(alarmIntent)
    }

    /** 기본 알람앱 이용하는 함수
     * <uses-permission android:name="com.android.alarm.permission.SET_ALARM" /> 설정 필요*/
    private fun setAlarmManagerTwo(){
        val intent = Intent(AlarmClock.ACTION_SET_ALARM)
        intent.putExtra(AlarmClock.EXTRA_MESSAGE, "My alarm")
        intent.putExtra(AlarmClock.EXTRA_HOUR, 8)
        intent.putExtra(AlarmClock.EXTRA_MINUTES, 30)
        startActivity(intent)
    }

    /** local broadcast Receiver
     * 로컬 브로드캐스트 테스트용 코드(앱 끄면 의미없음)*/
    private fun registerReceiver() {
        val context = context?:return
        br = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val getData = intent.getStringExtra("LOCAL_BROADCAST_KEY")
                d("AlarmReceiver localReceiverTest onReceive : ${getData}")
            }
        }
        br?.let { it ->
            LocalBroadcastManager.getInstance(context)
                .registerReceiver(it, IntentFilter("LOCAL_BROADCAST_ACTION"))
        }
    }

    private fun unRegisterReceiver() {
        val context = context?:return
        br?.let { it ->
            LocalBroadcastManager.getInstance(context).unregisterReceiver(it)
        }
    }

}