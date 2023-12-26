package com.hxx.sbcommon.common.hardware;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import sun.net.util.IPAddressUtil;

import java.net.*;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
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
    public static final String EMPTY_MAC = "99-99-99-99-99-99";
    private static final Pattern IP_PATTERN = Pattern.compile("\\d{1,3}(\\.\\d{1,3}){3,5}$");
    private static volatile InetAddress LOCAL_ADDRESS = getIPV4();

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
            InetAddress localAddress = getIPV4();
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
            InetAddress ip = getLocalAddress();
            if (ip != null) {
                return ip.getHostAddress();
            }
            InetAddress localHost = InetAddress.getLocalHost();
            if (localHost != null) {
                return localHost.getHostAddress();
            }
        } catch (Exception e) {
            log.warn("Failed to retriving ip address,{}", e.getMessage(), e);
        }
        return "127.0.0.1";
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
                return EMPTY_MAC;
            }
            return getInetAddressMac(inetAddress);
        } catch (Exception e) {
            log.warn("Failed to retriving ip mac,{}", e.getMessage(), e);
            return EMPTY_MAC;
        }
    }

    /**
     * 获取本地所有Mac地址
     * k=mac v=网卡信息
     *
     * @return
     */
    public static Map<String, String> getAllLocalMacs() {
        Map<String, String> macs = new HashMap<>();
        readIPV4InetAddress(null, (networkInterface, ipAddress) -> {
            try {
                String mac = getInetAddressMac(networkInterface);
                if (!StringUtils.isBlank(mac)) {
                    macs.put(mac, networkInterface.getName() + " (" + networkInterface.getDisplayName() + ")");
                }
            } catch (SocketException ignore) {
            }
        });
        return macs;
    }

    /**
     * 获取本地IP
     *
     * @return
     */
    public static InetAddress getIPV4() {
        InetAddress[] arr = new InetAddress[2];
        TreeMap<String, InetAddress> eths = new TreeMap<>();
        TreeMap<String, InetAddress> wlans = new TreeMap<>();
        TreeMap<String, InetAddress> others = new TreeMap<>();
        // eth1，wlan0 最优先
        readIPV4InetAddress(null, (networkInterface, ipAddress) -> {
//            String displayName = Optional.ofNullable(networkInterface.getDisplayName())
//                    .map(d -> d.toLowerCase())
//                    .orElse("");
//            if (displayName.contains("bluetooth") || displayName.contains("vpn")) {
//                return;
//            }
            String name = Optional.ofNullable(networkInterface.getName()).map(d -> d.toLowerCase()).orElse("");
            if ("eth1".equals(name)) {
                arr[0] = ipAddress;
            } else if ("wlan0".equals(name)) {
                arr[1] = ipAddress;
            } else if (name.contains("eth")) {
                eths.put(name, ipAddress);
            } else if (name.contains("wlan")) {
                wlans.put(name, ipAddress);
            } else {
                others.put(name, ipAddress);
            }
        });
        if (arr[0] != null) {
            return arr[0];
        } else if (arr[1] != null) {
            return arr[1];
        } else {
            Map.Entry<String, InetAddress> firstEth = eths.firstEntry();
            Map.Entry<String, InetAddress> firstWlan = wlans.firstEntry();
            int ethNum = firstEth == null ? -1 : NumberUtils.toInt(firstEth.getKey().replace("eth", ""));
            int wlanNum = firstWlan == null ? -1 : NumberUtils.toInt(firstWlan.getKey().replace("wlan", ""));
            if (ethNum != -1 && wlanNum != -1) {
                if (ethNum > wlanNum) {
                    return firstWlan.getValue();
                } else {
                    return firstEth.getValue();
                }
            } else if (ethNum != -1) {
                return firstEth.getValue();
            } else if (wlanNum != -1) {
                return firstWlan.getValue();
            } else {
                Map.Entry<String, InetAddress> firstOther = others.firstEntry();
                return Optional.ofNullable(firstOther).map(d -> d.getValue()).orElse(null);
            }
        }
    }

    /**
     * 获取网卡启用的IPV4,过滤环回
     *
     * @return
     */
    public static List<InetAddress> getIPV4InetAddress() {
        return getIPV4InetAddress(null);
    }

    /**
     * 获取网卡启用的IPV4,过滤环回
     *
     * @param filteredNetworkCardName 网卡名称 可为空 也可为Intel  Realtek
     * @return
     */
    public static List<InetAddress> getIPV4InetAddress(List<String> filteredNetworkCardName) {
        List<InetAddress> ls = new ArrayList<>();
        readIPV4InetAddress(filteredNetworkCardName, (networkInterface, ipAddress) -> {
            ls.add(ipAddress);
        });

        return ls;
    }

    /**
     * 读取网卡信息
     *
     * @param filteredNetworkCardName 保留的网卡关键字，传空或null为全部
     * @param netInfoConsumer
     */
    public static void readIPV4InetAddress(List<String> filteredNetworkCardName, BiConsumer<NetworkInterface, InetAddress> netInfoConsumer) {
        readInetAddress(d -> {
            // name:wlan0 (Intel(R) Wireless-AC 9462)
            NetworkInterface networkInterface = d.getNetworkInterface();
//            // wlan0
//            String name = networkInterface.getName();
            // Intel(R) Wireless-AC 9462
            String displayName = networkInterface.getDisplayName();
            InetAddress ip = d.getInetAddress();
            log.info("网卡：{} 本地IP：{}", networkInterface, ip);
            if (ip.isLoopbackAddress()) {
                return;
            }
            // && !displayName.contains("virtual")
            if (d.isUp() && d.isIPV4() && isValidAddress(ip)) {
                if (StringUtils.isBlank(displayName)
                        || CollectionUtils.isEmpty(filteredNetworkCardName)
                        || filteredNetworkCardName.stream().anyMatch(f -> displayName.contains(f))) {
                    netInfoConsumer.accept(networkInterface, ip);
                }
            }
        });
    }

    // 是否有效
    private static boolean isValidAddress(InetAddress address) {
        if (address != null && !address.isLoopbackAddress()) {
            String name = address.getHostAddress();
            return name != null && !"0.0.0.0".equals(name) && !"127.0.0.1".equals(name) && IP_PATTERN.matcher(name).matches();
        }
        return false;
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
        if (network != null) {
            return getInetAddressMac(network);
        }
        return null;
    }

    private static String getInetAddressMac(NetworkInterface network) throws SocketException {
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
            if (inetAddress instanceof Inet4Address) {
                this.isIPV4 = true;
            } else if (inetAddress instanceof Inet6Address) {
                this.isIPV6 = true;
            } else if (!StringUtils.isBlank(this.ip)) {
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
