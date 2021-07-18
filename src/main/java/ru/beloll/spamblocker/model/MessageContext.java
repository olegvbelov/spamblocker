package ru.beloll.spamblocker.model;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MessageContext {
    private String id;
    private String messageType;
    private String client;
    private String ipFrom;
    private String sender;
    private String subject;
    private String message;
    private String receiver;
    private String originalSender;
    private String originalSubject;
    private String originalMessage;
    private String originalEmail;
    private String originalPhone;
    private String originalSite;
    private boolean checked;
    private String machineDecision;
    private String machineComment;
    private String machineRouteComment;
    private String decision;
    private String decisionComment;
    
    public MessageContext() {
        this.checked = false;
        this.machineDecision = "не проверено";
        this.machineComment = "";
        this.decision = "не проверено";
        this.decisionComment = "";
        this.machineRouteComment = "";
    }
    
    public String getMessageType() {
        return messageType;
    }
    
    public String getClient() {
        return client;
    }
    
    public String getIpFrom() {
        return ipFrom;
    }
    
    public String getMessage() {
        return message;
    }
    
    public String getOriginalSender() {
        return originalSender;
    }
    
    public String getOriginalSubject() {
        return originalSubject;
    }
    
    public String getOriginalMessage() {
        return originalMessage;
    }
    
    public String getOriginalEmail() {
        return originalEmail;
    }
    
    public String getOriginalPhone() {
        return originalPhone;
    }
    
    public String getOriginalSite() {
        return originalSite;
    }
    
    public boolean isChecked() {
        return checked;
    }
    
    public String getMachineDecision() {
        return machineDecision;
    }
    
    public String getMachineComment() {
        return machineComment;
    }
    
    public String getMachineRouteComment() {
        return machineRouteComment;
    }
    
    public String getDecision() {
        return decision;
    }
    
    public String getDecisionComment() {
        return decisionComment;
    }
    
    public String getId() {
        return id;
    }
    
    public String getSender() {
        return sender;
    }
    
    public String getSubject() {
        return subject;
    }
    
    public String getReceiver() {
        return receiver;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MessageContext)) return false;
        MessageContext that = (MessageContext) o;
        return messageType.equals(that.messageType) && client.equals(that.client) && ipFrom.equals(that.ipFrom) && sender.equals(that.sender) && subject.equals(that.subject) && message.equals(that.message) && receiver.equals(that.receiver);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(messageType, client, ipFrom, sender, subject, message, receiver);
    }
}
