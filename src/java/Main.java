
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import javax.faces.bean.ManagedBean;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Daniel
 */
@ManagedBean(name = "main")
public class Main {

    public void UDPListener() {
        DatagramSocket datagramSocket = null;
        try {
            datagramSocket = new DatagramSocket(9090);
        } catch (SocketException e) {
            e.printStackTrace();
        }

        byte[] buffer = new byte[100];

        // Create a datagram packet.
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

        while (true) {
            // Receive the packet.
            try {
                datagramSocket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }

            buffer = packet.getData();
            // Print the data:
            System.out.println(new String(buffer));
        }
    }
}
