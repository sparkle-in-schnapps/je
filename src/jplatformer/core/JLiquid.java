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

import java.awt.Point;
import static jplatformer.core.JBlock.g;
import static org.lwjgl.opengl.GL11.*;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

/**
 *
 * @author yew_mentzaki
 */
public class JLiquid {

    public JEnvironment environment;
    public double x, y, vx, vy, size = 16;
    public int timer = 500;

    public int sing(double value) {
        if (value != 0) {
            return (int) (value / Math.abs(value));
        } else {
            return 0;
        }
    }

    public JLiquid(double x, double y, double vx, double vy) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
    }

    public JLiquid(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void tick() {
        timer -= 2;
        if(JMath.dist(vx, vy)<0.5&&timer<=10){
            timer=-5;
            if(environment.getBlock(x, y)==environment.block_type[0])
               environment.setBlock(x, y, environment.block_type[1]); 
        }
        vy += 0.15;
        boolean restore = true;
        for (JLiquid l : environment.liquids(new Point((int) x, (int) y))) {
            double dist = JMath.dist(x - l.x, y - l.y);
            if (dist < 32) {
                if (restore) {
                    restore = false;
                    timer++;
                }
                dist = (32 - dist) / 8;

                double a = Math.atan2(l.y - y, l.x - x);
                vx -= Math.cos(a) * 0.1;
                l.vx += Math.cos(a) * 0.1;
                vy -= Math.sin(a) * 0.1;
                l.vy += Math.sin(a) * 0.1;
                x -= dist * Math.cos(a);
                l.x += dist * Math.cos(a);
                y -= dist * Math.sin(a);
                l.y += dist * Math.sin(a);

            } else {
                break;
            }
        }

        if (environment.getBlock(x + sing(vx) * size / 2, y + vy + sing(vy) * size / 2).solid) {
            vy = -vy * 0.1;
        }

        if (environment.getBlock(x, y + 8).solid) {
            y--;
        }

        if (environment.getBlock(x + vx + sing(vx) * size / 2, y + sing(vy) * size / 2).solid) {
            vx = -vx * 0.1;
        }

        vx *= 0.99;

        x += vx;
        y += vy;
    }
    Texture i;

    public void render() {
        if (i == null) {
            i = (JTextureManager.getTexture("tiles/water.png"));
        }
        JLiquid[] jl = environment.liquids(new Point((int) x, (int) y));
        g.setColor(Color.white);
        //g.fillRect(-1, -1, 1, 1);
        for (int j = 0; j < jl.length - 2; j++) {
            double dist = Math.max(JMath.dist(jl[1 + j].x - x, jl[1 + j].y - y), JMath.dist(jl[2 + j].x - x, jl[2 + j].y - y));
            if (dist < 64) {
                i.bind();
                    glColor3d(1, 1, 1);
                
                glBegin(GL_POLYGON);
                glTexCoord2d(0, 0);
                glVertex2d(0, 8);
                glTexCoord2d(1, 0);
                glVertex2d(jl[1 + j].x - x, jl[1 + j].y - y + 8);
                glTexCoord2d(1, 1);
                glVertex2d(jl[2 + j].x - x, jl[2 + j].y - y + 8);
                glEnd();
            } else {
                break;
            }
        }

    }
}
