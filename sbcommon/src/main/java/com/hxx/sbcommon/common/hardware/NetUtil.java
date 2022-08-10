package com.hxx.sbcommon.common.hardware;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.regex.Pattern;

/**
 * 网卡
 *
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-07-18 15:06:08
 **/
public class NetUtil {
    private static final Logger logger = LoggerFactory.getLogger(NetUtil.class);
    private static final Pattern IP_PATTERN = Pattern.compile("\\d{1,3}(\\.\\d{1,3}){3,5}$");
    private static volatile InetAddress LOCAL_ADDRESS = getLocalAddress0();

    private NetUtil() {
    }

    private static boolean isValidAddress(InetAddress address) {
        if (address != null && !address.isLoopbackAddress()) {
            String name = address.getHostAddress();
            return name != null && !"0.0.0.0".equals(name) && !"127.0.0.1".equals(name) && IP_PATTERN.matcher(name).matches();
        } else {
            return false;
        }
    }

    public static InetAddress getLocalAddress() {
        if (LOCAL_ADDRESS != null) {
            return LOCAL_ADDRESS;
        } else {
            InetAddress localAddress = getLocalAddress0();
            LOCAL_ADDRESS = localAddress;
            return localAddress;
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
            logger.warn("Failed to retriving ip address,{}", var6.getMessage(), var6);
        }

        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            if (interfaces != null) {
                while (interfaces.hasMoreElements()) {
                    try {
                        NetworkInterface network = (NetworkInterface) interfaces.nextElement();
                        Enumeration<InetAddress> addresses = network.getInetAddresses();
                        if (addresses != null) {
                            while (addresses.hasMoreElements()) {
                                try {
                                    InetAddress address = (InetAddress) addresses.nextElement();
                                    if (isValidAddress(address)) {
                                        return address;
                                    }
                                } catch (Exception var5) {
                                    logger.warn("Failed to retriving ip address, {}", var5.getMessage(), var5);
                                }
                            }
                        }
                    } catch (Throwable var7) {
                        logger.warn("Failed to retriving ip address,{} ", var7.getMessage(), var7);
                    }
                }
            }
        } catch (Exception var8) {
            logger.warn("Failed to retriving ip address, {}", var8.getMessage(), var8);
        }

        logger.error("Could not get local host ip address, will use 127.0.0.1 instead.");
        return localAddress;
    }

}
