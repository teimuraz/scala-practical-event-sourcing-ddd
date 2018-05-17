package library.jooq;

import org.joda.time.DateTime;
import org.jooq.Converter;

import java.sql.Timestamp;

public class JodaDateTimeConverter implements Converter<Timestamp, DateTime> {

    @Override
    public DateTime from(Timestamp databaseObject) {
        return new DateTime(databaseObject);
    }

    @Override
    public Timestamp to(DateTime userObject) {
        return new Timestamp(userObject.getMillis());
    }

    @Override
    public Class<Timestamp> fromType() {
        return Timestamp.class;
    }

    @Override
    public Class<DateTime> toType() {
        return DateTime.class;
    }
}

