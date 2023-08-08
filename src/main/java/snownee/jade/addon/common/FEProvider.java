package snownee.jade.addon.common;

import java.util.List;

import mcjty.lib.api.power.IBigPower;
import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerDataProvider;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.ModList;
import snownee.jade.AmberPlugin;

public class FEProvider implements IComponentProvider, IServerDataProvider<TileEntity> {
    public static final FEProvider INSTANCE = new FEProvider();

    @Override
    public void appendBody(List<ITextComponent> tooltip, IDataAccessor accessor, IPluginConfig config) {
        if (!config.get(AmberPlugin.FORGE_ENERGY) || accessor.getBlock().asItem().getRegistryName().getNamespace().equals("mekanism") || accessor.getTileEntity()==null || !accessor.getTileEntity().getCapability(CapabilityEnergy.ENERGY).isPresent()) return;

        CompoundNBT tag = accessor.getServerData();
        if(tag.contains("storedEnergy") && tag.contains("maxEnergy")) tooltip.add(new TranslationTextComponent("amber.fe", tag.getLong("storedEnergy"), tag.getLong("maxEnergy")));
    }

    @Override
	public void appendServerData(CompoundNBT tag, ServerPlayerEntity player, World world, TileEntity te) {
        Long storedEnergy = null;
        Long maxEnergy = null;

		if (ModList.get().isLoaded("mcjtylib") && te instanceof IBigPower) {
            IBigPower energy = (IBigPower)te;
            storedEnergy = energy.getStoredPower();
            maxEnergy = energy.getCapacity();
        }
        else {
            IEnergyStorage energy = te.getCapability(CapabilityEnergy.ENERGY).orElse(null);
            if (energy!=null) {
                storedEnergy = Long.valueOf(energy.getEnergyStored());
                maxEnergy = Long.valueOf(energy.getMaxEnergyStored());
            }
        }

        if (storedEnergy!=null && maxEnergy!=null) {
            tag.putLong("storedEnergy", storedEnergy);
            tag.putLong("maxEnergy", maxEnergy);
        }
	}
}
