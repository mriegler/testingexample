import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec

class SampleTest: WordSpec({
    "Given a string, String.length" should {
        "return its length" {
            "asd".length shouldBe 3
        }
    }
})