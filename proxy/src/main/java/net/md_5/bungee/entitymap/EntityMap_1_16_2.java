package net.md_5.bungee.entitymap;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.protocol.DefinedPacket;
import net.md_5.bungee.protocol.PacketWrapper;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
class EntityMap_1_16_2 extends EntityMap
{

    static final EntityMap_1_16_2 INSTANCE_1_16_2 = new EntityMap_1_16_2( 0x04, 0x2D );
    static final EntityMap_1_16_2 INSTANCE_1_17 = new EntityMap_1_16_2( 0x04, 0x2D );
    static final EntityMap_1_16_2 INSTANCE_1_18 = new EntityMap_1_16_2( 0x04, 0x2D );
    //
    private final int spawnPlayerId;
    private final int spectateId;

    @Override
    @SuppressFBWarnings("DLS_DEAD_LOCAL_STORE")
    public void rewriteClientbound(PacketWrapper wrapper, int oldId, int newId, int protocolVersion)
    {
        ByteBuf packet = wrapper.buf;

        // Special cases
        int readerIndex = packet.readerIndex();
        int packetId = DefinedPacket.readVarInt( packet );
        int packetIdLength = packet.readerIndex() - readerIndex;

        if ( packetId == spawnPlayerId )
        {
            DefinedPacket.readVarInt( packet ); // Entity ID
            int idLength = packet.readerIndex() - readerIndex - packetIdLength;
            UUID uuid = DefinedPacket.readUUID( packet );
            ProxiedPlayer player;
            if ( ( player = BungeeCord.getInstance().getPlayerByOfflineUUID( uuid ) ) != null )
            {
                wrapper.destroyCompressed();
                int previous = packet.writerIndex();
                packet.readerIndex( readerIndex );
                packet.writerIndex( readerIndex + packetIdLength + idLength );
                DefinedPacket.writeUUID( player.getUniqueId(), packet );
                packet.writerIndex( previous );
            }
        }
        packet.readerIndex( readerIndex );
    }

    @Override
    public void rewriteServerbound(PacketWrapper wrapper, int oldId, int newId)
    {
        ByteBuf packet = wrapper.buf;

        // Special cases
        int readerIndex = packet.readerIndex();
        int packetId = DefinedPacket.readVarInt( packet );
        int packetIdLength = packet.readerIndex() - readerIndex;

        if ( packetId == spectateId && !BungeeCord.getInstance().getConfig().isIpForward() )
        {
            UUID uuid = DefinedPacket.readUUID( packet );
            ProxiedPlayer player;
            if ( ( player = BungeeCord.getInstance().getPlayer( uuid ) ) != null )
            {
                wrapper.destroyCompressed();
                int previous = packet.writerIndex();
                packet.readerIndex( readerIndex );
                packet.writerIndex( readerIndex + packetIdLength );
                DefinedPacket.writeUUID( ( (UserConnection) player ).getPendingConnection().getOfflineId(), packet );
                packet.writerIndex( previous );
            }
        }
        packet.readerIndex( readerIndex );
    }
}
