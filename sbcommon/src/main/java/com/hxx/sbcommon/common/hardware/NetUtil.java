package com.hxx.sbcommon.common.hardware;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.net.util.IPAddressUtil;

import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
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
            List<InetAddress> ls = getIPV4InetAddress();

            InetAddress inetAddress;
            if (CollectionUtils.isEmpty(ls)) {
                inetAddress = getLocalHost();
            } else {
                inetAddress = ls.get(0);
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
        InetAddress validInetAddress = getValidInetAddress();
        if (validInetAddress != null) {
            return validInetAddress;
        }

        log.error("Could not get local host ip address, will use 127.0.0.1 instead.");
        return getLocalHost();
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

    private static InetAddress getLocalHost() {
        try {
            InetAddress localAddress = InetAddress.getLocalHost();
            if (isValidAddress(localAddress)) {
                return localAddress;
            }
        } catch (Exception var6) {
            log.warn("Failed to retriving ip address,{}", var6.getMessage(), var6);
        }
        return null;
    }

    /**
     * 获取网卡启用的IPV4,过滤环回
     *
     * @return
     */
    public static List<InetAddress> getIPV4InetAddress() {
        return getIPV4InetAddress("");
    }

    /**
     * 获取网卡启用的IPV4,过滤环回
     *
     * @param networkCardName 网卡名称包含此名称  Intel  Realtek
     * @return
     */
    public static List<InetAddress> getIPV4InetAddress(String networkCardName) {
        final String networkCardName1 = networkCardName == null ? "" : networkCardName.toLowerCase();

        List<InetAddress> ls = new ArrayList<>();
        readInetAddress(d -> {
            NetworkInterface networkInterface = d.getNetworkInterface();
            InetAddress inetAddress = d.getInetAddress();

            String displayName = (networkInterface.getDisplayName() + "").toLowerCase();
            // && !displayName.contains("virtual")
            if (d.isUp() && d.isIPV4() && displayName.contains(networkCardName1) && isValidAddress(inetAddress)) {
                ls.add(inetAddress);
            }
        });

        return ls;
    }

    /**
     * 获取所有网卡信息,不包括环回地址
     *
     * @return
     */
    private static void readInetAddress(Consumer<NetInfo> consumer) {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            if (interfaces == null) {
                return;
            }

            while (interfaces.hasMoreElements()) {
                try {
                    NetworkInterface network = interfaces.nextElement();
                    Enumeration<InetAddress> addresses = network.getInetAddresses();
                    if (addresses == null) {
                        break;
                    }

                    while (addresses.hasMoreElements()) {
                        try {
                            InetAddress address = addresses.nextElement();
                            if (address == null || address.isLoopbackAddress()) {
                                break;
                            }

                            NetInfo netInfo = new NetInfo(network, address);
                            consumer.accept(netInfo);
                        } catch (Exception var5) {
                            log.warn("Failed to retriving ip address, {}", var5.getMessage(), var5);
                        }
                    }
                } catch (Throwable var7) {
                    log.warn("Failed to retriving ip address,{} ", var7.getMessage(), var7);
                }
            }
        } catch (Exception var8) {
            log.warn("Failed to retriving ip address, {}", var8.getMessage(), var8);
        }
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

    @lombok.Data
    static class NetInfo {
        private NetworkInterface networkInterface;
        private InetAddress inetAddress;

        private String ip;
        private boolean isIPV4;
        private boolean isIPV6;
        /**
         * 是否启用
         */
        private boolean isUp;
        /**
         * 是否虚接口（子接口）
         */
        private boolean isVirtual;

        public NetInfo() {
        }

        public NetInfo(NetworkInterface networkInterface, InetAddress inetAddress) {
            this.networkInterface = networkInterface;
            this.inetAddress = inetAddress;

            this.ip = this.inetAddress.getHostAddress();
            if (!StringUtils.isBlank(this.ip)) {
                this.isIPV4 = IPAddressUtil.isIPv4LiteralAddress(this.ip);
                this.isIPV6 = IPAddressUtil.isIPv6LiteralAddress(this.ip);
            }
            try {
                this.isUp = this.networkInterface.isUp();
            } catch (Exception ignore) {

            }
            this.isVirtual = this.networkInterface.isVirtual();
        }
    }

}
