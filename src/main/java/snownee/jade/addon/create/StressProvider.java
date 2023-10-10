package snownee.jade.addon.create;

import java.util.List;

import com.simibubi.create.content.contraptions.base.KineticTileEntity;

import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IPluginConfig;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.ModList;
import snownee.jade.AmberPlugin;

public class StressProvider implements IComponentProvider {
    public static final StressProvider INSTANCE = new StressProvider();

    @Override
    public void appendBody(List<ITextComponent> tooltip, IDataAccessor accessor, IPluginConfig config) {
        if (!config.get(AmberPlugin.CREATE_STRESS) || !ModList.get().isLoaded("create") || accessor.getTileEntity()==null || !(accessor.getTileEntity() instanceof KineticTileEntity)) return;

        KineticTileEntity kineticTile = (KineticTileEntity)accessor.getTileEntity();
        tooltip.add(new TranslationTextComponent("amber.create_stress", (kineticTile.getOrCreateNetwork().calculateStress()/kineticTile.getOrCreateNetwork().calculateCapacity())*100));
        if (kineticTile.isOverStressed()) tooltip.add(new TranslationTextComponent("create.tooltip.stressImpact.overstressed").withStyle(TextFormatting.RED));
    }
}
