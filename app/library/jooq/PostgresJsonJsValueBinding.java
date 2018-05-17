package library.jooq;

import static org.jooq.tools.Convert.convert;
import java.sql.*;
import java.util.Objects;

import org.jooq.*;
import org.jooq.impl.DSL;
import play.api.libs.json.JsValue;
import play.api.libs.json.Json;

// We're binding <T> = Object (unknown JDBC type), and <U> = JsValue (user type)
public class PostgresJsonJsValueBinding implements Binding<Object, JsValue> {

    // The converter does all the work
    @Override
    public Converter<Object, JsValue> converter() {
        return new JsValueConverter();
    }

    // Rending a bind variable for the binding context's value and casting it to the json type
    @Override
    public void sql(BindingSQLContext<JsValue> ctx) throws SQLException {
        // Depending on how you generate your SQL, you may need to explicitly distinguish
        // between jOOQ generating bind variables or inlined literals. If so, use this check:
        // ctx.render().paramType() == INLINED
        ctx.render().visit(DSL.val(ctx.convert(converter()).value())).sql("::json");
    }

    // Registering VARCHAR types for JDBC CallableStatement OUT parameters
    @Override
    public void register(BindingRegisterContext<JsValue> ctx) throws SQLException {
        ctx.statement().registerOutParameter(ctx.index(), Types.VARCHAR);
    }

    // Converting the JsValue to a String value and setting that on a JDBC PreparedStatement
    @Override
    public void set(BindingSetStatementContext<JsValue> ctx) throws SQLException {
        ctx.statement().setString(ctx.index(), Objects.toString(ctx.convert(converter()).value(), null));
    }

    // Getting a String value from a JDBC ResultSet and converting that to a JsValue
    @Override
    public void get(BindingGetResultSetContext<JsValue> ctx) throws SQLException {
        ctx.convert(converter()).value(ctx.resultSet().getString(ctx.index()));
    }

    // Getting a String value from a JDBC CallableStatement and converting that to a JsValue
    @Override
    public void get(BindingGetStatementContext<JsValue> ctx) throws SQLException {
        ctx.convert(converter()).value(ctx.statement().getString(ctx.index()));
    }

    // Setting a value on a JDBC SQLOutput (useful for Oracle OBJECT types)
    @Override
    public void set(BindingSetSQLOutputContext<JsValue> ctx) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    // Getting a value from a JDBC SQLInput (useful for Oracle OBJECT types)
    @Override
    public void get(BindingGetSQLInputContext<JsValue> ctx) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }
}

