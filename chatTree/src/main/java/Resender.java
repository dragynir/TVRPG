import java.net.SocketAddress;

public interface Resender {
    void resend(byte[] data, SocketAddress address);
    void detachConnection();
}
