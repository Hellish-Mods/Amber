package mcp.mobius.waila.overlay;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import mcp.mobius.waila.utils.WailaExceptionHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class DisplayUtil {

	private static final String[] NUM_SUFFIXES = new String[] { "", "k", "m", "b", "t" };
	private static final int MAX_LENGTH = 4;
	private static final Minecraft CLIENT = Minecraft.getInstance();

	public static void renderStack(MatrixStack matrixStack, int x, int y, ItemStack stack, float scale) {
		matrixStack.pushPose();
		enable3DRender();
		try {
			//MatrixStack matrixStack = new MatrixStack();
			//scale *= Waila.CONFIG.get().getOverlay().getOverlayScale();
			if (scale != 1)
				matrixStack.scale(scale, scale, scale);
			renderItemIntoGUI(stack, x, y, scale);
			ItemStack overlayRender = stack.copy();
			overlayRender.setCount(1);
			CLIENT.getItemRenderer().renderGuiItemDecorations(CLIENT.font, overlayRender, x, y, null);
			renderStackSize(matrixStack, CLIENT.font, stack, x, y);
		} catch (Exception e) {
			String stackStr = stack != null ? stack.toString() : "NullStack";
			WailaExceptionHandler.handleErr(e, "renderStack | " + stackStr, null);
		}
		enable2DRender();
		matrixStack.popPose();
	}

	public static void renderItemIntoGUI(ItemStack stack, int x, int y, float scale) {
		ItemRenderer renderer = CLIENT.getItemRenderer();
		renderItemModelIntoGUI(stack, x, y, renderer.getModel(stack, (World) null, (LivingEntity) null), scale);
	}

	@SuppressWarnings("deprecation")
	protected static void renderItemModelIntoGUI(ItemStack stack, int x, int y, IBakedModel bakedmodel, float scale) {
		ItemRenderer renderer = CLIENT.getItemRenderer();
		TextureManager textureManager = CLIENT.textureManager;
		RenderSystem.pushMatrix();
		textureManager.bind(AtlasTexture.LOCATION_BLOCKS);
		textureManager.getTexture(AtlasTexture.LOCATION_BLOCKS).setFilter(false, false);
		RenderSystem.enableRescaleNormal();
		RenderSystem.enableAlphaTest();
		RenderSystem.defaultAlphaFunc();
		RenderSystem.enableBlend();
		RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.translatef((float) x, (float) y, 100.0F + renderer.blitOffset);
		RenderSystem.translatef(8.0F * scale, 8.0F * scale, 0.0F);
		RenderSystem.scalef(scale, -scale, scale);
		RenderSystem.scalef(16.0F, 16.0F, 16.0F);
		MatrixStack matrixstack = new MatrixStack();
		IRenderTypeBuffer.Impl irendertypebuffer$impl = Minecraft.getInstance().renderBuffers().bufferSource();
		boolean flag = !bakedmodel.usesBlockLight();
		if (flag) {
			RenderHelper.setupForFlatItems();
		}

		renderer.render(stack, ItemCameraTransforms.TransformType.GUI, false, matrixstack, irendertypebuffer$impl, 15728880, OverlayTexture.NO_OVERLAY, bakedmodel);
		irendertypebuffer$impl.endBatch();
		RenderSystem.enableDepthTest();
		if (flag) {
			RenderHelper.setupFor3DItems();
		}

		RenderSystem.disableAlphaTest();
		RenderSystem.disableRescaleNormal();
		RenderSystem.popMatrix();
	}

	@SuppressWarnings("deprecation")
	public static void renderStackSize(MatrixStack matrixStack, FontRenderer fr, ItemStack stack, int xPosition, int yPosition) {
		if (!stack.isEmpty() && stack.getCount() != 1) {
			String s = shortHandNumber(stack.getCount());

			if (stack.getCount() < 1)
				s = TextFormatting.RED + String.valueOf(stack.getCount());

			Minecraft mc = Minecraft.getInstance();
			RenderSystem.disableLighting();
			RenderSystem.disableDepthTest();
			RenderSystem.disableBlend();
			matrixStack.pushPose();
			matrixStack.translate(0, 0, mc.getItemRenderer().blitOffset + 200F);
			fr.drawShadow(matrixStack, s, xPosition + 19 - 2 - fr.width(s), yPosition + 6 + 3, 16777215);
			matrixStack.popPose();
			RenderSystem.enableLighting();
			RenderSystem.enableDepthTest();
			RenderSystem.enableBlend();
		}
	}

	private static String shortHandNumber(Number number) {
		String shorthand = new DecimalFormat("##0E0").format(number);
		shorthand = shorthand.replaceAll("E[0-9]", NUM_SUFFIXES[Character.getNumericValue(shorthand.charAt(shorthand.length() - 1)) / 3]);
		while (shorthand.length() > MAX_LENGTH || shorthand.matches("[0-9]+\\.[a-z]"))
			shorthand = shorthand.substring(0, shorthand.length() - 2) + shorthand.substring(shorthand.length() - 1);

		return shorthand;
	}

	@SuppressWarnings("deprecation")
	public static void enable3DRender() {
		RenderSystem.enableLighting();
		RenderSystem.enableDepthTest();
	}

	@SuppressWarnings("deprecation")
	public static void enable2DRender() {
		RenderSystem.disableLighting();
		RenderSystem.disableDepthTest();
	}

	@SuppressWarnings("deprecation")
	public static void drawGradientRect(MatrixStack matrixStack, int left, int top, int right, int bottom, int startColor, int endColor) {
		float zLevel = 0.0F;
		Matrix4f matrix = matrixStack.last().pose();

		float f = (startColor >> 24 & 255) / 255.0F;
		float f1 = (startColor >> 16 & 255) / 255.0F;
		float f2 = (startColor >> 8 & 255) / 255.0F;
		float f3 = (startColor & 255) / 255.0F;
		float f4 = (endColor >> 24 & 255) / 255.0F;
		float f5 = (endColor >> 16 & 255) / 255.0F;
		float f6 = (endColor >> 8 & 255) / 255.0F;
		float f7 = (endColor & 255) / 255.0F;
		RenderSystem.disableTexture();
		RenderSystem.enableBlend();
		RenderSystem.disableAlphaTest();
		RenderSystem.blendFuncSeparate(770, 771, 1, 0);
		RenderSystem.shadeModel(7425);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuilder();
		buffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
		buffer.vertex(matrix, left + right, top, zLevel).color(f1, f2, f3, f).endVertex();
		buffer.vertex(matrix, left, top, zLevel).color(f1, f2, f3, f).endVertex();
		buffer.vertex(matrix, left, top + bottom, zLevel).color(f5, f6, f7, f4).endVertex();
		buffer.vertex(matrix, left + right, top + bottom, zLevel).color(f5, f6, f7, f4).endVertex();
		tessellator.end();
		RenderSystem.shadeModel(7424);
		RenderSystem.disableBlend();
		RenderSystem.enableAlphaTest();
		RenderSystem.enableTexture();
	}

	public static void drawTexturedModalRect(MatrixStack matrixStack, int x, int y, int textureX, int textureY, int width, int height, int tw, int th) {
		Matrix4f matrix = matrixStack.last().pose();
		float f = 0.00390625F;
		float f1 = 0.00390625F;
		float zLevel = 0.0F;
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuilder();
		buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
		buffer.vertex(matrix, x, y + height, zLevel).uv(((textureX) * f), ((textureY + th) * f1)).endVertex();
		buffer.vertex(matrix, x + width, y + height, zLevel).uv(((textureX + tw) * f), ((textureY + th) * f1)).endVertex();
		buffer.vertex(matrix, x + width, y, zLevel).uv(((textureX + tw) * f), ((textureY) * f1)).endVertex();
		buffer.vertex(matrix, x, y, zLevel).uv(((textureX) * f), ((textureY) * f1)).endVertex();
		tessellator.end();
	}

	public static List<ITextComponent> itemDisplayNameMultiline(ItemStack itemstack) {
		List<ITextComponent> namelist = null;
		try {
			namelist = itemstack.getTooltipLines(CLIENT.player, ITooltipFlag.TooltipFlags.NORMAL);
		} catch (Throwable ignored) {
		}

		if (namelist == null)
			namelist = new ArrayList<>();

		if (namelist.isEmpty())
			namelist.add(new StringTextComponent("Unnamed"));

		namelist.set(0, new StringTextComponent(itemstack.getRarity().color.toString() + namelist.get(0)));
		for (int i = 1; i < namelist.size(); i++)
			namelist.set(i, namelist.get(i));

		return namelist;
	}

	@SuppressWarnings("deprecation")
	public static void renderIcon(MatrixStack matrixStack, int x, int y, int sx, int sy, IconUI icon) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		CLIENT.getTextureManager().bind(AbstractGui.GUI_ICONS_LOCATION);

		if (icon == null)
			return;

		RenderSystem.enableAlphaTest();
		if (icon.bu != -1)
			DisplayUtil.drawTexturedModalRect(matrixStack, x, y, icon.bu, icon.bv, sx, sy, icon.bsu, icon.bsv);
		DisplayUtil.drawTexturedModalRect(matrixStack, x, y, icon.u, icon.v, sx, sy, icon.su, icon.sv);
		RenderSystem.disableAlphaTest();
	}
}
