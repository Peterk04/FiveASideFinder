package ie.setu.fiveasidefinder

// Use the search control added to the XML.
import androidx.appcompat.widget.SearchView
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MarkListActivity : AppCompatActivity() {

    // Keep the list and its adapter available to the other functions.
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PitchAdapter
    // Keep the search box available when refreshing the list.
    private lateinit var searchInput: SearchView

    // Show the list layout and connect it to the shared pitch store.
    override fun onCreate(savedInstanceState: Bundle?) {
        // Perform the normal Activity setup.
        super.onCreate(savedInstanceState)

        // Let the app draw behind the system bars.
        enableEdgeToEdge()

        // Show the list screen designed in XML.
        setContentView(R.layout.activity_mark_list)

        // Add padding so the system bars do not cover the screen.
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            // Measure the status and navigation bars.
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            // Apply that space around the list screen.
            view.setPadding(bars.left, bars.top, bars.right, bars.bottom)

            // Return the insets after handling them.
            insets
        }

        // Find the RecyclerView in the list layout.
        recyclerView = findViewById(R.id.pitchesRecyclerView)

        // Give the adapter the pitches and actions for its row buttons.
        adapter = PitchAdapter(
            pitches = AppData.pitches.findAll(),
            onEdit = { pitch -> editPitch(pitch) },
            onDelete = { pitch -> deletePitch(pitch) }
        )

        // Arrange pitch rows in a vertical scrolling list.
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Connect the adapter to the RecyclerView.
        recyclerView.adapter = adapter

        // Connect to the search box in the layout.
        searchInput = findViewById(R.id.pitchSearch)

// Update the list when the user changes the search.
        searchInput.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            // Results already update while typing, so handle Search without extra work.
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            // Show the matching pitches whenever the text changes.
            override fun onQueryTextChange(newText: String?): Boolean {
                refreshPitches()
                return true
            }
        })

        // Find the button below the list.
        val returnButton = findViewById<Button>(R.id.returnButton)

        // Return to the main menu when the button is tapped.
        returnButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    // Show pitches matching the search text.
    private fun refreshPitches() {
        // Read the search text and remove spaces around it.
        val query = searchInput.query.toString().trim()

        // Keep pitches whose name or description contains the text.
        val matches = AppData.pitches.findAll().filter { pitch ->
            pitch.name.contains(query, ignoreCase = true) ||
                    pitch.description.contains(query, ignoreCase = true)
        }

        // Display the matches using the adapter from Lab 5.
        adapter.updatePitches(matches)
    }

    // Refresh the rows after returning from the edit screen.
    override fun onResume() {
        // Let Android resume the Activity first.
        super.onResume()

        // Only refresh after onCreate has made the adapter.
        if (::adapter.isInitialized) {
            // Refresh the list while keeping the current search applied.
            refreshPitches()
        }
    }

    // Open the form with the selected pitch's ID so it can be edited.
    private fun editPitch(pitch: Pitch) {
        // Prepare an Intent for the existing add/edit screen.
        val intent = Intent(this, AddEditActivity::class.java)

        // Pass the selected ID to tell the form which pitch to load.
        intent.putExtra("id", pitch.id)

        // Open the form.
        startActivity(intent)
    }

    // Remove the selected pitch and refresh the visible list.
    private fun deletePitch(pitch: Pitch) {
        // Delete the pitch with this ID from the shared store.
        AppData.pitches.delete(pitch.id)

        // Ask the adapter to show the store's latest contents.
        // Refresh the list while keeping the current search applied.
        refreshPitches()
    }
}