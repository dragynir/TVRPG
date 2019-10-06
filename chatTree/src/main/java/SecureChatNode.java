import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SecureChatNode extends ChatNode implements Resender {

    private static final int GUID_LENGTH = 36;
    private int lossPercentage;
    private ConcurrentHashMap<UUID, SecureMessage> serviceMessages;

    static class SecureMessage{
        private byte[] data;
        private long sendTime;
        private int sendTrialsCount;
        private SocketAddress address;

        public SocketAddress getAddress() {
            return address;
        }

        public SecureMessage(byte[] data, long sendTime, SocketAddress address){
            this.address = address;
            this.data = data;
            this.sendTime = sendTime;
        }

        public void setSendTime(long sendTime) {
            this.sendTime = sendTime;
        }

        public long incrementSendTrialsCount(){
            return ++sendTrialsCount;
        }

        public byte[] getData() {
            return data;
        }

        public long getSendTime() {
            return sendTime;
        }

    }


    @Override
    public void resend(byte[] data, SocketAddress address) {
        super.addRootMessageToQueue(data);
    }

    @Override
    public void detachConnection() {
        // detach from parent
        super.setHasRoot(false);
        System.out.println("Detach from parent");
    }


    public SecureChatNode(NodeInitialData initialData) {
        super(initialData);
        lossPercentage = initialData.getLossPercentage();
        serviceMessages = new ConcurrentHashMap<>();

        TreeObserver observer = new TreeObserver(serviceMessages, this);
        observer.start();
    }

    @Override
    protected NodeConnection createNodeConnection(String connectionId,
                                                  int port, SocketAddress remoteAddress, SocketAddress parentAddress){
        return new SecureNodeConnection(
                connectionId, port, remoteAddress, parentAddress);
    }

    protected boolean receivePacket(
            DatagramSocket socket, DatagramPacket datagramPacket)throws IOException {

        //if lossPercentage ...
        /*try {
            socket.receive(datagramPacket);
            if(datagramPacket.getLength() == GUID_LENGTH) {
                // не выбрасывать, пришло подтверждение
            }
        }catch (SocketTimeoutException exc){
            return false;
        }*/

        // if loss


        if(!super.receivePacket(socket, datagramPacket)){
            return false;
        }
        System.out.println("Recv");


        String strGuid = new String(datagramPacket.getData(), 0, GUID_LENGTH);


        if(datagramPacket.getLength() == GUID_LENGTH) {
            UUID guid = UUID.fromString(strGuid);
            if (null != serviceMessages.get(guid)) {
                // подтверждение пришло
                System.out.println("get confirmation my");
                serviceMessages.remove(guid);
            }else{

                int port = datagramPacket.getPort();
                String address = datagramPacket.getAddress().getHostAddress();
                //connectionsIds.add("asdf");
                String id = address+port;

                SecureNodeConnection connection =
                        (SecureNodeConnection)super.getConnectionById(id);
                if(null != connection) {
                    connection.getConfirmation(guid);
                }
                // не зарегестрированная datagram
                // значит для NodeConnection
            }
            return false;
        }

        int randomPercentage = (int)(Math.random() * 100);
        if(lossPercentage > randomPercentage){
            System.out.println("Lost datagram");
            return false;
        }

        byte[] guidBytes = strGuid.getBytes();

        DatagramPacket confirmDatagram = new DatagramPacket(guidBytes, guidBytes.length);
        confirmDatagram.setSocketAddress(datagramPacket.getSocketAddress());
        socket.send(confirmDatagram);
        System.out.println("Send confirmation, ok");
        return true;
    }



    protected void sendDatagramToRoot(DatagramPacket datagramPacket)throws IOException{
        // for receive
        /*if(datagramPacket.getLength() == GUID_LENGTH) {
            UUID guid = UUID.fromString(new String(datagramPacket.getData(), 0, GUID_LENGTH));
            if (null != serviceMessages.get(guid)) {
                // подтверждение пришло
                serviceMessages.remove(guid);
            }
        }else{
            // это простое сообщение
            super.sendDatagramToRoot(datagramPacket);
        }*/

        UUID guid = UUID.fromString(new String(datagramPacket.getData(), 0, GUID_LENGTH));

        SecureMessage msg = serviceMessages.get(guid);

        if(null != msg){
            // уже была переотправка
            msg.setSendTime(System.currentTimeMillis());
        }else{
            serviceMessages.put(guid,
                    new SecureMessage(datagramPacket.getData(),
                            System.currentTimeMillis(),null));
        }
        super.sendDatagramToRoot(datagramPacket);
    }
}
