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
public class JLabel extends JElement {

    public int width, height;
    public String text;
    private ArrayList<JElement> jelement = new ArrayList<JElement>();
    
    boolean hover, press;

    @Override
    public void render() {
        
        glTranslated(x, y, 0);
        glColor3f(1, 1, 1);
        
        int w = JPlatformer.jTextRender.getWidth(text);
        JPlatformer.jTextRender.drawString(text, 0 + width / 2 - w / 2, 0 + height / 2 - JPlatformer.jTextRender.getFont().getSize(), Color.black);
        glTranslated(-x, -y, 0);
    }

    public void add(JElement e) {
        jelement.add(e);
    }

    public void remove(JElement e) {
        jelement.remove(e);
    }

    public JLabel(String text, int x, int y, int width, int height) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    boolean isPressed(int x, int y, int mouseButton) {
        boolean is = (x >= this.x && y >= this.y && x <= this.x + width && y <= this.y + height);
        if(is) {
            press = true;
            pressed(x, y, mouseButton);
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
        return is;
    }

}
