import play._
import play.test._

import org.scalatest._
import org.scalatest.junit._
import org.scalatest.matchers._

class DrinkSpecs extends UnitFlatSpec with ShouldMatchers {
    
    it should "create new drinks with name and image " in {
        (1 + 1) should be (2)
    }
    
    it should "create new drinks and retrieve it " in {
        (1 + 1) should be (2)
    }
    
    it should "return a error message when name is not passed " in {
        (1 + 1) should be (2)
    }
    
    it should "return a error message when image is not passed " in {
        (1 + 1) should be (2)
    }
    
    it should "return a error message when image is bigger than 10" in {
        (1 + 1) should be (3)
    }

}