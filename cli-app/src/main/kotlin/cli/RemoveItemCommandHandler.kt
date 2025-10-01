package cli

import cart.CartId
import cart.ProductId
import cart.ItemNotInCartException
import cart.EmptyCartException
import cart.ConcurrencyException
import commands.RemoveItemCommand
import events.EventVersion
import services.CartService
import kotlin.system.exitProcess

class RemoveItemCommandHandler(
    private val cartService: CartService
) {
    
    fun execute(args: Array<String>): Int {
        try {
            // Parse arguments
            val parsedArgs = parseArguments(args)
            
            // Show help if requested
            if (parsedArgs.help) {
                showHelp()
                return 0
            }
            
            // Validate required arguments
            val cartId = parsedArgs.cartId ?: run {
                println("Error: --cart-id is required")
                showUsage()
                return 1
            }
            
            val productId = parsedArgs.productId ?: run {
                println("Error: --product-id is required") 
                showUsage()
                return 1
            }
            
            val version = parsedArgs.version ?: run {
                println("Error: --version is required")
                showUsage() 
                return 1
            }
            
            // Create command
            val command = RemoveItemCommand(
                cartId = CartId(cartId),
                productId = ProductId(productId), 
                expectedVersion = EventVersion(version)
            )
            
            // Execute remove operation
            val result = cartService.removeItem(command)
            
            if (result.isSuccess) {
                val removeResult = result.getOrThrow()
                val remainingQuantity = removeResult.cart.lineItems[command.productId]?.quantity ?: 0
                
                if (remainingQuantity > 0) {
                    println("Removed 1 unit of ${productId} from cart ${cartId}")
                    println("Remaining quantity: ${remainingQuantity}")
                } else {
                    println("Removed last unit of ${productId} from cart ${cartId}")
                    println("Item removed from cart")
                }
                return 0
                
            } else {
                val exception = result.exceptionOrNull()!!
                return when (exception) {
                    is ItemNotInCartException -> {
                        println("Error: Item ${productId} not found in cart ${cartId}")
                        1
                    }
                    is EmptyCartException -> {
                        println("Error: Cannot remove items from empty cart ${cartId}")
                        1
                    }
                    is ConcurrencyException -> {
                        println("Error: Cart version mismatch - cart was modified by another operation")
                        2
                    }
                    else -> {
                        println("Error: ${exception.message}")
                        3
                    }
                }
            }
            
        } catch (e: IllegalArgumentException) {
            println("Error: ${e.message}")
            return 1
        } catch (e: Exception) {
            println("Error: ${e.message}")
            return 3
        }
    }
    
    private fun parseArguments(args: Array<String>): ParsedArgs {
        var cartId: String? = null
        var productId: String? = null
        var version: Long? = null
        var help = false
        
        var i = 0
        while (i < args.size) {
            when (args[i]) {
                "--cart-id" -> {
                    if (i + 1 < args.size) {
                        cartId = args[++i]
                    } else {
                        throw IllegalArgumentException("--cart-id requires a value")
                    }
                }
                "--product-id" -> {
                    if (i + 1 < args.size) {
                        productId = args[++i]
                    } else {
                        throw IllegalArgumentException("--product-id requires a value")
                    }
                }
                "--version" -> {
                    if (i + 1 < args.size) {
                        version = args[++i].toLongOrNull() 
                            ?: throw IllegalArgumentException("--version must be a valid number")
                    } else {
                        throw IllegalArgumentException("--version requires a value")
                    }
                }
                "--help" -> {
                    help = true
                }
                else -> {
                    // Skip unknown arguments or handle as needed
                }
            }
            i++
        }
        
        return ParsedArgs(cartId, productId, version, help)
    }
    
    private fun showHelp() {
        println("Remove Item Command")
        println("==================")
        println()
        println("Usage: remove-item --cart-id <cart-id> --product-id <product-id> --version <version>")
        println()
        println("Arguments:")
        println("  --cart-id     The ID of the cart to remove items from")
        println("  --product-id  The ID of the product to remove") 
        println("  --version     The expected cart version for concurrency control")
        println("  --help        Show this help message")
        println()
        println("Exit Codes:")
        println("  0: Success")
        println("  1: Validation error (item not found, empty cart)")
        println("  2: Concurrency error (version mismatch)")
        println("  3: System error")
    }
    
    private fun showUsage() {
        println("Usage: remove-item --cart-id <cart-id> --product-id <product-id> --version <version>")
        println("Use --help for more information")
    }
    
    data class ParsedArgs(
        val cartId: String?,
        val productId: String?,
        val version: Long?,
        val help: Boolean
    )
}
