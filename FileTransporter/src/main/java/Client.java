import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

public class Client {

    final static private int SEND_BUFFER_SIZE = 1024;
    final private static int SIZE_FIELD_LENGTH = 4;
    final private static int FILE_SIZE_FIELD_LENGTH = 8;
    final private static int STATUS_ERROR = 1;
    final private static int STATUS_OK = 0;


    static private void transferFile(
            File uploadFile, String fileName, Socket connection)throws IOException{

            try(InputStream uploadFileStream = new FileInputStream(uploadFile)) {

                DataOutputStream socketOut = new DataOutputStream(connection.getOutputStream());
                DataInputStream socketInput = new DataInputStream(connection.getInputStream());
                int pos = 0;
                for (int i = fileName.length() - 1; i >= 0; --i) {
                    if ('\\' == fileName.charAt(i)) {
                        pos = i;
                        break;
                    }
                }

                fileName = fileName.substring(pos);
                int headerLength =
                        FILE_SIZE_FIELD_LENGTH + SIZE_FIELD_LENGTH + fileName.length();
                socketOut.writeInt(headerLength);
                socketOut.writeInt(fileName.length());
                socketOut.write(fileName.getBytes());
                socketOut.writeLong(uploadFile.length());

                int count = 0;
                long totalSendedBytes = 0;
                byte[] bytes = new byte[SEND_BUFFER_SIZE];
                int status = 0;
                while ((count = uploadFileStream.read(bytes)) > 0) {
                    totalSendedBytes += count;
                    //System.out.println("Count: " + count);
                    //System.out.println(new String(bytes).substring(0, count));
                    socketOut.write(bytes, 0, count);
                    if (socketInput.available() > 0) {
                        status = socketInput.readInt();
                        if (STATUS_ERROR == status) {
                            System.out.println("Transfer failed, try again.");
                            return;
                        } else if (STATUS_OK == status) {
                            if (uploadFile.length() != totalSendedBytes) {
                                System.out.println("Transfer failed, try again.");
                            }
                            System.out.println("File transfer accomplished.");
                            return;
                        }
                    }
                }
                status = socketInput.readInt();
                if (STATUS_OK == status) {
                    System.out.println("File transfer accomplished.");
                } else {
                    System.out.println("Transfer failed, try again.");
                }
            }
    }

    public static void main(String[] args) {
        if(args.length != 3){
            System.out.println(args.length);
            System.out.println("Error, input format is: 'filename', 'DNS' or 'ip' , 'port'");
            return;
        }

        String fileName = args[0];
        String host = args[1];
        int port = Integer.parseInt(args[2]);


        // String fileName = "uploadfile.txt";
        // String host = "localhost";
        // int port = 4455;
        try(Socket uploadSocket = new Socket(host, port)) {
            File uploadFile = new File(fileName);
            transferFile(uploadFile, fileName, uploadSocket);
        }catch (UnknownHostException exc){
            System.out.println("Unknown host.");
        }catch (IOException exc){
            exc.printStackTrace();
        }
    }
}
