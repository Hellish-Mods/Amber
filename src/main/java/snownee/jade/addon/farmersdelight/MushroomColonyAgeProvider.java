package snownee.jade.addon.farmersdelight;

import java.util.List;

import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IPluginConfig;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.ModList;
import snownee.jade.JadePlugin;
import vectorwing.farmersdelight.blocks.MushroomColonyBlock;

public class MushroomColonyAgeProvider implements IComponentProvider {
    public static final MushroomColonyAgeProvider INSTANCE = new MushroomColonyAgeProvider();

    @Override
    public void appendBody(List<ITextComponent> tooltip, IDataAccessor accessor, IPluginConfig config) {
        if (!config.get(JadePlugin.MUSHROOM_COLONY) || !ModList.get().isLoaded("farmersdelight") || !(accessor.getBlock() instanceof MushroomColonyBlock)) return;

        tooltip.add(new TranslationTextComponent("jade.mushroom_colony", accessor.getBlockState().get(((MushroomColonyBlock)accessor.getBlock()).getAgeProperty()), ((MushroomColonyBlock)accessor.getBlock()).getMaxAge()));
    }
}
