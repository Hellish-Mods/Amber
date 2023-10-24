package snownee.jade.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import net.minecraft.entity.EntityType;
import snownee.jade.api.WailaBlacklisted;

@Mixin(EntityType.class)
public abstract class EntityTypeMixin implements WailaBlacklisted {

	@Unique private boolean amber$isInWailaBlacklist;

	@Override
	public boolean amber$isInWailaBlacklist() {
		return this.amber$isInWailaBlacklist;
	}

	@Override
	public void amber$setIsInWailaBlacklist(boolean isInWailaBlacklist) {
		this.amber$isInWailaBlacklist = isInWailaBlacklist;
	}
}
