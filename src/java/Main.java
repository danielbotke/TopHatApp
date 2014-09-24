
import bean.TopHatMB;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import javax.faces.bean.ManagedBean;
import org.slf4j.LoggerFactory;

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
        rx.start();
    }
    static Thread rx = new Thread() {
        public void run() {
            DatagramSocket sock = null;
            byte[] msg = new byte[91];
            DatagramPacket pack = new DatagramPacket(msg, msg.length);
            try {
                sock = new DatagramSocket(1901);
            } catch (SocketException ex) {
                org.slf4j.Logger logger = LoggerFactory.getLogger(TopHatMB.class);
                logger.error(ex.getMessage(), ex);
            }


            //Recebimento da mensagem
            while (true) {
                try {
                    sock.receive(pack);
                    String teste = new String(pack.getData()).trim();
                    System.out.println("Pacote recebido: " + teste);
                } catch (IOException ex) {
                    org.slf4j.Logger logger = LoggerFactory.getLogger(TopHatMB.class);
                    logger.error(ex.getMessage(), ex);
                }
            }
        }
    };
}
