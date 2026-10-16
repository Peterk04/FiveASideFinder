package ie.setu.fiveasidefinder.models

// a data class to represent one pitch and give functions such as toString and copy
data class Pitch(
    var id: Long = 0L, // the store assigns an id when the pitch is added
    var name: String = "",
    var description: String = "",
    var latitude: Double = 0.0,
    var longitude: Double = 0.0
)