package net.md_5.bungee.protocol;

import io.netty.buffer.ByteBuf;
import javax.annotation.Nullable;
import lombok.Setter;

public class PacketWrapper
{

    @Nullable
    public DefinedPacket packet;
    public ByteBuf buf;
    @Nullable
    public ByteBuf compressed;
    @Setter
    private boolean released;

    public PacketWrapper(@Nullable DefinedPacket packet, ByteBuf buf)
    {
        this.packet = packet;
        this.buf = buf;
    }

    public PacketWrapper(DefinedPacket packet, ByteBuf buf, ByteBuf compressed)
    {
        this.packet = packet;
        this.buf = buf;
        this.compressed = compressed;
    }

    public void trySingleRelease()
    {
        if ( !released )
        {
            buf.release();
            destroyCompressed();
            released = true;
        }
    }

    public void destroyCompressed()
    {
        if ( compressed != null )
        {
            compressed.release();
            compressed = null;
        }
    }
}
