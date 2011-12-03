package model
import java.util._
import siena._
import Rating._

class Review extends Model{
	
	@Id 	 								var id:Long = _
	@NotNull								var drink : Long = _
	@NotNull 								var comment : String = _
	@NotNull @Index(Array("date_index")) 	var date : Date = new Date
											var image: Picture = _
	
	def this(comment:String) = {
		this()
		this.comment = comment

	}
	
	def this(comment:String, image:Picture) = {
		this(comment)
		if (image != null) this.image = image
	}
	
}
