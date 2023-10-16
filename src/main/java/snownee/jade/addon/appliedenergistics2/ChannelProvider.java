package snownee.jade.addon.appliedenergistics2;

import java.util.List;

import appeng.api.networking.GridFlags;
import appeng.api.networking.IGridConnection;
import appeng.api.networking.IGridNode;
import appeng.api.parts.IPart;
import appeng.api.parts.IPartHost;
import appeng.parts.networking.IUsedChannelProvider;
import mcp.mobius.waila.Waila;
import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerDataProvider;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.ModList;
import snownee.jade.AmberPlugin;

public class ChannelProvider implements IComponentProvider, IServerDataProvider<TileEntity> {
    public static final ChannelProvider INSTANCE = new ChannelProvider();

    @Override
    public void appendBody(List<ITextComponent> tooltip, IDataAccessor accessor, IPluginConfig config) {
        if (!config.get(AmberPlugin.AE2_CHANNELS) || !ModList.get().isLoaded("appliedenergistics2") || accessor.getTileEntity()==null || !(accessor.getTileEntity() instanceof IPartHost)) return;

        CompoundNBT tag = accessor.getServerData();
        if(tag.contains("usedChannels") && tag.contains("maxChannels")) tooltip.add(new TranslationTextComponent("amber.ae2_channels", tag.getInt("usedChannels"), tag.getInt("maxChannels")));
    }

    @Override
	public void appendServerData(CompoundNBT tag, ServerPlayerEntity player, World world, TileEntity te) {
        if (!ModList.get().isLoaded("appliedenergistics2") || !(te instanceof IPartHost)) return;
        BlockPos pos = te.getBlockPos();

        IPart part = ((IPartHost)te).selectPart(player.pick(20, 0, false).getLocation().add(-pos.getX(), -pos.getY(), -pos.getZ())).part;
        if (part==null) return;

        if (Waila.CONFIG.get().getGeneral().getOnlyShowSmartNodes() && !(part instanceof IUsedChannelProvider)) return;

        IGridNode gridNode = part.getGridNode();
        if (gridNode==null) return;

        int usedChannels = 0;
        if (gridNode.isActive()) {
            for (IGridConnection c : gridNode.getConnections()) usedChannels = Math.max(c.getUsedChannels(), usedChannels);
        }
        int maxChannels = gridNode.getGridBlock().getFlags().contains(GridFlags.DENSE_CAPACITY) ? 32 : 8;

        tag.putInt("usedChannels", usedChannels);
        tag.putInt("maxChannels", maxChannels);
    }
}
