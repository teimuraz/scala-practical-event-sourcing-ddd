import org.scalatest.{MustMatchers, WordSpec}
import org.scalatestplus.play.PlaySpec

class UnitSpec extends WordSpec with MustMatchers {

  "test sum" in {
    1 + 3 mustBe 4
  }

}
