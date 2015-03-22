/*
 * Copyright (C) 2015 yew_mentzaki
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package jplatformer.core.gui;

import java.util.ArrayList;
import jplatformer.core.JPlatformer;
import jplatformer.core.JTextureManager;
import jplatformer.core.VTextRender;
import static org.lwjgl.opengl.GL11.*;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

/**
 *
 * @author yew_mentzaki
 */
public class JTrackBar extends JElement {

    public int minValue, maxValue;
    public int width, height;
    public int value;
    private JButton button;
    private ArrayList<JElement> jelement = new ArrayList<JElement>();
    private static Texture panel;

    boolean hover, press;

    @Override
    public void render() {
        {
        double w = width;
        double x = button.x;
        double v = minValue + (x / w) * (maxValue - minValue);
        if (((int) v) != value) {
            value = (int) v;
            valueChanged();
        }
        }
        if (panel == null) {
            panel = JTextureManager.getTexture("gui/trackbar.png");
            panel.setTextureFilter(GL_NEAREST);

        }
        panel.bind();

        glTranslated(x, y, 0);
        glColor3f(1, 1, 1);
        //Angles
        glBegin(GL_QUADS);
        {
            glTexCoord2d(0, 0);
            glVertex2d(0, 0);
            glTexCoord2d(0.3125, 0);
            glVertex2d(0 + panel.getTextureWidth() / 3.2, 0);
            glTexCoord2d(0.3125, 0.3125);
            glVertex2d(0 + panel.getTextureWidth() / 3.2, 0 + panel.getTextureHeight() / 3.2);
            glTexCoord2d(0, 0.3125);
            glVertex2d(0, 0 + panel.getTextureHeight() / 3.2);
        }
        {
            glTexCoord2d(0, 1);
            glVertex2d(0, 0 + height);
            glTexCoord2d(0.3125, 1);
            glVertex2d(0 + panel.getTextureWidth() / 3.2, 0 + height);
            glTexCoord2d(0.3125, 0.6875);
            glVertex2d(0 + panel.getTextureWidth() / 3.2, 0 + height - panel.getTextureHeight() / 3.2);
            glTexCoord2d(0, 0.6875);
            glVertex2d(0, 0 + height - panel.getTextureHeight() / 3.2);
        }
        {
            glTexCoord2d(1, 0);
            glVertex2d(0 + width, 0);
            glTexCoord2d(0.6875, 0);
            glVertex2d(0 + width - panel.getTextureWidth() / 3.2, 0);
            glTexCoord2d(0.6875, 0.3125);
            glVertex2d(0 + width - panel.getTextureWidth() / 3.2, 0 + panel.getTextureHeight() / 3.2);
            glTexCoord2d(1, 0.3125);
            glVertex2d(0 + width, 0 + panel.getTextureHeight() / 3.2);
        }
        {
            glTexCoord2d(1, 1);
            glVertex2d(0 + width, 0 + height);
            glTexCoord2d(0.6875, 1);
            glVertex2d(0 + width - panel.getTextureWidth() / 3.2, 0 + height);
            glTexCoord2d(0.6875, 0.6875);
            glVertex2d(0 + width - panel.getTextureWidth() / 3.2, 0 + height - panel.getTextureHeight() / 3.2);
            glTexCoord2d(1, 0.6875);
            glVertex2d(0 + width, 0 + height - panel.getTextureHeight() / 3.2);
        }
        //Lines
        {
            glTexCoord2d(0.3125, 0);
            glVertex2d(0 + panel.getTextureWidth() / 3.2, 0);
            glTexCoord2d(0.6875, 0);
            glVertex2d(0 + width - panel.getTextureWidth() / 3.2, 0);
            glTexCoord2d(0.6875, 0.3125);
            glVertex2d(0 + width - panel.getTextureWidth() / 3.2, 0 + panel.getTextureHeight() / 3.2);
            glTexCoord2d(0.3125, 0.3125);
            glVertex2d(0 + panel.getTextureWidth() / 3.2, 0 + panel.getTextureHeight() / 3.2);
        }
        {
            glTexCoord2d(0.3125, 1);
            glVertex2d(0 + width - panel.getTextureWidth() / 3.2, 0 + height);
            glTexCoord2d(0.6875, 1);
            glVertex2d(0 + panel.getTextureWidth() / 3.2, 0 + height);
            glTexCoord2d(0.6875, 0.6875);
            glVertex2d(0 + panel.getTextureWidth() / 3.2, 0 + height - panel.getTextureHeight() / 3.2);
            glTexCoord2d(0.3125, 0.6875);
            glVertex2d(0 + width - panel.getTextureWidth() / 3.2, 0 + height - panel.getTextureHeight() / 3.2);
        }
        {
            glTexCoord2d(1, 0.6875);
            glVertex2d(0 + width, 0 + height - panel.getTextureHeight() / 3.2);
            glTexCoord2d(0.6875, 0.6875);
            glVertex2d(0 + width - panel.getTextureWidth() / 3.2, 0 + height - panel.getTextureHeight() / 3.2);
            glTexCoord2d(0.6875, 0.3125);
            glVertex2d(0 + width - panel.getTextureWidth() / 3.2, 0 + panel.getTextureHeight() / 3.2);
            glTexCoord2d(1, 0.3125);
            glVertex2d(0 + width, 0 + panel.getTextureHeight() / 3.2);
        }
        {
            glTexCoord2d(0, 0.3125);
            glVertex2d(0, 0 + panel.getTextureHeight() / 3.2);
            glTexCoord2d(0.3125, 0.3125);
            glVertex2d(0 + panel.getTextureWidth() / 3.2, 0 + panel.getTextureHeight() / 3.2);
            glTexCoord2d(0.3125, 0.6875);
            glVertex2d(0 + panel.getTextureWidth() / 3.2, 0 + height - panel.getTextureHeight() / 3.2);
            glTexCoord2d(0, 0.6875);
            glVertex2d(0, 0 + height - panel.getTextureHeight() / 3.2);
        }
        //Panel
        {
            glTexCoord2d(0.3125, 0.3125);
            glVertex2d(0 + panel.getTextureWidth() / 3.2, 0 + panel.getTextureHeight() / 3.2);
            glTexCoord2d(0.6875, 0.3125);
            glVertex2d(0 + width - panel.getTextureWidth() / 3.2, 0 + panel.getTextureHeight() / 3.2);
            glTexCoord2d(0.6875, 0.6875);
            glVertex2d(0 + width - panel.getTextureWidth() / 3.2, 0 + height - panel.getTextureHeight() / 3.2);
            glTexCoord2d(0.3125, 0.6875);
            glVertex2d(0 + panel.getTextureWidth() / 3.2, 0 + height - panel.getTextureHeight() / 3.2);
        }
        glEnd();
        button.render();
        glTranslated(-x, -y, 0);
    }
    boolean b = false;

    public void valueChanged() {

    }
    public void setValue(double value){
        this.value = (int)value;
        value -= (float)minValue;
        value /= (float)(maxValue - minValue);
        value *= (float)(width);
        this.button.x = (int)value;
    }
    public JTrackBar(int minValue, int maxValue, int x, int y, int width, int height) {
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.button = new JButton("", 0, 0, height, height) {

            int mx;

            @Override
            public void pressed(int x, int y, int mouseButton) {
                if (((JTrackBar) (parent)).b) {
                    if (this.x + x - mx < 0) {
                        this.x = 0;
                    } else if (this.x + x - mx > (((JTrackBar) (parent)).width - height)) {

                        this.x = ((JTrackBar) (parent)).width - height;
                    } else {
                        this.x += x - mx;
                    }

                }
                ((JTrackBar) (parent)).b = true;
                mx = x;
            }

            @Override
            public void hover(int x, int y) {
                super.hover(x, y); //To change body of generated methods, choose Tools | Templates.
                mx = x;
            }

            @Override
            boolean isPressed(int x, int y, int mouseButton) {
                boolean b = (x >= this.x && y >= this.y && x <= this.x + width && y <= this.y + height);

                if (b) {
                    pressed(x, y, mouseButton);
                } else {
                    ((JTrackBar) (parent)).b = false;
                }
                return b;
            }

            @Override
            public void released(int x, int y, int mouseButton) {
                ((JTrackBar) (parent)).b = false;
            }

        };
        this.button.parent = this;
    }

    @Override
    boolean isPressed(int x, int y, int mouseButton) {
        boolean is = (x >= this.x && y >= this.y && x <= this.x + width && y <= this.y + height);
        if (is) {
            press = true;
            pressed(x, y, mouseButton);
            if (!button.isPressed(x - this.x, y - this.y, mouseButton)) {
                if (x > this.x + button.x && button.x < width - height) {
                    button.x++;
                } else {
                    button.x--;
                }
            }
        } else {
            b = false;
        }

        return is;
    }

    @Override
    boolean isReleased(int x, int y, int mouseButton) {
        boolean is = (x >= this.x && y >= this.y && x <= this.x + width && y <= this.y + height);
        if (is) {
            released(x, y, mouseButton);
        }
        return is;
    }

    @Override
    boolean isHover(int x, int y) {
        boolean is = (x >= this.x && y >= this.y && x <= this.x + width && y <= this.y + height);
        if (is) {
            hover = true;
            hover(x, y);

        } else {
            hover = false;
        }
        button.isHover(x - this.x, y - this.y);
        return is;
    }

}
