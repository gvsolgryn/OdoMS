package org.extalia.client.messages.commands;

import org.extalia.client.MapleClient;
import org.extalia.constants.ServerConstants;

public class CommandObject {
  private int gmLevelReq;
  
  private CommandExecute exe;
  
  public CommandObject(CommandExecute c, int gmLevel) {
    this.exe = c;
    this.gmLevelReq = gmLevel;
  }
  
  public int execute(MapleClient c, String[] splitted) {
    return this.exe.execute(c, splitted);
  }
  
  public ServerConstants.CommandType getType() {
    return this.exe.getType();
  }
  
  public int getReqGMLevel() {
    return this.gmLevelReq;
  }
}
