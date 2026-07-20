package uk.co.btsda.syllabus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import uk.co.btsda.syllabus.ui.SyllabusApp
import uk.co.btsda.syllabus.ui.theme.TSDTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TSDTheme {
                SyllabusApp()
            }
        }
    }
}
