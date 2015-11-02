package org.rorschach.complex;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class ReflectionHelper {

    public static Method[] getMarkedMethods(Object object) {
        return Arrays.stream(object.getClass().getMethods())
                .filter(it -> it.getAnnotation(Verifier.class) != null)
                .toArray(Method[]::new);
    }

    public static Object getFieldValueViaGetter(String fieldName, Object value) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String getterName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        Method m = value.getClass().getMethod(getterName);
        return m.invoke(value, (Object[])null);
    }

}
