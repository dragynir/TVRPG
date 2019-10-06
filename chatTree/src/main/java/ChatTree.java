import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.Scanner;



//TODO detach


public class ChatTree {
    public static void main(String[] args){

        if(args.length < 3){
            System.out.println("Wrong arguments");
            return;
        }

        /*try {
            DatagramSocket d = new DatagramSocket(null);
            d.setReuseAddress(true);
            d.bind(new InetSocketAddress("localhost",4343));

            boolean g = d.getReuseAddress();

            DatagramSocket d2 = new DatagramSocket(null);
            d2.setReuseAddress(true);
            d2.bind(new InetSocketAddress("localhost",4343));

            boolean g2 = d.getReuseAddress();

        } catch (SocketException e) {
            e.printStackTrace();
        }*/


        // name, port, loss, pIp, pPort
        NodeInitialData initialData = new NodeInitialData(args[0]);
        initialData.setNodePort(Integer.parseInt(args[1]));
        initialData.setLossPercentage(Integer.parseInt(args[2]));
        initialData.setRoot(true);
        if(5 == args.length){
            initialData.setRoot(false);
            initialData.setParentNodeHost(args[3]);
            initialData.setParentNodePort(Integer.parseInt(args[4]));
        }
        //ChatNode node = new ChatNode(initialData);
        //node.start();

        SecureChatNode node = new SecureChatNode(initialData);
        node.start();


        Scanner inputScanner = new Scanner(System.in);

        System.out.println("Enter your message:\n");

        while (inputScanner.hasNextLine()){
            String line = inputScanner.nextLine();
            if("exit".equals(line)){
                //node.interrupt();
                break;
            }
            if(!node.sendMessage(line)){
                System.out.println("Not attached.");
            }
        }
    }
}
