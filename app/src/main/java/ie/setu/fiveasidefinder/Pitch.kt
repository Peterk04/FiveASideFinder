package ie.setu.fiveasidefinder

// a data class to represent one pitch and give functions such as toString and copy
data class Pitch(
    var id: Long = 0L, // the store assigns an id when the pitch is added
    var name: String = "",
    var description: String = "",
    var latitude: Double = 0.0,
    // Keep the longitude, with a comma because another field follows.
    var longitude: Double = 0.0,
    // Store the selected surface; existing pitches use this default.
    var surface: String = "Astro turf",
    //add option for floodlights
    var floodlights: Boolean = false
)