package ie.setu.fiveasidefinder

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var listLayout: LinearLayout

    // Create the pitch list screen when this Activity starts.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createUserInterface()
    }

    // Refresh the list when returning from the add/edit screen.
    override fun onResume() {
        super.onResume()

        if (::listLayout.isInitialized) {
            displayPitches()
        }
    }

    // Build the title, add button and list area using Kotlin views.
    private fun createUserInterface() {
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32, 320, 32, 32)
        }

        val title = TextView(this).apply {
            text = "Pitches"
            textSize = 28f
            gravity = Gravity.CENTER
        }

        val addButton = Button(this).apply {
            text = "Add Pitch"
            setOnClickListener {
                val intent = Intent(this@MainActivity, AddEditActivity::class.java)
                startActivity(intent)
            }
        }

        listLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }

        root.addView(
            title,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        )

        root.addView(
            addButton,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        )

        root.addView(
            listLayout,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        )

        setContentView(root)
        displayPitches()
    }

    // Rebuild the visible list from the shared store, with edit and delete buttons.
    private fun displayPitches() {
        listLayout.removeAllViews()

        val pitches = AppData.pitches.findAll()

        if (pitches.isEmpty()) {
            val emptyText = TextView(this).apply {
                text = "No pitches yet."
                textSize = 18f
                setPadding(0, 40, 0, 40)
            }

            listLayout.addView(emptyText)
            return
        }

        for (pitch in pitches) {
            val pitchLayout = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(0, 20, 0, 20)
            }

            val pitchName = TextView(this).apply {
                text = "${pitch.id}: ${pitch.name}"
                textSize = 20f
            }

            val pitchDescription = TextView(this).apply {
                text = pitch.description
                textSize = 16f
            }

            val coordinates = TextView(this).apply {
                text = "Latitude: ${pitch.latitude}, Longitude: ${pitch.longitude}"
                textSize = 14f
            }

            val editButton = Button(this).apply {
                text = "Edit"

                setOnClickListener {
                    val intent = Intent(
                        this@MainActivity,
                        AddEditActivity::class.java
                    )

                    intent.putExtra("id", pitch.id)
                    startActivity(intent)
                }
            }

            val deleteButton = Button(this).apply {
                text = "Delete"

                setOnClickListener {
                    AppData.pitches.delete(pitch.id)
                    displayPitches()
                }
            }

            pitchLayout.addView(pitchName)
            pitchLayout.addView(pitchDescription)
            pitchLayout.addView(coordinates)
            pitchLayout.addView(editButton)
            pitchLayout.addView(deleteButton)

            listLayout.addView(pitchLayout)
        }
    }
}