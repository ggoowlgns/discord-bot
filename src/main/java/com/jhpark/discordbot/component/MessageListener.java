package com.jhpark.discordbot.component;

import discord4j.core.object.entity.Message;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public abstract class MessageListener {

  public Mono<Void> processAuthAndCommand(Message eventMessage) {
    return Mono.just(eventMessage)
        .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
        .filter(message -> message.getContent().equalsIgnoreCase("bot:"))
        .flatMap(Message::getChannel)
        .flatMap(channel -> {
          log.info("user : {} msg : {}", eventMessage.getAuthor(),eventMessage.toString());
          return channel.createMessage("jhpark discord bot, received message data: " + eventMessage.toString());
        })
        .then();
  }
}
