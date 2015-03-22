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
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

/**
 *
 * @author yew_mentzaki
 */
public class JGUI {

    boolean j[] = new boolean[3];

    public void render() {
        for (JElement e : jelement) {
            e.render();

        }
        int mx = Mouse.getX();
        int my = Display.getHeight() - Mouse.getY();

        for (int i = 0; i < 3; i++) {
            if (Mouse.isButtonDown(i)) {
                j[i] = true;
                for (JElement e : jelement) {
                    e.isPressed(mx, my, i);
                }
            } else if (j[i]) {
                j[i] = false;
                for (JElement e : jelement) {
                    e.isReleased(mx, my, i);
                }
            }
        }
        for (JElement e : jelement) {
            e.isHover(mx, my);
        }
    }

    public void add(JElement e) {
        e.parent = null;
        jelement.add(e);
    }

    public void remove(JElement e) {
        e.parent = null;
        jelement.remove(e);
    }

    private ArrayList<JElement> jelement = new ArrayList<JElement>();

    private void isPressed(int x, int y, int mouseButton) {
        for (JElement e : jelement) {
            if (e.isPressed(x, y, mouseButton)) {
                return;
            }
        }
    }

    private void isReleased(int x, int y, int mouseButton) {

        for (JElement e : jelement) {
            if (e.isReleased(x, y, mouseButton)) {
                return;
            }
        }
    }

    private void isHover(int x, int y) {

        for (JElement e : jelement) {
            if (e.isHover(x, y)) {
                return;
            }
        }
    }
}
