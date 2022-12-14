package com.hxx.sbcommon.common.hardware;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.regex.Pattern;

/**
 * 网卡
 *
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-07-18 15:06:08
 **/
@Slf4j
public class NetUtil {
    // 空mac地址
    public static final String EMPTYMAC = "99-99-99-99-99-99";
    private static final Pattern IP_PATTERN = Pattern.compile("\\d{1,3}(\\.\\d{1,3}){3,5}$");
    private static volatile InetAddress LOCAL_ADDRESS = getLocalAddress0();

    private NetUtil() {
    }

    /**
     * 获取本地IP
     *
     * @return
     */
    public static InetAddress getLocalAddress() {
        if (LOCAL_ADDRESS != null) {
            return LOCAL_ADDRESS;
        } else {
            InetAddress localAddress = getLocalAddress0();
            LOCAL_ADDRESS = localAddress;
            return localAddress;
        }
    }

    /**
     * 获取本地IP地址
     *
     * @return
     */
    public static String getLocalIP() {
        try {
            InetAddress inetAddress = getLocalAddress();
            if (inetAddress == null) {
                return "";
            }
            return inetAddress.getHostAddress();
        } catch (Exception e) {
            log.warn("Failed to retriving ip address,{}", e.getMessage(), e);
            return "";
        }
    }

    /**
     * 获取本地MAC地址
     *
     * @return
     */
    public static String getLocalMac() {
        try {
            InetAddress inetAddress = getLocalAddress();
            if (inetAddress == null) {
                return EMPTYMAC;
            }
            return getInetAddressMac(inetAddress);
        } catch (Exception e) {
            log.warn("Failed to retriving ip mac,{}", e.getMessage(), e);
            return EMPTYMAC;
        }
    }

    // 是否有效
    private static boolean isValidAddress(InetAddress address) {
        if (address != null && !address.isLoopbackAddress()) {
            String name = address.getHostAddress();
            return name != null && !"0.0.0.0".equals(name) && !"127.0.0.1".equals(name) && IP_PATTERN.matcher(name)
                    .matches();
        } else {
            return false;
        }
    }

    private static InetAddress getLocalAddress0() {
        InetAddress localAddress = null;

        try {
            localAddress = InetAddress.getLocalHost();
            if (isValidAddress(localAddress)) {
                return localAddress;
            }
        } catch (Exception var6) {
            log.warn("Failed to retriving ip address,{}", var6.getMessage(), var6);
        }

        InetAddress validInetAddress = getValidInetAddress();
        if (validInetAddress != null) {
            return validInetAddress;
        }

        log.error("Could not get local host ip address, will use 127.0.0.1 instead.");
        return localAddress;
    }

    /**
     * 获取有效的 InetAddress
     *
     * @return
     */
    private static InetAddress getValidInetAddress() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            if (interfaces != null) {
                while (interfaces.hasMoreElements()) {
                    try {
                        NetworkInterface network = interfaces.nextElement();
                        Enumeration<InetAddress> addresses = network.getInetAddresses();
                        if (addresses != null) {
                            while (addresses.hasMoreElements()) {
                                try {
                                    InetAddress address = addresses.nextElement();
                                    if (isValidAddress(address)) {
                                        return address;
                                    }
                                } catch (Exception var5) {
                                    log.warn("Failed to retriving ip address, {}", var5.getMessage(), var5);
                                }
                            }
                        }
                    } catch (Throwable var7) {
                        log.warn("Failed to retriving ip address,{} ", var7.getMessage(), var7);
                    }
                }
            }
        } catch (Exception var8) {
            log.warn("Failed to retriving ip address, {}", var8.getMessage(), var8);
        }

        return null;
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
