

import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ChatNode extends Thread{

    // написать с одним сокетом на весь узел для начала

    final private int RECEIVE_TIMEOUT = 5000;
    final private int DATAGRAM_LENGTH = 100;

    private boolean attached;
    private NodeInitialData initialData;



    private ConcurrentLinkedQueue<Message> messagesQueue;

    private DatagramSocket controlSocket;

    private LinkedList<NodeConnection> connections;
    private HashMap<String, NodeConnection> connectionsIds;

    private boolean hasRoot;

    // private wait for answer structure

    protected NodeConnection getConnectionById(String id){
        return connectionsIds.get(id);
    }

    protected void setHasRoot(boolean has){
        this.hasRoot = has;
    }


    private static class Message{
        private byte[] data;
        boolean broadcast;

        public byte[] getData() {
            return data;
        }

        public boolean isBroadcast() {
            return broadcast;
        }

        public Message(byte[] msg, boolean broadcast) {
            this.data = msg;
            this.broadcast = broadcast;
        }
    }


    public ChatNode(NodeInitialData initialData){
        this.initialData = initialData;
        this.messagesQueue = new ConcurrentLinkedQueue<>();
        connections = new LinkedList<>();
        connectionsIds = new HashMap<>();
    }

    @Override
    public void run(){
        attachToTree();
    }


    /*public void sendMessage(byte[] message){
        messagesQueue.add(message);
    }*/

    public boolean sendMessage(String message){
        if(!attached) return false;
        String msg = (UUID.randomUUID() + ":")
                + (initialData.getNodeName() + ":") + message;

        // nullpe if message is null
        messagesQueue.add(new Message(msg.getBytes().clone(), true));
        return true;
    }

    protected void addRootMessageToQueue(byte[] data){
        messagesQueue.add(new Message(data.clone(), false));
    }

    /*private boolean receivePacket(DatagramPacket datagramPacket)throws IOException{
        //if lossPercentage ...
        try {
            controlSocket.receive(datagramPacket);
        }catch (SocketTimeoutException exc){
            return false;
        }
        return true;
    }*/


    // разделить отправку на up  и child down


    private void sendToChildren(byte[] data, String exceptId){
        System.out.println("Send to children " + connections.size());

        Iterator<NodeConnection> iter = connections.iterator();
        while (iter.hasNext()){
            NodeConnection connection = iter.next();
            if(exceptId != null && exceptId.equals(connection.getConnectionId())){
                continue;
            }
            if(!connection.isAlive()){
                System.out.println("Remove");
                connections.remove(connection);
                connectionsIds.remove(connection.getConnectionId());
            }else {
                connection.send(data.clone(),
                        connection.getRemoteAddress());
            }
        }
    }



    private void attachToTree(){
         attached = true;

         String parentId = null;
         SocketAddress parentAddress = null;
         if(!initialData.isRoot()){
              this.hasRoot = true;
              parentId = initialData.getParentNodeHost() + initialData.getParentNodePort();
              parentAddress = new InetSocketAddress(
                     initialData.getParentNodeHost(),initialData.getParentNodePort());
              System.out.println("Has root");
         }




        byte[] bufferIn = new byte[TreeConfig.DATAGRAM_LENGTH];
        DatagramPacket datagramPacketIn = new DatagramPacket(bufferIn, bufferIn.length);

         try{
             controlSocket = new DatagramSocket(null);
             controlSocket.setReuseAddress(true);
             controlSocket.bind(new InetSocketAddress(
                     "localhost", initialData.getNodePort()));
             controlSocket.setSoTimeout(TreeConfig.RECEIVE_TIMEOUT);

             while (attached){



                 // wrong with offset
                 datagramPacketIn.setData(bufferIn);
                 // UDP send by decorator
                 if(receivePacket(controlSocket, datagramPacketIn)){


                     byte[] reSendBytes = new String(datagramPacketIn.getData(),
                             0, datagramPacketIn.getLength()).getBytes();

                     int port = datagramPacketIn.getPort();
                     String address = datagramPacketIn.getAddress().getHostAddress();
                     //connectionsIds.add("asdf");
                     String id = address+port;
                     System.out.println(parentId + "--" + id);

                     if(id.equals(parentId)){
                         if(!hasRoot){
                             System.out.println("msg from detached root");
                             continue;
                         }

                         System.out.println("Msg from root");



                         System.out.println(new String(reSendBytes));

                         sendToChildren(reSendBytes, null);
                         //1 наверх
                         // 2 вверх вниз

                     }else if(!connectionsIds.keySet().contains(id)){
                         System.out.println("Get child connection");
                         System.out.println(new String(reSendBytes));



                         sendToChildren(reSendBytes  , null);
                         addRootMessageToQueue(reSendBytes);

                         //add id to connection
                         NodeConnection c = createNodeConnection(
                                 id, initialData.getNodePort(), datagramPacketIn.getSocketAddress(), parentAddress);
                         connections.add(c);
                         connectionsIds.put(id, c);
                         c.start();

                         // create connection
                         // add msg to local queue
                     }else{
                         System.out.println(new String(reSendBytes));
                         NodeConnection connection = connectionsIds.get(id);



                         // check for secure udp
                         // send up
                         if(null != parentAddress){
                             connection.send(reSendBytes.clone(),
                                     connection.getParentAddress());
                         }
                         sendToChildren(reSendBytes, connection.getConnectionId());
                         // do nothing
                         //cause connection will add new msg to the queue
                     }
                 }

                 Message msgToSend = messagesQueue.poll();

                 if(null == msgToSend)continue;
                 if(hasRoot){
                     byte[] data = msgToSend.getData();
                     DatagramPacket datagramPacketOut =
                             new DatagramPacket(data, data.length);
                     datagramPacketOut.setSocketAddress(parentAddress);
                     sendDatagramToRoot(datagramPacketOut);
                 }
                 if(msgToSend.isBroadcast()){
                     //System.out.println("Broad");
                     sendToChildren(msgToSend.getData(), null);
                 }
             }

         }catch(SocketException e) {
             e.printStackTrace();
         } catch (IOException e) {
             e.printStackTrace();
         }finally {
             controlSocket.close();
         }
    }

    protected NodeConnection createNodeConnection(String connectionId,
          int port, SocketAddress remoteAddress, SocketAddress parentAddress){
        return new NodeConnection(
                connectionId, port, remoteAddress, parentAddress);
    }

    protected boolean receivePacket(
            DatagramSocket socket, DatagramPacket datagramPacket)throws IOException {
        //if lossPercentage ...
        try {
            socket.receive(datagramPacket);
        }catch (SocketTimeoutException exc){
            return false;
        }
        return true;
    }

    protected void sendDatagramToRoot(DatagramPacket datagramPacketOut)throws IOException{
        controlSocket.send(datagramPacketOut);
    }

    protected ConcurrentLinkedQueue<Message> getMessagesQueue() {
        return messagesQueue;
    }

    public void detach(){
        attached = false;
    }



}
