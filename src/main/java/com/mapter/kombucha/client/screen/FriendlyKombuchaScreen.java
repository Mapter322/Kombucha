package com.mapter.kombucha.client.screen;

import com.mapter.kombucha.Kombucha;
import com.mapter.kombucha.entity.FriendlyKombuchaMonster;
import com.mapter.kombucha.network.FriendlyKombuchaUpgradePayload;
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
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import java.util.Locale;

public class FriendlyKombuchaScreen extends Screen {
    private static final int STAT_ROW_SPACING = 12;
    private static final Identifier BACKGROUND =
            Identifier.fromNamespaceAndPath(Kombucha.MODID, "textures/screen/kombucha_menu.png");

    private static final int GUI_WIDTH = 276;
    private static final int GUI_HEIGHT = 220;
    private static final int MODEL_X = 20;
    private static final int MODEL_Y = 22;
    private static final int MODEL_WIDTH = 109;
    private static final int MODEL_HEIGHT = 65;
    private static final int INFO_X = 14;
    private static final int INFO_Y = 102;
    private static final int INFO_WIDTH = 248;
    private static final int INFO_DIVIDER = 4;
    private static final int INFO_COLUMN_WIDTH = (INFO_WIDTH - INFO_DIVIDER) / 2;
    private static final int INFO_CONTENT_TOP = INFO_Y + 24;
    private static final int INFO_CONTENT_BOTTOM = GUI_HEIGHT - 24;
    private static final int CHARACTERISTIC_ROW_COUNT = 8;
    private static final int SCROLLBAR_X = INFO_X + INFO_COLUMN_WIDTH;
    private static final int SCROLLBAR_WIDTH = INFO_DIVIDER;
    private static final int SCROLLBAR_TOP = INFO_Y + 2;
    private static final int SCROLLBAR_BOTTOM = GUI_HEIGHT - 13;
    private static final int SCROLLBAR_MIN_THUMB_HEIGHT = 16;
    private static final int MODEL_SCALE = 32;
    private static final int EXPERIENCE_BAR_X = INFO_X + 2;
    private static final int EXPERIENCE_BAR_Y = 90;
    private static final int EXPERIENCE_BAR_WIDTH = INFO_WIDTH - 4;
    private static final int EXPERIENCE_BAR_HEIGHT = 10;
    private static final int PLUS_X = INFO_X + INFO_COLUMN_WIDTH - 14;
    private static final int PLUS_WIDTH = 12;
    private static final int PLUS_HEIGHT = 12;

    private static final int COLOR_TITLE = 0xFF3F321F;
    private static final int COLOR_TEXT = 0xFF4A3A24;
    private static final int COLOR_MUTED = 0xFF776344;
    private static final int COLOR_CLOSE = 0x884A3A24;
    private static final int COLOR_CLOSE_HOVER = 0xFF4A3A24;
    private static final int COLOR_SCROLLBAR_THUMB = 0xCC4A3A24;
    private static final int COLOR_SCROLLBAR_THUMB_HOVER = 0xFF3F321F;
    private static final int COLOR_EXPERIENCE_FRAME = 0xCC4A3A24;
    private static final int COLOR_EXPERIENCE_INNER_FRAME = 0xFFE0BD70;
    private static final int COLOR_EXPERIENCE_BACKGROUND = 0xAA4A3A24;
    private static final int COLOR_EXPERIENCE = 0xFFB77B2B;
    private static final int COLOR_EXPERIENCE_HIGHLIGHT = 0xFFD49B3A;
    private static final int COLOR_EXPERIENCE_LIGHT_GREEN = 0xFF91C46C;
    private static final int COLOR_EXPERIENCE_LIGHT_GREEN_HIGHLIGHT = 0xFFB8E28B;
    private static final int COLOR_EXPERIENCE_DARK_GREEN = 0xFF3F7A45;
    private static final int COLOR_EXPERIENCE_DARK_GREEN_HIGHLIGHT = 0xFF6FA96F;
    private static final int COLOR_PLUS = 0xCC4A3A24;
    private static final int COLOR_PLUS_HOVER = 0xFF3F321F;

    private static final int CLOSE_X = GUI_WIDTH - 24;
    private static final int CLOSE_Y = 2;
    private static final int CLOSE_WIDTH = 18;
    private static final int CLOSE_HEIGHT = 18;

    private final FriendlyKombuchaMonster kombucha;
    private int leftPos;
    private int topPos;
    private double scrollOffset;
    private boolean draggingScrollbar;
    private double scrollbarDragOffset;

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

        drawExperienceBar(graphics);
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
                closeHovered ? COLOR_CLOSE_HOVER : COLOR_CLOSE, false);

        Component name = kombucha.getName();
        int nameWidth = GUI_WIDTH - 56;
        float nameScale = Math.min(1.0F, nameWidth / (float) Math.max(1, font.width(name)));
        graphics.pose().pushMatrix();
        graphics.pose().translate(leftPos + (GUI_WIDTH - Math.min(font.width(name), nameWidth)) / 2.0F,
                topPos + 6);
        graphics.pose().scale(nameScale, nameScale);
        graphics.text(font, name, 0, 0, COLOR_TITLE, false);
        graphics.pose().popMatrix();

        drawPanelLabels(graphics, mouseX, mouseY);
        if (isInside(mouseX - leftPos, mouseY - topPos,
                EXPERIENCE_BAR_X, EXPERIENCE_BAR_Y, EXPERIENCE_BAR_WIDTH, EXPERIENCE_BAR_HEIGHT)) {
            graphics.setTooltipForNextFrame(
                    Component.translatable("screen.kombucha.experience",
                            kombucha.getExperience(), kombucha.getExperienceToNextLevel()), mouseX, mouseY);
        }
    }

    private void drawPanelLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        int statsX = leftPos + INFO_X + 7;
        int statsY = topPos + INFO_Y + 7;
        graphics.text(font, Component.translatable("screen.kombucha.characteristics"),
                statsX, statsY, COLOR_TITLE, false);
        graphics.text(font, Component.translatable("screen.kombucha.perks"),
                leftPos + INFO_X + INFO_COLUMN_WIDTH + INFO_DIVIDER + 7, statsY, COLOR_TITLE, false);

        statsY += 18;
        graphics.enableScissor(leftPos + INFO_X, topPos + INFO_CONTENT_TOP,
                leftPos + INFO_X + INFO_WIDTH, topPos + INFO_CONTENT_BOTTOM);
        graphics.pose().pushMatrix();
        graphics.pose().translate(0.0F, -(float) scrollOffset);
        drawStat(graphics, "screen.kombucha.level", Integer.toString(kombucha.getLevel()), statsX, statsY);
        drawStat(graphics, "screen.kombucha.health", formatHealth(), statsX, statsY + STAT_ROW_SPACING);
        drawStat(graphics, "screen.kombucha.speed", formatNumber(kombucha.getAttributeValue(Attributes.MOVEMENT_SPEED)), statsX, statsY + STAT_ROW_SPACING * 2);
        drawStat(graphics, "screen.kombucha.melee_damage", formatNumber(kombucha.getAttributeValue(Attributes.ATTACK_DAMAGE)), statsX, statsY + STAT_ROW_SPACING * 3);
        drawStat(graphics, "screen.kombucha.ranged_damage", formatNumber(kombucha.getRangedDamage()), statsX, statsY + STAT_ROW_SPACING * 4);
        drawStat(graphics, "screen.kombucha.melee_speed", formatAttackInterval(kombucha.getMeleeAttackIntervalTicks()), statsX, statsY + STAT_ROW_SPACING * 5);
        drawStat(graphics, "screen.kombucha.ranged_speed", formatAttackInterval(kombucha.getRangedAttackIntervalTicks()), statsX, statsY + STAT_ROW_SPACING * 6);
        drawStat(graphics, "screen.kombucha.projectile_speed", formatProjectileSpeed(), statsX, statsY + STAT_ROW_SPACING * 7);
        for (int row = 1; row <= 7; row++) {
            drawUpgradeButton(graphics, row, statsY, mouseX, mouseY);
        }

        graphics.text(font, Component.translatable("screen.kombucha.perks.placeholder"),
                leftPos + INFO_X + INFO_COLUMN_WIDTH + INFO_DIVIDER + 7, statsY + 18, COLOR_MUTED, false);
        graphics.pose().popMatrix();
        graphics.disableScissor();

        drawScrollbar(graphics, mouseX, mouseY);
    }

    private void drawScrollbar(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        int trackX = leftPos + SCROLLBAR_X;
        int thumbY = getScrollbarY();
        int thumbHeight = getScrollbarThumbHeight();
        boolean hovered = isInside(mouseX - leftPos, mouseY - topPos,
                SCROLLBAR_X, SCROLLBAR_TOP, SCROLLBAR_WIDTH, SCROLLBAR_BOTTOM - SCROLLBAR_TOP);

        // The texture already provides the central separator as the scrollbar track.
        int thumbColor = hovered ? COLOR_SCROLLBAR_THUMB_HOVER : COLOR_SCROLLBAR_THUMB;
        graphics.fill(trackX + 1, thumbY, trackX + SCROLLBAR_WIDTH - 1, thumbY + 1, thumbColor);
        graphics.fill(trackX, thumbY + 1, trackX + SCROLLBAR_WIDTH, thumbY + thumbHeight - 1, thumbColor);
        graphics.fill(trackX + 1, thumbY + thumbHeight - 1,
                trackX + SCROLLBAR_WIDTH - 1, thumbY + thumbHeight, thumbColor);
    }

    private int getScrollbarThumbHeight() {
        int viewportHeight = INFO_CONTENT_BOTTOM - INFO_CONTENT_TOP;
        int trackHeight = SCROLLBAR_BOTTOM - SCROLLBAR_TOP;
        return Math.max(SCROLLBAR_MIN_THUMB_HEIGHT,
                Math.min(trackHeight, trackHeight * viewportHeight / getContentHeight()));
    }

    private int getScrollbarY() {
        int maxScroll = getMaxScroll();
        int thumbTravel = SCROLLBAR_BOTTOM - SCROLLBAR_TOP - getScrollbarThumbHeight();
        if (maxScroll == 0 || thumbTravel == 0) {
            return topPos + SCROLLBAR_TOP;
        }

        return topPos + SCROLLBAR_TOP + (int) Math.round(scrollOffset / maxScroll * thumbTravel);
    }

    private int getContentHeight() {
        return 1 + STAT_ROW_SPACING * (CHARACTERISTIC_ROW_COUNT - 1) + 9;
    }

    private int getMaxScroll() {
        return Math.max(0, getContentHeight() - (INFO_CONTENT_BOTTOM - INFO_CONTENT_TOP));
    }

    private void setScrollOffset(double scrollOffset) {
        this.scrollOffset = Math.max(0.0, Math.min(scrollOffset, getMaxScroll()));
    }

    private void drawStat(GuiGraphicsExtractor graphics, String labelKey, String value, int x, int y) {
        Component label = Component.translatable(labelKey);
        graphics.text(font, label, x, y, COLOR_MUTED, false);
        graphics.text(font, value, x + font.width(label) + 4, y, COLOR_TEXT, false);
    }

    private void drawExperienceBar(GuiGraphicsExtractor graphics) {
        int x = leftPos + EXPERIENCE_BAR_X;
        int y = topPos + EXPERIENCE_BAR_Y;
        drawRoundedRect(graphics, x, y, EXPERIENCE_BAR_WIDTH, EXPERIENCE_BAR_HEIGHT, 3, COLOR_EXPERIENCE_FRAME);
        drawRoundedRect(graphics, x + 1, y + 1, EXPERIENCE_BAR_WIDTH - 2, EXPERIENCE_BAR_HEIGHT - 2,
                2, COLOR_EXPERIENCE_INNER_FRAME);
        drawRoundedRect(graphics, x + 2, y + 2, EXPERIENCE_BAR_WIDTH - 4, EXPERIENCE_BAR_HEIGHT - 4,
                2, COLOR_EXPERIENCE_BACKGROUND);
        int required = kombucha.getExperienceToNextLevel();
        int progressWidth = Mth.clamp((int) ((EXPERIENCE_BAR_WIDTH - 4)
                * (kombucha.getExperience() / (float) required)), 0, EXPERIENCE_BAR_WIDTH - 4);
        if (progressWidth > 0) {
            float progress = kombucha.getExperience() / (float) required;
            int experienceColor = getExperienceColor(progress);
            drawRoundedRect(graphics, x + 2, y + 2, progressWidth, EXPERIENCE_BAR_HEIGHT - 4,
                    2, experienceColor);
            if (progressWidth > 2) {
                graphics.fill(x + 3, y + 2, x + progressWidth + 1, y + 3,
                        getExperienceHighlightColor(progress));
            }
        }
    }

    private int getExperienceColor(float progress) {
        if (progress <= 1.0F / 3.0F) {
            return COLOR_EXPERIENCE;
        }
        return progress <= 2.0F / 3.0F ? COLOR_EXPERIENCE_LIGHT_GREEN : COLOR_EXPERIENCE_DARK_GREEN;
    }

    private int getExperienceHighlightColor(float progress) {
        if (progress <= 1.0F / 3.0F) {
            return COLOR_EXPERIENCE_HIGHLIGHT;
        }
        return progress <= 2.0F / 3.0F
                ? COLOR_EXPERIENCE_LIGHT_GREEN_HIGHLIGHT : COLOR_EXPERIENCE_DARK_GREEN_HIGHLIGHT;
    }

    private void drawRoundedRect(GuiGraphicsExtractor graphics, int x, int y, int width, int height,
                                 int radius, int color) {
        for (int row = 0; row < height; row++) {
            int inset = Math.max(0, radius - Math.min(row, height - 1 - row));
            graphics.fill(x + inset, y + row, x + width - inset, y + row + 1, color);
        }
    }

    private void drawUpgradeButton(GuiGraphicsExtractor graphics, int row, int statsY, int mouseX, int mouseY) {
        if (kombucha.getAvailableUpgradePoints() <= 0) {
            return;
        }
        int buttonY = statsY + STAT_ROW_SPACING * row - (int) scrollOffset - 2;
        boolean hovered = isInside(mouseX - leftPos, mouseY - topPos,
                PLUS_X, buttonY - topPos, PLUS_WIDTH, PLUS_HEIGHT);
        graphics.text(font, "+", leftPos + PLUS_X + 3, statsY + STAT_ROW_SPACING * row - 1,
                hovered ? COLOR_PLUS_HOVER : COLOR_PLUS, false);
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
                leftPos + MODEL_X + 5, topPos + MODEL_Y + 4,
                leftPos + MODEL_X + MODEL_WIDTH - 5, topPos + MODEL_Y + MODEL_HEIGHT - 3);
    }

    private String formatHealth() {
        return String.format(Locale.ROOT, "%.1f / %.1f", kombucha.getHealth(), kombucha.getMaxHealth());
    }

    private String formatProjectileSpeed() {
        return String.format(Locale.ROOT, "%.2f",
                kombucha.getRangedProjectileSpeedWithUpgrades(FriendlyKombuchaMonster.RANGED_PROJECTILE_MIN_POWER));
    }

    private String formatAttackInterval(int ticks) {
        return String.format(Locale.ROOT, "%.2f %s",
                ticks / 20.0F,
                Component.translatable("screen.kombucha.seconds").getString());
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

            if (kombucha.getAvailableUpgradePoints() > 0) {
                for (int row = 1; row <= 7; row++) {
                    int buttonY = INFO_Y + 7 + 18 + STAT_ROW_SPACING * row - (int) scrollOffset - 2;
                    if (isInside(x, y, PLUS_X, buttonY, PLUS_WIDTH, PLUS_HEIGHT)) {
                        ClientPacketDistributor.sendToServer(
                                new FriendlyKombuchaUpgradePayload(kombucha.getId(), row - 1));
                        return true;
                    }
                }
            }

            if (isInside(x, y, SCROLLBAR_X, SCROLLBAR_TOP,
                    SCROLLBAR_WIDTH, SCROLLBAR_BOTTOM - SCROLLBAR_TOP)) {
                int thumbY = getScrollbarY() - topPos;
                int thumbHeight = getScrollbarThumbHeight();
                if (y >= thumbY && y < thumbY + thumbHeight) {
                    draggingScrollbar = true;
                    scrollbarDragOffset = y - thumbY;
                } else if (y < thumbY) {
                    setScrollOffset(scrollOffset - (SCROLLBAR_BOTTOM - SCROLLBAR_TOP));
                } else {
                    setScrollOffset(scrollOffset + (SCROLLBAR_BOTTOM - SCROLLBAR_TOP));
                }
                return true;
            }
        }
        return super.mouseClicked(event, doubleClick);
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent event, double dx, double dy) {
        if (draggingScrollbar && event.button() == 0) {
            int thumbTravel = SCROLLBAR_BOTTOM - SCROLLBAR_TOP - getScrollbarThumbHeight();
            if (thumbTravel > 0 && getMaxScroll() > 0) {
                double thumbY = event.y() - topPos - SCROLLBAR_TOP - scrollbarDragOffset;
                setScrollOffset(thumbY / thumbTravel * getMaxScroll());
            }
            return true;
        }
        return super.mouseDragged(event, dx, dy);
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent event) {
        if (event.button() == 0) {
            draggingScrollbar = false;
        }
        return super.mouseReleased(event);
    }

    @Override
    public boolean mouseScrolled(double x, double y, double scrollX, double scrollY) {
        int relativeX = (int) x - leftPos;
        int relativeY = (int) y - topPos;
        if (isInside(relativeX, relativeY, INFO_X, INFO_Y,
                INFO_WIDTH, INFO_CONTENT_BOTTOM - INFO_Y)) {
            setScrollOffset(scrollOffset - scrollY * STAT_ROW_SPACING);
            return true;
        }
        return super.mouseScrolled(x, y, scrollX, scrollY);
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
