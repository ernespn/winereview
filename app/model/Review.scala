package model
import java.util._
import siena._

class Review extends Model{
	
	@Id 	 				var id:Long = _
	@NotNull				var drink : Long = _
	@NotNull 				var comment : String = _
	@NotNull @Index(Array("date_index")) 	var date : Date = new Date
					        var image: Picture = _
                                                var rating : Int = -1 //no reviews
	
	def this(comment:String) = {
		this()
		this.comment = comment

	}
	
	def this(comment:String, image:Picture) = {
		this(comment)
		if (image != null) this.image = image
	}
	
        def addRating(rating:Int) = {
               rating match {
                 case x if 0 until 6 contains x  => this.rating = x 
                 case _                          => throw new IllegalArgumentException("Invalid rating")
                }
        }
}
