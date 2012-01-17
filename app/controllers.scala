package controllers

import play._
import play.mvc._
import views.Application._
import model._
import com.google.appengine.api.datastore.Blob
import play.cache.Cache
import play.libs.Images
import java.io._
import collection.JavaConversions._ 

object Application extends Controller { 
    
        def index = {
		val listDrinks:List[Drink] = Drink.getLastN(3).toList
                html.index(title = "Track your wine!", drinks = listDrinks)
        }
	
	def newdrink() = {
		html.newdrink("Create a new drink", null, "")
	}
	
	def postnewdrink() = {
		val name = params.get("drinkname")
		val image = params.get("drinkimage", classOf[Array[Byte]])
		try {
			val drink = new Drink(name, new Picture(image))
			html.newdrink("New Drink Posted", drink, "")
		}catch {
			case e:IllegalArgumentException =>  html.newdrink("Error posting a drink", null, e.getMessage())
		}	
	}
	
	def newreview(id: Long) = {
		html.newreview("Add a review to a drink", id, "")
	}
	
	def postreview() = {
		val drinkId = params.get("reviewDrink", classOf[Long])
		val comment = params.get("reviewComment")
		val rating = params.get("reviewRating", classOf[Int])
                val image = params.get("reviewImage", classOf[Array[Byte]])
		
		try{
			val drink = Drink.findById(drinkId)
			val review = new Review(comment, new Picture(image))
                        rating match {
                        case -1 => //nothing to do
                        case _ => review.addRating(_)
                        }
			drink.addReview(review)
			html.newdrink("New review posted", drink, "")
		}catch {
			case e:IllegalArgumentException =>  html.newdrink("Error posting a review for the drink -> "+drinkId, null, e.getMessage())
			case npe:NullPointerException 	=>	html.newdrink("Error posting a review for the drink -> "+drinkId, null, npe.getMessage())
		}
	}
	
	def search() = {
		val term = params.get("search")
		term match {
			case null	=> html.search("Search for a drink", null, "")
			case ""		=> html.search("Search for a drink", null, "Error the search term was empty")
			case x 		=> html.search("Search results", Drink.Search(x.toLowerCase).toList, "")
		}
	}
	
	def getImage(id:Long) = new ByteArrayInputStream(Picture.findById(id).ImageContent.getBytes())
    
}

object WebServices extends Controller {
        def lastAdded() = {
                val list =  Drink.getLastN(3).toList.map( d => "<wine name=\"" +d.name + "\" dateCreated=\"" + d.date + "\"><Image src=\"/Image/" + d.image.id + "\" ></Image></wine>" )
                val concatenated = list.reduceLeft((x1: String, x2: String) => x1 + x2)                
                Xml("<wines>" + concatenated  + "</wines>")
        }
}
