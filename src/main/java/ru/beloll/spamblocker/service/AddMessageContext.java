package ru.beloll.spamblocker.service;

import com.jsoniter.JsonIterator;
import com.jsoniter.output.JsonStream;
import com.yandex.ydb.table.Session;
import com.yandex.ydb.table.query.DataQuery;
import com.yandex.ydb.table.query.DataQueryResult;
import com.yandex.ydb.table.query.Params;
import com.yandex.ydb.table.transaction.TxControl;
import com.yandex.ydb.table.values.PrimitiveValue;
import ru.beloll.spamblocker.model.MessageContext;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Objects;

public class AddMessageContext extends HttpServlet {
    private final DBConnector dbConnector = new DBConnector();
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Session session = dbConnector.connect();
        String message = req.getReader().readLine();
        MessageContext messageContext = JsonIterator.deserialize(message, MessageContext.class);
        String id = messageContext.getId();
        if (Objects.isNull(id) || id.isEmpty()) {
            id = calculateHash(messageContext);
            messageContext.setId(id);
        }
        
        DataQuery query = session.prepareDataQuery(
                "DECLARE $id AS String;" +
                        "DECLARE $messageType AS String;" +
                        "DECLARE $client AS String;" +
                        "DECLARE $ipFrom AS String;" +
                        "DECLARE $sender AS String;" +
                        "DECLARE $subject AS String;" +
                        "DECLARE $message AS String;" +
                        "DECLARE $originalSender AS String;" +
                        "DECLARE $originalSubject AS String;" +
                        "DECLARE $originalMessage AS String;" +
                        "DECLARE $originalEmail AS String;" +
                        "DECLARE $originalPhone AS String;" +
                        "DECLARE $originalSite AS String;" +
                        "DECLARE $checked AS Bool;" +
                        "DECLARE $machineDecision AS String;" +
                        "DECLARE $machineComment AS String;" +
                        "DECLARE $machineRouteComment AS String;" +
                        "DECLARE $decision AS String;" +
                        "DECLARE $decisionComment AS String;" +
                        "UPSERT INTO messagecontext (id, messageType, client, ipFrom, sender,\n" +
                        "subject, message, originalSender, originalSubject, originalMessage, originalEmail,\n" +
                        "originalPhone, originalSite, checked, machineDecision, machineComment,\n" +
                        "machineRouteComment, decision, decisionComment) VALUES\n" +
                        "($id, $messageType, $client, $ipFrom, $sender, $subject, $message, originalSender,\n" +
                        "$originalSubject, $originalMessage, $originalEmail, $originalPhone,\n" +
                        "$originalSite, $checked, $machineDecision, $machineComment, $machineRouteComment,\n" +
                        "$decision, $decisionComment);")
                .join()
                .expect("query failed");
    
        Params params = query.newParams()
                .put("$id", PrimitiveValue.string(id.getBytes(StandardCharsets.UTF_8)))
                .put("$$messageType", PrimitiveValue.string(messageContext.getMessageType()
                        .getBytes(StandardCharsets.UTF_8)))
                .put("$client", PrimitiveValue.string(messageContext.getClient()
                        .getBytes(StandardCharsets.UTF_8)))
                .put("$ipFrom", PrimitiveValue.string(messageContext.getIpFrom()
                        .getBytes(StandardCharsets.UTF_8)))
                .put("$sender", PrimitiveValue.string(messageContext.getSender()
                        .getBytes(StandardCharsets.UTF_8)))
                .put("$subject", PrimitiveValue.string(messageContext.getSubject()
                        .getBytes(StandardCharsets.UTF_8)))
                .put("$message", PrimitiveValue.string(messageContext.getMessage()
                        .getBytes(StandardCharsets.UTF_8)))
                .put("$originalSender", PrimitiveValue.string(messageContext.getOriginalSender()
                        .getBytes(StandardCharsets.UTF_8)))
                .put("$originalSubject", PrimitiveValue.string(messageContext.getOriginalSubject()
                        .getBytes(StandardCharsets.UTF_8)))
                .put("$originalMessage", PrimitiveValue.string(messageContext.getOriginalMessage()
                        .getBytes(StandardCharsets.UTF_8)))
                .put("$originalEmail", PrimitiveValue.string(messageContext.getOriginalEmail()
                        .getBytes(StandardCharsets.UTF_8)))
                .put("$originalPhone", PrimitiveValue.string(messageContext.getOriginalPhone()
                        .getBytes(StandardCharsets.UTF_8)))
                .put("$originalSite", PrimitiveValue.string(messageContext.getOriginalSite()
                        .getBytes(StandardCharsets.UTF_8)))
                .put("$checked", PrimitiveValue.bool(messageContext.isChecked()))
                .put("$machineDecision", PrimitiveValue.string(messageContext.getMachineDecision()
                        .getBytes(StandardCharsets.UTF_8)))
                .put("$machineComment", PrimitiveValue.string(messageContext.getMachineComment()
                        .getBytes(StandardCharsets.UTF_8)))
                .put("$machineRouteComment", PrimitiveValue.string(messageContext.getMachineRouteComment()
                        .getBytes(StandardCharsets.UTF_8)))
                .put("$decision", PrimitiveValue.string(messageContext.getDecision()
                        .getBytes(StandardCharsets.UTF_8)))
                .put("$decisionComment", PrimitiveValue.string(messageContext.getDecisionComment()
                        .getBytes(StandardCharsets.UTF_8)));
        
        DataQueryResult result = query.execute(TxControl.serializableRw().setCommitTx(true), params)
                .join()
                .expect("query failed");
        
        PrintWriter writer = resp.getWriter();
    
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        
        writer.print(JsonStream.serialize(messageContext));
        writer.flush();
    }
    
    private String calculateHash(MessageContext messageContext) {
        return getHash(messageContext.getSender() + LocalDateTime.now() + messageContext.getSubject()
                + messageContext.getReceiver());
    }
    
    private String getHash(String s){
        long value=0L;
        long hash= 197L;
        long hashMod=10009L;
        
        for(int i=0; i<s.length(); i++){
            value=value*hash + (long)s.charAt(i);
            value%=hashMod;
        }
        return Long.toString(value);
    }
}
