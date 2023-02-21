package com.hxx.sbcommon.common.io.xml;

import com.hxx.sbcommon.model.XStreamItem;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.collections.CollectionConverter;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.mapper.ClassAliasingMapper;
import com.thoughtworks.xstream.security.NoTypePermission;
import com.thoughtworks.xstream.security.NullPermission;
import com.thoughtworks.xstream.security.PrimitiveTypePermission;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-05-10 17:00:40
 **/
public class XmlUtil {
    private static final Logger logger = LoggerFactory.getLogger(XmlUtil.class);
    private static Map<String, XStream> xstreams = new ConcurrentHashMap();
    private static Map<Object, Set<String>> allowedTypes = new ConcurrentHashMap();
    private static Set<String> defaultPermissionSets = new HashSet(Arrays.asList("java.lang.*", "java.util.*", "java.util.concurrent.*", "com.zto.**"));

    public XmlUtil() {
    }

    private static XStream getXStream(Class clazz) {
        if (!xstreams.containsKey(clazz.toString())) {
            XStream xstream = new XStream(new DomDriver());
            xstream.ignoreUnknownElements();
            xstream.processAnnotations(clazz);
            XStreamItem itemAnnotation = (XStreamItem)clazz.getAnnotation(XStreamItem.class);
            if (itemAnnotation != null) {
                ClassAliasingMapper mapper = new ClassAliasingMapper(xstream.getMapper());
                mapper.addClassAlias(itemAnnotation.item(), String.class);
                xstream.registerLocalConverter(itemAnnotation.clazz(), itemAnnotation.list(), new CollectionConverter(mapper));
            }

            xstream.addPermission(NoTypePermission.NONE);
            xstream.addPermission(NullPermission.NULL);
            xstream.addPermission(PrimitiveTypePermission.PRIMITIVES);
            String[] defaultPermissions = new String[defaultPermissionSets.size()];
            defaultPermissionSets.toArray(defaultPermissions);
            xstream.allowTypesByWildcard(defaultPermissions);
            allowedTypes.put(xstream, defaultPermissionSets);
            xstreams.put(clazz.toString(), xstream);
        }

        return (XStream)xstreams.get(clazz.toString());
    }

    public static String toXml(Object obj) {
        XStream xstream = getXStream(obj.getClass());
        return xstream.toXML(obj);
    }

    public static <T> T toBean(String xmlStr, Class<T> cls) {
        XStream xstream = getXStream(cls);
        Object obj = xstream.fromXML(xmlStr);
        return (T)obj;
    }

    public static <T> T toBeanNotThrowException(String xmlStr, Class<T> cls) {
        XStream xstream = getXStream(cls);
        Object obj = null;

        try {
            obj = xstream.fromXML(xmlStr);
        } catch (Exception var5) {
        }

        return (T)obj;
    }

    public static <T> T toBean(String xmlStr, Class<T> cls, String... additionalPermissionStrs) {
        processCustomerDefinedPermissions(xmlStr, cls, additionalPermissionStrs);
        return toBean(xmlStr, cls);
    }

    public static <T> T toBeanNotThrowException(String xmlStr, Class<T> cls, String... additionalPermissionStrs) {
        processCustomerDefinedPermissions(xmlStr, cls, additionalPermissionStrs);
        return toBean(xmlStr, cls);
    }

    private static <T> void processCustomerDefinedPermissions(String xmlStr, Class<T> cls, String... additionalPermissionStrs) {
        XStream xstream = getXStream(cls);
        Set<String> newPermissionStrs = new HashSet();
        String[] permissions = additionalPermissionStrs;
        int var6 = additionalPermissionStrs.length;

        for(int var7 = 0; var7 < var6; ++var7) {
            String permissionStr = permissions[var7];
            if (((Set)allowedTypes.get(xstream)).add(permissionStr)) {
                logger.info("Add Customer Permission for class[{}] of [{}]!", cls.getName(), permissionStr);
                newPermissionStrs.add(permissionStr);
            }
        }

        if (!newPermissionStrs.isEmpty()) {
            permissions = new String[newPermissionStrs.size()];
            newPermissionStrs.toArray(permissions);
            xstream.allowTypesByWildcard(permissions);
        }

    }
}

