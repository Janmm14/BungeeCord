package net.md_5.bungee.protocol;

import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;

public class SupplierDeserializable<OV, D> extends SimpleDeserializable<OV, D>
{
    private final Supplier<D> supplier;

    public SupplierDeserializable(OV ov, Supplier<D> supplier)
    {
        super( ov );
        this.supplier = supplier;
    }

    @NotNull
    @Override
    public D deserialize()
    {
        return supplier.get();
    }
}
