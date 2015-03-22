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

import java.io.Serializable;
import org.newdawn.slick.Color;

/**
 *
 * @author yew_mentzaki
 */
public class JEntity implements Serializable {

    public double x, y, vx, vy;
    public JEnvironment environment;

    public void init(Object... args) {
    }

    public void tick() {
    }

    public void render(org.newdawn.slick.Graphics g) {
        g.setColor(Color.green);
        g.fillOval(-32, -32, 64, 64);
    }

}
