package controllers

case class ViewCommonData(title: String, breadCrumbs: Seq[BreadCrumb] = Seq.empty)
case class BreadCrumb(label: String, url: Option[String] = None)
