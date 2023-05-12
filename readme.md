# VelocityUtils

<img src="https://slocraft.eu/slocraft-logo-512.png" width=124 height=124 align="right"/>

VelocityUtils was developed for use on the [SloCraft](https://slocraft.eu) network.

Please keep in mind that the plugin has not been updated since May 2022.

### Description

VelocityUtils is a plugin for Velocity proxy that provides a collection of useful features to enhance the proxy server functionality. 
It includes features such as alias management, custom player join/leave messages, and various other utilities.

### Features

- Command aliases
- Custom join/leave messages
- Per player join/leave messages
  - Allows color restriction

### Dependencies

VelocityUtils requires a Velocity proxy version 3.1.2 or higher (not tested).

### Configuration

On startup multiple configuration files are loaded: `config.conf`, `alias.conf` and `custom-messages.conf`. These files are generated automatically on first startup and can be found in the plugin's data folder.

- `config.conf` contains settings for the plugin and language strings.
- `alias.conf` contains all configured command aliases.
- `custom-messages.conf` contains player UUIDs mapped to custom join/leave messages.

### Usage

  - `/vutils help` - prints help for all available commands.
  - `/vutils joinmessage <clear|message>` - clears/sets your join message.
  - `/vutils leavemessage <clear|message>` - clears/sets your leave message.
  - `/vutils reload` - Reloads the plugin's configuration files. 

### Permissions

  - `velocityutils.command.help` - Allows the player to use the help command.
  - `velocityutils.command.reload` - Allows the player to reload the plugin.
  - `velocityutils.silent -` Join/leave messages for players with this permission will not be shown.
  - `velocityutils.color.vanilla` - Allows the player to use vanilla colors in their join/leave message.
  - `velocityutils.color.hex` - Allows the player to use hex colors in their join/leave message.
  - `velocityutils.color.gradient` - Allows the player to use gradients in their join/leave message.
  - `velocityutils.color.formatting` - Allows the player to use formatting in their join/leave message.
  - `velocityutils.messages.use` - Allows the player to use a custom join/leave message.
  - `velocityutils.messages.set` - Allows the player to change their join/leave message.
