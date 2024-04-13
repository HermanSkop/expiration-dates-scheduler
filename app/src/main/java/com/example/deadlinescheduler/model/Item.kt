package com.example.deadlinescheduler.model

import java.time.LocalDate

/**
 * Represents an item with a name, expiration date, category, and quantity.
 * The name must not be empty.
 * The expiration date must be in the future unless allowPastExpirationDate is set to true.
 * The category must not be NONE.
 * The quantity is represented by a Quantity object which can have a number and a unit.
 * Both number and unit must be provided or be empty.
 * If a number is provided, it must be positive.
 * If a unit is provided, it must not be empty.
 */
class Item {
    var name: String = ""
        set(value) {
            if (value.isBlank()) throw IllegalArgumentException("Name must not be empty")
            field = value
        }
    var expirationDate: LocalDate = LocalDate.now()
        set(value) {
            if (value.isBefore(LocalDate.now()) && !allowPastExpirationDate) throw IllegalArgumentException(
                "Expiration date must be in the future"
            )
            field = value
        }
    var category: Category = Category.NONE
        set(value) {
            if (value == Category.NONE) throw IllegalArgumentException("Category must be selected")
            field = value
        }
    var quantity: Quantity = Quantity(null, null)

    constructor(name: String, quantity: Quantity, expirationDate: LocalDate, category: Category) {
        this.name = name
        this.expirationDate = expirationDate
        this.category = category
        this.quantity = quantity
    }

    /**
     * Represents a quantity with a number and a unit.
     * Both number and unit must be provided or be empty.
     * If a number is provided, it must be positive.
     * If a unit is provided, it must not be empty.
     */
    class Quantity(val number: Int?, val unit: String?) {
        init {
            if (number != null && unit != null) {
                if (number < 0) throw IllegalArgumentException("Quantity must be positive")
                if (unit.isBlank()) throw IllegalArgumentException("Unit must not be empty")
            } else if (number == null && unit == null) {
            } else throw IllegalArgumentException("Both number and unit must be provided or be empty")

        }
    }

    override fun toString(): String {
        return name
    }

    override fun equals(other: Any?): Boolean {
        return this.name == (other as Item).name
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }

    companion object {
        private var allowPastExpirationDate: Boolean = false

        /**
         * Creates a list of sample items for testing purposes.
         * The expiration dates of the items can be in the past.
         * @return List<Item>. The list of sample items.
         */
        fun createSampleItems(): List<Item> {
            allowPastExpirationDate = true
            return listOf(
                Item("Banana", Quantity(3, "kg"), LocalDate.now().plusDays(1), Category.FOOD),
                Item("Carrot", Quantity(2, "pack"), LocalDate.now().minusDays(2), Category.FOOD),
                Item(
                    "Marijuana",
                    Quantity(1, "bottle"),
                    LocalDate.now().minusDays(3),
                    Category.MEDICINE
                ),
                Item(
                    "Foundation",
                    Quantity(1, "tube"),
                    LocalDate.now().plusDays(4),
                    Category.COSMETICS
                ),
                Item("Apple", Quantity(4, "kg"), LocalDate.now().minusDays(5), Category.FOOD),
                Item("Mango", Quantity(2, "pack"), LocalDate.now().plusDays(6), Category.FOOD),
                Item("Potato", Quantity(3, "bottle"), LocalDate.now().minusDays(7), Category.FOOD),
                Item("Aspirin", Quantity(1, "kg"), LocalDate.now().plusDays(8), Category.MEDICINE),
                Item(
                    "Lipstick",
                    Quantity(1, "pack"),
                    LocalDate.now().minusDays(9),
                    Category.COSMETICS
                ),
                Item("Pineapple", Quantity(5, "kg"), LocalDate.now().plusDays(10), Category.FOOD),
                Item("Blueberry", Quantity(2, "bottle"), LocalDate.now().minusDays(11), Category.FOOD),
                Item("Lettuce", Quantity(3, "kg"), LocalDate.now().minusDays(12), Category.FOOD),
                Item(
                    "CBD Oil", Quantity(1, "pack"), LocalDate.now().plusDays(13), Category.MEDICINE
                ),
                Item(
                    "Eyeliner",
                    Quantity(1, "tube"),
                    LocalDate.now().minusDays(14),
                    Category.COSMETICS
                ),
                Item("Orange", Quantity(3, "pack"), LocalDate.now().plusDays(15), Category.FOOD),
                Item("Grapes", Quantity(1, "kg"), LocalDate.now().plusDays(16), Category.FOOD),
                Item("Cucumber", Quantity(2, "tube"), LocalDate.now().minusDays(17), Category.FOOD),
                Item(
                    "Ibuprofen",
                    Quantity(1, "bottle"),
                    LocalDate.now().plusDays(18),
                    Category.MEDICINE
                ),
                Item(
                    "Nail Polish",
                    Quantity(1, "pack"),
                    LocalDate.now().plusDays(19),
                    Category.COSMETICS
                ),
                Item("Watermelon", Quantity(6, "kg"), LocalDate.now().minusDays(20), Category.FOOD),
                Item("Pear", Quantity(2, "kg"), LocalDate.now().minusDays(21), Category.FOOD),
                Item("Onion", Quantity(3, "pack"), LocalDate.now().plusDays(22), Category.FOOD),
                Item(
                    "Probiotics",
                    Quantity(1, "tube"),
                    LocalDate.now().plusDays(23),
                    Category.MEDICINE
                ),
                Item(
                    "Sunscreen",
                    Quantity(1, "bottle"),
                    LocalDate.now().minusDays(24),
                    Category.COSMETICS
                ),
                Item("Strawberry", Quantity(4, "pack"), LocalDate.now().minusDays(25), Category.FOOD),
                Item("Kiwi", Quantity(1, "bottle"), LocalDate.now().minusDays(26), Category.FOOD),
                Item("Spinach", Quantity(2, "kg"), LocalDate.now().minusDays(27), Category.FOOD),
                Item(
                    "Vitamin C",
                    Quantity(1, "pack"),
                    LocalDate.now().plusDays(28),
                    Category.MEDICINE
                ),
                Item(
                    "Concealer",
                    Quantity(1, "tube"),
                    LocalDate.now().plusDays(29),
                    Category.COSMETICS
                ),
                Item("Cherry", Quantity(5, "kg"), LocalDate.now().minusDays(30), Category.FOOD),
                Item("Peach", Quantity(2, "kg"), LocalDate.now().plusDays(31), Category.FOOD),
                Item("Celery", Quantity(3, "pack"), LocalDate.now().minusDays(32), Category.FOOD),
                Item(
                    "Antibiotics",
                    Quantity(1, "tube"),
                    LocalDate.now().plusDays(33),
                    Category.MEDICINE
                ),
                Item(
                    "Face Mask",
                    Quantity(1, "bottle"),
                    LocalDate.now().plusDays(34),
                    Category.COSMETICS
                ),
                Item("Plum", Quantity(3, "pack"), LocalDate.now().minusDays(35), Category.FOOD),
                Item("Avocado", Quantity(1, "bottle"), LocalDate.now().plusDays(36), Category.FOOD),
                Item("Beetroot", Quantity(2, "kg"), LocalDate.now().minusDays(37), Category.FOOD),
                Item(
                    "Painkillers",
                    Quantity(1, "pack"),
                    LocalDate.now().plusDays(38),
                    Category.MEDICINE
                ),
                Item(
                    "Eyeshadow",
                    Quantity(1, "tube"),
                    LocalDate.now().minusDays(39),
                    Category.COSMETICS
                ),
                Item("Apricot", Quantity(6, "kg"), LocalDate.now().plusDays(40), Category.FOOD),
                Item("Grapefruit", Quantity(2, "kg"), LocalDate.now().plusDays(41), Category.FOOD),
                Item("Radish", Quantity(3, "pack"), LocalDate.now().minusDays(42), Category.FOOD),
                Item(
                    "Stomach Relief",
                    Quantity(1, "tube"),
                    LocalDate.now().plusDays(43),
                    Category.MEDICINE
                ),
                Item(
                    "Perfume",
                    Quantity(1, "bottle"),
                    LocalDate.now().plusDays(44),
                    Category.COSMETICS
                ),
                Item(
                    "Blackberry", Quantity(4, "pack"), LocalDate.now().minusDays(45), Category.FOOD
                ),
                Item("Coconut", Quantity(1, "bottle"), LocalDate.now().plusDays(46), Category.FOOD),
                Item("Broccoli", Quantity(2, "kg"), LocalDate.now().plusDays(47), Category.FOOD),
                Item(
                    "Antifungal Cream",
                    Quantity(1, "pack"),
                    LocalDate.now().minusDays(48),
                    Category.MEDICINE
                ),
                Item(
                    "Lip Balm",
                    Quantity(1, "tube"),
                    LocalDate.now().plusDays(49),
                    Category.COSMETICS
                ),
                Item("Cantaloupe", Quantity(5, "kg"), LocalDate.now().plusDays(50), Category.FOOD)
            ).let { allowPastExpirationDate = false; it }
        }
    }
}