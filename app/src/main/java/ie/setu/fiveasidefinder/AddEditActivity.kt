package ie.setu.fiveasidefinder

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddEditActivity : AppCompatActivity() {

    private lateinit var nameInput: EditText
    private lateinit var descriptionInput: EditText
    private lateinit var latitudeInput: EditText
    private lateinit var longitudeInput: EditText

    private var editingId: Long? = null

    // Build the form and load a pitch when an ID was passed from the list screen.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createUserInterface()

        editingId = intent.getLongExtra("id", -1L)

        if (editingId != -1L) {
            loadExistingPitch(editingId!!)
        }
    }

    // Build the four input fields and Save and Cancel buttons in Kotlin.
    private fun createUserInterface() {
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32, 320, 32, 32)
        }

        nameInput = EditText(this).apply {
            hint = "Pitch name"
        }

        descriptionInput = EditText(this).apply {
            hint = "Description"
        }

        latitudeInput = EditText(this).apply {
            hint = "Latitude"
            inputType =
                android.text.InputType.TYPE_CLASS_NUMBER or
                        android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
        }

        longitudeInput = EditText(this).apply {
            hint = "Longitude"
            inputType =
                android.text.InputType.TYPE_CLASS_NUMBER or
                        android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
        }

        val saveButton = Button(this).apply {
            text = "Save"

            setOnClickListener {
                savePitch()
            }
        }

        val cancelButton = Button(this).apply {
            text = "Cancel"

            setOnClickListener {
                finish()
            }
        }

        root.addView(nameInput)
        root.addView(descriptionInput)
        root.addView(latitudeInput)
        root.addView(longitudeInput)
        root.addView(saveButton)
        root.addView(cancelButton)

        setContentView(root)
    }

    // Find the selected pitch and fill the form with its current details.
    private fun loadExistingPitch(id: Long) {
        val pitch = AppData.pitches.findOne(id)

        if (pitch == null) {
            Toast.makeText(
                this,
                "Pitch not found",
                Toast.LENGTH_SHORT
            ).show()

            finish()
            return
        }

        nameInput.setText(pitch.name)
        descriptionInput.setText(pitch.description)
        latitudeInput.setText(pitch.latitude.toString())
        longitudeInput.setText(pitch.longitude.toString())
    }

    // Validate the form, create or update the pitch, then return to the list.
    private fun savePitch() {
        val name = nameInput.text.toString().trim()
        val description = descriptionInput.text.toString().trim()

        if (name.isEmpty()) {
            nameInput.error = "Pitch name is required"
            return
        }

        val latitude = latitudeInput.text.toString().toDoubleOrNull()

        if (latitude == null) {
            latitudeInput.error = "Enter a valid number"
            return
        }

        val longitude = longitudeInput.text.toString().toDoubleOrNull()

        if (longitude == null) {
            longitudeInput.error = "Enter a valid number"
            return
        }

        if (editingId == null || editingId == -1L) {
            val pitch = Pitch(
                name = name,
                description = description,
                latitude = latitude,
                longitude = longitude
            )

            AppData.pitches.create(pitch)

            Toast.makeText(
                this,
                "Pitch created",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            val pitch = Pitch(
                id = editingId!!,
                name = name,
                description = description,
                latitude = latitude,
                longitude = longitude
            )

            AppData.pitches.update(pitch)

            Toast.makeText(
                this,
                "Pitch updated",
                Toast.LENGTH_SHORT
            ).show()
        }

        finish()
    }
}