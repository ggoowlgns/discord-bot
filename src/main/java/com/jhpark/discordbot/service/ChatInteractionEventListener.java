package com.jhpark.discordbot.service;

import com.jhpark.discordbot.component.EventListener;
import com.jhpark.discordbot.component.MessageListener;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ChatInteractionEventListener implements EventListener<ChatInputInteractionEvent> {
  @Override
  public Class<ChatInputInteractionEvent> getEventType() {
    return ChatInputInteractionEvent.class;
  }

  @Override
  public Mono<Void> execute(ChatInputInteractionEvent event) {
    return null;
  }
}
