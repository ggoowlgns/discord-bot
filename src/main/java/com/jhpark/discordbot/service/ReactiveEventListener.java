package com.jhpark.discordbot.service;

import discord4j.core.event.ReactiveEventAdapter;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteraction;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Random;

@Slf4j
@Service
public class ReactiveEventListener extends ReactiveEventAdapter {

  private final Random random = new Random();

  @Override
  public Publisher<?> onChatInputInteraction(ChatInputInteractionEvent event) {
    log.info("event : {}", event.getInteraction());
    if (event.getCommandName().equals("random")) {
      String result = result(random, event.getInteraction().getCommandInteraction().get());
      return event.reply(result);
    }
    return Mono.empty();
  }

  private static String result(Random random, ApplicationCommandInteraction acid) {
    long digits = acid.getOption("digits")
        .flatMap(ApplicationCommandInteractionOption::getValue)
        .map(ApplicationCommandInteractionOptionValue::asLong)
        .orElse(1L);
    StringBuilder result = new StringBuilder();
    for (int i = 0; i < Math.max(1, Math.min(20, digits)); i++) {
      result.append(random.nextInt(10));
    }
    return result.toString();
  }
}
