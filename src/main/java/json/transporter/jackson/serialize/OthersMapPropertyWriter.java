package json.transporter.jackson.serialize;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;

import static json.transporter.jackson.OthersFieldInstrumentation.getOthersMap;

public class OthersMapPropertyWriter extends BeanPropertyWriter {
    public OthersMapPropertyWriter(BeanPropertyWriter beanPropertyWriter) {
        super(beanPropertyWriter);
    }

    @Override
    public void serializeAsField(Object bean, JsonGenerator gen, SerializerProvider prov) {
        getOthersMap(bean).ifPresent(others -> others.forEach((key, value) -> {
            try {
                gen.writeFieldName(key);
                gen.writeRawValue(value);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }));
    }
}
