package model
import java.util._
import siena._

class Drink extends Model{
	
	@Id 	 var id:Long = _
	@NotNull var name : String = _
	@NotNull var image: Picture = _
	@NotNull  @Index(Array("date_index")) var date : Date = new Date
	
	def this(name: String, image: Picture) = {
		this()
		if (name == null || name == "") throw new IllegalArgumentException("name is null or empty")
		if (image == null) throw new IllegalArgumentException("image is null")	
	    this.name = name
	    this.image = image
		insert()
	}
	
	override def toString() = {
		"name:" + this.name + " ->id:"+this.id+" ->date:"+this.date
	}
}

object Drink extends Model {
	def findByName(name:String):Drink = {
	  return Model.all(classOf[Drink]).filter("name", name).get()
	}
	
	def getLastN(n:Int):java.util.List[Drink] = {
	  return Model.all(classOf[Drink]).order("-date").fetch(n) 
	}

}