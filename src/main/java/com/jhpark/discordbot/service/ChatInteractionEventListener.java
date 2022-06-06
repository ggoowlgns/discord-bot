package com.jhpark.discordbot.service;

import com.jhpark.discordbot.component.EventListener;
import com.jhpark.discordbot.component.MessageListener;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.rest.interaction.InteractionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class ChatInteractionEventListener implements EventListener<ChatInputInteractionEvent> {
  @Override
  public Class<ChatInputInteractionEvent> getEventType() {
    return ChatInputInteractionEvent.class;
  }

  @Override
  public Mono execute(ChatInputInteractionEvent event) {
    return Mono.just(event)
        .map(ChatInputInteractionEvent::getInteraction)
        .map(interaction -> {
          log.info("interaction data : {}", interaction.getData());
          if ("greet".equals(interaction.getCommandInteraction().get().getName().orElse(""))) {
            String name = interaction.getCommandInteraction().get()
                .getOption("name")
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asString).orElse("");
            return event.reply().withContent("welcome : " + name);
          }
          return event.deferReply();
        });
  }
}
