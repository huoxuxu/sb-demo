package com.hxx.hdblite;

import com.hxx.hdblite.Models.HConnInfo;
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
public class DAL implements AutoCloseable {
    public static Boolean UsePool = false;//使用连接池。默认不使用
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
    private DAL(HConnInfo connInfo) throws Exception {
        this.GuidID = UUID.randomUUID();
        this.ConnCfgName = connInfo.getConnCfgName();
        this.ConnInfo = connInfo;

        //注册此连接
        //RegistryByConnInfo(connInfo);

        if (!UsePool) {
            this.Conn = createConn(connInfo);//原始的创建链接的方法
        } else {
            String cfgName = connInfo.getConnCfgName();
            DALPool pool = DALPool.Create(cfgName);
            this.Conn = pool.getConn();
        }
    }

    //创建数据库连接
    private static Connection createConn(HConnInfo connInfo) throws Exception {
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
            connection = DriverManager.getConnection(connInfo.getConnStr(), user, pwd);
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

        if (!DAL.ConnDic.containsKey(ConnCfgName)) {
            DAL.ConnDic.put(ConnCfgName, connInfo);
        }

        if (UsePool) {
            DALPool.RegistryByConnInfo(connInfo);
        }
    }

    /**
     * 须先注册连接配置名
     *
     * @param connCfgName
     * @return
     * @throws Exception
     */
    public static DAL Create(String connCfgName) throws Exception {
        if (!DAL.ConnDic.containsKey(connCfgName)) {
            throw new Exception("请先注册配置名：" + connCfgName + " !");
        }

        HConnInfo hci = DAL.ConnDic.get(connCfgName);
        return new DAL(hci);
    }

    /**
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
     * @return
     */
    public static Map<String, HConnInfo> GetConnDic() {
        return DAL.ConnDic;
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

        DAL.RegistryByConnInfo(hConnInfo);
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
