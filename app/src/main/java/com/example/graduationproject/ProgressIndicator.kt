package com.example.graduationproject
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.progressindicator.LinearProgressIndicator

class ProgressIndicator : AppCompatActivity() {
    private lateinit var lpi: LinearProgressIndicator
    private lateinit var cpi: CircularProgressIndicator
    private lateinit var mHandler: Handler
    private lateinit var edittxt: EditText

    @SuppressLint("MissingInflatedId", "Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_progress_indicator)
        /*  lpi = findViewById(R.id.lpi)
        cpi = findViewById(R.id.cpi)
      val z=findViewById<ImageView>(R.id.ze)
        z.animate().translationX(-1000f).duration =5000
        z.animate().translationY(1000f).duration =5000

        z.animate().translationX(1000f).duration =5000
        z.animate().translationY(-1000f).duration =5000

       // lpi.isIndeterminate = false
      //  cpi.isIndeterminate = false
      /*  val time = object : Timer()*/
        /*  var task = object : TimerTask() {
         var progress by Delegates.notNull<Int>()
           @SuppressLint("SetTextI18n")
           override fun run() {
               progress+=progress
               lpi.isIndeterminate=true
               cpi.isIndeterminate=true
               mHandler.post(Runnable {
                       edittxt.setText("downloading$progress%") })
               if(progress==100){ time.cancel()}
           }
       }
        time.schedule(task,1000,1000)
    }*/
    }
}*/
    }
}

