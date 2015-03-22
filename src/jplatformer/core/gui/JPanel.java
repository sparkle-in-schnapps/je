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
import static org.lwjgl.opengl.GL11.*;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

/**
 *
 * @author yew_mentzaki
 */
public class JPanel extends JElement {

    public int width, height;
    private ArrayList<JElement> jelement = new ArrayList<JElement>();
    private static Texture panel;

    @Override
    public void render() {
        if (panel == null) {
            panel = JTextureManager.getTexture("gui/panel.png");
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
            glVertex2d(0 + panel.getTextureWidth() /3.2, 0 + height);
            glTexCoord2d(0.3125, 0.6875);
            glVertex2d(0 + panel.getTextureWidth() /3.2, 0 + height - panel.getTextureHeight() /3.2);
            glTexCoord2d(0, 0.6875);
            glVertex2d(0, 0 + height - panel.getTextureHeight() /3.2);
        }
        {
            glTexCoord2d(1, 0);
            glVertex2d(0 + width, 0);
            glTexCoord2d(0.6875, 0);
            glVertex2d(0 + width - panel.getTextureWidth() /3.2, 0);
            glTexCoord2d(0.6875, 0.3125);
            glVertex2d(0 + width - panel.getTextureWidth() /3.2, 0 + panel.getTextureHeight() /3.2);
            glTexCoord2d(1, 0.3125);
            glVertex2d(0 + width, 0 + panel.getTextureHeight() /3.2);
        }
        {
            glTexCoord2d(1, 1);
            glVertex2d(0 + width, 0 + height);
            glTexCoord2d(0.6875, 1);
            glVertex2d(0 + width - panel.getTextureWidth() /3.2, 0 + height);
            glTexCoord2d(0.6875, 0.6875);
            glVertex2d(0 + width - panel.getTextureWidth() /3.2, 0 + height - panel.getTextureHeight() /3.2);
            glTexCoord2d(1, 0.6875);
            glVertex2d(0 + width, 0 + height - panel.getTextureHeight() /3.2);
        }
        //Lines
        {
            glTexCoord2d(0.3125, 0);
            glVertex2d(0 + panel.getTextureWidth() /3.2, 0);
            glTexCoord2d(0.6875, 0);
            glVertex2d(0 + width - panel.getTextureWidth() /3.2, 0);
            glTexCoord2d(0.6875, 0.3125);
            glVertex2d(0 + width - panel.getTextureWidth() /3.2, 0 + panel.getTextureHeight() /3.2);
            glTexCoord2d(0.3125, 0.3125);
            glVertex2d(0 + panel.getTextureWidth() /3.2, 0 + panel.getTextureHeight() /3.2);
        }
        {
            glTexCoord2d(0.3125, 1);
            glVertex2d(0 + width - panel.getTextureWidth() /3.2, 0 + height);
            glTexCoord2d(0.6875, 1);
            glVertex2d(0 + panel.getTextureWidth() /3.2, 0 + height);
            glTexCoord2d(0.6875, 0.6875);
            glVertex2d(0 + panel.getTextureWidth() /3.2, 0 + height - panel.getTextureHeight() /3.2);
            glTexCoord2d(0.3125, 0.6875);
            glVertex2d(0 + width - panel.getTextureWidth() /3.2, 0 + height - panel.getTextureHeight() /3.2);
        }
        {
            glTexCoord2d(1, 0.6875);
            glVertex2d(0 + width, 0 + height - panel.getTextureHeight() /3.2);
            glTexCoord2d(0.6875, 0.6875);
            glVertex2d(0 + width - panel.getTextureWidth() /3.2, 0 + height - panel.getTextureHeight() /3.2);
            glTexCoord2d(0.6875, 0.3125);
            glVertex2d(0 + width - panel.getTextureWidth() /3.2, 0 + panel.getTextureHeight() /3.2);
            glTexCoord2d(1, 0.3125);
            glVertex2d(0 + width, 0 + panel.getTextureHeight() /3.2);
        }
        {
            glTexCoord2d(0, 0.3125);
            glVertex2d(0, 0 + panel.getTextureHeight() /3.2);
            glTexCoord2d(0.3125, 0.3125);
            glVertex2d(0 + panel.getTextureWidth() /3.2,  0 + panel.getTextureHeight() /3.2);
            glTexCoord2d(0.3125, 0.6875);
            glVertex2d(0 + panel.getTextureWidth() /3.2, 0 + height - panel.getTextureHeight() /3.2);
            glTexCoord2d(0, 0.6875);
            glVertex2d(0, 0 + height - panel.getTextureHeight() /3.2);
        }
        //Panel
        {
            glTexCoord2d(0.3125, 0.3125);
            glVertex2d(0 + panel.getTextureWidth() /3.2, 0 + panel.getTextureHeight() /3.2);
            glTexCoord2d(0.6875, 0.3125);
            glVertex2d(0 + width - panel.getTextureWidth() /3.2,  0 + panel.getTextureHeight() /3.2);
            glTexCoord2d(0.6875, 0.6875);
            glVertex2d(0 + width - panel.getTextureWidth() /3.2, 0 + height - panel.getTextureHeight() /3.2);
            glTexCoord2d(0.3125, 0.6875);
            glVertex2d(0 + panel.getTextureWidth() /3.2, 0 + height - panel.getTextureHeight() /3.2);
        }
        glEnd();
        for (JElement je : jelement) {
            je.render();
        }
        glTranslated(-x, -y, 0);
    }

    public void add(JElement e) {
        e.parent = this;
        jelement.add(e);
    }

    public void remove(JElement e) {
        e.parent = null;
        jelement.remove(e);
    }

    public JPanel(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    boolean isPressed(int x, int y, int mouseButton) {
        for (JElement e : jelement) {
            if (e.isPressed(x - this.x, y - this.y, mouseButton)) {
                return true;
            }
        }
        boolean is = (x >= this.x && y >= this.y && x <= this.x + width && y <= this.y + height);
        if(is)pressed(x, y, mouseButton);
        return is;
    }

    @Override
    boolean isReleased(int x, int y, int mouseButton) {
        
        for (JElement e : jelement) {
            if (e.isReleased(x - this.x, y - this.y, mouseButton)) {
                return true;
            }
        }
        boolean is = (x >= this.x && y >= this.y && x <= this.x + width && y <= this.y + height);
        if(is)released(x, y, mouseButton);
        return is;
    }

    @Override
    boolean isHover(int x, int y) {
        
        for (JElement e : jelement) {
            if (e.isHover(x - this.x, y - this.y)) {
                return true;
            }
        }
        boolean is = (x >= this.x && y >= this.y && x <= this.x + width && y <= this.y + height);
        if(is)hover(x, y);
        return is;
    }

}
