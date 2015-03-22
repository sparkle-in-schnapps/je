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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import jplatformer.core.gui.*;
import jplatformer.core.jcfg.JCFG;
import jplatformer.core.jcfg.Parameter;
import jplatformer.core.jcfg.Parser;
import jplatformer.core.jcfg.Writer;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

/**
 *
 * @author yew_mentzaki
 */
public class JMainMenu {

    private static JBlock selected;

    private static boolean showed, inited;
    private static JPanel mainMenu = new JPanel(0, 200, 200, 300) {
        @Override
        public void render() {
            x = JPlatformer.display_width / 2 - 100;
            super.render();
        }

    };
    private static JPanel settingsMenu = new JPanel(0, 100, 420, 400) {
        @Override
        public void render() {
            x = JPlatformer.display_width / 2 - 210;
            super.render();
        }

    };
    static private int mode = 0;

    static private void blocks() {
        mode = 0;
        worldEditorMenu.remove(blockPanel);
        worldEditorMenu.remove(entitiesPanel);
        worldEditorMenu.remove(mapsPanel);
        mode = 1;
        worldEditorMenu.add(blockPanel);
    }

    static private void entities() {
        mode = 0;
        worldEditorMenu.remove(blockPanel);
        worldEditorMenu.remove(entitiesPanel);
        worldEditorMenu.remove(mapsPanel);
        mode = 2;
        worldEditorMenu.add(entitiesPanel);
    }

    static private void maps() {
        mode = 0;
        worldEditorMenu.remove(blockPanel);
        worldEditorMenu.remove(entitiesPanel);
        worldEditorMenu.remove(mapsPanel);
        mode = 3;
        worldEditorMenu.add(mapsPanel);
    }
    private static JPanel blockPanel = new JPanel(20, 146, 160, 192) {

        @Override
        public void render() {
            height = Display.getHeight() - 248;
            super.render(); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void released(int x, int y, int mouseButton) {

        }

    };
    private static JPanel entitiesPanel = new JPanel(20, 146, 160, 192) {

        @Override
        public void render() {
            height = Display.getHeight() - 248;
            super.render(); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void released(int x, int y, int mouseButton) {

        }

    };

    private static JPanel mapsPanel = new JPanel(20, 146, 160, 192) {

        @Override
        public void render() {
            height = Display.getHeight() - 248;
            super.render(); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void released(int x, int y, int mouseButton) {

        }

    };

    private static ArrayList<Class<?>> getClassesForPackage(Package pkg) {
        String pkgname = pkg.getName();
        ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
        // Get a File object for the package
        File directory = null;
        String fullPath;
        String relPath = pkgname.replace('.', '/');
        URL resource = ClassLoader.getSystemClassLoader().getResource(relPath);
        if (resource == null) {
            throw new RuntimeException("No resource for " + relPath);
        }
        fullPath = resource.getFile();

        try {
            directory = new File(resource.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(pkgname + " (" + resource + ") does not appear to be a valid URL / URI.  Strange, since we got it from the system...", e);
        } catch (IllegalArgumentException e) {
            directory = null;
        }

        if (directory != null && directory.exists()) {
            // Get the list of the files contained in the package
            String[] files = directory.list();
            for (int i = 0; i < files.length; i++) {
                // we are only interested in .class files
                if (files[i].endsWith(".class")) {
                    // removes the .class extension
                    String className = pkgname + '.' + files[i].substring(0, files[i].length() - 6);
                    try {
                        classes.add(Class.forName(className));
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException("ClassNotFoundException loading " + className);
                    }
                }
            }
        } else {
            try {
                String jarPath = fullPath.replaceFirst("[.]jar[!].*", ".jar").replaceFirst("file:", "");
                JarFile jarFile = new JarFile(jarPath);
                Enumeration<JarEntry> entries = jarFile.entries();
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    String entryName = entry.getName();
                    if (entryName.startsWith(relPath) && entryName.length() > (relPath.length() + "/".length())) {
                        String className = entryName.replace('/', '.').replace('\\', '.').replace(".class", "");
                        try {
                            classes.add(Class.forName(className));
                        } catch (ClassNotFoundException e) {
                            throw new RuntimeException("ClassNotFoundException loading " + className);
                        }
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(pkgname + " (" + directory + ") does not appear to be a valid package", e);
            }
        }
        return classes;
    }
    private static JPanel worldEditorMenu = new JPanel(20, 20, 200, 400) {
        boolean start = true;

        @Override
        public void render() {
            if (start) {
                start = false;

                int y = 10;
                for (final JBlock jb : JPlatformer.je.block_type) {
                    if (jb == null) {
                        break;
                    }
                    blockPanel.add(new JButton(jb.name, 10, y, 140, 22) {

                        @Override
                        public void released(int x, int y, int mouseButton) {
                            JMainMenu.selected = jb;
                        }

                    });
                    y += 24;
                }
                y = 10;
                for (final Class entityClass : getClassesForPackage(Package.getPackage(JPlatformer.game.get("package").getValueAsString()))) {
                    if (!JEntity.class.isAssignableFrom(entityClass)) {
                        continue;
                    } else {
                        entitiesPanel.add(new JButton(entityClass.getSimpleName(), 10, y, 140, 22) {

                            @Override
                            public void released(int x, int y, int mouseButton) {
                                try {
                                    JEntity entity = (JEntity) entityClass.newInstance();
                                    entity.x = JPlatformer.je.camera.x;
                                    entity.y = JPlatformer.je.camera.y;
                                    JPlatformer.je.add(entity);
                                } catch (InstantiationException ex) {
                                    Logger.getLogger(JMainMenu.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (IllegalAccessException ex) {
                                    Logger.getLogger(JMainMenu.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }

                        });
                        y += 24;
                    }
                }

            }
            if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
                JPlatformer.je.camera_target.x += 15;
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
                JPlatformer.je.camera_target.x -= 15;
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
                JPlatformer.je.camera_target.y -= 15;
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
                JPlatformer.je.camera_target.y += 15;
            }
            if (mode == 1) {
                if (Mouse.isButtonDown(0)) {
                    JPlatformer.je.camera.x -= Mouse.getDX();
                    JPlatformer.je.camera.y += Mouse.getDY();
                    JPlatformer.je.camera_target.x = JPlatformer.je.camera.x;
                    JPlatformer.je.camera_target.y = JPlatformer.je.camera.y;

                } else if (Mouse.isButtonDown(1)) {
                    JPlatformer.je.setBlock(JPlatformer.je.camera.x - Display.getWidth() / 2 + Mouse.getX(), JPlatformer.je.camera.y + Display.getHeight() / 2 - Mouse.getY(), JMainMenu.selected);
                } else if (Mouse.isButtonDown(2)) {
                    JPlatformer.je.setBack(JPlatformer.je.camera.x - Display.getWidth() / 2 + Mouse.getX(), JPlatformer.je.camera.y + Display.getHeight() / 2 - Mouse.getY(), JMainMenu.selected);
                }
            } else if (mode == 2) {
                if (Mouse.isButtonDown(0)) {
                    JEntity[] entities = JPlatformer.je.objects(new Point(JPlatformer.je.camera.x+Mouse.getX()-Display.getWidth()/2,JPlatformer.je.camera.y-Mouse.getY()+Display.getHeight()/2));
                    if(entities.length>0&&JMath.dist(JPlatformer.je.camera.x+Mouse.getX()-Display.getWidth()/2-entities[0].x,JPlatformer.je.camera.y-Mouse.getY()+Display.getHeight()/2-entities[0].y)<32){
                    entities[0].x += Mouse.getDX();
                    entities[0].y -= Mouse.getDY();
                    }else{
                    JPlatformer.je.camera.x -= Mouse.getDX();
                    JPlatformer.je.camera.y += Mouse.getDY();
                    JPlatformer.je.camera_target.x = JPlatformer.je.camera.x;
                    JPlatformer.je.camera_target.y = JPlatformer.je.camera.y;
                    }
                }
            }
            height = Display.getHeight() - 40;
            super.render();
        }

    };

    private static void init() {

        if (!inited) {
            mainMenu.add(new JButton("Play", 20, 20, 160, 32) {

                @Override
                public void released(int x, int y, int mouseButton) {

                }

            });

            mainMenu.add(new JButton("Settings", 20, 62, 160, 32) {

                @Override
                public void released(int x, int y, int mouseButton) {
                    JPlatformer.gui.remove(mainMenu);
                    JPlatformer.gui.add(settingsMenu);
                }

            });

            if (JPlatformer.game.get("mapeditor").getValueAsBoolean()) {
                mainMenu.add(new JButton("Map editor", 20, 104, 160, 32) {

                    @Override
                    public void released(int x, int y, int mouseButton) {
                        JPlatformer.gui.remove(mainMenu);
                        JPlatformer.gui.add(worldEditorMenu);
                    }

                });

            }

            mainMenu.add(new JButton("Exit", 20, 248, 160, 32) {

                @Override
                public void released(int x, int y, int mouseButton) {
                    System.exit(0);
                }

            });

            settingsMenu.add(new JButton("Back", 20, 348, 160, 32) {

                @Override
                public void released(int x, int y, int mouseButton) {
                    try {
                        Writer.writeToFile(JPlatformer.settings, new File("cfg/settings.cfg"));
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(JMainMenu.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    JPlatformer.gui.remove(settingsMenu);
                    JPlatformer.gui.add(mainMenu);
                }

            });

            worldEditorMenu.add(new JButton("Back", 20, 348, 160, 32) {

                @Override
                public void render() {
                    y = Display.getHeight() - 92;
                    super.render();
                }

                @Override
                public void released(int x, int y, int mouseButton) {
                    JPlatformer.gui.remove(worldEditorMenu);
                    JPlatformer.gui.add(mainMenu);
                }

            });

            worldEditorMenu.add(new JButton("Maps", 20, 20, 160, 32) {

                @Override
                public void released(int x, int y, int mouseButton) {
                    maps();
                }

            });

            worldEditorMenu.add(new JButton("Entities", 20, 62, 160, 32) {

                @Override
                public void released(int x, int y, int mouseButton) {
                    entities();

                }

            });

            worldEditorMenu.add(new JButton("Blocks", 20, 104, 160, 32) {

                @Override
                public void released(int x, int y, int mouseButton) {
                    blocks();
                }

            });

            int y = 20;
            try {
                JCFG settings = Parser.parse(new File("cfg/settings_menu.cfg"));
                for (final Parameter p : settings.list()) {
                    if (p.getValueAsString().contains("track")) {
                        String c[] = p.getValueAsString().substring(6).split("-");
                        JLabel jl = new JLabel(p.getName(), 20, y, 380, 32);
                        JTrackBar jt = new JTrackBar(Integer.parseInt(c[0]), Integer.parseInt(c[1]), 20, y + 32, 380, 16) {

                            @Override
                            public void valueChanged() {
                                JPlatformer.settings.get(p.getName()).setValue(value);
                            }

                        };
                        jt.setValue(JPlatformer.settings.get(p.getName()).getValueAsInteger());
                        y += 58;
                        settingsMenu.add(jl);
                        settingsMenu.add(jt);
                    }
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(JMainMenu.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    public static void hide() {
        if (showed) {
            JPlatformer.gui.remove(mainMenu);
            showed = false;
        }
    }

    public static void show() {
        init();
        if (!showed) {
            JPlatformer.gui.add(mainMenu);
            showed = true;
        }
    }
}
