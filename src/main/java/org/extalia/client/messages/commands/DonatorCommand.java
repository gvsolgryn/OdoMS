package org.extalia.client.messages.commands;

import org.extalia.constants.ServerConstants;

public class DonatorCommand {
  public static ServerConstants.PlayerGMRank getPlayerLevelRequired() {
    return ServerConstants.PlayerGMRank.DONATOR;
  }
}
