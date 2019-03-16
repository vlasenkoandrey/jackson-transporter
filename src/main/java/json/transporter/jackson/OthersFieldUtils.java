package json.transporter.jackson;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Optional.empty;

public class OthersFieldUtils {
    public static final String OTHERS_FIELD_NAME = "_others";

    private static final Map<Class<?>, Optional<Field>> othersFields = new ConcurrentHashMap<>();

    public static Optional<Field> getOthersField(Object bean) {
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

    public static Optional<Map<String, String>> getOthersMap(Object o) {
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
}
