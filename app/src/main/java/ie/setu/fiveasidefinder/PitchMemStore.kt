package ie.setu.fiveasidefinder

import java.util.concurrent.atomic.AtomicLong

class PitchMemStore {
    // val keeps the list reference and ID generator from being reassigned.
    private val pitches = ArrayList<Pitch>()
    private val lastId = AtomicLong(0L)

    // declare the type this function returns with List<Pitch>
    fun findAll(): List<Pitch> {
        return pitches
    }
    // declare a param and give it an id before storing it
    fun create(pitch: Pitch) {
        pitch.id = lastId.incrementAndGet()
        pitches.add(pitch)
    }
    // boolean tells if a pitch with this id was updated
    fun update(pitch: Pitch): Boolean {
        // Find the pitch stored in mem by ID and then change its details.
        val foundPitch = findOne(pitch.id)
        return if (foundPitch != null) {
            foundPitch.name = pitch.name
            foundPitch.description = pitch.description
            foundPitch.latitude = pitch.latitude
            foundPitch.longitude = pitch.longitude
            true
        } else {
            false
        }
    }

    // Long is the id type
    // the result is false if that ID does not exist
    fun delete(id: Long): Boolean {
        val foundPitch = findOne(id)
        return if (foundPitch != null) {
            pitches.remove(foundPitch)
            true
        } else {
            false
        }
    }

    // A missing ID returns null, which the caller must handle.
    // ? means can return null
    fun findOne(id: Long): Pitch? {
        return pitches.find { pitch -> pitch.id == id }
    }
}