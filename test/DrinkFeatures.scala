import play.test.UnitFeatureSpec
import org.scalatest.GivenWhenThen
import org.scalatest.BeforeAndAfterEach
import org.scalatest.mock
import org.scalatest.junit._
import model._
import siena._
import scala.collection.JavaConversions._
import com.google.appengine.api.datastore.Blob
import org.scalatest.mock.MockitoSugar
import java.io.File;
import java.io.FileInputStream
import play.Play;

class DrinkFeature extends UnitFeatureSpec with GivenWhenThen with BeforeAndAfterEach with MockitoSugar{ 
    
	var picture:Picture = _
	
	override def beforeEach() {
			Model.all(classOf[Picture]).fetch().foreach(d => d.delete()) 
			Model.all(classOf[Drink]).fetch().foreach(d => d.delete()) 
			val file = Play.getFile("test/Wine.jpg")
			val is = new FileInputStream(file)
			val len = file.length.toInt
			val bytes = new Array[Byte](len)
			this.picture = new Picture(bytes)
    }
	
    //Feature
    feature("The user create a drink record") { 
    	//Story
        info("As a programmer")
	    info("I want to be able to create new drink object")
	    info("So I can store as much as I want")
        //Scenarios
        scenario("create new drinks with name and image") {
    	  given("name to create a new drink")
    	  val name = "nameOfDrink"
    	  and("Image to create a new drink")
	      val picture = this.picture
    	  when("create a drink")
    	  val newDrink = new Drink(name, picture)
	      then("the new drinks name is the same")
	      assert(name === newDrink.name)  
        }
        
        scenario("return a error message when name is null"){
          given("name is null")
    	  val name = null
    	  and("Image to create a new drink")
	      val picture = this.picture
	      when("create a drink")
	      then("IllegalArgumentException should be thrown")
	      intercept[IllegalArgumentException] {
	         val newDrink = new Drink(name, picture)
	      }
        }
        
        scenario("return a error message when name is empty"){
          given("name is empty to create a new drink")
    	  val name = ""
    	  and("Image to create a new drink")
	      val picture = this.picture
	      when("create a drink")
	      then("IllegalArgumentException should be thrown")
	      intercept[IllegalArgumentException] {
	         val newDrink = new Drink(name, picture)
	      }
        }
            
        scenario("return a error message when image is not passed") {
           given("name to create a new drink")
    	  val name = "nameOfDrink"
    	  and("Image is null")
	      val picture = null
	      when("create a drink")
	      then("IllegalArgumentException should be thrown")
	      intercept[IllegalArgumentException] {
	         val newDrink = new Drink(name, picture)
	      }
        }

		}
    feature("The user can retrieve drinks") { 
    	//Story
        info("As a programmer")
	    info("I want to be able to retrieve the drinks stored  on the catalog")
	    info("So I can see the latest drinks added")
        //Scenarios
	    scenario("retrieve one drink when there is none") {
    	   given("an empty drink list")
	       when("retrieve last drink")
		   val drinks = Drink.getLastN(1)
	       then("return 0 drinks")
		   assert(drinks.size() === 0 ) 
        }
		
		scenario("retrieve two drinks when there is none") {
    	  given("an empty drink list")
		  when("retrieve last two drink")
		  val drinks = Drink.getLastN(2)
	      then("return 0 drinks")
		  assert(drinks.size() === 0 ) 
        }
		
		scenario("create one drinks  and retrieve two items will return just one") {
    	  given("a list of one drink")
		  val pic = this.picture
		  val newDrink = new Drink("drink", pic)
		  when("retrieve the last two drink")
		  val drinks = Drink.getLastN(2)
	      then("return the only one stored drink")
		  assert(drinks.size() === 1 )
		  and ("the retrieved drink is the same we inserted")
		  assert(drinks.get(0) === newDrink)
        }
		
		scenario("create two drinks and retrieve the last one") {
    	  given("an empty list of drinks")
		  assert(Model.all(classOf[Drink]).fetch().size === 0)
		  and("inserted a new drink")
		  val pic = this.picture
		  val newDrink = new Drink("drink", pic)
		  assert(Model.all(classOf[Drink]).fetch().size === 1)
		  and("inserted a second drink")
		  val newDrink2 = new Drink("drink2", pic)
		  assert(Model.all(classOf[Drink]).fetch().size === 2)
		  when("retrieve the last drink")
		  val drinks = Drink.getLastN(1)
	      then("return just one drink")
		  assert(drinks.size() === 1 )
		  and ("the retrieved drink is the last inserted")
		  assert(drinks.get(0).id === newDrink2.id)
        }
		
		scenario("create three drinks and retrieve the last two") {
    	  given("an empty list of drinks")
		  assert(Model.all(classOf[Drink]).fetch().size === 0)
		  and("inserted a new drink")
		  val pic =this.picture
		  pic.insert()
		  val newDrink = new Drink("drink", pic)
		  assert(Model.all(classOf[Drink]).fetch().size === 1)
		  and("inserted a second drink")
		  val newDrink2 = new Drink("drink2", pic)
		  assert(Model.all(classOf[Drink]).fetch().size === 2)
		  and("inserted a third drink")
		  val newDrink3 = new Drink("drink3", pic)
		  assert(Model.all(classOf[Drink]).fetch().size === 3)
		  when("retrieve the last two drinks")
		  val drinks = Drink.getLastN(2)
	      then("return just two drinks")
		  assert(drinks.size() === 2 )
		  and ("the first retrieved drink is the last inserted")
		  assert(drinks.get(0).id === newDrink3.id)
		  and ("the second retrieved drink is the 2 inserted")
		  assert(drinks.get(1).id === newDrink2.id)
        }
		
		scenario("create three drinks and retrieve the last one") {
    	  given("a empty list of drinks")
		  assert(Model.all(classOf[Drink]).fetch().size === 0)
		  and("inserted a new drink")
		  val pic = this.picture
		  pic.insert()
		  val newDrink = new Drink("drink", pic)
		  assert(Model.all(classOf[Drink]).fetch().size === 1)
		  and("inserted a second drink")
		  val newDrink2 = new Drink("drink2", pic)
		  assert(Model.all(classOf[Drink]).fetch().size === 2)
		  and("inserted a third drink")
		  val newDrink3 = new Drink("drink3", pic)
		  assert(Model.all(classOf[Drink]).fetch().size === 3)
		  when("retrieve the last drink")
		  val drinks = Drink.getLastN(1)
	      then("return just one drink")
		  assert(drinks.size() === 1 )
		  and ("the retrieved drink is the last inserted")
		  assert(drinks.get(0).id === newDrink3.id)
        }
		
		scenario("retrieve drink by name") {
    	   given("a empty list of drinks")
		  assert(Model.all(classOf[Drink]).fetch().size === 0)
		  and("inserted a new drink")
		  val pic = this.picture
		  pic.insert()
		  val newDrink = new Drink("drink", pic)
		  and("inserted a second drink")
		  assert(Model.all(classOf[Drink]).fetch().size === 1)
		  val newDrink2 = new Drink("drink2", pic)
		  and("inserted a third drink")
		  assert(Model.all(classOf[Drink]).fetch().size === 2)
		  val newDrink3 = new Drink("drink3", pic)
		  assert(Model.all(classOf[Drink]).fetch().size === 3)
		  when("retrieve drink by name")
		  val drink = Drink.findByName("drink2")
	      then("return correct drink")
		  assert(drink.id === newDrink2.id ) 
        }
		
		scenario("retrieve drink by name retrieve nothing if name doesnt exist") {
    	  given("a empty list of drinks")
		  assert(Model.all(classOf[Drink]).fetch().size === 0)
		  and("inserted a new drink")
		  val pic = this.picture
		  pic.insert()
		  val newDrink = new Drink("drink", pic)
		  assert(Model.all(classOf[Drink]).fetch().size === 1)
		  and("inserted a second drink")
		  val newDrink2 = new Drink("drink2", pic)
		  and("inserted a third drink")
		  assert(Model.all(classOf[Drink]).fetch().size === 2)
		  val newDrink3 = new Drink("drink3", pic)
		  assert(Model.all(classOf[Drink]).fetch().size === 3)
		  when("retrieve drink by name")
		  val drink = Drink.findByName("drinkNoExists")
		  then("return null")
		  assert(drink === null ) 
        }
		
     }
	
	
	
     feature("The user can browse drinks") { 
    	
		//Story
        info("As a programmer")
	    info("I want to be able to browse the drink catalog")
	    info("So I can see the latest drinks added")
        
		//Scenarios
	    scenario("search for drink name in LOWER CASE returns the drinks with that name") {
    	  given("inserting a drink with name drinkNotFound1")
		   val pic = this.picture
		  pic.insert()
		  new Drink("drinkNotFound1", pic)
		  and("inserting a drink with name drinkFound1")
		  new Drink("drinkFound1", pic)
		  and("inserting a drink with name drinkFound2")
		  new Drink("drinkFound2", pic)
		  and("inserting a drink with name drinkNotFound2")
		   new Drink("drinkNotFound2", pic)
		  when("search for drinkfound (LOWER CASE)")
		  val drinks = Drink.Search("drinkfound")
	      then("return a list of drinks")
		  assert(drinks.size() === 2)
		  and("the first element is drinkFound1")
		  assert(drinks.get(0).name === "drinkFound1")
		  and("the second element is drinkFound2")
		  assert(drinks.get(1).name === "drinkFound2")
        }
		
		scenario("search for name which does not exist") {
    	  given("inserting a drink with name drinkNotFound1")
		   val pic = this.picture
		  pic.insert()
		  new Drink("drinkNotFound1", pic)
		  and("inserting a drink with name drinkFound1")
		  new Drink("drinkFound1", pic)
		  and("inserting a drink with name drinkFound2")
		  new Drink("drinkFound2", pic)
		  and("inserting a drink with name drinkNotFound2")
		   new Drink("drinkNotFound2", pic)
		  when("search for NotFound")
		  val drinks = Drink.Search("NotFound")
	      then("return a empty list of drinks")
		  assert(drinks.size() === 0)
        }
		
		scenario("search for drink with UPPER CASE doesnt work") {
    	  given("inserting a drink with name drinkNotFound1")
		   val pic = this.picture
		  pic.insert()
		  new Drink("drinkNotFound1", pic)
		  and("inserting a drink with name drinkFound1")
		  new Drink("drinkFound1", pic)
		  and("inserting a drink with name drinkFound2")
		  new Drink("drinkFound2", pic)
		  and("inserting a drink with name drinkNotFound2")
		   new Drink("drinkNotFound2", pic)
		  when("search for DRINKFOUND")
		  val drinks = Drink.Search("DRINKFOUND")
	      then("return a list of drinks")
		  assert(drinks.size() === 0)
        }
		
		scenario("search for drink with MIXING CASE doesnt work") {
    	  given("inserting a drink with name drinkNotFound1")
		   val pic = this.picture
		  pic.insert()
		  new Drink("drinkNotFound1", pic)
		  and("inserting a drink with name drinkFound1")
		  new Drink("drinkFound1", pic)
		  and("inserting a drink with name drinkFound2")
		  new Drink("drinkFound2", pic)
		  and("inserting a drink with name drinkNotFound2")
		   new Drink("drinkNotFound2", pic)
		  when("search for drinkFound")
		  val drinks = Drink.Search("drinkFound")
	      then("return a list of drinks")
		  assert(drinks.size() === 0)
        }
     }
  
}
