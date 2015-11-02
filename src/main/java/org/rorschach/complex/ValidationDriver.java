package org.rorschach.complex;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ValidationDriver<V, T> {

    public boolean RunValidation(V validator, T target) throws NoSuchFieldException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        FieldDepend[] depends = accumulateDepends(
                Arrays.stream(ReflectionHelper.getMarkedMethods(validator))
                        .map(ValidationDriver::buildFieldDependFromSignature)
                        .toArray(FieldDepend[]::new));

        String[] order = solveDepend(depends);
        Arrays.stream(order).forEach(System.out::println);

        for(String t : order) {
            Method[] methods = Arrays.stream(ReflectionHelper.getMarkedMethods(validator))
                    .filter(it -> buildFieldDependFromSignature(it).getTarget().equals(t))
                    .toArray(Method[]::new);

            for(Method m : methods) {
                boolean result = (boolean)m.invoke(validator, buildParameter(signatureToFieldName(m), target));
                if(!result) return false;
            }
        }

        return true;
    }

    private static FieldDepend buildFieldDependFromSignature(Method method) {
        Parameter[] p = method.getParameters();
        Target t = p[0].getAnnotation(Target.class);
        Stream<Depend> d = Arrays.stream(p)
                .skip(1)
                .map(it -> it.getAnnotation(Depend.class));

        FieldDepend depend = new FieldDepend(t.value());
        d.forEach(it -> depend.getDepend().add(it.value()));
        return depend;
    }

    private static String getParameterIndicateFieldName(Parameter parameter) {
        if(parameter.getAnnotation(Target.class) != null) {
            return parameter.getAnnotation(Target.class).value();
        } else {
            return parameter.getAnnotation(Depend.class).value();
        }
    }

    private static String[] signatureToFieldName(Method method) {
        return Arrays.stream(method.getParameters())
                .map(ValidationDriver::getParameterIndicateFieldName)
                .toArray(String[]::new);
    }

    private static Object[] buildParameter(String[] fieldNames, Object value) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        List<Object> param = new ArrayList<>(fieldNames.length);
        for(String f : fieldNames) {
            param.add(ReflectionHelper.getFieldValueViaGetter(f, value));
        }
        return param.toArray();
    }

    private static FieldDepend[] accumulateDepends(FieldDepend[] depends) {
        Set<String> fields = new HashSet<>();
        for(FieldDepend depend : depends) {
            fields.add(depend.getTarget());
            fields.addAll(depend.getDepend().stream().collect(Collectors.toList()));
        }

        Map<String, FieldDepend> d = new HashMap<>();
        for(String it : fields) {
            d.put(it, new FieldDepend(it));
        }

        for(FieldDepend depend : depends) {
            d.get(depend.getTarget())
                    .getDepend().
                    addAll(depend.getDepend().stream().collect(Collectors.toList()));
        }

        return d.values().stream().toArray(FieldDepend[]::new);
    }

    private static String[] solveDepend(FieldDepend[] depends) {
        List<String> order = new ArrayList<>(depends.length);

        order.addAll(Arrays.stream(depends)
                .filter(it -> it.getDepend().size() == 0)
                .map(FieldDepend::getTarget)
                .collect(Collectors.toList()));

        List<FieldDepend> d = Arrays.stream(depends)
                .filter(it -> it.getDepend().size() != 0)
                .collect(Collectors.toList());

        while(d.size() != 0) {
            for(FieldDepend depend : d) {
                if(order.containsAll(depend.getDepend())) {
                    order.add(depend.getTarget());
                }
            }
            for(String o : order) {
                d.removeIf(it -> it.getTarget().equals(o));
            }
        }

        return order.stream().toArray(String[]::new);
    }

}
