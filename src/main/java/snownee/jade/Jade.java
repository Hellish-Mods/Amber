package snownee.jade;

import java.text.DecimalFormat;

import org.apache.commons.lang3.tuple.Pair;

import mcp.mobius.waila.Waila;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag.INamedTag;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import net.minecraftforge.registries.ForgeRegistries;
import snownee.jade.api.ExtendedTileEntityType;
import snownee.jade.api.WailaBlacklisted;

@Mod(Jade.MODID)
public class Jade {
	public static final String MODID = "jade";
	public static final String NAME = "Jade";
	public static DecimalFormat dfCommas = new DecimalFormat("##.##");
	public static final INamedTag<Block> PICK = BlockTags.createOptional(new ResourceLocation(MODID, "pick"));

	public Jade() {
		ModLoadingContext ctx = ModLoadingContext.get();
		IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
		ctx.registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));
		ctx.registerConfig(ModConfig.Type.COMMON, JadeCommonConfig.spec);
		modBus.register(JadeCommonConfig.class);
		modBus.addListener(this::init);
		modBus.addListener(this::clientSetup);
	}

	private void init(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			JadeCommonConfig.refresh();
			for (TileEntityType<?> tileEntityType : ForgeRegistries.TILE_ENTITIES) ((ExtendedTileEntityType)tileEntityType).amber$setShouldShowCustomName(JadeCommonConfig.shouldShowCustomName(tileEntityType));
		});
	}

	@SuppressWarnings("DataFlowIssue")
	private void clientSetup(FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			for (EntityType<?> entityType : ForgeRegistries.ENTITIES) ((WailaBlacklisted)entityType).amber$setIsInWailaBlacklist(entityType.canSerialize() && Waila.CONFIG.get().getGeneral().getEntityBlacklist().contains(entityType.getRegistryName().toString()));
			for (Item item : ForgeRegistries.ITEMS) ((WailaBlacklisted)item).amber$setIsInWailaBlacklist(Waila.CONFIG.get().getGeneral().getBlockBlacklist().contains(item.getRegistryName().toString()));
		});
	}
}
