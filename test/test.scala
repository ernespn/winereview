import org.scalatest._
import play.test._
import org.scalatest.matchers.ShouldMatchers

class SpecStyle extends UnitFlatSpec with ShouldMatchers {
 
    val name = "Hello World"
 
    "'Hello World'" should "not contain the X letter" in {
        name should not include ("X")
    }
 
    it should "have 11 chars" in {
        name should have length (11)      
    }
    
}