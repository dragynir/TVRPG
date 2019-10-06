import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketTimeoutException;

public class NodeReceiver {
    public boolean receivePacket(
            DatagramSocket socket, DatagramPacket datagramPacket)throws IOException {
        //if lossPercentage ...
        try {
            socket.receive(datagramPacket);
        }catch (SocketTimeoutException exc){
            return false;
        }
        return true;
    }
}
