import play.test.UnitFeatureSpec
import org.scalatest.GivenWhenThen
import org.scalatest.BeforeAndAfterEach
import org.scalatest.junit._
import model._
import siena._
import scala.collection.JavaConversions._
import com.google.appengine.api.datastore.Blob
import java.io.File;
import java.io.FileInputStream
import play.Play;

class ReviewFeature extends UnitFeatureSpec with GivenWhenThen with BeforeAndAfterEach { 
    
	var picture:Picture = _
	
	override def beforeEach() {
			Model.all(classOf[Picture]).fetch().foreach(d => d.delete()) 
			Model.all(classOf[Drink]).fetch().foreach(d => d.delete()) 
			Model.all(classOf[Review]).fetch().foreach(d => d.delete()) 
			val file = Play.getFile("test/Wine.jpg")
			val is = new FileInputStream(file)
			val len = file.length.toInt
			val bytes = new Array[Byte](len)
			this.picture = new Picture(bytes)
    }
		
	//Feature
    feature("The user can add review to drinks") { 
    	//Story
        info("As a user")
	    info("I want to be able to add reviews")
	    info("So I can see how good is a wine")
        //Scenarios
		
		scenario("user can add a review to a drink") {
			given("an existing drink")
			val newDrink = new Drink("drinkTestName", this.picture)
			when("create a review")
			assert(newDrink.getReviews().length === 0)
			val review = new Review("comment")			
			newDrink.addReview(review)
			then("the review list is one")
			assert(newDrink.getReviews().length === 1)
			and("the reviews is appended to that drink")
			val lastReview = newDrink.getReviews().last
			assert(lastReview === review)  
        }
					
		scenario("user can add more than one review to a drink") {
			given("an existing drink with a review")
			val newDrink = new Drink("drinkTestName", this.picture)
			val review = new Review("comment")
			newDrink.addReview(review)
			assert(newDrink.getReviews().length === 1)
			when("adding a second review review")
			val review2 = new Review("comment2")
			newDrink.addReview(review2)
			then("the reviews is appended to that drink")
			val lastReview = newDrink.getReviews().first
			assert(lastReview === review2)  
			and("the review list length is two")
			assert(newDrink.getReviews().length === 2)
        }
		
    }
	
	feature("THe user can add rating to the reviews"){
		//Story
        info("As a user")
	    info("I want to be able to add a rating to the reviews")
	    info("So I can see how well scored is a wine")
		
		//Scenarios
		scenario("the reviews has positive rating") {
			given("a drink with a positive review")
			//val newDrink = new Drink("drinkTestName", this.picture)
			//val review = new Review("comment", PlusOne)
			//newDrink.addReview(review)
			when("ask for rating")
			
			then("the rating returned should be positive")
			pending
        }
		
		scenario("the reviews has negative rating") {
			given("a drink with a negative review")
			when("ask for rating")
			then("the rating returned should be negative")
			pending			
        }
		
		scenario("the reviews has equalizer rating") {
			given("a drink with a pse-pse review")
			when("ask for rating")
			then("the rating returned should be 0")	
			pending
        }
		
		scenario("the system return the average review rating") {
			given("a drink with a pse-pse review")
			and("adding a positive review") 
			and("adding another positive review") 
			and("adding a negative review") 
			when("ask for rating")
			then("the rating returned should be positive (2 - 1 - 1")
			pending
        }
		
		scenario("the system return the average review rating (negative)") {
			given("a drink with a pse-pse review")
			and("adding a positive review") 
			and("adding another positive review") 
			and("adding a negative review") 
			and("adding a negative review") 
			and("adding a negative review") 
			when("ask for rating")
			then("the rating returned should be negative (3 - 1 - 2)")	
			pending
        }
		
		
	
	}
	
	feature("The user can add images to the reviews"){
		//Story
        info("As a user")
	    info("I want to be able to add a rating to the reviews")
	    info("So I can see how well scored is a wine")
		

		//Scenarios
		scenario("the reviews can have images") {
			given("a drink")
			val newDrink = new Drink("drinkTestName", this.picture)
			when("add a review with a image")
			val review = new Review("comment", this.picture)
			newDrink.addReview(review)
			then("the image is attached to the review")	
			val retrievedImage = Picture.findById(review.image.id)
			assert(retrievedImage === review.image)  
        }

	}
  
}