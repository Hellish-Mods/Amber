package snownee.jade;

import com.simibubi.create.content.contraptions.base.KineticBlock;

import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.TooltipPosition;
import mcp.mobius.waila.api.WailaPlugin;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.ModList;
import snownee.jade.addon.appliedenergistics2.ChannelProvider;
import snownee.jade.addon.common.FEProvider;
import snownee.jade.addon.create.SpeedProvider;
import snownee.jade.addon.create.StressProvider;
import snownee.jade.addon.farmersdelight.MushroomColonyAgeProvider;
import vectorwing.farmersdelight.blocks.MushroomColonyBlock;

@WailaPlugin
public class AmberPlugin implements IWailaPlugin {

	private static ResourceLocation RL(String path) {
		return new ResourceLocation("amber", path);
	}

	public static final ResourceLocation FORGE_ENERGY = RL("forge_energy");
	public static final ResourceLocation AE2_CHANNELS = RL("ae2_channels");
	public static final ResourceLocation CREATE_STRESS = RL("create_stress");
	public static final ResourceLocation CREATE_SPEED = RL("create_speed");
	public static final ResourceLocation MUSHROOM_COLONY = RL("fd_mushroom_colony");

	@Override
	public void register(IRegistrar registrar) {
		registrar.registerComponentProvider(FEProvider.INSTANCE, TooltipPosition.BODY, Block.class);
		registrar.registerBlockDataProvider(FEProvider.INSTANCE, Block.class);
		registrar.addConfig(FORGE_ENERGY, true);

		if (ModList.get().isLoaded("appliedenergistics2")) {
			registrar.registerComponentProvider(ChannelProvider.INSTANCE, TooltipPosition.BODY, Block.class);
			registrar.registerBlockDataProvider(ChannelProvider.INSTANCE, Block.class);
			registrar.addConfig(AE2_CHANNELS, true);
		}
		if (ModList.get().isLoaded("create")) {
			registrar.registerComponentProvider(SpeedProvider.INSTANCE, TooltipPosition.BODY, KineticBlock.class);
			registrar.addConfig(CREATE_SPEED, true);
			registrar.registerComponentProvider(StressProvider.INSTANCE, TooltipPosition.BODY, KineticBlock.class);
			registrar.registerBlockDataProvider(StressProvider.INSTANCE, KineticBlock.class);
			registrar.addConfig(CREATE_STRESS, true);
		}

		if (ModList.get().isLoaded("farmersdelight")) {
			registrar.registerComponentProvider(MushroomColonyAgeProvider.INSTANCE, TooltipPosition.BODY, MushroomColonyBlock.class);
			registrar.addConfig(MUSHROOM_COLONY, true);
		}
	}

}
