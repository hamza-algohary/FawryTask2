/*
 * This source file was generated by the Gradle 'init' task
 */
package org.example

import java.time.LocalDate
import java.util.*
import kotlin.collections.HashMap

//enum class BookType{PAPER,EBOOK,DEMO}
typealias ISBN = String
//typealias Quantity = Int
//typealias BooksInventory = HashMap<ISBN , Book>
data class BookData(
    val ISBN : ISBN ,
    val title : String ,
    val author: String ,
    val publicationYear : LocalDate ,
    val price : Double ,
    var ebookAvailable : Boolean ,
    var quantity: Int
)

class BookUnregestierdException(ISBN: ISBN) : Exception()
class EBookUnavailableException(ISBN : ISBN) : Exception()
class PaperBooksOutOfStock(ISBN: String , remainingQuantity : Int) : Exception()

class BookStore {
    private val books = HashMap<ISBN , BookData>()

    fun put(ISBN : String , title: String , author : String , publicationYear: LocalDate , price: Double , ebook: Boolean = false , quantity: Int = 0) {
        books[ISBN] = BookData(ISBN , title , author , publicationYear , price , ebook , quantity)
    }
    private fun get(ISBN: String) =
        books.getOrElse(ISBN) {
            throw BookUnregestierdException(ISBN)
        }
    fun enableEbook(ISBN: String , enable : Boolean = true) {
        get(ISBN).ebookAvailable = enable
    }
    fun quantityOf(ISBN: String) = get(ISBN).quantity
    fun increaseQuantity(ISBN: String , addedQuantity : Int) {
        get(ISBN).quantity += addedQuantity
    }

    /** @return total price */
    fun purchaseEbook(ISBN : String , quantity : Int , email : String) =
        get(ISBN).let {
            if(!it.ebookAvailable)
                throw EBookUnavailableException(ISBN)
            sendEBook(ISBN , quantity , email)
            quantity * it.price
        }
    fun purchasePaperBook(ISBN : String , quantity: Int , address : String, shippingService: ShippingService = ShippingService()) =
        get(ISBN).let {
            if(quantity > it.quantity)
                throw PaperBooksOutOfStock(ISBN , it.quantity)
            it.quantity -= quantity
            shippingService.ship(ISBN , quantity , address)
            quantity * it.price
        }
}

fun sendEBook(ISBN : String , quantity: Int , email: String) {}

open class ShippingService{
    fun ship(ISBN : String , quantity: Int , address : String) {}
}

fun main() {
    val store = BookStore()
    store.put(
        "1401971369" ,
        "The Let Them Theory: A Life-Changing Tool That Millions of People Can't Stop Talking About" ,
        "Mel Robbins" ,
        LocalDate.of(2024 ,12, 24) ,
        14.0,
        true,
        5
    )
    try { // Try uncommenting any of these lines indiviually
        //store.purchaseEbook("1401971367" , 3) // Should fail
        //println(store.purchaseEbook("1401971369" , 3)) // Should succeed
        //println(store.purchasePaperBook("1401971369" , 3)) // Should succeed
        //println(store.purchasePaperBook("1401971369" , 7)) // Should fail
    } catch (e : Exception) {
        println(e)
    }


}
