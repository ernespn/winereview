import play.test.UnitFeatureSpec
import org.scalatest.GivenWhenThen
import org.scalatest._
import org.scalatest.junit._
import model.Drink
import model.Picture

class DrinkFeature extends UnitFeatureSpec with GivenWhenThen{ 
    //Feature
    feature("The user can create a drink record") { 
    	//Story
        info("As a programmer")
	    info("I want to be able to create new drink object")
	    info("So I can store as much as I want")
        //Scenarios
	    scenario("create new drinks with name and image") {
    	  given("values to create a new drink")
    	  val name = "nameOfDrink"
	      val picture = new Picture()
    	  when("create a drink")
    	  val newDrink = new Drink(name, new Picture())
	      then("the new drinks can be retrieved")
	      val drink = Drink.findByName(name)
	      assert(drink === newDrink)      
        }
        
        scenario("return a error message when name is not passed"){
          given("values to create a new drink with no name")
          val name = null
	      val picture = new Picture()
	      when("create a drink")
	      then("DrinkSException should be thrown")
	      intercept[IllegalArgumentException] {
	         val newDrink = new Drink(name, new Picture())
	      }
        }
        scenario("return a error message when image is not passed") {
          given("values to create a new drink with no image")
	      when("create a drink")
	      then("the drink is not created")
	      and("an error is reported")
	      pending
        }
        scenario("return a error message when image is bigger than 10Mb") {
           given("values to create a new drink with an image bigger than 10Mb")
	       when("create a drink")
	       then("the drink is not created")
	       and("an error is reported")
	       pending
        }
    }
    
     feature("The user can browse drinks") { 
    	//Story
        info("As a programmer")
	    info("I want to be able to browse the drink catalog")
	    info("So I can see the latest drinks added")
        //Scenarios
	    scenario("create new drinks with name and image") {
    	  pending    
        }
     }
  
}