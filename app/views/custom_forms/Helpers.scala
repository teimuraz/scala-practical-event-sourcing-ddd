package views.custom_forms

import views.html.custom_forms.customFieldConstructor

object Helpers {
  import views.html.helper.FieldConstructor
  implicit val customFields = FieldConstructor(customFieldConstructor.f)
}
