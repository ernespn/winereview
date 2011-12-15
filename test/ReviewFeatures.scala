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
		scenario("the reviews has 5 star rating") {
			given("a drink with a 5 star review")
			val newDrink = new Drink("drinkTestName", this.picture)
			val review = new Review("comment", null)
                        review.addRating(5)
			newDrink.addReview(review)
			when("ask for rating")
			val lastReview = newDrink.getReviews().first
			then("the rating returned should be 5")
			assert(lastReview.rating === 5)
                }
		
		scenario("the reviews has 0 star rating") {
			given("a drink with a 0 star review")
			val newDrink = new Drink("drinkTestName", this.picture)
			val review = new Review("comment", null)
                        review.addRating(0)
			newDrink.addReview(review)
			when("ask for rating")
			val lastReview = newDrink.getReviews().first
			then("the rating returned should be 0")
			assert(lastReview.rating === 0)		
                }
                
                scenario("the reviews has 3 star rating") {
			given("a drink with a 3 star review")
			val newDrink = new Drink("drinkTestName", this.picture)
			val review = new Review("comment", null)
                        review.addRating(3)
			newDrink.addReview(review)
			when("ask for rating")
			val lastReview = newDrink.getReviews().first
			then("the rating returned should be 3")
			assert(lastReview.rating === 3)		
                }
		
		scenario("the system return the average review rating rounding up") {
			given("a drink with a 0 review")
                        val newDrink = new Drink("drinkTestName", this.picture)
			val review = new Review("comment", null)
                        newDrink.addReview(review)
			and("adding a 5 review") 
                        val review2 = new Review("comment", null)
                        review2.addRating(5)
			newDrink.addReview(review2)
			and("adding another 4 review")
                        val review3 = new Review("comment", null)
                        review3.addRating(4)
			newDrink.addReview(review3)
			and("adding a 2 review") 
                        val review4 = new Review("comment", null)
                        review4.addRating(2)
			newDrink.addReview(review4)
			when("ask for rating")
                        val avgRating = newDrink.getReviewsAverageRating()
			then("the rating returned should be 4 rounding up 3.6")
			assert(avgRating === 4)	
                 }
		
		scenario("the system return the average review rating rounding down") {
			given("a drink with a 0 review")
                        val newDrink = new Drink("drinkTestName", this.picture)
			val review = new Review("comment", null)
                        newDrink.addReview(review)
			and("adding a 5 review") 
                        val review2 = new Review("comment", null)
                        review2.addRating(5)
			newDrink.addReview(review2)
			and("adding another 4 review")
                        val review3 = new Review("comment", null)
                        review3.addRating(4)
			newDrink.addReview(review3)
			and("adding a 2 review") 
                        val review4 = new Review("comment", null)
                        review4.addRating(1)
			newDrink.addReview(review4)
			when("ask for rating")
                        val avgRating = newDrink.getReviewsAverageRating()
			then("the rating returned should be 3 rounding down 3.3")
			assert(avgRating === 3)	
                 }
        
                 scenario("review doesnt have any rating return -1") {
	                given("a drink with a 1 review and no rating")
                        val newDrink = new Drink("drinkTestName", this.picture)
			val review = new Review("comment", null)
                        newDrink.addReview(review)
	        	when("ask for rating")
                        val avgRating = newDrink.getReviewsAverageRating()
                        then("the rating returned should be -1")
                        assert(avgRating === -1)	
	        }

                 scenario("drink doesnt have any review return -1") {
			given("a drink with a 0 review")
                        val newDrink = new Drink("drinkTestName", this.picture)
	        	when("ask for rating")
                        val avgRating = newDrink.getReviewsAverageRating()
                        then("the rating returned should be -1")
                        assert(avgRating === -1)	
	        }

                scenario("review with rating -1 return exception") {
			given("a review")
                        val review = new Review("comment", null)
	                when("add -1 rating")
                        then("it should return an IllegalArgumentException")
                        intercept[IllegalArgumentException] {
	                       review.addRating(-1)
	                }	
	        }

                scenario("review with rating 6 return exception") {
			given("a review")
                        val review = new Review("comment", null)
	                when("add 6 rating")
                        then("it should return an IllegalArgumentException")
                        intercept[IllegalArgumentException] {
	                       review.addRating(6)
	                }	
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
		
		scenario("the reviews can have no images") {
			given("a drink")
			val newDrink = new Drink("drinkTestName", this.picture)
			when("add a review with a image")
			val review = new Review("comment", null)
			newDrink.addReview(review)
			then("the review is created correctly")	
			val lastReview = newDrink.getReviews().first
			assert(lastReview.id === review.id)  
        }

	}
  
}
