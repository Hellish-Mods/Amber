package mcp.mobius.waila.network;

import java.util.function.Supplier;

import mcp.mobius.waila.Waila;
import mcp.mobius.waila.api.impl.WailaRegistrar;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

public class MessageRequestEntity {

	public int entityId;

	public MessageRequestEntity(Entity entity) {
		this.entityId = entity.getId();
	}

	private MessageRequestEntity(int entityId) {
		this.entityId = entityId;
	}

	public static MessageRequestEntity read(PacketBuffer buffer) {
		return new MessageRequestEntity(buffer.readVarInt());
	}

	public static void write(MessageRequestEntity message, PacketBuffer buffer) {
		buffer.writeVarInt(message.entityId);
	}

	public static class Handler {

		public static void onMessage(final MessageRequestEntity message, Supplier<NetworkEvent.Context> context) {
			context.get().enqueueWork(() -> {
				ServerPlayerEntity player = context.get().getSender();
				World world = player.level;
				Entity entity = world.getEntity(message.entityId);

				if (entity == null || player.distanceToSqr(entity) > MessageRequestTile.MAX_DISTANCE_SQR)
					return;

				CompoundNBT tag = new CompoundNBT();
				if (WailaRegistrar.INSTANCE.hasNBTEntityProviders(entity)) {
					WailaRegistrar.INSTANCE.getNBTEntityProviders(entity).values().forEach(l -> l.forEach(p -> p.appendServerData(tag, player, world, entity)));
				} else {
					entity.saveWithoutId(tag);
				}

				tag.putInt("WailaEntityID", entity.getId());

				Waila.NETWORK.sendTo(new MessageReceiveData(tag), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
			});
			context.get().setPacketHandled(true);
		}
	}
}
