package json.transporter.builder.interceptor;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.concurrent.Callable;

import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.implementation.bind.annotation.This;

import static json.transporter.jackson.OthersFieldInstrumentation.copyFieldValues;
import static json.transporter.jackson.OthersFieldInstrumentation.getGeneratedClass;
import static json.transporter.jackson.OthersFieldInstrumentation.getOthersField;
import static json.transporter.jackson.OthersFieldInstrumentation.getOthersMap;
import static json.transporter.jackson.UnsafeAllocator.createObjectStub;

public class EnrichInterceptor {
    public static <T> T enrichWithOthers(@SuperCall Callable<T> toEnrich, @This Object donor) throws Exception {
        final T originalCopy = toEnrich.call();
        final T newInstance = (T) createObjectStub(getGeneratedClass(originalCopy.getClass()));
        copyFieldValues(originalCopy, newInstance);
        final Optional<Field> othersField = getOthersField(newInstance);
        if (othersField.isPresent()) {
            othersField.get().set(newInstance, getOthersMap(donor).orElse(null));
        }
        return newInstance;
    }
}
