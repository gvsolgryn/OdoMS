package handler.channel;

import java.util.Collection;

import client.MapleCharacter;
import client.MapleClient;
import client.commands.CommandProcessor;
import community.MapleMultiChat;
import community.MapleMultiChatCharacter;
import constants.ServerConstants;
import handler.admin.AdminToolPacket;
import launch.AdminToolServer;
import launch.CashShopServer;
import launch.ChannelServer;
import launch.world.WorldBroadcasting;
import launch.world.WorldCommunity;
import packet.creators.MainPacketCreator;
import packet.transfer.read.ReadingMaple;
import tools.LoggerChatting;
import constants.programs.ControlUnit;
import server.quest.MapleQuest;

public class ChatHandler {

    public static void GeneralChat(String text, byte unk, MapleClient c, MapleCharacter chr) {
         if (!CommandProcessor.getInstance().processCommand(c, text)) {
            if (!chr.isGM() && text.length() >= 80) {
                return;
            }
            if (WorldCommunity.isFreeze) {
                chr.dropMessage(1, "채팅이 얼려져있는 상태이므로\r\n채팅을 할 수 없습니다.");
                return;
            }
            if (chr.getChatban().equals("true")) {
                chr.dropMessage(1, "채팅 금지 상태에선 채팅을 할 수 없습니다.");
                return;
            }
            if (text.charAt(0) == '~') {
                if (chr.getMeso() < 20000) {
                    chr.dropMessage(1, "20000메소가 없어 전체채팅을 사용할 수 없습니다.");
                    return;
                } else {
                    if (ServerConstants.chatlimit >= 500) {
                        ServerConstants.chatlimit = 0;
                        ControlUnit.ChatList.clear();
                        ControlUnit.Chat.setModel(ControlUnit.ChatList);
                    }
                    ServerConstants.chatlimit++;
                    chr.gainMeso(-20000, false);
                    StringBuilder sb = new StringBuilder();
                    InventoryHandler.addMedalString(c.getPlayer(), sb);
                    sb.append(c.getPlayer().getName());
                    sb.append(" : ");
                    sb.append(text.substring(1));
                    
                    String Rank = c.getPlayer().getQuestNAdd(MapleQuest.getInstance(201801)).getCustomData();
                    String Star = Rank == null ? "0" : Rank;
                        if (c.getPlayer().getItemQuantity(1142535, true) > 0) {
                            WorldBroadcasting.broadcastMessage(MainPacketCreator.getGMText(25, "<VIP> < " + c.getPlayer().getHope() +  " > " + c.getPlayer().getName() + " [" + Star + "성] : " + text.replaceAll("~", "")));
                        } else if (c.getPlayer().getItemQuantity(1142930, true) > 0) {
                            WorldBroadcasting.broadcastMessage(MainPacketCreator.getGMText(8, "《 VIP 》 < " + c.getPlayer().getHope() +  " > " + c.getPlayer().getName() + " [" + Star + "성] : " + text.replaceAll("~", "")));
                        } else if (c.getPlayer().getItemQuantity(1142931, true) > 0) {
                            WorldBroadcasting.broadcastMessage(MainPacketCreator.getGMText(8, "《 CEO 》 < " + c.getPlayer().getHope() +  " > " + c.getPlayer().getName() + " [" + Star + "성] : " + text.replaceAll("~", "")));
                        } else if (c.getPlayer().getItemQuantity(1142932, true) > 0) {
                            WorldBroadcasting.broadcastMessage(MainPacketCreator.getGMText(8, "《 금수저 》 < " + c.getPlayer().getHope() +  " > " + c.getPlayer().getName() + " [" + Star + "성] : " + text.replaceAll("~", "")));
                        } else if (c.getPlayer().getItemQuantity(1142933, true) > 0) {
                            WorldBroadcasting.broadcastMessage(MainPacketCreator.getGMText(8, "《 다이아 》 < " + c.getPlayer().getHope() +  " > " + c.getPlayer().getName() + " [" + Star + "성] : " + text.replaceAll("~", "")));
                        } else if (c.getPlayer().getItemQuantity(1142934, true) > 0) {
                            WorldBroadcasting.broadcastMessage(MainPacketCreator.getGMText(22, "♥츄♥ < " + c.getPlayer().getHope() +  " > " + c.getPlayer().getName() + " [" + Star + "성] : " + text.replaceAll("~", "")));
                        } else if (c.getPlayer().getItemQuantity(1142935, true) > 0) {
                            WorldBroadcasting.broadcastMessage(MainPacketCreator.getGMText(22, "♤이타치♤ < " + c.getPlayer().getHope() +  " > " + c.getPlayer().getName() + " [" + Star + "성] : " + text.replaceAll("~", "")));
                        } else if (c.getPlayer().getItemQuantity(1142940, true) > 0) {
                            WorldBroadcasting.broadcastMessage(MainPacketCreator.getGMText(22, "JYP < " + c.getPlayer().getHope() +  " > " + c.getPlayer().getName() + " [" + Star + "성] : " + text.replaceAll("~", "")));
                        } else if (c.getPlayer().getItemQuantity(1142936, true) > 0) {
                            WorldBroadcasting.broadcastMessage(MainPacketCreator.getGMText(5, "♤마스터♤ < " + c.getPlayer().getHope() +  " > " + c.getPlayer().getName() + " [" + Star + "성] : " + text.replaceAll("~", "")));
                        } else if (c.getPlayer().getItemQuantity(1142937, true) > 0) {
                            WorldBroadcasting.broadcastMessage(MainPacketCreator.getGMText(5, "JYP < " + c.getPlayer().getHope() +  " > " + c.getPlayer().getName() + " [" + Star + "성] : " + text.replaceAll("~", "")));
                        } else if (c.getPlayer().getItemQuantity(1142938, true) > 0) {
                            WorldBroadcasting.broadcastMessage(MainPacketCreator.getGMText(5, "♥백화♥ < " + c.getPlayer().getHope() +  " > " + c.getPlayer().getName() + " [" + Star + "성] : " + text.replaceAll("~", "")));
                        } else if (c.getPlayer().getItemQuantity(1142939, true) > 0) {
                            WorldBroadcasting.broadcastMessage(MainPacketCreator.getGMText(5, "나태 < " + c.getPlayer().getHope() +  " > " + c.getPlayer().getName() + " [" + Star + "성] : " + text.replaceAll("~", "")));
                        } else if (c.getPlayer().getItemQuantity(1142941, true) > 0) {
                            WorldBroadcasting.broadcastMessage(MainPacketCreator.getGMText(5, "♥뿜♥ < " + c.getPlayer().getHope() +  " > " + c.getPlayer().getName() + " [" + Star + "성] : " + text.replaceAll("~", "")));
                        } else if (c.getPlayer().getItemQuantity(1142942, true) > 0) {
                            WorldBroadcasting.broadcastMessage(MainPacketCreator.getGMText(5, "돈지랄 < " + c.getPlayer().getHope() +  " > " + c.getPlayer().getName() + " [" + Star + "성] : " + text.replaceAll("~", "")));
                        } else if (c.getPlayer().getItemQuantity(1142943, true) > 0) {
                            WorldBroadcasting.broadcastMessage(MainPacketCreator.getGMText(5, "♤ 운명 ♤ < " + c.getPlayer().getHope() +  " > " + c.getPlayer().getName() + " [" + Star + "성] : " + text.replaceAll("~", "")));
                        } else if (c.getPlayer().getItemQuantity(1142944, true) > 0) {
                            WorldBroadcasting.broadcastMessage(MainPacketCreator.getGMText(5, "☆ 인싸 ★ < " + c.getPlayer().getHope() +  " > " + c.getPlayer().getName() + " [" + Star + "성] : " + text.replaceAll("~", "")));
                        } else if (c.getPlayer().getItemQuantity(1142970, true) > 0) {
                            WorldBroadcasting.broadcastMessage(MainPacketCreator.getGMText(29, "★초원블로거★ < " + c.getPlayer().getHope() +  " > " + c.getPlayer().getName() + " [" + Star + "성] : " + text.replaceAll("~", "")));
                        } else if (c.getPlayer().getItemQuantity(1142971, true) > 0) {
                            WorldBroadcasting.broadcastMessage(MainPacketCreator.getGMText(29, "★초원방송인★ < " + c.getPlayer().getHope() +  " > " + c.getPlayer().getName() + " [" + Star + "성] : " + text.replaceAll("~", "")));
                        } else if (c.getPlayer().getItemQuantity(1142972, true) > 0) {
                            WorldBroadcasting.broadcastMessage(MainPacketCreator.getGMText(29, "★초원환생왕★ < " + c.getPlayer().getHope() +  " > " + c.getPlayer().getName() + " [" + Star + "성] : " + text.replaceAll("~", "")));
                        } else if (c.getPlayer().getItemQuantity(1142973, true) > 0) {
                            WorldBroadcasting.broadcastMessage(MainPacketCreator.getGMText(29, "★초원점프왕★ < " + c.getPlayer().getHope() +  " > " + c.getPlayer().getName() + " [" + Star + "성] : " + text.replaceAll("~", "")));
                        } else if (c.getPlayer().getItemQuantity(1142974, true) > 0) {
                            WorldBroadcasting.broadcastMessage(MainPacketCreator.getGMText(29, "★초원낚시왕★ < " + c.getPlayer().getHope() +  " > " + c.getPlayer().getName() + " [" + Star + "성] : " + text.replaceAll("~", "")));
                        } else if (c.getPlayer().getItemQuantity(1142975, true) > 0) {
                            WorldBroadcasting.broadcastMessage(MainPacketCreator.getGMText(1, "★초원서포터즈★ < " + c.getPlayer().getHope() +  " > " + c.getPlayer().getName() + " [" + Star + "성] : " + text.replaceAll("~", "")));
                        } else if (c.getPlayer().getItemQuantity(1142976, true) > 0) {
                            WorldBroadcasting.broadcastMessage(MainPacketCreator.getGMText(29, "★초원슈퍼스타★ < " + c.getPlayer().getHope() +  " > " + c.getPlayer().getName() + " [" + Star + "성] : " + text.replaceAll("~", "")));
                        } else if (c.getPlayer().getItemQuantity(1142977, true) > 0) {
                            WorldBroadcasting.broadcastMessage(MainPacketCreator.getGMText(29, "★초원갑부★ < " + c.getPlayer().getHope() +  " > " + c.getPlayer().getName() + " [" + Star + "성] : " + text.replaceAll("~", "")));
                        } else if (c.getPlayer().getItemQuantity(1142978, true) > 0) {
                            WorldBroadcasting.broadcastMessage(MainPacketCreator.getGMText(29, "★쿠로네코★ < " + c.getPlayer().getHope() +  " > " + c.getPlayer().getName() + " [" + Star + "성] : " + text.replaceAll("~", "")));
                    } else if (c.getPlayer().getHope() != "장례희망 없음") {
                        WorldBroadcasting.broadcastMessage(MainPacketCreator.getGMText(12, "[전체] < " + c.getPlayer().getHope() +  " > " + c.getPlayer().getName() + " [" + Star + "성] : " + text.replaceAll("~", "")));
                    } else {
                        WorldBroadcasting.broadcastMessage(MainPacketCreator.getGMText(12, "[전체]< " + c.getPlayer().getHope() +  " > " + c.getPlayer().getName() + " [" + Star + "성] : " + text.replaceAll("~", "")));
                        AdminToolServer.broadcastMessage(AdminToolPacket.sendChatText("[전체]< " + c.getPlayer().getHope() +  " > " + c.getPlayer().getName() + " [" + Star + "성] : " + text.replaceAll("~", "")));
                    }
                    ControlUnit.ChatList.addElement("[전체]" + c.getPlayer().getName() + " : " + text.substring(1).toString());
                    ControlUnit.Chat.setModel(ControlUnit.ChatList);
                }
            } else {
                if (ServerConstants.chatlimit >= 500) {
                    ServerConstants.chatlimit = 0;
                    ControlUnit.ChatList.clear();
                    ControlUnit.Chat.setModel(ControlUnit.ChatList);
                }
                ServerConstants.chatlimit++;

                StringBuilder sb = new StringBuilder();
                InventoryHandler.addMedalString(c.getPlayer(), sb);
                sb.append(text.substring(0));
                if (c.getPlayer().haveItem(1142113, 1, true, false)) {
                    chr.getMap().broadcastMessage(MainPacketCreator.getChatText(chr.getId(), sb.toString(), c.getPlayer().isGM(), unk), c.getPlayer().getPosition());
                } else {
                    AdminToolServer.broadcastMessage(AdminToolPacket.sendChatText("[일반][Ch." + chr.getClient().getChannel() + "]" + chr.getName() + " : " + text));
                    chr.getMap().broadcastMessage(MainPacketCreator.getChatText(chr.getId(), text, c.getPlayer().isGM(), unk), c.getPlayer().getPosition());
                }
                ControlUnit.ChatList.addElement("[일반][Ch." + chr.getClient().getChannel() + "]" + chr.getName() + " : " + text);
                ControlUnit.Chat.setModel(ControlUnit.ChatList);
            }
            LoggerChatting.writeLog(LoggerChatting.chatLog, LoggerChatting.getChatLogType("일반 채팅 : ", chr, text));
        }
    }

    public static void Others(ReadingMaple rh, MapleClient c, MapleCharacter chr) {
        int type = rh.readByte();
        byte numRecipients = rh.readByte();
        int recipients[] = new int[numRecipients];

        for (byte i = 0; i < numRecipients; i++) {
            recipients[i] = rh.readInt();
        }
        
        if (WorldCommunity.isFreeze) {
            c.getPlayer().dropMessage(1, "채팅이 얼려져있는 상태이므로\r\n채팅을 할 수 없습니다.");
            return;
        }
        if (c.getPlayer().getChatban().equals("true")) {
            c.getPlayer().dropMessage(1, "채팅 금지 상태에선 채팅을 할 수 없습니다.");
            return;
        }
        
        String chattext = rh.readMapleAsciiString();
        if (!CommandProcessor.getInstance().processCommand(c, chattext)) {
            switch (type) {
                case 0:
                    ServerConstants.chatlimit++;
                    AdminToolServer.broadcastMessage(AdminToolPacket.sendChatText("[친구][Ch." + chr.getClient().getChannel() + "]" + chr.getName() + " : " + chattext));
                    WorldCommunity.buddyChat(recipients, chr.getId(), chr.getName(), chattext);
                    LoggerChatting.writeLog(LoggerChatting.chatLog, LoggerChatting.getChatLogType("친구 : ", chr, chattext));
                    break;
                case 1:
                    if (ServerConstants.chatlimit >= 500) {
                        ServerConstants.chatlimit = 0;
                        ControlUnit.PartyChatList.clear();
                        ControlUnit.party.setModel(ControlUnit.PartyChatList);
                    }
                    ServerConstants.chatlimit++;
                    AdminToolServer.broadcastMessage(AdminToolPacket.sendChatText("[파티][Ch." + chr.getClient().getChannel() + "]" + chr.getName() + " : " + chattext));
                    WorldCommunity.partyChat(chr.getParty(), chattext, chr.getName());
                    ControlUnit.PartyChatList.addElement("[파티][Ch." + chr.getClient().getChannel() + "]" + chr.getName() + " : " + chattext);
                    ControlUnit.party.setModel(ControlUnit.PartyChatList);
                    LoggerChatting.writeLog(LoggerChatting.chatLog, LoggerChatting.getChatLogType("파티 : ", chr, chattext));
                    break;
                case 2:
                    ServerConstants.chatlimit++;
                    AdminToolServer.broadcastMessage(AdminToolPacket.sendChatText("[길드][Ch." + chr.getClient().getChannel() + "]" + chr.getName() + " : " + chattext));
                    ChannelServer.guildChat(chr.getGuildId(), chr.getName(), chr.getId(), chattext);
                    LoggerChatting.writeLog(LoggerChatting.chatLog, LoggerChatting.getChatLogType("길드 : ", chr, chattext));
                    break;
                case 3:
                    ServerConstants.chatlimit++;
                    AdminToolServer.broadcastMessage(AdminToolPacket.sendChatText("[연합][Ch." + chr.getClient().getChannel() + "]" + chr.getName() + " : " + chattext));
                    WorldCommunity.allianceChat(chr.getGuildId(), chr.getName(), chr.getId(), chattext);
                    LoggerChatting.writeLog(LoggerChatting.chatLog, LoggerChatting.getChatLogType("연합 : ", chr, chattext));
                    break;
                case 4:
                    ServerConstants.chatlimit++;
                    AdminToolServer.broadcastMessage(AdminToolPacket.sendChatText("[원정대][Ch." + chr.getClient().getChannel() + "]" + chr.getName() + " : " + chattext));
                    chr.getParty().getExpedition().broadcastMessage(chr, MainPacketCreator.multiChat(chr.getName(), chattext, 4));
                    LoggerChatting.writeLog(LoggerChatting.chatLog, LoggerChatting.getChatLogType("원정대 : ", chr, chattext));
                    break;
            }
        }
    }

    public static void Messenger(ReadingMaple slea, MapleClient c) {
        String name;
        String input;
        MapleMultiChat messenger = c.getPlayer().getMessenger();
        
        if (WorldCommunity.isFreeze) {
            c.getPlayer().dropMessage(1, "채팅이 얼려져있는 상태이므로\r\n채팅을 할 수 없습니다.");
            return;
        }
        if (c.getPlayer().getChatban().equals("true")) {
            c.getPlayer().dropMessage(1, "채팅 금지 상태에선 채팅을 할 수 없습니다.");
            return;
        }

        switch (slea.readByte()) {
            case 0x00:
                if (messenger == null) {
                    slea.skip(1);
                    byte player = slea.readByte();
                    int messengerid = slea.readInt();
                    if (messengerid == 0) {
                        MapleMultiChatCharacter messengerplayer = new MapleMultiChatCharacter(c.getPlayer());
                        messenger = WorldCommunity.createMessenger(messengerplayer);
                        c.getPlayer().setMessenger(messenger);
                        c.getPlayer().setMessengerPosition(0);
                    } else {
                        messenger = WorldCommunity.getMessenger(messengerid);
                        int position = messenger.getLowestPosition();
                        MapleMultiChatCharacter messengerplayer = new MapleMultiChatCharacter(c.getPlayer(), position);
                        messenger.addMember(messengerplayer);
                        if (messenger != null) {
                            if (messenger.getMembers().size() < player) {
                                c.getPlayer().setMessenger(messenger);
                                c.getPlayer().setMessengerPosition(position);
                                WorldCommunity.joinMessenger(messenger.getId(), messengerplayer, c.getPlayer().getName(), messengerplayer.getChannel());
                            } else {
                                c.getPlayer().dropMessage(5, "이미 해당 메신저는 최대 인원 입니다.");
                            }
                        }
                    }
                } else {
                    c.getPlayer().dropMessage(1, "이미 닫힌 방입니다.");
                }
                break;
            case 0x02:
                if (messenger != null) {
                    MapleMultiChatCharacter messengerplayer = new MapleMultiChatCharacter(c.getPlayer());
                    WorldCommunity.leaveMessenger(messenger.getId(), messengerplayer);
                    c.getPlayer().setMessenger(null);
                    c.getPlayer().setMessengerPosition(4);
                }
                break;
            case 0x03: {
                if (messenger.getMembers().size() < 6) {
                    input = slea.readMapleAsciiString();
                    MapleCharacter target = null;
                    for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                        target = cserv.getPlayerStorage().getCharacterByName(input);
                        if (target != null) {
                            break;
                        }
                    }
                    if (target != null) {
                        if (target.getMessenger() == null) {
                            if (!c.getPlayer().isGM() && target.isGM()) {
                                c.getSession().writeAndFlush(MainPacketCreator.messengerNote(input, 4, 0));
                                return;
                            }
                            target.getClient().getSession().writeAndFlush(MainPacketCreator.messengerInvite(c.getPlayer().getName(), messenger.getId()));
                            c.getSession().writeAndFlush(MainPacketCreator.messengerNote(input, 4, 1));
                        } else {
                            c.getSession().writeAndFlush(MainPacketCreator.messengerChat(c.getPlayer().getName(), c.getPlayer().getName() + " : " + input + " 님은 이미 메신저를 사용하는 중입니다."));
                        }
                    } else {
                        c.getSession().writeAndFlush(MainPacketCreator.messengerNote(input, 4, 0));
                    }
                    break;
                }
            }
            case 0x05:
                String targeted = slea.readMapleAsciiString();
                MapleCharacter target = null;
                for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                    target = cserv.getPlayerStorage().getCharacterByName(targeted);
                    if (target != null) {
                        break;
                    }
                }
                if (target != null) {
                    if (target.getMessenger() != null) {
                        target.getClient().getSession().writeAndFlush(MainPacketCreator.messengerNote(c.getPlayer().getName(), 5, 0));
                    }
                }
                break;
            case 0x06:
                if (messenger != null) {
                    name = slea.readMapleAsciiString();
                    input = slea.readMapleAsciiString();
                    WorldCommunity.messengerChat(messenger.getId(), input, name);
                }
                break;
        }
    }

    public static void Whisper_Find(ReadingMaple rh, MapleClient c) {
        byte mode = rh.readByte();
        rh.skip(4);
        boolean friend = false;
        
        if (WorldCommunity.isFreeze) {
            c.getPlayer().dropMessage(1, "채팅이 얼려져있는 상태이므로\r\n채팅을 할 수 없습니다.");
            return;
        }
        if (c.getPlayer().getChatban().equals("true")) {
            c.getPlayer().dropMessage(1, "채팅 금지 상태에선 채팅을 할 수 없습니다.");
            return;
        }
        
        switch (mode) {
            case 0x44:
                friend = true;
            case 5: {
                String recipient = rh.readMapleAsciiString();
                MapleCharacter player = c.getChannelServer().getPlayerStorage().getCharacterByName(recipient);
                if (player != null) {
                    if (!player.isGM() || (c.getPlayer().isGM() && player.isGM())) {
                        if (CashShopServer.getInstance().isCharacterInCS(recipient)) {
                            c.getSession().writeAndFlush(MainPacketCreator.getFindReplyWithCS(recipient, friend));
                        } else {
                            c.getSession().writeAndFlush(MainPacketCreator.getFindReplyWithMap(recipient, friend, player.getMap().getId()));
                        }
                    } else {
                        c.getSession().writeAndFlush(MainPacketCreator.getWhisperReply(recipient, (byte) 0));
                    }
                } else {
                    for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                        player = cserv.getPlayerStorage().getCharacterByName(recipient);
                        if (player != null) {
                            break;
                        }
                    }
                    if (CashShopServer.getInstance().getPlayerStorage().isCharacterConnected(recipient)) {
                        c.getSession().writeAndFlush(MainPacketCreator.getFindReplyWithCS(recipient, friend));
                        return;
                    } else if (player != null) {
                        c.send(MainPacketCreator.getFindReply(recipient, friend, player.getClient().getChannel()));
                    } else {
                        c.send(MainPacketCreator.getWhisperReply(recipient, (byte) 0));
                    }
                }
                break;
            }
            case 6: {
                String recipient = rh.readMapleAsciiString();
                String text = rh.readMapleAsciiString();
                ServerConstants.chatlimit++;
                AdminToolServer.broadcastMessage(AdminToolPacket.sendChatText("[귓속말][Ch." + c.getChannel() + "]" + c.getPlayer().getName() + " : " + text));
                if (!CommandProcessor.getInstance().processCommand(c, text)) {
                    MapleCharacter player = c.getChannelServer().getPlayerStorage().getCharacterByName(recipient);
                    if (player != null) {
                        if (player.isGM() && !c.getPlayer().isGM()) {
                            c.getSession().writeAndFlush(MainPacketCreator.getWhisperReply(recipient, (byte) 0));
                        } else {
                            player.getClient().getSession().writeAndFlush(MainPacketCreator.getWhisper(c.getPlayer().getName(), c.getChannel(), text));
                            c.getSession().writeAndFlush(MainPacketCreator.getWhisperReply(recipient, (byte) 1));
                            LoggerChatting.writeLog(LoggerChatting.chatLog, LoggerChatting.getChatLogType("귓", c.getPlayer(), "[대상 : " + player.getName() + "] : " + text));
                        }
                    } else {
                        Collection<ChannelServer> cservs = ChannelServer.getAllInstances();
                        for (ChannelServer cserv : cservs) {
                            player = cserv.getPlayerStorage().getCharacterByName(recipient);
                            if (player != null) {
                                break;
                            }
                        }
                        if (player != null) {
                            if (!c.getPlayer().isGM() && player.isGM()) {
                                c.getSession().writeAndFlush(MainPacketCreator.getWhisperReply(recipient, (byte) 0));
                            } else {
                                player.getClient().getSession().writeAndFlush(MainPacketCreator.getWhisper(c.getPlayer().getName(), c.getChannel(), text));
                                LoggerChatting.writeLog(LoggerChatting.chatLog, LoggerChatting.getChatLogType("귓", c.getPlayer(), "[대상 : " + player.getName() + "] : " + text));
                                c.getSession().writeAndFlush(MainPacketCreator.getWhisperReply(recipient, (byte) 1));
                            }
                        } else {
                            c.getSession().writeAndFlush(MainPacketCreator.getWhisperReply(recipient, (byte) 0));
                        }
                    }
                }
                break;
            }
        }
    }
}
