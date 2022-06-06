package com.jhpark.discordbot.service;

import com.jhpark.discordbot.component.EventListener;
import discord4j.core.event.domain.command.ApplicationCommandEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ApplicationCommandEventListener implements EventListener<ApplicationCommandEvent> {
  @Override
  public Class<ApplicationCommandEvent> getEventType() {
    return ApplicationCommandEvent.class;
  }

  @Override
  public Mono<Void> execute(ApplicationCommandEvent event) {
    return null;
  }
}
