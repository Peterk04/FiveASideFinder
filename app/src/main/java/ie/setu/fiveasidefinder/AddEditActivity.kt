package ie.setu.fiveasidefinder

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddEditActivity : AppCompatActivity() {

    // Hold the input fields so loading and saving can both use them.
    private lateinit var nameInput: EditText
    private lateinit var descriptionInput: EditText
    private lateinit var latitudeInput: EditText
    private lateinit var longitudeInput: EditText

    // Hold the selected pitch ID, or null when adding a new pitch.
    private var editingId: Long? = null

    // Show the XML form and load an existing pitch when an ID was passed in.
    override fun onCreate(savedInstanceState: Bundle?) {
        // Run the normal Activity setup.
        super.onCreate(savedInstanceState)

        // Show the form made in the visual Layout Editor.
        setContentView(R.layout.activity_add_edit)

        // Connect each Kotlin property to its EditText in the XML.
        nameInput = findViewById(R.id.nameInput)
        descriptionInput = findViewById(R.id.descriptionInput)
        latitudeInput = findViewById(R.id.latitudeInput)
        longitudeInput = findViewById(R.id.longitudeInput)

        // Find the form's Save and Cancel buttons.
        val saveButton = findViewById<Button>(R.id.saveButton)
        val cancelButton = findViewById<Button>(R.id.cancelButton)

        // Read the ID sent by the list; treat -1 as "adding a new pitch".
        editingId = intent.getLongExtra("id", -1L).takeIf { it != -1L }

        // When editing, change the heading and fill the fields.
        if (editingId != null) {
            findViewById<TextView>(R.id.formTitle).text = "Edit Pitch"
            loadExistingPitch(editingId!!)
        }

        // Save the entered details when Save is tapped.
        saveButton.setOnClickListener {
            savePitch()
        }

        // Close the form without changing a pitch when Cancel is tapped.
        cancelButton.setOnClickListener {
            finish()
        }
    }

    // Find the selected pitch and copy its details into the form.
    private fun loadExistingPitch(id: Long) {
        // Look up the pitch that the list screen selected.
        val pitch = AppData.pitches.findOne(id)

        // Close the form if that pitch no longer exists.
        if (pitch == null) {
            Toast.makeText(this, "Pitch not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Display the saved values so they can be edited.
        nameInput.setText(pitch.name)
        descriptionInput.setText(pitch.description)
        latitudeInput.setText(pitch.latitude.toString())
        longitudeInput.setText(pitch.longitude.toString())
    }

    // Validate the fields, create or update a pitch, and close the form.
    private fun savePitch() {
        // Read the text fields and remove extra spaces around the text.
        val name = nameInput.text.toString().trim()
        val description = descriptionInput.text.toString().trim()

        // Stop and show an error if the pitch has no name.
        if (name.isEmpty()) {
            nameInput.error = "Pitch name is required"
            return
        }

        // Convert the latitude to a number without crashing on bad input.
        val latitude = latitudeInput.text.toString().toDoubleOrNull()

        // Stop and show an error if the latitude is not a number.
        if (latitude == null) {
            latitudeInput.error = "Enter a valid number"
            return
        }

        // Convert the longitude to a number without crashing on bad input.
        val longitude = longitudeInput.text.toString().toDoubleOrNull()

        // Stop and show an error if the longitude is not a number.
        if (longitude == null) {
            longitudeInput.error = "Enter a valid number"
            return
        }

        // An absent ID means this is a new pitch.
        if (editingId == null) {
            // Make a Pitch from the form values.
            val pitch = Pitch(
                name = name,
                description = description,
                latitude = latitude,
                longitude = longitude
            )

            // Give the new pitch to the shared store.
            AppData.pitches.create(pitch)

            // Tell the user that the pitch was added.
            Toast.makeText(this, "Pitch created", Toast.LENGTH_SHORT).show()
        } else {
            // Keep the selected ID while replacing that pitch's details.
            val pitch = Pitch(
                id = editingId!!,
                name = name,
                description = description,
                latitude = latitude,
                longitude = longitude
            )

            // Update the matching pitch in the shared store.
            AppData.pitches.update(pitch)

            // Tell the user that the edit was saved.
            Toast.makeText(this, "Pitch updated", Toast.LENGTH_SHORT).show()
        }

        // Return to the menu or list that opened this form.
        finish()
    }
}
