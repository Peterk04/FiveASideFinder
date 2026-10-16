package ie.setu.fiveasidefinder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// Connect the pitch data to the row layout used by RecyclerView.
class PitchAdapter(
    private var pitches: List<Pitch>,
    private val onEdit: (Pitch) -> Unit,
    private val onDelete: (Pitch) -> Unit
) : RecyclerView.Adapter<PitchAdapter.PitchViewHolder>() {

    // Keep references to the views inside one pitch row.
    class PitchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Find the view that will show the pitch ID and name.
        val titleText: TextView = itemView.findViewById(R.id.titleText)

        // Find the view that will show the description.
        val descriptionText: TextView = itemView.findViewById(R.id.descriptionText)

        // Find the view that will show the coordinates.
        val coordinatesText: TextView = itemView.findViewById(R.id.coordinatesText)

        // Find the button used to edit this pitch.
        val editButton: Button = itemView.findViewById(R.id.editButton)

        // Find the button used to delete this pitch.
        val deleteButton: Button = itemView.findViewById(R.id.deleteButton)
    }

    // Create a new row from item_pitch.xml when RecyclerView needs one.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PitchViewHolder {
        // Turn the XML row layout into a View.
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pitch, parent, false)

        // Store the row's child views in a ViewHolder.
        return PitchViewHolder(view)
    }

    // Put one pitch's details and button actions into a row.
    override fun onBindViewHolder(holder: PitchViewHolder, position: Int) {
        // Get the pitch that belongs at this position.
        val pitch = pitches[position]

        // Show the pitch's ID and name.
        holder.titleText.text = "${pitch.id}: ${pitch.name}"

        // Turn the Boolean into a readable answer.
        val lights = if (pitch.floodlights) "Yes" else "No"

        // Show the details using the existing TextView.
        holder.descriptionText.text =
            "${pitch.description}\nSurface: ${pitch.surface}\nFloodlights: $lights"

        // Show the pitch's latitude and longitude.
        holder.coordinatesText.text =
            "Latitude: ${pitch.latitude}, Longitude: ${pitch.longitude}"

        // Send this pitch to the list screen when Edit is tapped.
        holder.editButton.setOnClickListener {
            onEdit(pitch)
        }

        // Send this pitch to the list screen when Delete is tapped.
        holder.deleteButton.setOnClickListener {
            onDelete(pitch)
        }
    }

    // Tell RecyclerView how many pitch rows it needs.
    override fun getItemCount(): Int {
        return pitches.size
    }

    // Replace the displayed data and ask RecyclerView to redraw its rows.
    fun updatePitches(newPitches: List<Pitch>) {
        pitches = newPitches
        notifyDataSetChanged()
    }
}