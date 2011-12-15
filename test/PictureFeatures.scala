import play.test.UnitFeatureSpec
import org.scalatest.GivenWhenThen
import org.scalatest.BeforeAndAfterEach
import org.scalatest._
import org.scalatest.junit._
import model._
import siena._
import scala.collection.JavaConversions._
import com.google.appengine.api.datastore.Blob
import java.io.File;
import java.io.FileInputStream
import play.Play;

class PictureFeature extends UnitFeatureSpec with GivenWhenThen with BeforeAndAfterEach{ 
    
	override def beforeEach() {
			Model.all(classOf[Picture]).fetch().foreach(d => d.delete()) 
    }
	
	//Feature
    feature("The user can find Images") { 
    	//Story
        info("As a programmer")
	    info("I want to be able to find images")
	    info("So I can see images")
        //Scenarios
		scenario("user can save images") {  
		  given("an Image")
			val file = Play.getFile("test/Wine.jpg")
			val is = new FileInputStream(file)
			val len = file.length.toInt
			val bytes = new Array[Byte](len)

    	  when("create a Picture")
		  val picture = new Picture(bytes)
  	      then("the new Picture has been stored correctly and can be retrieved")
	      val retrievedPic = Picture.findById(picture.id)
		  assert(retrievedPic === picture)

        }
		
		scenario("user cannot save images bigger than 1MB") {  
		  given("an Image")
			val file = Play.getFile("test/ImageBiggerThanAllow.jpg")
			val is = new FileInputStream(file)
			val len = file.length.toInt
			val bytes = new Array[Byte](len)

    	  when("create a Picture")
		  then("the return an Illegal argument exception")
		  intercept[IllegalArgumentException] {
	          val picture = new Picture(bytes)
	      }
        }
		

    }
  
}