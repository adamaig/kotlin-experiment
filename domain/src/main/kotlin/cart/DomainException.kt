package cart

/**
 * Base class for all domain-specific exceptions.
 * Represents business rule violations and validation errors within the cart domain.
 */
abstract class DomainException(message: String) : Exception(message)
