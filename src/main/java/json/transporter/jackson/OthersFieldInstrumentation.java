package json.transporter.jackson;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import json.transporter.builder.Builder;
import json.transporter.builder.interceptor.CopyBuilderMethodInterceptor;
import json.transporter.builder.CopyBuilderProvider;
import json.transporter.builder.CopyMethodProvider;
import json.transporter.builder.interceptor.EnrichInterceptor;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.scaffold.subclass.ConstructorStrategy.Default;
import net.bytebuddy.implementation.MethodDelegation;

import static java.lang.reflect.Modifier.PUBLIC;
import static java.util.Arrays.stream;
import static java.util.Optional.empty;

public class OthersFieldInstrumentation {
    public static final String OTHERS_FIELD_NAME = "_others";

    private static final Map<Class<?>, Class<?>> enhancedClasses = new ConcurrentHashMap<>();
    private static final Map<Class<?>, Class<?>> enhancedBuilderClasses = new ConcurrentHashMap<>();
    private static final Map<Class<?>, Optional<Constructor<?>>> withArgsConstructors = new ConcurrentHashMap<>();
    private static final Map<Class<?>, Optional<Field>> othersFields = new ConcurrentHashMap<>();
    private static final Map<Class<?>, Object> methodDelegators = new ConcurrentHashMap<>();

    public static Optional<Field> getOthersField(final Object bean) {
        return othersFields.computeIfAbsent(bean.getClass(), clazz -> {
            try {
                Field found = clazz.getDeclaredField(OTHERS_FIELD_NAME);
                if (Map.class.isAssignableFrom(found.getType())) {
                    found.setAccessible(true);
                    return Optional.of(found);

                } else {
                    //Found, but it is not Map
                    return empty();
                }
            } catch (NoSuchFieldException e) {
                //just skip if it not enchanced
                return empty();
            }
        });
    }

    public static Optional<Map<String, String>> getOthersMap(final Object o) {
        return getOthersField(o).map(mapField -> {
            try {
                Map<String, String> _others = (Map<String, String>) mapField.get(o);
                if (_others == null) {
                    _others = new HashMap<>();
                    mapField.set(o, _others);
                }
                return _others;
            } catch (IllegalAccessException e) {
                //should not happen
                throw new RuntimeException(e);
            }
        });
    }

    public static <T> Optional<Constructor<?>> getWithArgsConstructor(final Class<T> valueClass, final SettableBeanProperty[] props) {
        return withArgsConstructors.computeIfAbsent(getGeneratedClass(valueClass), clazz -> {
            try {
                return Optional.of(clazz.getConstructor(stream(props)
                        .map(p -> p.getType().getRawClass()).toArray(size -> new Class[props.length])));
            } catch (NoSuchMethodException e) {
                return empty();
            }
        });
    }

    public static <T> Class<?> getGeneratedClass(final Class<T> valueClass) {
        return enhancedClasses.computeIfAbsent(valueClass, toEnhance -> {
            try {
                DynamicType.Builder<?> typeBuilder = new ByteBuddy().subclass(toEnhance, Default.IMITATE_SUPER_CLASS)
                        .defineField(OTHERS_FIELD_NAME, Map.class, Visibility.PRIVATE);
                if (CopyBuilderProvider.class.isAssignableFrom(valueClass)) {
                    typeBuilder = typeBuilder.defineMethod("copyBuilder", Builder.class, PUBLIC).intercept(MethodDelegation.to(CopyBuilderMethodInterceptor.class));
                }
                if (CopyMethodProvider.class.isAssignableFrom(valueClass)) {
                    typeBuilder = typeBuilder.defineMethod("copy", Object.class, PUBLIC).intercept(MethodDelegation.to(EnrichInterceptor.class));
                }
                return typeBuilder.make().load(toEnhance.getClassLoader()).getLoaded();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static <B extends Builder> Class<?> getGeneratedBuilderClass(final Class<B> builderClass) {
        return enhancedBuilderClasses.computeIfAbsent(builderClass, toEnhance ->
            new ByteBuddy().subclass(toEnhance, Default.IMITATE_SUPER_CLASS)
                    .defineField(OTHERS_FIELD_NAME, Map.class, Visibility.PRIVATE)
                    .defineMethod("build", Object.class, PUBLIC)
                    .intercept(MethodDelegation.to(EnrichInterceptor.class))
                    .make().load(toEnhance.getClassLoader()).getLoaded()
        );
    }

    public static <T> void copyFieldValues(final T from, final T to) {
        stream(from.getClass().getDeclaredFields()).forEach(field -> {
            try {
                field.setAccessible(true);
                final Object fieldValue = field.get(from);
                field.set(to, fieldValue);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
