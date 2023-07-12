package snownee.jade.addon.appliedenergistics2;

import java.util.List;

import appeng.api.parts.IPart;
import appeng.api.parts.IPartHost;
import appeng.parts.networking.IUsedChannelProvider;
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
import snownee.jade.JadePlugin;

public class ChannelProvider implements IComponentProvider, IServerDataProvider<TileEntity> {
    public static final ChannelProvider INSTANCE = new ChannelProvider();

    @Override
    public void appendBody(List<ITextComponent> tooltip, IDataAccessor accessor, IPluginConfig config) {
        if (!config.get(JadePlugin.AE2_CHANNELS) || !ModList.get().isLoaded("appliedenergistics2") || accessor.getTileEntity()==null || !(accessor.getTileEntity() instanceof IPartHost)) return;

        CompoundNBT tag = accessor.getServerData();
        if(tag.contains("usedChannels") && tag.contains("maxChannels")) tooltip.add(new TranslationTextComponent("jade.ae2_channels", tag.getInt("usedChannels"), tag.getInt("maxChannels"))); // TODO: fix
    }

    @Override
	public void appendServerData(CompoundNBT tag, ServerPlayerEntity player, World world, TileEntity te) {
        if (!ModList.get().isLoaded("appliedenergistics2") || !(te instanceof IPartHost)) return;
        BlockPos pos = te.getPos();
        IPart part = ((IPartHost)te).selectPart(player.pick(20, 0, false).getHitVec().add(-pos.getX(), -pos.getY(), -pos.getZ())).part;

        if (!(part instanceof IUsedChannelProvider)) return;
        IUsedChannelProvider partInfo = (IUsedChannelProvider)part;

        tag.putInt("usedChannels", partInfo.getUsedChannelsInfo());
        tag.putInt("maxChannels", partInfo.getMaxChannelsInfo());
    }
}
