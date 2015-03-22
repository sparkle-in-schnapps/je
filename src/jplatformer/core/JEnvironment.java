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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Timer;
import static org.lwjgl.opengl.GL11.*;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.reflections.Reflections;
import org.json.*;
import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Image;

/**
 *
 * @author yew_mentzaki
 */
public class JEnvironment {

    /**
     * Work with blocks
     */
    public int block_size = 64;
    public final JBlock[] block_type = new JBlock[1024];
    private short blocks[][] = new short[512][512];
    private short blocks_back[][] = new short[512][512];

    public void setBlock(double location_x, double location_y, JBlock block) {

        int x = (int) Math.round(location_x / (float) block_size);
        int y = (int) Math.round(location_y / (float) block_size);

        if (x >= 0 & y >= 0 & x < 512 & y < 512) {
            for (int id = 0; id < block_type.length; id++) {
                if (block == block_type[id]) {
                    blocks[x][y] = (short) id;
                    break;
                }
            }
        }
    }

    public void setBack(double location_x, double location_y, JBlock block) {

        int x = (int) Math.round(location_x / (float) block_size);
        int y = (int) Math.round(location_y / (float) block_size);

        if (x >= 0 & y >= 0 & x < 512 & y < 512) {
            for (int id = 0; id < block_type.length; id++) {
                if (block == block_type[id]) {
                    blocks_back[x][y] = (short) id;
                    break;
                }
            }
        }
    }

    public JBlock getBlock(double location_x, double location_y) {

        int x = (int) Math.round(location_x / (float) block_size);
        int y = (int) Math.round(location_y / (float) block_size);

        if (x >= 0 & y >= 0 & x < 512 & y < 512) {

            return block_type[blocks[x][y]];
        } else {
            return block_type[0];
        }
    }

    public JBlock getBack(double location_x, double location_y) {

        int x = (int) Math.round(location_x / (float) block_size);
        int y = (int) Math.round(location_y / (float) block_size);

        if (x >= 0 & y >= 0 & x < 512 & y < 512) {

            return block_type[blocks_back[x][y]];
        } else {
            return block_type[0];
        }
    }

    /**
     * Work with objects
     */
    public final Point camera_target = new Point();
    final Point camera = new Point();
    private ArrayList<JEntity> objects = new ArrayList<>();
    private ArrayList<JLiquid> liquids = new ArrayList<>();

    private ActionListener tickListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent ae) {
            tick();
        }
    };
    private ActionListener camListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent ae) {
            camera.x = (camera.x * 29 + camera_target.x) / 30;
            camera.y = (camera.y * 29 + camera_target.y) / 30;

        }
    };

    private Timer timer = new Timer(1000 / 100, tickListener);
    private Timer timer_cam = new Timer(1000 / 100, camListener);

    public JEnvironment() {
        timer_cam.start();
        try {
            String blocks_json_line = new String();
            Scanner scanner = new Scanner(new File("cfg/blocks.json"));
            while (scanner.hasNextLine()) {
                blocks_json_line += scanner.nextLine();
            }
            JSONObject blocks_json = new JSONObject(blocks_json_line);
            JSONArray blocks = blocks_json.getJSONArray("blocks");
            for (int i = 0; i < blocks.length(); i++) {
                JSONObject block = blocks.getJSONObject(i);
                block_type[i] = new JBlock(
                        block.getString("name"),
                        block.getBoolean("solid"),
                        Color.decode(block.getString("color")),
                        block.getString("texture")
                );
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(JEnvironment.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void pause() {
        if (!timer.isRunning()) {
            timer.start();
        } else {
            timer.stop();
        }
    }

    public JEntity[] objects() {
        ArrayList<JEntity> jo = new ArrayList<>();
        double dist = JMath.dist(JPlatformer.display_height / 2, JPlatformer.display_width / 2);
        int i = 0;
        while (i < objects.size()) {
            if (JMath.dist(camera.x - objects.get(i).x, camera.y - objects.get(i).y) < dist) {
                jo.add(objects.get(i));
            }
            i++;
        }
        JEntity[] ao = new JEntity[jo.size()];
        for (int j = 0; j < ao.length; j++) {
            ao[j] = jo.get(j);
        }
        return ao;
    }

    public JEntity[] objects(Point p) {
        ArrayList<JEntity> jo = new ArrayList<>();
        ArrayList<Double> io = new ArrayList<>();

        double dist = JMath.dist(JPlatformer.display_height / 2, JPlatformer.display_width / 2);
        {
            int i = 0;
            while (i < objects.size()) {
                if (JMath.dist(camera.x - objects.get(i).x, camera.y - objects.get(i).y) < dist) {
                    jo.add(objects.get(i));
                    io.add(new Double(JMath.dist(p.x - objects.get(i).x, p.y - objects.get(i).y)));
                }
                i++;
            }
        }
        for (int i = 0; i < io.size() - 1; i++) {
            for (int j = 0; j < io.size() - i - 1; j++) {
                if (io.get(j) > io.get(j + 1)) {
                    Collections.swap(io, j, j + 1);
                    Collections.swap(jo, j, j + 1);
                }
            }
        }
        JEntity[] ao = new JEntity[jo.size()];
        for (int j = 0; j < ao.length; j++) {
            ao[j] = jo.get(j);
        }
        return ao;
    }

    public JLiquid[] liquids(Point p) {
        ArrayList<JLiquid> jo = new ArrayList<>();
        ArrayList<Double> io = new ArrayList<>();

        double dist = JMath.dist(JPlatformer.display_height / 2, JPlatformer.display_width / 2);
        {
            int i = 0;
            while (i < liquids.size()) {
                if (liquids.get(i) != null) {
                    if (JMath.dist(p.x - liquids.get(i).x, p.y - liquids.get(i).y) < dist) {
                        jo.add(liquids.get(i));
                        io.add(new Double(JMath.dist(p.x - liquids.get(i).x, p.y - liquids.get(i).y)));
                    }
                }
                i++;
            }
        }
        for (int i = 0; i < io.size() - 1; i++) {
            for (int j = 0; j < io.size() - i - 1; j++) {
                if (io.get(j) > io.get(j + 1)) {
                    Collections.swap(io, j, j + 1);
                    Collections.swap(jo, j, j + 1);
                }
            }
        }
        JLiquid[] ao = new JLiquid[jo.size()];
        for (int j = 0; j < ao.length; j++) {
            ao[j] = jo.get(j);
        }
        return ao;
    }

    public void add(JEntity jo) {
        jo.environment = this;
        objects.add(jo);
    }

    public void add(JLiquid jo) {
        jo.environment = this;
        if (liquids.size() < 120) {
            liquids.add(jo);
        } else {
            liquids.set(r.nextInt(120), jo);
        }
    }
    public Random r = new Random();

    public void remove(JEntity jo) {
        jo.environment = null;
        objects.remove(jo);
    }

    public void tick() {

        for (JEntity object : objects()) {
            object.tick();
        }
        ArrayList<JLiquid> remove = new ArrayList<>();
        for (JLiquid object : liquids(camera)) {
            object.tick();
            if (object.timer < 0) {
                remove.add(object);
            }
        }
        for (JLiquid object : remove) {
            liquids.remove(object);
        }

    }
    Graphics g = new Graphics();
    Image sky, parallax;

    public void render() {

        try {

            Point camera = new Point(this.camera.x, this.camera.y);

            int h = JPlatformer.display_height;
            if (sky == null) {
                sky = new JImage("world/background_sky.png");
                sky.setFilter(GL_NEAREST);
                parallax = new JImage("world/background_parallax.png");
                parallax.setFilter(GL_NEAREST);
            }

            for (double i = -camera.x / 3 - JPlatformer.display_width / 2.0 * 3.0; i < camera.x / 3 + JPlatformer.display_width / 2.0 * 3.0; i += h) {
                sky.draw((int) i, 0, h, h);
            }
            for (double i = -camera.x / 2 - JPlatformer.display_width / 2.0 * 3.0; i < camera.x / 2 + JPlatformer.display_width / 2.0 * 3.0; i += h) {
                parallax.draw((int) i, 0, h, h);
            }
            glTranslated(JPlatformer.display_width / 2 - camera.x, JPlatformer.display_height / 2 - camera.y, 0);

            for (int x = -JPlatformer.display_width / 2 / block_size - 2 + (camera.x / block_size); x < JPlatformer.display_width / 2 / block_size + 4 + (camera.x / block_size); x++) {
                for (int y = -JPlatformer.display_height / 2 / block_size - 2 + (camera.y / block_size); y < JPlatformer.display_height / 2 / block_size + 4 + (camera.y / block_size); y++) {

                    getBack(x * block_size, y * block_size).render(x * block_size, y * block_size, false);

                    getBlock(x * block_size, y * block_size).render(x * block_size, y * block_size, true);

                }
            }
            for (JLiquid object : liquids(camera)) {
                double x = object.x, y = object.y;
                glTranslated(x, y, 0);
                object.render();
                glTranslated(-x, -y, 0);
            }

            for (JEntity object : objects()) {
                double x = object.x, y = object.y;
                glTranslated(x, y, 0);
                object.render(g);
                glTranslated(-x, -y, 0);
            }
            glTranslated(camera.x - JPlatformer.display_width / 2, camera.y - JPlatformer.display_height / 2, 0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
