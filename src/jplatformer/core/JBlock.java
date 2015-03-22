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
package jplatformer.core;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.opengl.Texture;

/**
 *
 * @author yew_mentzaki
 */
public class JBlock {

    public static final Graphics g = new Graphics();

    public JBlock(String name, boolean solid, Color color, String texture) {
        this.name = name;
        this.solid = solid;
        this.color = color;
        this.texture = texture;
    }

    public String name;
    public boolean solid;
    public Color color;
    public String texture;
    private Image i;

    public void render(int x, int y, boolean front) {
        if (texture != null) {
            if (texture.equals("none")) {
                texture = null;
                return;
            }
            if (i == null) {
                System.out.println(texture);
                System.out.println(JTextureManager.getTexture(texture));
                i = new Image(JTextureManager.getTexture(texture));
                i.setFilter(GL11.GL_NEAREST);
            }
            if (front) {
                i.setImageColor(1, 1, 1);
            } else {
                i.setImageColor(0.5f, 0.5f, 0.5f);
            }
            i.draw(x - 32, y - 32, 64, 64);
        } else {
        }
    }

    @Override
    public String toString() {
        return ("\"" + name + "\" is " + (solid ? "solid" : "phantom") + " block with " + color + " and " + texture);
    }

}
