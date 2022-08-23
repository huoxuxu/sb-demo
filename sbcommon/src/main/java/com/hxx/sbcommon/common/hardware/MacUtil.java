package com.hxx.sbcommon.common.hardware;

import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-08-30 16:16:44
 **/
public class MacUtil {
    public static final String EMPTYMAC = "00-00-00-00-00-00";

    public static String getMac() throws Exception {
        java.util.Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();

        while (en.hasMoreElements()) {
            NetworkInterface iface = en.nextElement();
            List<InterfaceAddress> addrs = iface.getInterfaceAddresses();
            for (InterfaceAddress addr : addrs) {
                InetAddress ip = addr.getAddress();
                NetworkInterface network = NetworkInterface.getByInetAddress(ip);
                if (network == null) {
                    continue;
                }

                byte[] mac = network.getHardwareAddress();
                if (mac == null || mac.length == 0) {
                    continue;
                }

                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < mac.length; i++) {
                    sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
                }

                String macStr = sb.toString();
                if (!EMPTYMAC.equalsIgnoreCase(macStr)) {
                    return macStr;
                }
            }
        }

        return EMPTYMAC;
    }

    /**
     * 因为一台机器不一定只有一个网卡呀，所以返回的是数组是很合理的
     *
     * @return
     * @throws Exception
     */
    public static List<String> getMacList() throws Exception {
        java.util.Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
        StringBuilder sb = new StringBuilder();
        Set<String> tmpMacList = new HashSet<>();

        while (en.hasMoreElements()) {
            NetworkInterface iface = en.nextElement();
            List<InterfaceAddress> addrs = iface.getInterfaceAddresses();
            for (InterfaceAddress addr : addrs) {
                InetAddress ip = addr.getAddress();
                NetworkInterface network = NetworkInterface.getByInetAddress(ip);
                if (network == null) {
                    continue;
                }

                byte[] mac = network.getHardwareAddress();
                if (mac == null || mac.length == 0) {
                    continue;
                }

                sb.delete(0, sb.length());
                for (int i = 0; i < mac.length; i++) {
                    sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
                }
                tmpMacList.add(sb.toString());
            }
        }
        if (tmpMacList.size() <= 0) {
            return new ArrayList<>();
        }
        /***去重，别忘了同一个网卡的ipv4,ipv6得到的mac都是一样的，肯定有重复，下面这段代码是。。流式处理***/
        List<String> unique = new ArrayList<>(tmpMacList);
        return unique;
    }


}
