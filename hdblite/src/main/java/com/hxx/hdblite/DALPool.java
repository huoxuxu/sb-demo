package com.hxx.hdblite;

import com.hxx.hdblite.Models.HConnInfo;
import com.hxx.hdblite.Pools.HikariCPUtil;
import com.hxx.hdblite.tools.PropertiesHelper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 数据库连接信息类
 */
public class DALPool implements AutoCloseable {

    //数据库连接名，对应数据库连接信息
    private static Map<String, HConnInfo> ConnDic = new HashMap<String, HConnInfo>();

    private UUID GuidID;//当前连接标识

    private String ConnCfgName;//当前连接配置名
    private HConnInfo ConnInfo;//数据库连接信息

    //数据库连接
    private Connection Conn;
    //数据库类型
    private HXXDBType DBType;

    private boolean OpenTrans;

    /**
     * 注册并创建连接
     *
     * @param connInfo 数据库连接信息
     * @throws Exception
     */
    private DALPool(HConnInfo connInfo) throws Exception {
        this.GuidID = UUID.randomUUID();
        this.ConnCfgName = connInfo.getConnCfgName();
        this.ConnInfo = connInfo;

        //注册此连接
        //RegistryByConnInfo(connInfo);

        //this.Conn = createConn(connInfo);//原始的创建链接的方法

        String cfgName=connInfo.getConnCfgName();
        //连接池
        HikariCPUtil conPool=HikariCPUtil.getInstance();
        this.Conn = conPool.getConnection(cfgName);
    }

    //创建数据库连接
    private static Connection createConn(HConnInfo connInfo) throws Exception {
        String cfgName=connInfo.getConnCfgName();
        HXXDBType dbType = connInfo.getDBType();
        //加载 数据库 的驱动类
        String driverName = GetDriveName(dbType);
        Class.forName(driverName);

        Connection connection = null;
        if (dbType == HXXDBType.SQLite) {
            //创建连接
            connection = DriverManager.getConnection(connInfo.getConnStr());
        } else {
            //设置用户名和密码
            String user = connInfo.getUser();
            String pwd = connInfo.getPassword();

            //创建连接
            connection = DriverManager.getConnection(connInfo.getConnStr(), user, pwd);//原始的创建链接的方法
        }

        return connection;
    }

    /**
     * 注册连接
     *
     * @param connInfo
     */
    public static void RegistryByConnInfo(HConnInfo connInfo) {
        String ConnCfgName = connInfo.getConnCfgName();

        HikariCPUtil conPool=HikariCPUtil.getInstance();
        conPool.add(connInfo);

        if (!DALPool.ConnDic.containsKey(ConnCfgName)) {
            DALPool.ConnDic.put(ConnCfgName, connInfo);
        }
    }

    /**
     * 须先注册连接配置名
     *
     * @param ConnCfgName
     * @return
     * @throws Exception
     */
    public static DALPool Create(String ConnCfgName) throws Exception {
        if (!DALPool.ConnDic.containsKey(ConnCfgName)) {
            throw new Exception("请先注册配置名：" + ConnCfgName + " !");
        }

        HConnInfo hci = DALPool.ConnDic.get(ConnCfgName);
        return new DALPool(hci);
    }

    /**
     *
     * @throws SQLException
     */
    public void close() throws SQLException {
            if (this.Conn != null) {
                //System.out.println("dal Closed!");
                this.Conn.close();
            }
    }

    //Static----------------------------------------------------------------------

    /**
     *
     * @return
     */
    public static Map<String, HConnInfo> GetConnDic() {
        return DALPool.ConnDic;
    }

    /**
     * 获取指定连接配置名对应的连接信息，
     * 未注册，返回null
     *
     * @param ConnCfgName
     * @return
     */
    public static HConnInfo GetConnInfo(String ConnCfgName) {
        if (ConnDic.containsKey(ConnCfgName)) {
            return ConnDic.get(ConnCfgName);
        }

        return null;
    }

    /**
     * properties文件必须包含：jdbc.conncfgname,jdbc.url,jdbc.username,jdbc.password
     * 多个数据库连接信息，请指定多个资源文件，多次调用此方法即可
     *
     * @param propertiesFileName jdbc资源文件名，带后缀
     */
    public static void RegistryByDbCfg(String propertiesFileName) throws Exception {
        PropertiesHelper pt = new PropertiesHelper(propertiesFileName);

        String conncfgname = pt.Get("jdbc.conncfgname");
        String connStr = pt.Get("jdbc.url");
        String userName = pt.Get("jdbc.username");
        String userPwd = pt.Get("jdbc.password");

        HConnInfo hConnInfo = new HConnInfo(conncfgname, connStr, HXXDBType.MySQL);
        hConnInfo.setUser(userName);
        hConnInfo.setPassword(userPwd);

        DALPool.RegistryByConnInfo(hConnInfo);
    }


    //辅助方法

    /**
     * 传入HXXDBType返回对应的驱动名
     *
     * @param dbtype 自定义数据库枚举
     * @return
     */
    public static String GetDriveName(HXXDBType dbtype) {
        if (dbtype == HXXDBType.MySQL) {
            //com.mysql.jdbc.Driver
            //jdbc:mysql://127.0.0.1:3306/hxxtestdb?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT
            return "com.mysql.cj.jdbc.Driver";
        }
        if (dbtype == HXXDBType.Oracle) {
            //jdbc:oracle:thin:@//127.0.0.1:1521/orcl
            return "oracle.jdbc.driver.OracleDriver";
        }
        if (dbtype == HXXDBType.SQLServer) {
            //jdbc:sqlserver://localhost:1433;databaseName=AdventureWorks
            //jdbc:sqlserver://localhost:1433; DatabaseName=sample
            return "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        }
        if (dbtype == HXXDBType.SQLite) {
            //jdbc:sqlite:sample.db
            return "org.sqlite.JDBC";
        }
//        if (dbtype==HXXDBType.Hive){
//            //jdbc:hive2://192.168.92.215:10000
//            return "org.apache.hive.jdbc.HiveDriver";
//        }

        return "";
    }

    //get set--------------------------------------------------
    public HXXDBType getDBType() {
        HConnInfo connInfo = getConnInfo();
        DBType = connInfo.getDBType();
        return DBType;
    }

    public HConnInfo getConnInfo() {
        return ConnInfo;
    }

    public String getConnCfgName() {
        return ConnCfgName;
    }

    public Connection getConn() {
        return Conn;
    }

    public boolean isOpenTrans() {
        return OpenTrans;
    }

    public void setOpenTrans(boolean openTrans) {
        OpenTrans = openTrans;
    }
}
