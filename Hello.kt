import kotlin.io.*

fun main() {
    println("🎉 Hello, Kotlin!")
    println("Welcome to your Kotlin development environment!")
    
    // Demonstrate some Kotlin features
    val message = "Kotlin is awesome!"
    val numbers = listOf(1, 2, 3, 4, 5)
    
    println("Message: $message")
    println("Numbers: $numbers")
    println("Sum of numbers: ${numbers.sum()}")
    
    // Nullable types
    val nullableString: String? = null
    println("Null-safe length: ${nullableString?.length ?: "null"}")
    
    // Data class
    data class Person(val name: String, val age: Int)
    val person = Person("Kotlin Developer", 25)
    println("Person: $person")
}
