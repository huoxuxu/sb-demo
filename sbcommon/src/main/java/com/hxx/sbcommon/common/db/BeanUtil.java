package com.hxx.sbcommon.common.db;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

public class BeanUtil {

    /**
     * Special array value used by {@code mapColumnsToProperties} that
     * indicates there is no bean property that matches a column from a
     * {@code ResultSet}.
     */
    protected static final int PROPERTY_NOT_FOUND = -1;
    /**
     * Set a bean's primitive properties to these defaults when SQL NULL
     * is returned.  These are the same as the defaults that ResultSet get*
     * methods return in the event of a NULL column.
     */
    private static final Map<Class<?>, Object> PRIMITIVE_DEFAULTS = new HashMap<>();

    /**
     * ResultSet column to bean property name overrides.
     */
    private final Map<String, String> columnToPropertyOverrides = new HashMap<>();


    static {
        PRIMITIVE_DEFAULTS.put(Integer.TYPE, Integer.valueOf(0));
        PRIMITIVE_DEFAULTS.put(Short.TYPE, Short.valueOf((short) 0));
        PRIMITIVE_DEFAULTS.put(Byte.TYPE, Byte.valueOf((byte) 0));
        PRIMITIVE_DEFAULTS.put(Float.TYPE, Float.valueOf(0f));
        PRIMITIVE_DEFAULTS.put(Double.TYPE, Double.valueOf(0d));
        PRIMITIVE_DEFAULTS.put(Long.TYPE, Long.valueOf(0L));
        PRIMITIVE_DEFAULTS.put(Boolean.TYPE, Boolean.FALSE);
        PRIMITIVE_DEFAULTS.put(Character.TYPE, Character.valueOf((char) 0));
    }

    /**
     * Initializes the fields of the provided bean from the ResultSet.
     *
     * @param <T>       The type of bean
     * @param resultSet The result set.
     * @param bean      The bean to be populated.
     * @return An initialized object.
     * @throws SQLException if a database error occurs.
     */
    public <T> T populateBean(final ResultSet resultSet, final T bean) throws SQLException {
        final PropertyDescriptor[] props = this.propertyDescriptors(bean.getClass());
        final ResultSetMetaData rsmd = resultSet.getMetaData();
        final int[] columnToProperty = this.mapColumnsToProperties(rsmd, props);

        return populateBean(resultSet, bean, props, columnToProperty);
    }

    /**
     * This method populates a bean from the ResultSet based upon the underlying meta-data.
     *
     * @param <T>              The type of bean
     * @param resultSet        The result set.
     * @param bean             The bean to be populated.
     * @param props            The property descriptors.
     * @param columnToProperty The column indices in the result set.
     * @return An initialized object.
     * @throws SQLException if a database error occurs.
     */
    private <T> T populateBean(final ResultSet resultSet, final T bean,
                               final PropertyDescriptor[] props, final int[] columnToProperty)
            throws SQLException {

        for (int i = 1; i < columnToProperty.length; i++) {

            if (columnToProperty[i] == PROPERTY_NOT_FOUND) {
                continue;
            }

            final PropertyDescriptor prop = props[columnToProperty[i]];
            final Class<?> propType = prop.getPropertyType();

            Object value = null;
            if (propType != null) {
                value = this.processColumn(resultSet, i, propType);

                if (value == null && propType.isPrimitive()) {
                    value = PRIMITIVE_DEFAULTS.get(propType);
                }
            }

            this.callSetter(bean, prop, value);
        }

        return bean;
    }

    private <T> T populateBean(final Map<String, Object> resultSet, final T bean,
                               final PropertyDescriptor[] props, final int[] columnToProperty)
            throws SQLException {

        List<String> keys = new ArrayList<>(resultSet.keySet());
        for (int i = 1; i < columnToProperty.length; i++) {

            if (columnToProperty[i] == PROPERTY_NOT_FOUND) {
                continue;
            }

            final PropertyDescriptor prop = props[columnToProperty[i]];
            final Class<?> propType = prop.getPropertyType();

            Object value = null;
            if (propType != null) {
                value = this.processColumn(resultSet, keys.get(i), propType);

                if (value == null && propType.isPrimitive()) {
                    value = PRIMITIVE_DEFAULTS.get(propType);
                }
            }

            this.callSetter(bean, prop, value);
        }

        return bean;
    }

    /**
     * Convert a {@code ResultSet} column into an object.  Simple
     * implementations could just call {@code rs.getObject(index)} while
     * more complex implementations could perform type manipulation to match
     * the column's type to the bean property type.
     *
     * <p>
     * This implementation calls the appropriate {@code ResultSet} getter
     * method for the given property type to perform the type conversion.  If
     * the property type doesn't match one of the supported
     * {@code ResultSet} types, {@code getObject} is called.
     * </p>
     *
     * @param resultSet The {@code ResultSet} currently being processed.  It is
     *                  positioned on a valid row before being passed into this method.
     * @param index     The current column index being processed.
     * @param propType  The bean property type that this column needs to be
     *                  converted into.
     * @return The object from the {@code ResultSet} at the given column
     * index after optional type processing or {@code null} if the column
     * value was SQL NULL.
     * @throws SQLException if a database access error occurs
     */
    protected Object processColumn(final ResultSet resultSet, final int index, final Class<?> propType)
            throws SQLException {

        Object retval = resultSet.getObject(index);

        if (retval == null && !propType.isPrimitive()) {
            return null;
        }

//        for (final ColumnHandler<?> handler : COLUMN_HANDLERS) {
//            if (handler.match(propType)) {
//                retval = handler.apply(resultSet, index);
//                break;
//            }
//        }

        return retval;

    }

    protected Object processColumn(final Map<String, Object> resultSet, final String key, final Class<?> propType)
            throws SQLException {

        Object retval = resultSet.get(key);

        if (retval == null && !propType.isPrimitive()) {
            return null;
        }

//        for (final ColumnHandler<?> handler : COLUMN_HANDLERS) {
//            if (handler.match(propType)) {
//                retval = handler.apply(resultSet, index);
//                break;
//            }
//        }

        return retval;
    }

    /**
     * Get the write method to use when setting {@code value} to the {@code target}.
     *
     * @param target Object where the write method will be called.
     * @param prop   BeanUtils information.
     * @param value  The value that will be passed to the write method.
     * @return The {@link java.lang.reflect.Method} to call on {@code target} to write {@code value} or {@code null} if
     * there is no suitable write method.
     */
    protected Method getWriteMethod(final Object target, final PropertyDescriptor prop, final Object value) {
        return prop.getWriteMethod();
    }

    /**
     * The positions in the returned array represent column numbers.  The
     * values stored at each position represent the index in the
     * {@code PropertyDescriptor[]} for the bean property that matches
     * the column name.  If no bean property was found for a column, the
     * position is set to {@code PROPERTY_NOT_FOUND}.
     *
     * @param rsmd  The {@code ResultSetMetaData} containing column
     *              information.
     * @param props The bean property descriptors.
     * @return An int[] with column index to property index mappings.  The 0th
     * element is meaningless because JDBC column indexing starts at 1.
     * @throws SQLException if a database access error occurs
     */
    protected int[] mapColumnsToProperties(final ResultSetMetaData rsmd,
                                           final PropertyDescriptor[] props) throws SQLException {

        final int colCounts = rsmd.getColumnCount();
        final int[] columnToProperty = new int[colCounts + 1];
        Arrays.fill(columnToProperty, PROPERTY_NOT_FOUND);

        for (int colIndex = 1; colIndex <= colCounts; colIndex++) {
            String columnName = rsmd.getColumnLabel(colIndex);
            if (null == columnName || columnName.isEmpty()) {
                columnName = rsmd.getColumnName(colIndex);
            }

            String propertyName = columnToPropertyOverrides.get(columnName);
            if (propertyName == null) {
                propertyName = columnName;
            }

            if (propertyName == null) {
                propertyName = Integer.toString(colIndex);
            }

            for (int i = 0; i < props.length; i++) {
                final PropertyDescriptor prop = props[i];
                final Method reader = prop.getReadMethod();

//                // Check for @Column annotations as explicit marks
//                final Column column;
//                if (reader != null) {
//                    column = reader.getAnnotation(Column.class);
//                } else {
//                    column = null;
//                }

                final String propertyColumnName;
//                if (column != null) {
//                    propertyColumnName = column.name();
//                } else {
                propertyColumnName = prop.getName();
//                }
                if (propertyName.equalsIgnoreCase(propertyColumnName)) {
                    columnToProperty[colIndex] = i;
                    break;
                }
            }
        }

        return columnToProperty;
    }

    protected int[] mapColumnsToProperties(final List<String> keys,
                                           final PropertyDescriptor[] props) throws SQLException {

        final int colCounts = keys.size();
        final int[] columnToProperty = new int[colCounts + 1];
        Arrays.fill(columnToProperty, PROPERTY_NOT_FOUND);

        for (int colIndex = 1; colIndex <= colCounts; colIndex++) {
            String columnName = keys.get(colIndex);

            String propertyName = columnToPropertyOverrides.get(columnName);
            if (propertyName == null) {
                propertyName = columnName;
            }

            if (propertyName == null) {
                propertyName = Integer.toString(colIndex);
            }

            for (int i = 0; i < props.length; i++) {
                final PropertyDescriptor prop = props[i];
                final Method reader = prop.getReadMethod();

//                // Check for @Column annotations as explicit marks
//                final Column column;
//                if (reader != null) {
//                    column = reader.getAnnotation(Column.class);
//                } else {
//                    column = null;
//                }

                final String propertyColumnName;
//                if (column != null) {
//                    propertyColumnName = column.name();
//                } else {
                propertyColumnName = prop.getName();
//                }
                if (propertyName.equalsIgnoreCase(propertyColumnName)) {
                    columnToProperty[colIndex] = i;
                    break;
                }
            }
        }

        return columnToProperty;
    }

    /**
     * Returns a PropertyDescriptor[] for the given Class.
     *
     * @param c The Class to retrieve PropertyDescriptors for.
     * @return A PropertyDescriptor[] describing the Class.
     * @throws SQLException if introspection failed.
     */
    private PropertyDescriptor[] propertyDescriptors(final Class<?> c) throws SQLException {
        // Introspector caches BeanInfo classes for better performance
        BeanInfo beanInfo;
        try {
            beanInfo = Introspector.getBeanInfo(c);
            return beanInfo.getPropertyDescriptors();
        } catch (final IntrospectionException e) {
            throw new SQLException("Bean introspection failed: " + e.getMessage());
        }
    }

    /**
     * Calls the setter method on the target object for the given property.
     * If no setter method exists for the property, this method does nothing.
     *
     * @param target The object to set the property on.
     * @param prop   The property to set.
     * @param value  The value to pass into the setter.
     * @throws SQLException if an error occurs setting the property.
     */
    private void callSetter(final Object target, final PropertyDescriptor prop, Object value)
            throws SQLException {

        final Method setter = getWriteMethod(target, prop, value);
        if (setter == null || setter.getParameterTypes().length != 1) {
            return;
        }

        try {
            final Class<?> firstParam = setter.getParameterTypes()[0];
//            for (final PropertyHandler handler : PROPERTY_HANDLERS) {
//                if (handler.match(firstParam, value)) {
//                    value = handler.apply(firstParam, value);
//                    break;
//                }
//            }

            // Don't call setter if the value object isn't the right type
            if (!this.isCompatibleType(value, firstParam)) {
                throw new SQLException("Cannot set " + prop.getName() + ": incompatible types, cannot convert " + value.getClass().getName() + " to " + firstParam.getName());
                // value cannot be null here because isCompatibleType allows null
            }
            setter.invoke(target, value);

        } catch (final IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
            throw new SQLException("Cannot set " + prop.getName() + ": " + e.getMessage());
        }
    }

    /**
     * ResultSet.getObject() returns an Integer object for an INT column.  The
     * setter method for the property might take an Integer or a primitive int.
     * This method returns true if the value can be successfully passed into
     * the setter method.  Remember, Method.invoke() handles the unwrapping
     * of Integer into an int.
     *
     * @param value The value to be passed into the setter method.
     * @param type  The setter's parameter type (non-null)
     * @return boolean True if the value is compatible (null => true)
     */
    private boolean isCompatibleType(final Object value, final Class<?> type) {
        // Do object check first, then primitives
        return value == null || type.isInstance(value) || matchesPrimitive(type, value.getClass());
    }


    /**
     * Check whether a value is of the same primitive type as {@code targetType}.
     *
     * @param targetType The primitive type to target.
     * @param valueType  The value to match to the primitive type.
     * @return Whether {@code valueType} can be coerced (e.g. autoboxed) into {@code targetType}.
     */
    private boolean matchesPrimitive(final Class<?> targetType, final Class<?> valueType) {
        if (!targetType.isPrimitive()) {
            return false;
        }

        try {
            // see if there is a "TYPE" field.  This is present for primitive wrappers.
            final Field typeField = valueType.getField("TYPE");
            final Object primitiveValueType = typeField.get(valueType);

            if (targetType == primitiveValueType) {
                return true;
            }
        } catch (final NoSuchFieldException | IllegalAccessException ignored) {
            // an inaccessible TYPE field is a good sign that we're not working with a primitive wrapper.
            // nothing to do.  we can't match for compatibility
        }
        return false;
    }

}
