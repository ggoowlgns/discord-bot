package com.jhpark.discordbot.component;

import discord4j.core.object.entity.Message;
import reactor.core.publisher.Mono;

public abstract class MessageListener {

  public Mono<Void> processAuthAndCommand(Message eventMessage) {
    return Mono.just(eventMessage)
        .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
        .filter(message -> message.getContent().equalsIgnoreCase("!todo"))
        .flatMap(Message::getChannel)
        .flatMap(channel -> channel.createMessage("jhpark discord bot, received message data: " + eventMessage.toString()))
        .then();
  }
}
