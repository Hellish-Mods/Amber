package snownee.jade.addon.create;

import java.util.List;

import com.simibubi.create.content.contraptions.base.KineticTileEntity;

import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IPluginConfig;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.ModList;
import snownee.jade.JadePlugin;

public class SpeedProvider implements IComponentProvider {
    public static final SpeedProvider INSTANCE = new SpeedProvider();

    @Override
    public void appendBody(List<ITextComponent> tooltip, IDataAccessor accessor, IPluginConfig config) {
        if (!config.get(JadePlugin.CREATE_SPEED) || !ModList.get().isLoaded("create") || accessor.getTileEntity()==null || !(accessor.getTileEntity() instanceof KineticTileEntity)) return;

        tooltip.add(new TranslationTextComponent("jade.create_speed", ((KineticTileEntity)accessor.getTileEntity()).getSpeed()));
    }
}
