package snownee.jade.addon.common;

import java.util.List;

import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IPluginConfig;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import snownee.jade.JadePlugin;

public class FEProvider implements IComponentProvider {
    public static final FEProvider INSTANCE = new FEProvider();

    @Override
    public void appendBody(List<ITextComponent> tooltip, IDataAccessor accessor, IPluginConfig config) {
        String modId = accessor.getBlock().asItem().getCreatorModId(new ItemStack(accessor.getBlock().asItem()));
        if (!config.get(JadePlugin.FORGE_ENERGY) || modId.equals("mekanism") || accessor.getTileEntity()==null || !accessor.getTileEntity().getCapability(CapabilityEnergy.ENERGY).isPresent()) return;

        IEnergyStorage energy = accessor.getTileEntity().getCapability(CapabilityEnergy.ENERGY).orElse(null);
        tooltip.add(new TranslationTextComponent("jade.fe", energy.getEnergyStored(), energy.getMaxEnergyStored()));
    }
}
