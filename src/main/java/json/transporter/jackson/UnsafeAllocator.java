package json.transporter.jackson;

import java.lang.reflect.Field;

import sun.misc.Unsafe;

public class UnsafeAllocator {

    private static final Unsafe theUnsafe;
    static {
        try {
            Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
            Field f = unsafeClass.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            theUnsafe = (Unsafe)f.get(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T createObjectStub(final Class<T> clazz) {
        try {
            return (T)theUnsafe.allocateInstance(clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
