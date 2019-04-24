package ServerIO;

import Core.Message;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

public class MsgBase {
    private Map<String, ConcurrentLinkedDeque<Message>> msgMap = new ConcurrentHashMap<>();

    public LinkedList<Message> getAllMessage(String userName) {
        Deque<Message> curDeque = msgMap.get(userName);
        LinkedList<Message> linkedList = new LinkedList<Message>(curDeque);
        curDeque.clear();
        return linkedList;
    }

    public boolean putMessage(String userName, Message msg) {
        if(userName==null) return false;
        if (msgMap.containsKey(userName)) {
            msgMap.get(userName).addLast(msg);
            return true;
        }
        return false;
    }

    public boolean multiPutMessage(Message msg) {
        for (Deque<Message> deque : msgMap.values()) {
            deque.offer(msg);
        }
        return true;
    }

    public boolean registerNewUser(String userName) {
        if (msgMap.containsKey(userName)) return false;
        ConcurrentLinkedDeque<Message> deque = new ConcurrentLinkedDeque<>();
        msgMap.put(userName, deque);
        return true;
    }

    public boolean unregisterUser(String userName) {
        if(userName==null) return false;
        if (!msgMap.containsKey(userName)) return false;
        msgMap.remove(userName);
        return true;
    }
}
