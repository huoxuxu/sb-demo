package com.hxx.hdblite.tools.log;

import com.hxx.hdblite.tools.DateTimeTools;

import java.util.Date;

/**
 * 日志
 */
public final class XTrace implements ILog {
    public static String ProjectPath;
    public static String LogPath = "../Log";
    private static Boolean IsConsole = false;

    static {
//        ProjectPath = FileTools.GetJarRunDir();
    }

    /**
     * 显示在控制台
     */
    public static void UseConsole() {
        IsConsole = true;
    }

    public void WriteLog(String msg) {
        WriteLine(msg);
    }

    /**
     * @param msg
     * @throws Exception
     */
    public static void WriteLine(String msg) {
        //msg = TextFileLog.GetMsg(msg, args);
        msg = getMsg(msg);

        if (IsConsole) {
            System.out.println(msg);
        }

        Date now = new Date();
        String nowStr = DateTimeTools.ToStr(now, "yyyy-MM-dd");
        String filePath = String.format("%s/%s/%s.log", ProjectPath, LogPath, nowStr);// ProjectPath + "/" + LogPath + "/" +  + ".log";
        WriteTxt(filePath, msg);
    }

    /**
     * @param ex
     * @throws Exception
     */
    public static void WriteException(Exception ex) {
        String msg = ex + "";
        //写到error.txt中
        {
            String logFullPath = String.format("%s/%s/error.txt", ProjectPath, LogPath);// lp + "/error.txt";
            String msg1 = getMsg(msg);
            WriteTxt(logFullPath, msg1);
        }

        WriteLine(msg);
    }

    //辅助方法===============================
    private static String getMsg(String msg) {
        Date now = new Date();
        String nowStr = DateTimeTools.ToStr(now, "HH:mm:ss");
        long tid = Thread.currentThread().getId();

        return String.format("%s %s %s\n", nowStr, tid, msg);
    }

    //写日志
    private static void WriteTxt(String filePath, String msg) {
//        try {
//            FileTools.MustBeExists(filePath);
//            FileTools.WriteAllTxt(filePath, msg);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

}
