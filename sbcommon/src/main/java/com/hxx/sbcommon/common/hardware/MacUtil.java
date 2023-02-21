package com.hxx.sbcommon.common.hardware;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-08-30 16:16:44
 **/
@Slf4j
public class MacUtil {
    public static final String EMPTYMAC = "00-00-00-00-00-00";

    /**
     * 获取本地MAC
     *
     * @return
     * @throws Exception
     */
    public static String getLocalMac() throws Exception {
        try {
            java.util.Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();

            while (en.hasMoreElements()) {
                NetworkInterface iface = en.nextElement();
                List<InterfaceAddress> addrs = iface.getInterfaceAddresses();
                for (InterfaceAddress addr : addrs) {
                    InetAddress ip = addr.getAddress();
                    String macStr = getInetAddressMac(ip);
                    if (!EMPTYMAC.equalsIgnoreCase(macStr)) {
                        return macStr;
                    }
                }
            }
        } catch (Exception ignore) {

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
        Set<String> tmpMacList = new HashSet<>();

        while (en.hasMoreElements()) {
            NetworkInterface iface = en.nextElement();
            List<InterfaceAddress> addrs = iface.getInterfaceAddresses();
            for (InterfaceAddress addr : addrs) {
                InetAddress ip = addr.getAddress();
                String inetAddressMac = getInetAddressMac(ip);
                if(StringUtils.isBlank(inetAddressMac)){
                    continue;
                }
                tmpMacList.add(inetAddressMac);
            }
        }
        if (tmpMacList.size() <= 0) {
            return new ArrayList<>();
        }

        List<String> unique = new ArrayList<>(tmpMacList);
        return unique;
    }

    // 获取mac地址
    private static String getInetAddressMac(InetAddress ip) throws SocketException {
        NetworkInterface network = NetworkInterface.getByInetAddress(ip);
        if (network == null) {
            return null;
        }

        byte[] mac = network.getHardwareAddress();
        if (mac == null || mac.length == 0) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mac.length; i++) {
            sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
        }

        return sb.toString();
    }

}
