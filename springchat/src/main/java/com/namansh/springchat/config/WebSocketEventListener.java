package com.namansh.springchat.config;


import com.namansh.springchat.chat.ChatMessage;
import com.namansh.springchat.chat.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {
    private  final SimpMessageSendingOperations messageTemplate;

    @EventListener
    public void handleWebDisconnectListener(
            SessionDisconnectEvent event
    ){
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String)headerAccessor.getSessionAttributes().get("username");
        if (username!= null){
            log.info("User Disconnected {}",username);
            String chatMessage = String.valueOf(ChatMessage.builder()
                    .Type(MessageType.LEAVE)
                    .Sender(username)
                    .build());
            messageTemplate.convertAndSend("/topic/public",chatMessage);
        }
    }
}
