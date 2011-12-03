package model

object Rating extends Enumeration("PlusOne", "MinusOne", "PsePse") {
	type Rating = Value
	val PlusOne, MinusOne, PsePse = Value
}
