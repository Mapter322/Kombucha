package com.mapter.kombucha.client.screen;

import com.mapter.kombucha.Kombucha;
import com.mapter.kombucha.entity.FriendlyKombuchaMonster;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import java.util.Locale;

public class FriendlyKombuchaScreen extends Screen {
    private static final Identifier BACKGROUND =
            Identifier.fromNamespaceAndPath(Kombucha.MODID, "textures/screen/kombucha_menu.png");

    private static final int GUI_WIDTH = 320;
    private static final int GUI_HEIGHT = 220;
    private static final int PANEL_X = 12;
    private static final int PANEL_GAP = 12;
    private static final int PANEL_WIDTH = 142;
    private static final int TOP_PANEL_Y = 32;
    private static final int TOP_PANEL_HEIGHT = 82;
    private static final int BOTTOM_PANEL_Y = 128;
    private static final int BOTTOM_PANEL_HEIGHT = 80;
    private static final int MODEL_SCALE = 48;

    private static final int COLOR_TITLE = 0xFF222222;
    private static final int COLOR_TEXT = 0xFFFFFFFF;
    private static final int COLOR_MUTED = 0xFFBBBBBB;
    private static final int COLOR_PANEL = 0xCC333333;
    private static final int COLOR_SEPARATOR = 0x88666666;

    private static final int CLOSE_X = GUI_WIDTH - 28;
    private static final int CLOSE_Y = 8;
    private static final int CLOSE_WIDTH = 18;
    private static final int CLOSE_HEIGHT = 18;

    private final FriendlyKombuchaMonster kombucha;
    private int leftPos;
    private int topPos;

    public FriendlyKombuchaScreen(FriendlyKombuchaMonster kombucha) {
        super(Component.translatable("screen.kombucha.title"));
        this.kombucha = kombucha;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    protected void init() {
        leftPos = (width - GUI_WIDTH) / 2;
        topPos = (height - GUI_HEIGHT) / 2;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        graphics.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND,
                leftPos, topPos, 0.0F, 0.0F, GUI_WIDTH, GUI_HEIGHT,
                GUI_WIDTH, GUI_HEIGHT, GUI_WIDTH, GUI_HEIGHT);

        drawPanel(graphics, PANEL_X, TOP_PANEL_Y, PANEL_WIDTH, TOP_PANEL_HEIGHT);
        drawPanel(graphics, PANEL_X + PANEL_WIDTH + PANEL_GAP, TOP_PANEL_Y,
                PANEL_WIDTH, GUI_HEIGHT - TOP_PANEL_Y - 12);
        drawPanel(graphics, PANEL_X, BOTTOM_PANEL_Y, PANEL_WIDTH, BOTTOM_PANEL_HEIGHT);

        extractKombucha(graphics, partialTick);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);

        int relativeMouseX = mouseX - leftPos;
        int relativeMouseY = mouseY - topPos;
        boolean closeHovered = isInside(relativeMouseX, relativeMouseY,
                CLOSE_X, CLOSE_Y, CLOSE_WIDTH, CLOSE_HEIGHT);

        graphics.text(font, "×", leftPos + CLOSE_X + 5, topPos + CLOSE_Y + 4,
                closeHovered ? COLOR_TEXT : COLOR_TITLE, false);

        Component title = Component.translatable("screen.kombucha.title");
        graphics.text(font, title, leftPos + (GUI_WIDTH - font.width(title)) / 2, topPos + 7,
                COLOR_TITLE, false);
        graphics.fill(leftPos + PANEL_X, topPos + 26,
                leftPos + GUI_WIDTH - PANEL_X, topPos + 27, COLOR_SEPARATOR);

        drawPanelLabels(graphics);
    }

    private void drawPanelLabels(GuiGraphicsExtractor graphics) {
        Component modelName = kombucha.getName();
        int modelNameX = leftPos + PANEL_X + 6;
        int modelNameY = topPos + TOP_PANEL_Y + 6;
        int modelNameWidth = PANEL_WIDTH - 12;
        float modelNameScale = Math.min(1.0F, modelNameWidth / (float) Math.max(1, font.width(modelName)));
        graphics.pose().pushMatrix();
        graphics.pose().translate(modelNameX, modelNameY);
        graphics.pose().scale(modelNameScale, modelNameScale);
        graphics.text(font, modelName, 0, 0, COLOR_TEXT, false);
        graphics.pose().popMatrix();
        graphics.text(font, Component.translatable("screen.kombucha.stats"),
                leftPos + PANEL_X + PANEL_WIDTH + PANEL_GAP + 6, topPos + TOP_PANEL_Y + 6, COLOR_TEXT, false);

        int statsX = leftPos + PANEL_X + PANEL_WIDTH + PANEL_GAP + 6;
        int statsY = topPos + TOP_PANEL_Y + 28;
        drawStat(graphics, "screen.kombucha.speed", formatNumber(kombucha.getAttributeValue(Attributes.MOVEMENT_SPEED)), statsX, statsY);
        drawStat(graphics, "screen.kombucha.damage", formatNumber(kombucha.getAttributeValue(Attributes.ATTACK_DAMAGE)), statsX, statsY + 14);
        drawStat(graphics, "screen.kombucha.health", formatHealth(), statsX, statsY + 28);

        graphics.text(font, Component.translatable("screen.kombucha.commands"),
                leftPos + PANEL_X + 6, topPos + BOTTOM_PANEL_Y + 6, COLOR_TEXT, false);
        graphics.text(font, Component.translatable("screen.kombucha.commands.placeholder"),
                leftPos + PANEL_X + 6, topPos + BOTTOM_PANEL_Y + 30, COLOR_MUTED, false);
    }

    private void drawStat(GuiGraphicsExtractor graphics, String labelKey, String value, int x, int y) {
        Component label = Component.translatable(labelKey);
        graphics.text(font, label, x, y, COLOR_MUTED, false);
        graphics.text(font, value, x + font.width(label) + 4, y, COLOR_TEXT, false);
    }

    private void drawPanel(GuiGraphicsExtractor graphics, int x, int y, int width, int height) {
        graphics.fill(leftPos + x, topPos + y, leftPos + x + width, topPos + y + height, COLOR_PANEL);
    }

    private void extractKombucha(GuiGraphicsExtractor graphics, float partialTick) {
        EntityRenderDispatcher dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        EntityRenderer<? super FriendlyKombuchaMonster, ?> renderer = dispatcher.getRenderer(kombucha);
        EntityRenderState renderState = renderer.createRenderState(kombucha, partialTick);
        renderState.shadowPieces.clear();
        renderState.outlineColor = 0;

        if (renderState instanceof LivingEntityRenderState livingState) {
            // Keep the model facing forward; the GUI quaternion below handles the spin.
            livingState.bodyRot = 0.0F;
            livingState.yRot = 0.0F;
            livingState.xRot = 0.0F;
            livingState.boundingBoxWidth /= livingState.scale;
            livingState.boundingBoxHeight /= livingState.scale;
            livingState.scale = 1.0F;
        }

        float angle = (kombucha.tickCount + partialTick) * 1.5F * Mth.DEG_TO_RAD;
        Quaternionf rotation = new Quaternionf().rotateZ((float) Math.PI).rotateY(angle);
        Vector3f translation = new Vector3f(0.0F, renderState.boundingBoxHeight / 2.0F + 0.0625F, 0.0F);
        graphics.entity(renderState, MODEL_SCALE, translation, rotation, null,
                leftPos + PANEL_X + 6, topPos + TOP_PANEL_Y + 22,
                leftPos + PANEL_X + PANEL_WIDTH - 6, topPos + TOP_PANEL_Y + TOP_PANEL_HEIGHT - 5);
    }

    private String formatHealth() {
        return String.format(Locale.ROOT, "%.1f / %.1f", kombucha.getHealth(), kombucha.getMaxHealth());
    }

    private String formatNumber(double value) {
        return String.format(Locale.ROOT, "%.2f", value);
    }

    private boolean isInside(int x, int y, int boxX, int boxY, int boxWidth, int boxHeight) {
        return x >= boxX && x < boxX + boxWidth && y >= boxY && y < boxY + boxHeight;
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        if (event.button() == 0) {
            int x = (int) event.x() - leftPos;
            int y = (int) event.y() - topPos;
            if (isInside(x, y, CLOSE_X, CLOSE_Y, CLOSE_WIDTH, CLOSE_HEIGHT)) {
                onClose();
                return true;
            }
        }
        return super.mouseClicked(event, doubleClick);
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        if (event.key() == GLFW.GLFW_KEY_E) {
            onClose();
            return true;
        }
        return super.keyPressed(event);
    }
}
