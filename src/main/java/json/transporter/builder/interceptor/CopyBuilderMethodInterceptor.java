package json.transporter.builder.interceptor;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.concurrent.Callable;

import json.transporter.builder.Builder;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.implementation.bind.annotation.This;

import static json.transporter.jackson.OthersFieldInstrumentation.copyFieldValues;
import static json.transporter.jackson.OthersFieldInstrumentation.getGeneratedBuilderClass;
import static json.transporter.jackson.OthersFieldInstrumentation.getOthersField;
import static json.transporter.jackson.OthersFieldInstrumentation.getOthersMap;
import static json.transporter.jackson.UnsafeAllocator.createObjectStub;

public class CopyBuilderMethodInterceptor {
    public static <T> Builder<T> intercept(@SuperCall Callable<Builder<T>> zuper, @This Object donor) throws Exception {
        final Builder<T> builder = zuper.call();
        final Builder<T> enhancedBuilder = (Builder<T>)createObjectStub(getGeneratedBuilderClass(builder.getClass()));
        copyFieldValues(builder, enhancedBuilder);
        final Optional<Field> othersField = getOthersField(enhancedBuilder);
        if (othersField.isPresent()) {
            othersField.get().set(enhancedBuilder, getOthersMap(donor).orElse(null));
        }
        return enhancedBuilder;
    }
}