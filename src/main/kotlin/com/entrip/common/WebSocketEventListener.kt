package com.entrip.common

import com.entrip.domain.MessageType
import com.entrip.domain.SocketMessages
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.event.EventListener
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.web.socket.config.WebSocketMessageBrokerStats
import org.springframework.web.socket.messaging.SessionConnectedEvent
import org.springframework.web.socket.messaging.SessionDisconnectEvent

class WebSocketEventListener(
    private val messagingTemplate: SimpMessageSendingOperations?
    ) {
    private val logger: Logger = LoggerFactory.getLogger(WebSocketEventListener::class.java)

    //Listener function when connected
    @EventListener
    fun handleWebSocketConnectListener(event: SessionConnectedEvent?) {
        logger.info("Received a new web socket connection")
    }

    //Listener function when disconnected
    @EventListener
    fun handleWebSocketDisconnectListener(event: SessionDisconnectEvent) {
        val headerAccessor = StompHeaderAccessor.wrap(event.message)
        val username = headerAccessor.sessionAttributes!!["username"] as String?
        if (username != null) {
            logger.info("User Disconnected : $username")
            val socketMessages = SocketMessages(MessageType.LEAVE, -1, username)

            messagingTemplate!!.convertAndSend("topic/public", socketMessages)
        }
    }

}