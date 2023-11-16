package snownee.jade.addon.create;

import java.util.List;

import com.simibubi.create.content.contraptions.base.KineticTileEntity;
import com.simibubi.create.content.contraptions.goggles.GogglesItem;

import mcp.mobius.waila.Waila;
import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IPluginConfig;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.ModList;
import snownee.jade.AmberPlugin;

public class SpeedProvider implements IComponentProvider {
    public static final SpeedProvider INSTANCE = new SpeedProvider();

    @Override
    public void appendBody(List<ITextComponent> tooltip, IDataAccessor accessor, IPluginConfig config) {
        if (!config.get(AmberPlugin.CREATE_SPEED) || !ModList.get().isLoaded("create") || accessor.getTileEntity()==null || !(accessor.getTileEntity() instanceof KineticTileEntity) || (Waila.CONFIG.get().getGeneral().areGogglesRequired() && !GogglesItem.canSeeParticles(accessor.getPlayer()))) return;

        tooltip.add(new TranslationTextComponent("amber.create_speed", Math.abs(((KineticTileEntity)accessor.getTileEntity()).getSpeed())));
    }
}
