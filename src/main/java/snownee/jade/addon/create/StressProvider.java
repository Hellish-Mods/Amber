package snownee.jade.addon.create;

import java.lang.reflect.Field;
import java.util.List;

import com.simibubi.create.content.contraptions.base.KineticTileEntity;
import com.simibubi.create.content.contraptions.goggles.GogglesItem;

import mcp.mobius.waila.Waila;
import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerDataProvider;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.ModList;
import snownee.jade.AmberPlugin;

public class StressProvider implements IComponentProvider, IServerDataProvider<TileEntity> {
    public static final StressProvider INSTANCE = new StressProvider();

    @Override
    public void appendBody(List<ITextComponent> tooltip, IDataAccessor accessor, IPluginConfig config) {
        if (!config.get(AmberPlugin.CREATE_STRESS) || !ModList.get().isLoaded("create") || accessor.getTileEntity()==null || !(accessor.getTileEntity() instanceof KineticTileEntity)  || (Waila.CONFIG.get().getGeneral().areGogglesRequired() && !GogglesItem.canSeeParticles(accessor.getPlayer()))) return;

        CompoundNBT tag = accessor.getServerData();
        
        tooltip.add(new TranslationTextComponent("amber.create_stress", Math.round(tag.getFloat("stress")), Math.round(tag.getFloat("capacity")), (int)Math.floor((tag.getFloat("stress")/tag.getFloat("capacity"))*100)));
        if (tag.getBoolean("overstressed")) tooltip.add(new TranslationTextComponent("create.tooltip.stressImpact.overstressed").withStyle(TextFormatting.RED));
    }

    @Override
    public void appendServerData(CompoundNBT tag, ServerPlayerEntity player, World world, TileEntity te) {
        if (!(te instanceof KineticTileEntity)) return;
        KineticTileEntity kineticTile = (KineticTileEntity)te;

        float stress = 0f;
        float capacity = 0f;
        try {
            Field stressField = KineticTileEntity.class.getDeclaredField("stress");
            stressField.setAccessible(true);
            Field capacityField = KineticTileEntity.class.getDeclaredField("capacity");
            capacityField.setAccessible(true);
            stress = (float)stressField.get(kineticTile);
            capacity = (float)capacityField.get(kineticTile);
        } catch (Throwable e) {WailaExceptionHandler.handleErr(e, this.getClass().toString(), null);}

        tag.putFloat("stress", stress);
        tag.putFloat("capacity", capacity);
        tag.putBoolean("overstressed", kineticTile.isOverStressed());
    }
}
