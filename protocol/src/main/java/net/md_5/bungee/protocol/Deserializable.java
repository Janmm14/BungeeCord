package net.md_5.bungee.protocol;

/**
 * Represents a value that can be deserialized from another value if needed.
 * @param <OV> the original value
 * @param <D> the deserialized value
 */
public interface Deserializable<OV, D>
{
    D get();

    /**
     * should not be used if {@link #hasDeserialized()} returns true
     * @return the original value
     */
    OV original();

    boolean hasDeserialized();
}
