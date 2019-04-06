package json.transporter.jackson;

import com.fasterxml.jackson.databind.cfg.DeserializerFactoryConfig;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;
import json.transporter.jackson.deserialize.CustomDeserializationProblemsHandler;
import json.transporter.jackson.deserialize.WithPatchedInstantiatorDeserializerFactory;
import json.transporter.jackson.serialize.WithOthersSerializerModifier;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;

public final class JsonTransporterMapper {
    public static JsonMapper.Builder builder() {
        return JsonMapper.builder().addHandler(new CustomDeserializationProblemsHandler())
                .serializerFactory(BeanSerializerFactory.instance.withSerializerModifier(new WithOthersSerializerModifier()))
                .deserializerFactory(new WithPatchedInstantiatorDeserializerFactory(new DeserializerFactoryConfig()))
                .changeDefaultVisibility(v -> v.withFieldVisibility(ANY));
    }
}
