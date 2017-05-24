///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package cz.ctu.ctuconference.utils.communication;
//
//import com.google.gson.JsonObject;
//import com.google.gson.JsonParser;
//import cz.ctu.ctuconference.call.CallMessageReceiver;
//import cz.ctu.ctuconference.authentication.AuthenticationMessageReceiver;
//import cz.ctu.ctuconference.contact.ContactMessageReceiver;
//import cz.ctu.ctuconference.conversation.ConversationMessageReceiver;
//import cz.ctu.ctuconference.speech.SpeechMessageReceiver;
//import cz.ctu.ctuconference.user.UserRegistry;
//import cz.ctu.ctuconference.user.UserSession;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.socket.server.standard.SpringConfigurator;
//
//import javax.websocket.OnClose;
//import javax.websocket.OnMessage;
//import javax.websocket.OnOpen;
//import javax.websocket.Session;
//import javax.websocket.server.ServerEndpoint;
//import java.io.IOException;
//
///**
// *
// * @author Nick Nemame
// */
//@ServerEndpoint(value = "/conversation", configurator = SpringConfigurator.class)
//public class ConversationEndpoint {
//
//    //@Autowired
//    private UserRegistry registry;
//
//    @Autowired
//    private WebSocketSender websocketSender;
//
//	@Autowired
//	private CallMessageReceiver callMessageReceiver;
//
//	@Autowired
//	private ContactMessageReceiver contactMessageReceiver;
//
//	@Autowired
//	private ConversationMessageReceiver conversationMessageReceiver;
//
//	@Autowired
//	private AuthenticationMessageReceiver authenticationMessageReceiver;
//
//	@Autowired
//	private SpeechMessageReceiver speechMessageReceiver;
//
//    private final Logger log = LoggerFactory.getLogger(ConversationEndpoint.class);
//
//    @OnOpen
//    public void onOpen(Session session) {
//
//    }
//
//    @OnClose
//    public void onClose(Session session) throws IOException {
//        System.out.println("Close session: "+session.getId());
//        callMessageReceiver.handleConnectionClose(null);
//    }
//
//    @OnMessage
//    public void handleMessage(Session session, String message) throws IOException, Exception {
//        try {
//			JsonParser parser = new JsonParser();
//            JsonObject jsonMessage = parser.parse(message).getAsJsonObject();
//            //logIncomingMessage(jsonMessage, session);
//			if(jsonMessage.get("type") == null) throw new WSMessageException("Message 'type' property is missing");
//			if(jsonMessage.get("data") == null) throw new WSMessageException("Message 'data' property is missing");
//			String messageType = jsonMessage.get("type").getAsString();
//			JsonObject jsonMessageData = jsonMessage.getAsJsonObject("data");
//            String[] messageTypeParts = messageType.split("\\.");
//            IMessageReceiver messageReceiver;
//            switch(messageTypeParts[0]) {
//                case "authentication":
//                    messageReceiver = authenticationMessageReceiver;
//                    break;
//				case "call":
//					messageReceiver = callMessageReceiver;
//					break;
//				case "speech":
//					messageReceiver = speechMessageReceiver;
//					break;
//				case "contact":
//					messageReceiver = contactMessageReceiver;
//					break;
//				case "conversation":
//					messageReceiver = conversationMessageReceiver;
//					break;
//                default:
//                    throw new WSMessageException("Message type in 'type' property is not recognised");
//            }
//            messageReceiver.handleMessage(messageType, jsonMessageData, null);
//        } catch(WSMessageException e) {
//            websocketSender.sendErrorMessage(null, e.getMessage(), message);
//        } catch (Exception e) {
//            websocketSender.sendErrorMessage(null, "Processing of incoming message failed. Message: "+e.getMessage(), message);
//        }
//    }
//
//    private void logIncomingMessage(JsonObject jsonMessage, Session session) {
//        final UserSession user = registry.getBySession(null);
//        if (user != null) {
//            log.debug("Incoming message from user '{}': {}", user.getUserId(), jsonMessage);
//        } else {
//            log.debug("Incoming message from new user: {}", jsonMessage);
//        }
//    }
//
//}
