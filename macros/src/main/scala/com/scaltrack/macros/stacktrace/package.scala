package com.scaltrack.macros

import scala.concurrent.Future
import scala.reflect.macros.blackbox.Context
import scala.language.experimental.macros

package object stacktrace {

  /**
   * When exception is thrown in different thread, only stack track of that thread is
   * shown in the log and, for example, if something went wrong in the slick,
   * the log message is not very helpful, since it does not shows in which place
   * in app's code the failed method / function, etc was called.
   *
   * This macro improves it little bit.
   * It recovers from failed future and rethrow exception so we can have at least
   * source of app's code from which that failed was called.
   *
   * This macro ignores exceptions thrown from app code (e.g. all subclasses of
   * library.error.BaseException class).
   *
   * This is mostly uses tu surround app code which uses third party libraries.
   *
   * @param body
   * @tparam T
   * @return
   */
  def withCurrentStack[T](body: => Future[T]): Future[T] = macro withCurrentStackImpl

  def withCurrentStackImpl(c: Context)(body: c.Tree): c.Tree = {
    import c.universe._

    q"""
        $body.recover {
          case e if !e.isInstanceOf[library.error.BaseException] =>
           throw new library.error.RethrownFromTrackedStackException(e)
        }
     """
  }
}
