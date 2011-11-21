package model
import java.util._
import siena._

class Drink extends Model{
	
	@Id var id:Long = _
	@NotNull var name : String = _
	@NotNull var image: Picture = _
	@NotNull  @Index(Array("date_index")) var date : Date = new Date
	
	def this(name: String, image: Picture) = {
	    this()
	    this.name = name
	    this.image = image
	  }
}

object Drink extends Model {
	def findByName(name:String):Drink = {
	  return null
	}

}