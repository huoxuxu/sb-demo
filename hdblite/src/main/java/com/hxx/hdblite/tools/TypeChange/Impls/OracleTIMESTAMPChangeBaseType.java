//package com.hxx.hdblite.tools.TypeChange.Impls.E;
//
//import com.hxx.hdblite.tools.TypeChange.IChangeBaseType;
//import com.hxx.hdblite.tools.TypeChange.Impls.D.TimestampChangeBaseType;
//import oracle.sql.TIMESTAMP;
//
//import java.sql.SQLException;
//import java.sql.Timestamp;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//
//public class OracleTIMESTAMPChangeBaseType implements IChangeBaseType {
//    private TIMESTAMP val;
//
//    /// <summary>
//    ///
//    /// </summary>
//    /// <param name="val"></param>
//    public OracleTIMESTAMPChangeBaseType(TIMESTAMP val) {
//        this.val = val;
//    }
//
//    /// <summary>
//    /// 忽略大小写的true 为true
//    /// </summary>
//    /// <returns></returns>
//    public boolean ToBoolean() {
//        return false;
//    }
//
//    /// <summary>
//    ///
//    /// </summary>
//    /// <returns></returns>
//    public LocalDateTime ToDateTime() {
//        try {
//            Timestamp timestamp = val.timestampValue();
//            TimestampChangeBaseType timestampChangeBaseType = new TimestampChangeBaseType(timestamp);
//            return timestampChangeBaseType.ToDateTime();
//        } catch (SQLException e) {
//            return LocalDateTime.MIN;
//        }
//    }
//
//    /// <summary>
//    ///
//    /// </summary>
//    /// <returns></returns>
//    public double ToDouble() {
//        return 0;
//    }
//
//    /// <summary>
//    ///
//    /// </summary>
//    /// <returns></returns>
//    public int ToInt32() {
//        return 0;
//    }
//
//    /// <summary>
//    ///
//    /// </summary>
//    /// <returns></returns>
//    public long ToInt64() {
//        return 0;
//    }
//
//    /// <summary>
//    /// yyyy-MM-dd HH:mm:ss
//    /// </summary>
//    /// <returns></returns>
//    public String ToStr() {
//        LocalDateTime localDateTime = ToDateTime();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        String formattedDateTime = localDateTime.format(formatter); // "1986-04-08 12:30:00"
//        return formattedDateTime;
//    }
//}
