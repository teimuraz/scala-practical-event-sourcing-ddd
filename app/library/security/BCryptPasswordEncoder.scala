package library.security

import javax.inject.Singleton
import org.mindrot.jbcrypt.BCrypt

@Singleton
class BCryptPasswordEncoder extends PasswordEncoder {

  override def encode(password: String, salt: String): String = {
    val encoded = BCrypt.hashpw(password, salt)
    encoded
  }

  override def generateSalt(length: Int = 10): String = BCrypt.gensalt(length)
}
