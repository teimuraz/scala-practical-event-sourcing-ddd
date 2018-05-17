package library.jooq;

import org.joda.time.DateTime;
import org.jooq.Converter;
import play.api.libs.json.JsValue;
import play.api.libs.json.Json;

import java.sql.Timestamp;

public class JsValueConverter implements Converter<Object, JsValue> {
    @Override
    public JsValue from(Object t) {
        //                return t == null ? JsValue. : new Gson().fromJson("" + t, JsValue.class);
        return Json.parse("" + t);
    }

    @Override
    public Object to(JsValue u) {
        //                return u == null || u == JsonNull.INSTANCE ? null : new Gson().toJson(u);
        return u.toString();
    }

    @Override
    public Class<Object> fromType() {
        return Object.class;
    }

    @Override
    public Class<JsValue> toType() {
        return JsValue.class;
    }
};
