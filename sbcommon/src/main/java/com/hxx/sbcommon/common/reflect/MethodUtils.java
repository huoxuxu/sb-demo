package com.hxx.sbcommon.common.reflect;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Duration;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2024-01-09 13:44:02
 **/
public class MethodUtils {
    public static final Set<Class<?>> SIMPLE_TYPES = ofSet(
            Void.class,
            Boolean.class,
            Character.class,
            Byte.class,
            Short.class,
            Integer.class,
            Long.class,
            Float.class,
            Double.class,
            String.class,
            BigDecimal.class,
            BigInteger.class,
            Date.class,
            Object.class,
            Duration.class);

    /**
     * Convert to multiple values to be {@link LinkedHashSet}
     *
     * @param values one or more values
     * @param <T>    the type of <code>values</code>
     * @return read-only {@link Set}
     */
    public static <T> Set<T> ofSet(T... values) {
        int size = values == null ? 0 : values.length;
        if (size < 1) {
            return Collections.emptySet();
        }

        float loadFactor = 1f / ((size + 1) * 1.0f);

        if (loadFactor > 0.75f) {
            loadFactor = 0.75f;
        }

        Set<T> elements = new LinkedHashSet<>(size, loadFactor);
        for (int i = 0; i < size; i++) {
            elements.add(values[i]);
        }
        return Collections.unmodifiableSet(elements);
    }

    /**
     * The specified type is primitive type or simple type
     *
     * @param type the type to test
     * @return
     * @deprecated as 2.7.6, use {@link Class#isPrimitive()} plus {@link #isSimpleType(Class)} instead
     */
    public static boolean isPrimitive(Class<?> type) {
        return type != null && (type.isPrimitive() || isSimpleType(type));
    }

    /**
     * The specified type is simple type or not
     *
     * @param type the type to test
     * @return if <code>type</code> is one element of {@link #SIMPLE_TYPES}, return <code>true</code>, or <code>false</code>
     * @see #SIMPLE_TYPES
     * @since 2.7.6
     */
    public static boolean isSimpleType(Class<?> type) {
        return SIMPLE_TYPES.contains(type);
    }

    /**
     * Return {@code true} if the provided method is a set method.
     * Otherwise, return {@code false}.
     *
     * @param method the method to check
     * @return whether the given method is setter method
     */
    static boolean isSetter(Method method) {
        return method.getName().startsWith("set")
                && !"set".equals(method.getName())
                && Modifier.isPublic(method.getModifiers())
                && method.getParameterCount() == 1
                && isPrimitive(method.getParameterTypes()[0]);
    }

    /**
     * Return {@code true} if the provided method is a get method.
     * Otherwise, return {@code false}.
     *
     * @param method the method to check
     * @return whether the given method is getter method
     */
    static boolean isGetter(Method method) {
        String name = method.getName();
        return (name.startsWith("get") || name.startsWith("is"))
                && !"get".equals(name)
                && !"is".equals(name)
                && !"getClass".equals(name)
                && !"getObject".equals(name)
                && Modifier.isPublic(method.getModifiers())
                && method.getParameterTypes().length == 0
                && isPrimitive(method.getReturnType());
    }

    /**
     * Return {@code true} If this method is a meta method.
     * Otherwise, return {@code false}.
     *
     * @param method the method to check
     * @return whether the given method is meta method
     */
    static boolean isMetaMethod(Method method) {
        String name = method.getName();
        if (!(name.startsWith("get") || name.startsWith("is"))) {
            return false;
        }
        if ("get".equals(name)) {
            return false;
        }
        if ("getClass".equals(name)) {
            return false;
        }
        if (!Modifier.isPublic(method.getModifiers())) {
            return false;
        }
        if (method.getParameterTypes().length != 0) {
            return false;
        }
        if (!isPrimitive(method.getReturnType())) {
            return false;
        }
        return true;
    }

    /**
     * Check if the method is a deprecated method. The standard is whether the {@link java.lang.Deprecated} annotation is declared on the class.
     * Return {@code true} if this annotation is present.
     * Otherwise, return {@code false}.
     *
     * @param method the method to check
     * @return whether the given method is deprecated method
     */
    static boolean isDeprecated(Method method) {
        return method.getAnnotation(Deprecated.class) != null;
    }

    /**
     * Create an instance of {@link Predicate} for {@link Method} to exclude the specified declared class
     *
     * @param declaredClass the declared class to exclude
     * @return non-null
     * @since 2.7.6
     */
    static Predicate<Method> excludedDeclaredClass(Class<?> declaredClass) {
        return method -> !Objects.equals(declaredClass, method.getDeclaringClass());
    }

    /**
     * Get all {@link Method methods} of the declared class
     *
     * @param declaringClass        the declared class
     * @param includeInheritedTypes include the inherited types, e,g. super classes or interfaces
     * @param publicOnly            only public method
     * @param methodsToFilter       (optional) the methods to be filtered
     * @return non-null read-only {@link List}
     * @since 2.7.6
     */
    static List<Method> getMethods(
            Class<?> declaringClass,
            boolean includeInheritedTypes,
            boolean publicOnly,
            Predicate<Method>... methodsToFilter) {

        if (declaringClass == null || declaringClass.isPrimitive()) {
            return Collections.emptyList();
        }

        // All declared classes
        List<Class<?>> declaredClasses = new LinkedList<>();
        // Add the top declaring class
        declaredClasses.add(declaringClass);
        // If the super classes are resolved, all them into declaredClasses
        if (includeInheritedTypes) {
            declaredClasses.addAll(getAllInheritedTypes(declaringClass));
        }

        // All methods
        List<Method> allMethods = new LinkedList<>();

        for (Class<?> clsItem : declaredClasses) {
            Method[] methods = publicOnly ? clsItem.getMethods() : clsItem.getDeclaredMethods();
            // Add the declared methods or public methods
            allMethods.addAll(Arrays.asList(methods));
        }

        return Collections.unmodifiableList(Streams.filterAll(allMethods, methodsToFilter));
    }

    /**
     * Get all inherited types from the specified type
     *
     * @param type        the specified type
     * @param typeFilters the filters for types
     * @return non-null read-only {@link Set}
     * @since 2.7.6
     */
    public static Set<Class<?>> getAllInheritedTypes(Class<?> type, Predicate<Class<?>>... typeFilters) {
        // Add all super classes
        Set<Class<?>> types = new LinkedHashSet<>(getAllSuperClasses(type, typeFilters));
        // Add all interface classes
        types.addAll(getAllInterfaces(type, typeFilters));
        return Collections.unmodifiableSet(types);
    }


    /**
     * Get all super classes from the specified type
     *
     * @param type         the specified type
     * @param classFilters the filters for classes
     * @return non-null read-only {@link Set}
     * @since 2.7.6
     */
    public static Set<Class<?>> getAllSuperClasses(Class<?> type, Predicate<Class<?>>... classFilters) {

        Set<Class<?>> allSuperClasses = new LinkedHashSet<>();

        Class<?> superClass = type.getSuperclass();
        while (superClass != null) {
            // add current super class
            allSuperClasses.add(superClass);
            superClass = superClass.getSuperclass();
        }

        return Collections.unmodifiableSet(Streams.filterAll(allSuperClasses, classFilters));
    }

    /**
     * Get all interfaces from the specified type
     *
     * @param type             the specified type
     * @param interfaceFilters the filters for interfaces
     * @return non-null read-only {@link Set}
     * @since 2.7.6
     */
    public static Set<Class<?>> getAllInterfaces(Class<?> type, Predicate<Class<?>>... interfaceFilters) {
        if (type == null || type.isPrimitive()) {
            return Collections.emptySet();
        }

        Set<Class<?>> allInterfaces = new LinkedHashSet<>();
        Set<Class<?>> resolved = new LinkedHashSet<>();
        Queue<Class<?>> waitResolve = new LinkedList<>();

        resolved.add(type);
        Class<?> clazz = type;
        while (clazz != null) {

            Class<?>[] interfaces = clazz.getInterfaces();

            if (ArrayUtils.isNotEmpty(interfaces)) {
                // add current interfaces
                Arrays.stream(interfaces).filter(resolved::add).forEach(cls -> {
                    allInterfaces.add(cls);
                    waitResolve.add(cls);
                });
            }

            // add all super classes to waitResolve
            getAllSuperClasses(clazz).stream().filter(resolved::add).forEach(waitResolve::add);

            clazz = waitResolve.poll();
        }

        return Streams.filterAll(allInterfaces, interfaceFilters);
    }

    /**
     * Get all declared {@link Method methods} of the declared class, excluding the inherited methods
     *
     * @param declaringClass  the declared class
     * @param methodsToFilter (optional) the methods to be filtered
     * @return non-null read-only {@link List}
     * @see #getMethods(Class, boolean, boolean, Predicate[])
     * @since 2.7.6
     */
    static List<Method> getDeclaredMethods(Class<?> declaringClass, Predicate<Method>... methodsToFilter) {
        return getMethods(declaringClass, false, false, methodsToFilter);
    }

    /**
     * Get all public {@link Method methods} of the declared class, including the inherited methods.
     *
     * @param declaringClass  the declared class
     * @param methodsToFilter (optional) the methods to be filtered
     * @return non-null read-only {@link List}
     * @see #getMethods(Class, boolean, boolean, Predicate[])
     * @since 2.7.6
     */
    static List<Method> getMethods(Class<?> declaringClass, Predicate<Method>... methodsToFilter) {
        return getMethods(declaringClass, false, true, methodsToFilter);
    }

    /**
     * Get all declared {@link Method methods} of the declared class, including the inherited methods.
     *
     * @param declaringClass  the declared class
     * @param methodsToFilter (optional) the methods to be filtered
     * @return non-null read-only {@link List}
     * @see #getMethods(Class, boolean, boolean, Predicate[])
     * @since 2.7.6
     */
    static List<Method> getAllDeclaredMethods(Class<?> declaringClass, Predicate<Method>... methodsToFilter) {
        return getMethods(declaringClass, true, false, methodsToFilter);
    }

    /**
     * Get all public {@link Method methods} of the declared class, including the inherited methods.
     *
     * @param declaringClass  the declared class
     * @param methodsToFilter (optional) the methods to be filtered
     * @return non-null read-only {@link List}
     * @see #getMethods(Class, boolean, boolean, Predicate[])
     * @since 2.7.6
     */
    static List<Method> getAllMethods(Class<?> declaringClass, Predicate<Method>... methodsToFilter) {
        return getMethods(declaringClass, true, true, methodsToFilter);
    }

    //    static List<Method> getOverriderMethods(Class<?> implementationClass, Class<?>... superTypes) {

    //

    //    }

    /**
     * Find the {@link Method} by the the specified type and method name without the parameter types
     *
     * @param type       the target type
     * @param methodName the specified method name
     * @return if not found, return <code>null</code>
     * @since 2.7.6
     */
    static Method findMethod(Class type, String methodName) {
        return findMethod(type, methodName, new Class[0]);
    }

    /**
     * Find the {@link Method} by the the specified type, method name and parameter types
     *
     * @param type           the target type
     * @param methodName     the method name
     * @param parameterTypes the parameter types
     * @return if not found, return <code>null</code>
     * @since 2.7.6
     */
    static Method findMethod(Class<?> type, String methodName, Class<?>... parameterTypes) {
        try {
            if (type != null && StringUtils.isNotEmpty(methodName)) {
                return type.getDeclaredMethod(methodName, parameterTypes);
            }
        } catch (NoSuchMethodException ignore) {
        }
        return null;
    }

    /**
     * Invoke the target object and method
     *
     * @param object           the target object
     * @param methodName       the method name
     * @param methodParameters the method parameters
     * @param <T>              the return type
     * @return the target method's execution result
     * @since 2.7.6
     */
    static <T> T invokeMethod(Object object, String methodName, Object... methodParameters) {
        Class type = object.getClass();
        Class[] parameterTypes = resolveTypes(methodParameters);
        Method method = findMethod(type, methodName, parameterTypes);
        T value;

        if (method == null) {
            throw new IllegalStateException(
                    String.format("cannot find method %s,class: %s", methodName, type.getName()));
        }

        try {
            final boolean isAccessible = method.isAccessible();

            if (!isAccessible) {
                method.setAccessible(true);
            }
            value = (T) method.invoke(object, methodParameters);
            method.setAccessible(isAccessible);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }

        return value;
    }

    /**
     * Resolve the types of the specified values
     *
     * @param values the values
     * @return If can't be resolved, return  empty class array
     * @since 2.7.6
     */
    public static Class[] resolveTypes(Object... values) {

        if (ArrayUtils.isEmpty(values)) {
            return new Class[0];
        }

        int size = values.length;

        Class[] types = new Class[size];

        for (int i = 0; i < size; i++) {
            Object value = values[i];
            types[i] = value == null ? null : value.getClass();
        }

        return types;
    }

    /**
     * Extract fieldName from set/get/is method. if it's not a set/get/is method, return empty string.
     * If method equals get/is/getClass/getObject, also return empty string.
     *
     * @param method method
     * @return fieldName
     */
    static String extractFieldName(Method method) {
        List<String> emptyFieldMethod = Arrays.asList("is", "get", "getObject", "getClass");
        String methodName = method.getName();
        String fieldName = "";

        if (emptyFieldMethod.contains(methodName)) {
            return fieldName;
        } else if (methodName.startsWith("get")) {
            fieldName = methodName.substring("get".length());
        } else if (methodName.startsWith("set")) {
            fieldName = methodName.substring("set".length());
        } else if (methodName.startsWith("is")) {
            fieldName = methodName.substring("is".length());
        } else {
            return fieldName;
        }

        if (StringUtils.isNotEmpty(fieldName)) {
            fieldName = fieldName.substring(0, 1).toLowerCase() + fieldName.substring(1);
        }

        return fieldName;
    }

    /**
     * Invoke and return double value.
     *
     * @param method    method
     * @param targetObj the object the method is invoked from
     * @return double value
     */
    static double invokeAndReturnDouble(Method method, Object targetObj) {
        try {
            return method != null ? (double) method.invoke(targetObj) : Double.NaN;
        } catch (Exception e) {
            return Double.NaN;
        }
    }

    /**
     * Invoke and return long value.
     *
     * @param method    method
     * @param targetObj the object the method is invoked from
     * @return long value
     */
    static long invokeAndReturnLong(Method method, Object targetObj) {
        try {
            return method != null ? (long) method.invoke(targetObj) : -1;
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * 获取方法签名
     * @param method
     * @return
     */
    public static String getSignature(Method method) {
        StringBuilder sb = new StringBuilder();
        Class<?> returnType = method.getReturnType();
        if (returnType != null) {
            sb.append(returnType.getName()).append('#');
        }
        sb.append(method.getName());
        Class<?>[] parameters = method.getParameterTypes();
        for (int i = 0; i < parameters.length; i++) {
            if (i == 0) {
                sb.append(':');
            } else {
                sb.append(',');
            }
            sb.append(parameters[i].getName());
        }
        return sb.toString();
    }

    public interface Streams {

        static <T, S extends Iterable<T>> Stream<T> filterStream(S values, Predicate<T> predicate) {
            return stream(values.spliterator(), false).filter(predicate);
        }

        static <T, S extends Iterable<T>> List<T> filterList(S values, Predicate<T> predicate) {
            return filterStream(values, predicate).collect(toList());
        }

        static <T, S extends Iterable<T>> Set<T> filterSet(S values, Predicate<T> predicate) {
            // new Set with insertion order
            return filterStream(values, predicate).collect(LinkedHashSet::new, Set::add, Set::addAll);
        }

        @SuppressWarnings("unchecked")
        static <T, S extends Iterable<T>> S filter(S values, Predicate<T> predicate) {
            final boolean isSet = Set.class.isAssignableFrom(values.getClass());
            return (S) (isSet ? filterSet(values, predicate) : filterList(values, predicate));
        }

        static <T, S extends Iterable<T>> S filterAll(S values, Predicate<T>... predicates) {
            return filter(values, Predicates.and(predicates));
        }

        static <T, S extends Iterable<T>> S filterAny(S values, Predicate<T>... predicates) {
            return filter(values, Predicates.or(predicates));
        }

        static <T> T filterFirst(Iterable<T> values, Predicate<T>... predicates) {
            return stream(values.spliterator(), false)
                    .filter(Predicates.and(predicates))
                    .findFirst()
                    .orElse(null);
        }
    }

    public interface Predicates {

        Predicate[] EMPTY_ARRAY = new Predicate[0];

        /**
         * {@link Predicate} always return <code>true</code>
         *
         * @param <T> the type to test
         * @return <code>true</code>
         */
        static <T> Predicate<T> alwaysTrue() {
            return e -> true;
        }

        /**
         * {@link Predicate} always return <code>false</code>
         *
         * @param <T> the type to test
         * @return <code>false</code>
         */
        static <T> Predicate<T> alwaysFalse() {
            return e -> false;
        }

        /**
         * a composed predicate that represents a short-circuiting logical AND of {@link Predicate predicates}
         *
         * @param predicates {@link Predicate predicates}
         * @param <T>        the type to test
         * @return non-null
         */
        static <T> Predicate<T> and(Predicate<T>... predicates) {
            return Stream.of(predicates).reduce(Predicate::and).orElseGet(Predicates::alwaysTrue);
        }

        /**
         * a composed predicate that represents a short-circuiting logical OR of {@link Predicate predicates}
         *
         * @param predicates {@link Predicate predicates}
         * @param <T>        the detected type
         * @return non-null
         */
        static <T> Predicate<T> or(Predicate<T>... predicates) {
            return Stream.of(predicates).reduce(Predicate::or).orElse(e -> true);
        }
    }
}
