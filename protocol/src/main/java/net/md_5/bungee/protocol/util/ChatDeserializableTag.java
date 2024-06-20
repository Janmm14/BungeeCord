package net.md_5.bungee.protocol.util;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.VersionedComponentSerializer;
import net.md_5.bungee.protocol.ChatSerializer;
import org.jetbrains.annotations.NotNull;
import se.llbit.nbt.SpecificTag;

@ToString
@EqualsAndHashCode(callSuper = true)
public class ChatDeserializableTag extends ChatCapturingDeserializable
{
    private final @NonNull VersionedComponentSerializer serializer;

    public ChatDeserializableTag(int protocolVersion, @NonNull SpecificTag chatTag)
    {
        this( ChatSerializer.forVersion( protocolVersion ), chatTag );
    }

    public ChatDeserializableTag(@NonNull VersionedComponentSerializer serializer, @NonNull SpecificTag chatTag)
    {
        super( Either.right( chatTag ) );
        this.serializer = serializer;
    }

    @NotNull
    @Override
    public BaseComponent deserialize()
    {
        return serializer.deserialize( TagUtil.toJson( original().getRight() ) );
    }
}
