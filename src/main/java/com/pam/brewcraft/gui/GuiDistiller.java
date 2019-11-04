package com.pam.brewcraft.gui;

import org.lwjgl.opengl.GL11;

import com.pam.brewcraft.tileentities.TileEntityDistiller;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.ResourceLocation;

public class GuiDistiller extends GuiContainer {
    private static final ResourceLocation gui = new ResourceLocation("brewcraft:textures/gui/distiller.png");
    
    private final TileEntityDistiller entityDistiller;

    public GuiDistiller(InventoryPlayer invPlayer, TileEntityDistiller entityDistiller) {
        super(new ContainerDistiller(invPlayer, entityDistiller));
        this.entityDistiller = entityDistiller;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRenderer.drawString("Distiller", 62, 5, 4210752);
        fontRenderer.drawString(I18n.format("container.inventory"), 8, ySize - 98 + 4, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        mc.getTextureManager().bindTexture(gui);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
        
        //if (TileEntityDistiller.isBurning(this.entityDistiller))
        //{
        //    int k = this.getBurnLeftScaled(13);
        //    this.drawTexturedModalRect(x + 56, y + 36 + 12 - k, 176, 12 - k, 14, k + 1);
        //}
        int progress = entityDistiller.getCookProgressScaled(24);
        drawTexturedModalRect(x + 79, y + 34, 176, 14, progress + 1, 16);
        
    }
    

    
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        renderHoveredToolTip(mouseX, mouseY);
    }
}