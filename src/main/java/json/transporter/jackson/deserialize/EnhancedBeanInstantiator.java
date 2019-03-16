package json.transporter.jackson.deserialize;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.impl.PropertyValueBuffer;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.modifier.Visibility;

import static json.transporter.jackson.OthersFieldUtils.OTHERS_FIELD_NAME;

public class EnhancedBeanInstantiator extends ValueInstantiator {
    private static final Map<Class<?>, Class<?>> enhancedClasses = new HashMap<>();

    private final ValueInstantiator decoratedInstantiator;

    public EnhancedBeanInstantiator(ValueInstantiator decoratedInstantiator) {
        this.decoratedInstantiator = decoratedInstantiator;
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
        Class<?> enhancedClass = enhancedClasses.computeIfAbsent(decoratedInstantiator.getValueClass(), toEnhance ->
            new ByteBuddy()
                .subclass(toEnhance)
                .defineField(OTHERS_FIELD_NAME, Map.class, Visibility.PRIVATE)
                .make()
                .load(toEnhance.getClassLoader())
                .getLoaded()
        );
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
    public Object createFromObjectWith(DeserializationContext ctxt, SettableBeanProperty[] props, PropertyValueBuffer buffer) throws IOException {
        return decoratedInstantiator.createFromObjectWith(ctxt, props, buffer);
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
