import java.io.IOException;
import java.net.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class NodeConnection extends Thread{
    private boolean active;
    private String id;


    private SocketAddress remoteAddress;

    public SocketAddress getRemoteAddress() {
        return remoteAddress;
    }

    public SocketAddress getParentAddress() {
        return parentAddress;
    }

    private SocketAddress parentAddress;
    private int port;

    private DatagramSocket datagramSocket;

    private  ConcurrentLinkedQueue<Message> messagesQueue;

    private static class Message{
        private byte[] msg;
        private SocketAddress address;

        public byte[] getMsg() {
            return msg;
        }

        public SocketAddress getAddress() {
            return address;
        }

        Message(byte[] msg, SocketAddress address){
            this.msg = msg;
            this.address = address;
        }

    }


    public NodeConnection(String connectionId,
                          int port, SocketAddress remoteAddress, SocketAddress parentAddress){
        this.id = connectionId;
        this.port = port;
        this.remoteAddress = remoteAddress;
        this.parentAddress = parentAddress;
        this.messagesQueue = new ConcurrentLinkedQueue<>();
    }


    /*private boolean receivePacket(DatagramPacket datagramPacket)throws IOException{
        //if lossPercentage ...
        try {
            datagramSocket.receive(datagramPacket);
        }catch (SocketTimeoutException exc){
            return false;
        }
        return true;
    }*/

    @Override
    public void run(){
        active = true;

        byte[] buffer = new byte[TreeConfig.DATAGRAM_LENGTH];
        DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length);
        NodeReceiver receiver = new NodeReceiver();

        try {
            datagramSocket = new DatagramSocket(null);
            datagramSocket.setReuseAddress(true);
            datagramSocket.bind(new InetSocketAddress("localhost", port));
            datagramSocket.connect(remoteAddress);
            datagramSocket.setSoTimeout(TreeConfig.RECEIVE_TIMEOUT);

            Message msgToSend = null;



            boolean swap = true;
            SocketAddress address;
            ConcurrentLinkedQueue<byte[]> queue;

            while (true) {
                if(Thread.interrupted()){
                    System.out.println("Interrupted child");
                    return;
                }
                msgToSend = messagesQueue.poll();
                if (null != msgToSend){
                    datagramPacket.setData(msgToSend.getMsg());
                    datagramPacket.setSocketAddress(msgToSend.getAddress());
                    sendPacket(datagramPacket);
                }
            }
        }catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            datagramSocket.close();
        }


    }

    protected void sendPacket(DatagramPacket packet) throws IOException {
        datagramSocket.send(packet);
        System.out.println("Sending");
    }



    public void send(byte[] message, SocketAddress address){
        // check UDP
        messagesQueue.add(new Message(message, address));
    }

    public boolean isActive(){
        return active;
    }

    public String getConnectionId(){
        return id;
    }


}
