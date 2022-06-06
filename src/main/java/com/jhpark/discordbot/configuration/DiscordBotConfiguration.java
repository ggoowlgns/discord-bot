package com.jhpark.discordbot.configuration;

import com.jhpark.discordbot.component.EventListener;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.Event;
import discord4j.core.object.command.ApplicationCommandOption;
import discord4j.discordjson.json.ApplicationCommandData;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import discord4j.discordjson.json.ApplicationCommandRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Slf4j
@Configuration
public class DiscordBotConfiguration {

  @Value("${discord.token}")
  private String token;

  @Value("${discord.application.id}")
  private String applicationId;

  @Value("${discord.guild.id}")
  private String guildId;

  @Bean
  public <T extends Event> GatewayDiscordClient gatewayDiscordClient(List<EventListener<T>> eventListeners) {
    GatewayDiscordClient client = DiscordClientBuilder.create(token)
        .build()
        .login()
        .block();
    addGlobalSlashCommand(client);
    addGuildSlashCommand(client);
    editGuildSlashCommand(client);

    addEventListenersInGWDiscord(client, eventListeners);
    return client;
  }

  private <T extends Event> void addEventListenersInGWDiscord(GatewayDiscordClient client, List<EventListener<T>> eventListeners) {
    for(EventListener<T> listener : eventListeners) {
      client.on(listener.getEventType())
          .flatMap(listener::execute)
          .onErrorResume(listener::handleError)
          .subscribe();
    }
  }

  private void addGlobalSlashCommand(GatewayDiscordClient client) {
    // Get our application's ID
//    long applicationId = client.getRestClient().getApplicationId().block();

    // Create the command with Discord
    client.getRestClient().getApplicationService()
        .createGlobalApplicationCommand(Long.parseLong(applicationId), greetCmdRequest())
        .subscribe();
  }

  private void addGuildSlashCommand(GatewayDiscordClient client) {
    // application ID and command definition are the same as the global command
    client.getRestClient().getApplicationService()
        .createGuildApplicationCommand(Long.parseLong(applicationId), Long.parseLong(guildId), greetCmdRequest())
        .subscribe();
  }


  private ApplicationCommandRequest greetCmdRequest() {
    // Build our command's definition
    ApplicationCommandRequest greetCmdRequest = ApplicationCommandRequest.builder()
        .name("greet")
        .description("Greets You")
        .addOption(ApplicationCommandOptionData.builder()
            .name("name")
            .description("Your name")
            .type(ApplicationCommandOption.Type.STRING.getValue())
            .required(true)
            .build()
        ).build();
    return greetCmdRequest;
  }

  private void editGuildSlashCommand(GatewayDiscordClient client) {
// Get the commands from discord as a Map
    Map<String, ApplicationCommandData> discordCommands = client.getRestClient()
        .getApplicationService()
        .getGuildApplicationCommands(Long.parseLong(applicationId), Long.parseLong(guildId))
        .collectMap(ApplicationCommandData::name)
        .block();
    log.info("discordCommands : {}", discordCommands);

// Pull out the copy of the greet command
    ApplicationCommandData discordGreetCmd = discordCommands.get(greetCmdRequest().name());

// Modify command
    client.getRestClient().getApplicationService()
        .modifyGuildApplicationCommand(Long.parseLong(applicationId), Long.parseLong(guildId), Long.parseLong(discordGreetCmd.id()), greetCmdRequest())
        .subscribe();
  }

}
