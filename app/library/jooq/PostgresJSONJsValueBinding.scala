package library.jooq

import org.jooq._
import play.api.libs.json.{JsValue, Json}

//class PostgresJSONJsValueBinding extends Binding[Object, JsValue] {
//
//  override def converter(): Converter[Object, JsValue] = new Converter[Object, JsValue] {
//    override def from(databaseObject: Object): JsValue = Json.parse(databaseObject.toString)
//
//    override def to(userObject: JsValue): Object = userObject.toString()
//
//    override def fromType(): Class[Object] = classOf[Object]
//
//    override def toType: Class[JsValue] = classOf[JsValue]
//  }
//
//  override def set(ctx: BindingSetStatementContext[JsValue]): Unit = {
//    import java.util.Objects
//    ctx.statement.setString(ctx.index, Objects.toString(ctx.convert(converter).value, null))
//  }
//
//  override def set(ctx: BindingSetSQLOutputContext[JsValue]): Unit = ???
//
//  override def sql(ctx: BindingSQLContext[JsValue]): Unit = {
//    import org.jooq.impl.DSL
//    ctx.render.visit(DSL.`val`(ctx.convert(converter).value)).sql("::json")
//  }
//
//  override def get(ctx: BindingGetResultSetContext[JsValue]): Unit = {
//    ctx.convert(converter).value(ctx.resultSet.getString(ctx.index))
//  }
//
//  override def get(ctx: BindingGetStatementContext[JsValue]): Unit = {
//    ctx.convert(converter).value(ctx.statement.getString(ctx.index))
//  }
//
//  override def get(ctx: BindingGetSQLInputContext[JsValue]): Unit = ???
//
//  override def register(ctx: BindingRegisterContext[JsValue]): Unit = {
//    import java.sql.Types
//    ctx.statement.registerOutParameter(ctx.index, Types.VARCHAR)
//  }
//}
