package json.transporter.jackson.deserialize;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.cfg.DeserializerFactoryConfig;
import com.fasterxml.jackson.databind.deser.BeanDeserializerFactory;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;

public class WithPatchedInstantiatorDeserializerFactory extends BeanDeserializerFactory {
    private final Map<JavaType, ValueInstantiator> valueInstantiators = new ConcurrentHashMap<>();

    public WithPatchedInstantiatorDeserializerFactory(final DeserializerFactoryConfig config) {
        super(config);
    }

    @Override
    public ValueInstantiator findValueInstantiator(final DeserializationContext ctxt, final BeanDescription beanDesc) {
        return valueInstantiators.computeIfAbsent(beanDesc.getType(), javaType -> {
            try {
                return new EnhancedBeanInstantiator(super.findValueInstantiator(ctxt, beanDesc));
            } catch (JsonMappingException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
