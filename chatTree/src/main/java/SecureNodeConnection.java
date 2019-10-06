import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketAddress;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SecureNodeConnection extends NodeConnection implements Resender{


    private static final int GUID_LENGTH = 36;
    private ConcurrentHashMap<UUID, SecureChatNode.SecureMessage> serviceMessages;

    public SecureNodeConnection(String connectionId, int port, SocketAddress remoteAddress, SocketAddress parentAddress) {
        super(connectionId, port, remoteAddress, parentAddress);
        serviceMessages = new ConcurrentHashMap<>();

        TreeObserver observer = new TreeObserver(serviceMessages, this);
        observer.start();
    }

    protected void sendPacket(DatagramPacket packet) throws IOException {
        UUID guid = UUID.fromString(new String(packet.getData(), 0, GUID_LENGTH));
        SecureChatNode.SecureMessage msg = serviceMessages.get(guid);

        if(null != msg){
            // уже была переотправка
            msg.setSendTime(System.currentTimeMillis());
        }else{
            serviceMessages.put(guid,
                    new SecureChatNode.SecureMessage(packet.getData(),
                            System.currentTimeMillis(),packet.getSocketAddress()));
        }
        super.sendPacket(packet);
    }

    public void getConfirmation(UUID guid){
        if (null != serviceMessages.get(guid)) {
            // подтверждение пришло
            System.out.println("get confirmation child");
            serviceMessages.remove(guid);
        }else{
            // не мое
        }
    }

    /*@Override
    public void send(byte[] message, SocketAddress address){
        if(message.length == GUID_LENGTH) {
            UUID guid = UUID.fromString(new String(message, 0, GUID_LENGTH));
            if (null != serviceMessages.get(guid)) {
                // подтверждение пришло
                System.out.println("get confirmation");
                serviceMessages.remove(guid);
            }
        }
        super.send(message, address);
    }*/

    @Override
    public void resend(byte[] data, SocketAddress address) {
        super.send(data, address);
    }

    @Override
    public void detachConnection() {
        //detach
        super.interrupt();
        System.out.println("Detach node connection");
    }
}
