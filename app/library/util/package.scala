package library

package object util {

  implicit class StringOps(str: String) {
    /**
     * Trim white spaces in the beginning and the end, also leave only single white spaces in string.
     * @return
     */
    def fullTrim: String = str.replaceAll("\\s{2,}", " ").trim

    def slugify: String = {
      import java.text.Normalizer
      Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\w ]", "").replace(" ", "-").toLowerCase
    }

    def lowerCaseFirstCharacter: String = {
      if (str.length > 0) {
        Character.toLowerCase(str.charAt(0)) + str.substring(1)
      } else {
        str
      }
    }
  }
}


