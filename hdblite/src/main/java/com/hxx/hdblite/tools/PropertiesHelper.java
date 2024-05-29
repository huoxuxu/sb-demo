package com.hxx.hdblite.tools;

import java.io.InputStream;
import java.util.Properties;

public class PropertiesHelper {

    private String Name;//资源文件名带后缀
    private Properties properties;

    /**
     *
     * @param name 资源文件名，带后缀
     * @throws Exception
     */
    public PropertiesHelper(String name) throws Exception {
        this.Name = name;

        InputStream in = this.getClass().getClassLoader().getResourceAsStream(this.Name);
        properties = new Properties();
        properties.load(in);
    }

    /**
     *
     * @param name 资源文件名，带后缀
     * @param in 资源文件流
     * @throws Exception
     */
    public PropertiesHelper(String name,InputStream in) throws Exception {
        this.Name = name;
        properties = new Properties();
        properties.load(in);
    }

    /**
     * @param propName
     * @return
     */
    public String Get(String propName) {
        return properties.getProperty(propName);
    }

    //get&set
    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
