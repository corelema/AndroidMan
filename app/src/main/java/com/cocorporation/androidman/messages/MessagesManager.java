package com.cocorporation.androidman.messages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Corentin on 5/27/2015.
 */
public class MessagesManager {
    private static MessagesManager instance;
    private HashMap<MessagesType,List<MessageReceiver>> messagesReceivers;

    private MessagesManager()
    {
        messagesReceivers = new HashMap<MessagesType,List<MessageReceiver>>();
    }

    public static MessagesManager getInstance()
    {
        if (instance == null)
            instance = new MessagesManager();
        return instance;
    }

    public void registerForMessage(MessagesType messagesType, MessageReceiver instance)
    {
        List<MessageReceiver> receiversForThisType = messagesReceivers.get(messagesType);
        if (receiversForThisType != null)
        {
            if (!receiversForThisType.contains(instance))
            receiversForThisType.add(instance);
        }
        else
        {
            receiversForThisType = new ArrayList<MessageReceiver>();
            receiversForThisType.add(instance);
            messagesReceivers.put(messagesType, receiversForThisType);
        }
    }

    public void unsubscribe(MessagesType messagesType, MessageReceiver instance)
    {
        List<MessageReceiver> receiversForThisType = messagesReceivers.get(messagesType);
        if (receiversForThisType != null) {
            receiversForThisType.remove(instance);
        }
    }

    public void sendMessage(MessagesType messagesType, Object message)
    {
        List<MessageReceiver> receiversForThisType = messagesReceivers.get(messagesType);
        if (receiversForThisType != null)
        {
            for (MessageReceiver receiver : receiversForThisType)
            {
                receiver.receiveMessage(message);
            }
        }
    }
}
