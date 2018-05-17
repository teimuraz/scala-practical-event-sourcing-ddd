package library.crypto

import java.security.MessageDigest
import javax.xml.bind.DatatypeConverter

object Crypto {

  def md5(value: String): String = {
    val md = MessageDigest.getInstance("MD5")
    md.update(value.getBytes)
    DatatypeConverter.printHexBinary(md.digest).toUpperCase()
  }
}
