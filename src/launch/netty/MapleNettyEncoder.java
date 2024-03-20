package launch.netty;

import java.util.concurrent.locks.Lock;

import client.MapleClient;
import constants.ServerConstants;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import packet.crypto.MapleCrypto;
import tools.HexTool;

public class MapleNettyEncoder extends MessageToByteEncoder<byte[]> {

    @Override
    protected void encode(ChannelHandlerContext ctx, byte[] msg, ByteBuf buffer) throws Exception {

        final MapleClient client = ctx.channel().attr(MapleClient.CLIENTKEY).get();

        if (client != null) {
            final Lock mutex = client.getLock();

            mutex.lock();
            try {
                final MapleCrypto send_crypto = client.getSendCrypto();

                buffer.writeBytes(send_crypto.getPacketHeader(msg.length));
                buffer.writeBytes(send_crypto.crypt(msg));
                ctx.flush();
            } finally {
                mutex.unlock();
            }
            
        } else { // no client object created yet, send unencrypted (hello)
            buffer.writeBytes(msg);
            // ctx.F(buffer);
        }
        if (ServerConstants.showPackets) {
            //System.out.println(HexTool.toString(msg));
        }
    }
}
