package json.transporter.jackson.deserialize;

import java.io.IOException;

import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.impl.PropertyValueBuffer;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;

import static json.transporter.jackson.OthersFieldInstrumentation.getGeneratedClass;
import static json.transporter.jackson.OthersFieldInstrumentation.getWithArgsConstructor;

public class EnhancedBeanInstantiator extends ValueInstantiator {
    private final ValueInstantiator decoratedInstantiator;

    public EnhancedBeanInstantiator(ValueInstantiator decoratedInstantiator) {
        this.decoratedInstantiator = decoratedInstantiator;
        decoratedInstantiator.getWithArgsCreator();
    }

    @Override
    public Class<?> getValueClass() {
        return decoratedInstantiator.getValueClass();
    }

    @Override
    public String getValueTypeDesc() {
        return decoratedInstantiator.getValueTypeDesc();
    }

    @Override
    public boolean canInstantiate() {
        return decoratedInstantiator.canInstantiate();
    }

    @Override
    public boolean canCreateFromString() {
        return decoratedInstantiator.canCreateFromString();
    }

    @Override
    public boolean canCreateFromInt() {
        return decoratedInstantiator.canCreateFromInt();
    }

    @Override
    public boolean canCreateFromLong() {
        return decoratedInstantiator.canCreateFromLong();
    }

    @Override
    public boolean canCreateFromDouble() {
        return decoratedInstantiator.canCreateFromDouble();
    }

    @Override
    public boolean canCreateFromBoolean() {
        return decoratedInstantiator.canCreateFromBoolean();
    }

    @Override
    public boolean canCreateUsingDefault() {
        return decoratedInstantiator.canCreateUsingDefault();
    }

    @Override
    public boolean canCreateUsingDelegate() {
        return decoratedInstantiator.canCreateUsingDelegate();
    }

    @Override
    public boolean canCreateUsingArrayDelegate() {
        return decoratedInstantiator.canCreateUsingArrayDelegate();
    }

    @Override
    public boolean canCreateFromObjectWith() {
        return decoratedInstantiator.canCreateFromObjectWith();
    }

    @Override
    public SettableBeanProperty[] getFromObjectArguments(DeserializationContext ctxt) {
        return decoratedInstantiator.getFromObjectArguments(ctxt);
    }

    @Override
    public JavaType getDelegateType(DeserializationConfig config) {
        return decoratedInstantiator.getDelegateType(config);
    }

    @Override
    public JavaType getArrayDelegateType(DeserializationConfig config) {
        return decoratedInstantiator.getArrayDelegateType(config);
    }

    @Override
    public Object createUsingDefault(DeserializationContext ctxt) {
        final Class<?> enhancedClass = getGeneratedClass(decoratedInstantiator.getValueClass());
        try {
            return enhancedClass.getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Cannot instantiate bean, `" + decoratedInstantiator.getValueClass() +
                    "`, it must have public default constructor", e);
        }
    }

    @Override
    public Object createFromObjectWith(DeserializationContext ctxt, Object[] args) throws IOException {
        return decoratedInstantiator.createFromObjectWith(ctxt, args);
    }

    @Override
    public Object createFromObjectWith(DeserializationContext ctxt, SettableBeanProperty[] props, PropertyValueBuffer buffer) {
        return getWithArgsConstructor(decoratedInstantiator.getValueClass(), props)
            .map(constructor -> {
                try {
                    return (Object)constructor.newInstance(buffer.getParameters(props));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }).orElseGet(() -> {
                try {
                    return decoratedInstantiator.createFromObjectWith(ctxt, props, buffer);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    @Override
    public Object createUsingDelegate(DeserializationContext ctxt, Object delegate) throws IOException {
        return decoratedInstantiator.createUsingDelegate(ctxt, delegate);
    }

    @Override
    public Object createUsingArrayDelegate(DeserializationContext ctxt, Object delegate) throws IOException {
        return decoratedInstantiator.createUsingArrayDelegate(ctxt, delegate);
    }

    @Override
    public Object createFromString(DeserializationContext ctxt, String value) throws IOException {
        return decoratedInstantiator.createFromString(ctxt, value);
    }

    @Override
    public Object createFromInt(DeserializationContext ctxt, int value) throws IOException {
        return decoratedInstantiator.createFromInt(ctxt, value);
    }

    @Override
    public Object createFromLong(DeserializationContext ctxt, long value) throws IOException {
        return decoratedInstantiator.createFromLong(ctxt, value);
    }

    @Override
    public Object createFromDouble(DeserializationContext ctxt, double value) throws IOException {
        return decoratedInstantiator.createFromDouble(ctxt, value);
    }

    @Override
    public Object createFromBoolean(DeserializationContext ctxt, boolean value) throws IOException {
        return decoratedInstantiator.createFromBoolean(ctxt, value);
    }

    @Override
    public AnnotatedWithParams getDefaultCreator() {
        return decoratedInstantiator.getDefaultCreator();
    }

    @Override
    public AnnotatedWithParams getDelegateCreator() {
        return decoratedInstantiator.getDelegateCreator();
    }

    @Override
    public AnnotatedWithParams getArrayDelegateCreator() {
        return decoratedInstantiator.getArrayDelegateCreator();
    }

    @Override
    public AnnotatedWithParams getWithArgsCreator() {
        return decoratedInstantiator.getWithArgsCreator();
    }
}
