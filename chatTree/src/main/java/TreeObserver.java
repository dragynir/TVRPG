import java.util.Enumeration;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class TreeObserver extends Thread{

    private ConcurrentHashMap<UUID, SecureChatNode.SecureMessage> serviceMessages;
    private Resender resender;

    public TreeObserver(
            ConcurrentHashMap<UUID, SecureChatNode.SecureMessage> serviceMessages,
            Resender resender){
        this.resender = resender;
        this.serviceMessages = serviceMessages;
    }

    @Override
    public void run() {
        while (true){
            if(!serviceMessages.isEmpty()){
                Enumeration<UUID> keys = serviceMessages.keys();
                while(keys.hasMoreElements()){
                    SecureChatNode.SecureMessage msg = serviceMessages.get(keys.nextElement());
                    if(null == msg)continue;
                    if(System.currentTimeMillis() - msg.getSendTime() > TreeConfig.DETACH_NODE_TIMEOUT){
                        if(msg.incrementSendTrialsCount() > TreeConfig.SEND_TRIALS_COUNT){

                            //?
                            resender.detachConnection();
                            return;
                        }
                        resender.resend(msg.getData(), msg.getAddress());
                        msg.setSendTime(System.currentTimeMillis());
                    }

                }
            }
        }
    }
}
