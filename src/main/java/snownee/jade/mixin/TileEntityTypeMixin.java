package snownee.jade.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import net.minecraft.tileentity.TileEntityType;
import snownee.jade.api.ExtendedTileEntityType;

@Mixin(TileEntityType.class)
public class TileEntityTypeMixin implements ExtendedTileEntityType {
	@Unique private boolean amber$shouldShowCustomName;

	@Override
	public boolean amber$shouldShowCustomName() {
		return this.amber$shouldShowCustomName;
	}

	@Override
	public void amber$setShouldShowCustomName(boolean shouldShowCustomName) {
		this.amber$shouldShowCustomName = shouldShowCustomName;
	}
}
