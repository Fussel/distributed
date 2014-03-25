/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package distributed.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;

/**
 *
 * @author b0n3h_000
 */
public class IpAdressAdapter {
    
    /*
    * Create a ip list with the active Ip
    */
    public ArrayList<String> getNetworkInterfaces() throws SocketException {
        ArrayList<String> ipList = new ArrayList<String>();
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            NetworkInterface current = interfaces.nextElement();
            //System.out.println(current);
            if (!current.isUp() || current.isLoopback() || current.isVirtual()) {
                continue;
            }
            Enumeration<InetAddress> addresses = current.getInetAddresses();

            while (addresses.hasMoreElements()) {
                InetAddress current_addr = addresses.nextElement();
                if (current_addr.isLoopbackAddress()) {
                    continue;
                }

                if (current_addr instanceof Inet4Address) {
                    System.out.println(current_addr.getHostAddress());
                    ipList.add(current_addr.getHostAddress());
                } /*else if (current_addr instanceof Inet6Address) {
                 System.out.println(current_addr.getHostAddress());
                 }*/

            }
        }
        return ipList;
    }
    
    /*
    *  Return a array with seperated numbers of the ip
    */
    public String[] getSeperatedNumber(int position, ArrayList<String> ipList) {
        String ip = ipList.get(position);
        String[] seperated = ip.split(".");
        return seperated;
    }
}
