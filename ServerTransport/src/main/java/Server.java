import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;


// without TCP speed



class ClientConnectionThread extends Thread {

    private Socket socket;
    private FileOutputStream newFileOutputStream = null;

    final private static int READ_LENGTH = 1024;
    final private static int CHANGE_FILE_NAME_TRIES = 10;

    final private static int STATUS_ERROR = 1;
    final private static int STATUS_OK = 0;
    final private static long CHECK_CONNECTION_SPEED_INTERVAL = 5000;

    private final ConnectionSpeedObserver speedObserver =
            new ConnectionSpeedObserver();

    static private class FileHeader{
        private String fileName;
        private long fileSize;

        FileHeader(String fileName, long fileSize) {
            this.fileName = fileName;
            this.fileSize = fileSize;
        }

        String getFileName() {
            return fileName;
        }

        long getFileSize() {
            return fileSize;
        }
    }


    private class ConnectionSpeedObserver{
        int bytesPerIntervalCount = 0;
        double meanSpeed = 0;
        int measureCount = 0;
        boolean state = true;

        void updateMeanSpeed(double addedSpeed){
            meanSpeed =  measureCount * meanSpeed / (measureCount + 1)
                    + addedSpeed / (measureCount + 1);
            long tmp = Math.round(meanSpeed * 100);
            meanSpeed = (double)tmp / 100;
            measureCount++;
        }

        double getMeanSpeed(){
            return this.meanSpeed;
        }

        void updateBytesCount(int bytes_received){
            bytesPerIntervalCount+=bytes_received;
        }
        void resetBytesCount(){
            this.bytesPerIntervalCount = 0;
        }
        void changeObserverState(boolean state){
            this.state = state;
        }
        int getBytesPerIntervalCount(){
            return bytesPerIntervalCount;
        }
        boolean getObserverState(){
            return this.state;
        }
    }


    // final static int

    ClientConnectionThread(Socket socket) {
        this.socket = socket;
    }

    void close() throws IOException {
        this.socket.close();
    }

    private FileHeader parseHeader(byte[] buffer) throws NumberFormatException, IOException {
        DataInputStream d = new DataInputStream(new ByteArrayInputStream(buffer));
        int fileNameSize = d.readInt();
        byte[] bytes = new byte[fileNameSize];

        int count = d.read(bytes);

        String fileName = new String(bytes);

        long fileSize = d.readLong();
        return new FileHeader(fileName, fileSize);
    }





    @Override
    public void run() {
        try {

            //final AtomicBoolean dataAvailable = new AtomicBoolean(false);
            //final AtomicBoolean clientTimeout = new AtomicBoolean(false);



            /*double instantTransportSpeed = 0;
            double meanInstantTransportSpeed = 0;
            int speedMeasurementsCount = 1;
            long lastMeasureTime = System.currentTimeMillis();*/

            /*new java.util.Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    if(!dataAvailable.get()){
                        clientTimeout.set(true);
                    }
                }
            }, CLIENT_TIME_TO_DETACH);*/
            DataInputStream socketInput = new DataInputStream(socket.getInputStream());
            DataOutputStream socketOutput = new DataOutputStream(socket.getOutputStream());

            // Header Length, Name length, Name, Total Size, Block Size , Block ...
            /*byte[] sizeBuffer = new byte[SIZE_FIELD_LENGTH];
            while (socketInput.available() < SIZE_FIELD_LENGTH){
                if(clientTimeout.get() || Thread.interrupted()){
                    socket.close();
                    return;
                }
            }
            dataAvailable.set(true);*/



            // System.out.println("Data available");
            int headerLength = socketInput.readInt();
            //dataAvailable.set(true);


            int offset = 0, read = 0;

            byte[] headerBuffer = new byte[headerLength];

            while ((read = socketInput.read(headerBuffer, offset, headerLength - offset)) > 0) {
                offset+=read;
                if(offset == headerLength){
                    break;
                }
            }
            FileHeader fileHeader =  parseHeader(headerBuffer);


            if(Thread.interrupted()) return;
            /*int headerLength = socketInput.readInt();

            int fileNameSize = socketInput.readInt();
            byte[] fil = new byte[fileNameSize];


            String fileName = "";

            socketInput.read(fil);
            fileName = new String(fil);

            long fileSize = socketInput.readLong();

            FileHeader fileHeader = new FileHeader(fileName, fileSize);*/

            /*byte[] headerBuffer = new byte[headerLength];

            int offset = 0, read = 0;


            while ((read = socketInput.read(headerBuffer, offset, headerLength)) > 0) {
                offset+=read;
                if(offset == headerLength){
                    break;
                }
            }

            FileHeader fileHeader = parseHeader(headerBuffer);*/



            /*File uploadFile = new File("1"+fileHeader.getFileName());

            try {
                uploadFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }*/

            File uploads = new File("uploads");

            if(!uploads.exists()){
                if(!uploads.mkdir()){
                    socketOutput.writeInt(STATUS_ERROR);
                    return;
                }
            }

            String newFilePath = uploads.getPath() + "/" + fileHeader.getFileName();

            String preFix = "";
            File newFile = null;
            for (int i = 0;i < CHANGE_FILE_NAME_TRIES;++i){
                newFile = new File(newFilePath);

                if(!newFile.exists()){
                    break;
                }
                preFix = String.valueOf((int)(Math.random() * 100));
                newFilePath = uploads.getPath() +
                        "/" + preFix + fileHeader.getFileName();
                if(Thread.interrupted()) return;
            }

            newFileOutputStream = new FileOutputStream(newFile);


            int receivedCount = 0, totalFileLength = 0;
            long clientFileSize =  fileHeader.getFileSize();



            byte[] bytes = new byte[READ_LENGTH];

            while ((receivedCount = socketInput.read(bytes)) > 0) {
                if(Thread.interrupted()) return;
                newFileOutputStream.write(bytes, 0, receivedCount);
                totalFileLength+=receivedCount;
                if(clientFileSize < totalFileLength || Thread.interrupted()){
                    socketOutput.writeInt(STATUS_ERROR);
                    return;
                }else if(clientFileSize == totalFileLength){
                    socketOutput.writeInt(STATUS_OK);
                    System.out.println("Done: " + fileHeader.getFileName());
                    return;
                }

                synchronized (speedObserver){
                    if(speedObserver.getObserverState()){
                        speedObserver.changeObserverState(false);
                        new java.util.Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                synchronized (speedObserver){
                                   double bytesCount =
                                           speedObserver.getBytesPerIntervalCount();
                                   double connectionSpeed =
                                           bytesCount / CHECK_CONNECTION_SPEED_INTERVAL / 1024;

                                    long tmp = Math.round(connectionSpeed * 100);
                                    connectionSpeed = (double)tmp / 100;

                                    System.out.println("Instant speed: " + connectionSpeed + " Kb");
                                    System.out.println("Mean speed: " +
                                            speedObserver.getMeanSpeed() + " Kb\n");

                                    speedObserver.resetBytesCount();
                                    speedObserver.updateMeanSpeed(connectionSpeed);
                                    speedObserver.changeObserverState(true);

                                }
                            }
                        }, CHECK_CONNECTION_SPEED_INTERVAL);

                    }
                    speedObserver.updateBytesCount(receivedCount);
                }
            }

            newFileOutputStream.close();


            if(totalFileLength != clientFileSize){
                socketOutput.writeInt(STATUS_ERROR);
            }else{
                socketOutput.writeInt(STATUS_OK);
            }

            socket.close();
            System.out.println("Done: " + fileHeader.getFileName());
        }
        catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException ne){
            System.out.println("Proto format exception");
            ne.printStackTrace();
        } finally {
            if(null != newFileOutputStream){
                try {
                    newFileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(!socket.isClosed()){
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}


class ConnectionReceiver extends Thread{

    private ServerSocket initialSocket;

    ConnectionReceiver(int  port) throws IOException {
        this.initialSocket = new ServerSocket(port);
    }

    void close() throws IOException {
        this.initialSocket.close();
    }

    public void run() {
        try {
                        /*initialSocket.bind(new InetSocketAddress(
                                "10.9.75.154", 4455));*/
            List<ClientConnectionThread> threads = new LinkedList<>();
            while (true) {
                Socket acceptSocket = initialSocket.accept();

                System.out.println("Server get connection from: "
                        + acceptSocket.getInetAddress());

                ClientConnectionThread clientThread = new ClientConnectionThread(acceptSocket);
                threads.add(clientThread);
                clientThread.start();
                if(Thread.interrupted()){
                    for(ClientConnectionThread connection : threads){
                        connection.interrupt();
                        connection.close();
                        connection.join();
                    }
                    break;
                }
            }
        }catch (IOException | InterruptedException e){
            e.printStackTrace();
        }finally {
            try {
                initialSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

public class Server {

    public static void main(String[] args) {
        if(args.length != 1){
            System.out.println("You need to enter port.");
            return;
        }

        System.out.println("Server startup");
        int port = Integer.parseInt(args[0]);

        try {

            //int port = Integer.parseInt(args[1]);
            // start listen

            ConnectionReceiver listenThread =  new ConnectionReceiver(port);
            listenThread.start();

            Scanner scanner = new Scanner(System.in);
            // cmd = 1 => finish program
            while(true){
                if(scanner.hasNextInt()){
                    int cmd = scanner.nextInt();
                    if(cmd == 1){
                        System.out.println("Out");
                        listenThread.interrupt();
                        listenThread.close();
                        return;
                    }
                }
            }
        }catch (NumberFormatException exc){
            System.out.println("Port format exception.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

