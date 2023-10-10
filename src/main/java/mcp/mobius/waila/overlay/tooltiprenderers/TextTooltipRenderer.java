package mcp.mobius.waila.overlay.tooltiprenderers;

import java.awt.Dimension;

import com.mojang.blaze3d.matrix.MatrixStack;

import mcp.mobius.waila.Waila;
import mcp.mobius.waila.api.ICommonAccessor;
import mcp.mobius.waila.api.ITooltipRenderer;
import mcp.mobius.waila.api.impl.config.WailaConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TextTooltipRenderer implements ITooltipRenderer {
	@Override
	public Dimension getSize(CompoundNBT tag, ICommonAccessor accessor) {
		ITextComponent component = ITextComponent.Serializer.fromJson(tag.getString("text"));
		if (component == null) {
			return new Dimension();
		}
		FontRenderer fontRenderer = Minecraft.getInstance().font;
		return new Dimension(fontRenderer.width(component.getString()), fontRenderer.lineHeight + 1);
	}

	@Override
	public void draw(CompoundNBT tag, ICommonAccessor accessor, int x, int y) {
		ITextComponent component = ITextComponent.Serializer.fromJson(tag.getString("text"));
		if (component == null) {
			return;
		}
		FontRenderer fontRenderer = Minecraft.getInstance().font;
		WailaConfig.ConfigOverlay.ConfigOverlayColor color = Waila.CONFIG.get().getOverlay().getColor();
		IRenderTypeBuffer.Impl irendertypebuffer$impl = IRenderTypeBuffer.immediate(Tessellator.getInstance().getBuilder());
		fontRenderer.drawInBatch(component.getVisualOrderText(), x, y, color.getFontColor(), true, new MatrixStack().last().pose(), irendertypebuffer$impl, false, 0, 15728880);
		irendertypebuffer$impl.endBatch();
	}

}
