package ie.setu.fiveasidefinder

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    // Show the XML menu and open the screen selected by each button.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }
        // Find the View Pitches button by its ID in the XML layout.
        val viewPitchesButton = findViewById<Button>(R.id.viewMarksButton)
        // Find the Add Pitch button by its ID in the XML layout.
        val addPitchButton = findViewById<Button>(R.id.addMarkButton)

        // Open the pitch list when View Pitches is tapped.
        viewPitchesButton.setOnClickListener {
            startActivity(Intent(this, MarkListActivity::class.java))
        }

        // Open the existing add/edit form when Add Pitch is tapped.
        addPitchButton.setOnClickListener {
            startActivity(Intent(this, AddEditActivity::class.java))
        }
    }
}