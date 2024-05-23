package com.hxx.sbcommon.common.db;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ResultSetUtil {

    public Map<String, Object> toMap(final ResultSet resultSet) throws SQLException {
        final ResultSetMetaData rsmd = resultSet.getMetaData();
        final int cols = rsmd.getColumnCount();
        final Map<String, Object> result = new LinkedHashMap<>(cols);

        for (int i = 1; i <= cols; i++) {
            String propKey = rsmd.getColumnLabel(i);
            if (null == propKey || 0 == propKey.length()) {
                propKey = rsmd.getColumnName(i);
            }
            if (null == propKey || 0 == propKey.length()) {
                // The column index can't be null
                propKey = Integer.toString(i);
            }
            result.put(propKey, resultSet.getObject(i));
        }

        return result;
    }

    public Object[] toArray(final ResultSet resultSet) throws SQLException {
        final ResultSetMetaData meta = resultSet.getMetaData();
        final int cols = meta.getColumnCount();
        final Object[] result = new Object[cols];

        for (int i = 0; i < cols; i++) {
            result[i] = resultSet.getObject(i + 1);
        }

        return result;
    }
}
