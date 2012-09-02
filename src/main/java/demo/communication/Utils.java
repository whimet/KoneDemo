package demo.communication;

import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;

public class Utils {
    public static String extractMessage(DatagramPacket datagramPacket) throws UnsupportedEncodingException {
        byte[] data = datagramPacket.getData();
        byte[] buf = new byte[datagramPacket.getLength()];
        System.arraycopy(data, datagramPacket.getOffset(), buf, 0, datagramPacket.getLength());
        return new String(buf, "UTF-8");
    }
}
