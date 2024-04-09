package com.hxx.sbcommon.common.io.console;

import java.util.Scanner;
import java.util.function.Consumer;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2024-02-18 15:38:11
 **/
public class ConsoleUtil {

    /**
     * ConsoleTools.Read(line->{
     * MyPrint.ln(">"+line);
     * });
     *
     * @param c
     */
    public static void Read(Consumer<String> c) {
        Scanner scan = new Scanner(System.in);
        while (scan.hasNext()) {
            c.accept(scan.next());
        }
    }
}
