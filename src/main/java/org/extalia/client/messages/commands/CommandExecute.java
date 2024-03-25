package org.extalia.client.messages.commands;

import org.extalia.client.MapleClient;
import org.extalia.constants.ServerConstants;

public abstract class CommandExecute {
  public abstract int execute(MapleClient paramMapleClient, String[] paramArrayOfString);
  
  enum ReturnValue {
    DONT_LOG, LOG;
  }
  
  public ServerConstants.CommandType getType() {
    return ServerConstants.CommandType.NORMAL;
  }
  
  public static abstract class TradeExecute extends CommandExecute {
    public ServerConstants.CommandType getType() {
      return ServerConstants.CommandType.TRADE;
    }
  }
}
