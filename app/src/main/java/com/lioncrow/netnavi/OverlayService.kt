package com.lioncrow.netnavi
import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.view.*
import android.widget.*
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform

class OverlayService : Service() {
    private lateinit var wm: WindowManager
    private lateinit var view: View

    override fun onBind(intent: Intent): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        if (!Python.isStarted()) Python.start(AndroidPlatform(this))
        val engine = Python.getInstance().getModule("lex_infinita.core.singularity_engine")
        val orchestrator = engine.callAttr("HarmoniaOrchestrator", "Gustavo Arturo Alba")

        wm = getSystemService(WINDOW_SERVICE) as WindowManager
        view = FrameLayout(this).apply {
            setBackgroundColor(0x99000000.toInt()) // Semi-transparent black
            val text = TextView(context).apply {
                text = "HARMONIA: STANDBY"
                setTextColor(0xFF00FFFF.toInt())
                textSize = 14f
                setPadding(20, 20, 20, 20)
            }
            addView(text)
            setOnClickListener {
                val res = orchestrator.callAttr("generate_economic_manifestation").toString()
                text.text = "PROTOCOL OMEGA: $res"
            }
        }

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )
        wm.addView(view, params)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::view.isInitialized) wm.removeView(view)
    }
}
