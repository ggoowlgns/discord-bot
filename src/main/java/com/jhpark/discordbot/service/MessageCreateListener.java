package com.jhpark.discordbot.service;

import com.jhpark.discordbot.component.EventListener;
import com.jhpark.discordbot.component.MessageListener;
import discord4j.core.event.domain.message.MessageCreateEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class MessageCreateListener extends MessageListener implements EventListener<MessageCreateEvent> {

  @Override
  public Class<MessageCreateEvent> getEventType() {
    return MessageCreateEvent.class;
  }

  @Override
  public Mono<Void> execute(MessageCreateEvent event) {
    return processAuthAndCommand(event.getMessage());
  }
}
