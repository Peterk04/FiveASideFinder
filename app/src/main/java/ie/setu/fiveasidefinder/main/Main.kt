package ie.setu.fiveasidefinder.main

import ie.setu.fiveasidefinder.models.Pitch
import ie.setu.fiveasidefinder.models.PitchMemStore

// declared store outside every function so all funcs can access it
//val doesnt make the pitches immutable, pitches can still be added, edited and deleted
val store = PitchMemStore()

// Keep showing the menu and run the chosen action until the user exits.
fun main() {
    println("=== Pitch Console App (Lab 1) ===")
    var input: Int

    do {
        input = menu()
        when (input) {
            1 -> addPitch()
            2 -> listPitches()
            3 -> updatePitch()
            4 -> deletePitch()
            5 -> searchPitch()
            0 -> println("\nExiting pitch application. Goodbye!")
            else -> println("\nInvalid option. Please try again.")
        }
    } while (input != 0)
}

// Show the available actions and read the user's choice.
fun menu(): Int {
    println("\n----------------------------------")
    println(" MAIN MENU")
    println("----------------------------------")
    println(" 1. Add Pitch")
    println(" 2. List All Pitches")
    println(" 3. Update a Pitch")
    println(" 4. Delete a Pitch")
    println(" 5. Search Pitch by ID")
    println(" 0. Exit")
    print("\nEnter option: ")

    return readlnOrNull()?.toIntOrNull() ?: -1
}

// Ask for pitch details, check the name and save the new pitch.
fun addPitch() {
    println("\n--- Add Pitch ---")
    print("Enter Name: ")
    val name = readlnOrNull()?.trim().orEmpty()
    print("Enter Description: ")
    val description = readlnOrNull()?.trim().orEmpty()

    if (name.isNotEmpty()) {
        val pitch = Pitch(name = name, description = description)
        store.create(pitch)
        println("Pitch added successfully with ID: ${pitch.id}")
    } else {
        println("Name cannot be empty. Creation cancelled.")
    }
}

// Show all saved pitches or say when the list is empty.
fun listPitches() {
    println("\n--- All Pitches ---")
    val pitches = store.findAll()

    if (pitches.isEmpty()) {
        println("No pitches stored yet.")
    } else {
        pitches.forEach {
            println("ID: ${it.id} | Name: ${it.name} | Description: ${it.description}")
        }
    }
}

// toLongOrNull() avoids a crash if the entered ID is not a number.
// Find a pitch by ID, ask for new details and save the changes.
fun updatePitch() {
    println("\n--- Update Pitch ---")
    listPitches()
    if (store.findAll().isEmpty()) return

    print("\nEnter ID of Pitch to update: ")
    val id = readlnOrNull()?.toLongOrNull()

    if (id != null && store.findOne(id) != null) {
        print("Enter New Name: ")
        val name = readlnOrNull()?.trim().orEmpty()
        print("Enter New Description: ")
        val description = readlnOrNull()?.trim().orEmpty()

        if (name.isNotEmpty()) {
            val updated = store.update(
                Pitch(id = id, name = name, description = description)
            )
            if (updated) println("Pitch updated successfully.")
        } else {
            println("Name cannot be empty. Update cancelled.")
        }
    } else {
        println("Pitch with ID $id not found.")
    }
}

// The store returns a Boolean so we can report whether deletion succeeded.
// Ask for a pitch ID and remove it if it exists.
fun deletePitch() {
    println("\n--- Delete Pitch ---")
    listPitches()
    if (store.findAll().isEmpty()) return

    print("\nEnter ID of Pitch to delete: ")
    val id = readlnOrNull()?.toLongOrNull()

    if (id != null) {
        val deleted = store.delete(id)
        if (deleted) {
            println("Pitch with ID $id deleted successfully.")
        } else {
            println("Pitch with ID $id not found.")
        }
    } else {
        println("Invalid ID entered.")
    }
}

// Pitch? from findOne() may be null, so check before reading its properties.
// Look up a pitch by ID and show its details if found.
fun searchPitch() {
    println("\n--- Search Pitch ---")
    print("Enter ID: ")
    val id = readlnOrNull()?.toLongOrNull()

    if (id != null) {
        val pitch = store.findOne(id)
        if (pitch != null) {
            println("Found: ID: ${pitch.id} | Name: ${pitch.name} | Description: ${pitch.description}")
        } else {
            println("No pitch found with ID $id.")
        }
    } else {
        println("Invalid ID entered.")
    }
}