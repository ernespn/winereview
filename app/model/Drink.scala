package model
import java.util._
import siena._
import scala.collection.JavaConversions._

class Drink extends Model{
	
	@Id 		                        var id:Long = _
	@NotNull	                        var name : String = _
	@NotNull                        	var image: Picture = _
	@NotNull  @Index(Array("date_index")) 	var date : Date = new Date
						var nameLower : String = _
	
	def this(name: String, image: Picture) = {
		this()
		if (name == null || name == "") throw new IllegalArgumentException("name is null or empty")
		if (image == null) throw new IllegalArgumentException("image is null")	
	        this.name = name
		this.nameLower = name.toLowerCase
	        this.image = image
		insert()
	}
	
	def addReview(review:Review) = {
		review.drink = this.id
		review.insert()
	}
	
	def getReviews():List[Review] = {
	        return Model.all(classOf[Review]).filter("drink", this.id).order("-date").fetch().toList 
	}

        def getReviewsAverageRating():Int = {
                val listReviews = getReviews().filter( r => r.rating != -1)      
                listReviews.length match {
                        case 0 => return -1
                        case _ => math.round( (0.0 /: listReviews){_ + _.rating} / listReviews.length).toInt
                }
        }
}

object Drink extends Model {
	def findByName(name:String):Drink = {
	        return Model.all(classOf[Drink]).filter("name", name).get()
	}
	
	def findById(id:Long):Drink = {
	        return Model.all(classOf[Drink]).filter("id", id).get()
	}
	
	def getLastN(n:Int):java.util.List[Drink] = {
	        return Model.all(classOf[Drink]).order("-date").fetch(n) 
	}
	
	def Search(term:String):List[Drink] = {
	        return Model.all(classOf[Drink]).search(term+"*", "nameLower").fetch().toList;
	}

}
