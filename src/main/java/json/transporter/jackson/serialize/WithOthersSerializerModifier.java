package json.transporter.jackson.serialize;

import java.util.List;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;

import static json.transporter.jackson.OthersFieldInstrumentation.OTHERS_FIELD_NAME;

public class WithOthersSerializerModifier extends BeanSerializerModifier {
    @Override
    public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc, List<BeanPropertyWriter> beanProperties) {
        for (int i = 0; i < beanProperties.size(); i++) {
            BeanPropertyWriter beanPropertyWriter = beanProperties.get(i);
            if (OTHERS_FIELD_NAME.equals(beanPropertyWriter.getName())) {
                beanProperties.set(i, new OthersMapPropertyWriter(beanPropertyWriter));
            }
        }
        return beanProperties;
    }
}
