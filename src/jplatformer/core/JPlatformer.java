package jplatformer.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import jplatformer.core.gui.*;
import jplatformer.core.jcfg.*;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import static org.lwjgl.opengl.GL11.*;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.ImageIOImageData;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.reflections.Reflections;

/**
 *
 * @author yew_mentzaki
 */
public class JPlatformer {

    public static VTextRender jTextRender;
    public static JGUI gui = new JGUI();
    public static int display_width = 800, display_height = 600;
    public static jplatformer.core.jcfg.JCFG settings, game;
    public static final JEnvironment je = new JEnvironment();

    

    public static void main(String[] args) {
        try {
            settings = Parser.parse(new File("cfg/settings.cfg"));
            game = Parser.parse(new File("cfg/game.cfg"));
            System.out.println(JPlatformer.game.get("package").getValueAsString());
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(JPlatformer.class.getName()).log(Level.SEVERE, null, ex);
        }

        setUpNatives();
        setUpGraphics(new JRenderTask() {

            @Override
            public void render() {
                je.render();

            }
        });
        Random r = new Random();
        int h = 500;
        for (int i = 0; i < 512 * 16; i += 16) {

            h += r.nextInt(32) - 16;
            for (int j = 0; j < h; j += 16) {
                je.setBlock(i, 800 - j, je.block_type[1]);
            }
        }

    }

    public static void setUpNatives() {
        if (!new File("native").exists()) {
            JOptionPane.showMessageDialog(null, "Error!\nNative libraries not found!");
            System.exit(1);
        }
        try {

            System.setProperty("java.library.path", new File("native").getAbsolutePath());

            Field fieldSysPath = ClassLoader.class.getDeclaredField("sys_paths");
            fieldSysPath.setAccessible(true);

            try {
                fieldSysPath.set(null, null);
            } catch (IllegalArgumentException ex) {

                JOptionPane.showMessageDialog(null, "Error!\n" + ex.toString());
                System.exit(1);
            } catch (IllegalAccessException ex) {
                JOptionPane.showMessageDialog(null, "Error!\n" + ex.toString());
                System.exit(1);
            }
        } catch (NoSuchFieldException ex) {
            JOptionPane.showMessageDialog(null, "Error!\n" + ex.toString());
            System.exit(1);
        } catch (SecurityException ex) {
            JOptionPane.showMessageDialog(null, "Error!\n" + ex.toString());
            System.exit(1);
        }
    }

    public static void setUpGraphics(final JRenderTask renderTask) {
        Thread renderingThread;
        renderingThread = new Thread("Main Rendering Thread") {

            @Override
            public void run() {
                try {
                    JCFG window = new JCFG();
                    int w = 800, h = 600, x = 0, y = 0;
                    try {
                        window = Parser.parse(new File("cfg/window.cfg"));
                        w = window.get("width").getValueAsInteger();
                        h = window.get("height").getValueAsInteger();
                        x = window.get("x").getValueAsInteger();
                        y = window.get("y").getValueAsInteger();

                    } catch (Exception ex) {

                    }

                    Display.setDisplayMode(new DisplayMode(w, h));
                    Display.setLocation(x, y);
                    Display.setTitle(game.get("title").getValueAsString());
                    Display.setFullscreen(true);
                    Display.setResizable(true);
                    try {
                        Display.setIcon(new ByteBuffer[]{
                            new ImageIOImageData().imageToByteBuffer(ImageIO.read(new File("res/engine/icon.png")), false, false, null),
                            new ImageIOImageData().imageToByteBuffer(ImageIO.read(new File("res/engine/icon.png")), false, false, null)
                        });
                    } catch (IOException ex) {
                        Logger.getLogger(JPlatformer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    Display.create();
                    GL11.glMatrixMode(GL11.GL_PROJECTION);
                    GL11.glLoadIdentity();
                    GL11.glOrtho(0, 1, 1, 0, 1, -1);
                    GL11.glMatrixMode(GL11.GL_MODELVIEW);

                    Random r = new Random();

                    long mc = 0;
                    boolean loaded = false;
                    Texture background = null;
                    Texture logo = null;
                    Texture gamelogo = null;

                    try {
                        background = TextureLoader.getTexture("PNG", getClass().getResourceAsStream("background.png"));
                        logo = TextureLoader.getTexture("PNG", getClass().getResourceAsStream("logo.png"));
                        gamelogo = TextureLoader.getTexture("PNG", new FileInputStream("res/engine/logo.png"));
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Error!\n" + ex.toString());
                        System.exit(1);
                    }
                    {
                        display_width = Display.getWidth();
                        display_height = Display.getHeight();

                        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
                        GL11.glEnable(GL11.GL_TEXTURE_2D);
                        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                        GL11.glEnable(GL11.GL_BLEND);
                        GL11.glMatrixMode(GL11.GL_PROJECTION);
                        GL11.glLoadIdentity();
                        GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
                        GL11.glOrtho(0, Display.getWidth(), Display.getHeight(), 0, 1, -1);
                        GL11.glMatrixMode(GL11.GL_MODELVIEW);
                        glColor4f(1, 1, 1, 1);
                        background.bind();
                        glBegin(GL_QUADS);
                        glTexCoord2d(0, 0);
                        glVertex2d(0, 0);
                        glTexCoord2d(background.getWidth(), 0);
                        glVertex2d(display_width, 0);
                        glTexCoord2d(background.getWidth(), background.getHeight());
                        glVertex2d(display_width, display_height);
                        glTexCoord2d(0, background.getHeight());
                        glVertex2d(0, display_height);
                        glEnd();
                        logo.bind();
                        glBegin(GL_QUADS);
                        glTexCoord2d(0, 0);
                        glVertex2d(display_width - logo.getImageWidth(), 0);
                        glTexCoord2d(logo.getWidth(), 0);
                        glVertex2d(display_width, 0);
                        glTexCoord2d(logo.getWidth(), logo.getHeight());
                        glVertex2d(display_width, logo.getImageHeight());
                        glTexCoord2d(0, logo.getHeight());
                        glVertex2d(display_width - logo.getImageWidth(), logo.getImageHeight());
                        glEnd();
                        gamelogo.bind();
                        glBegin(GL_QUADS);
                        glTexCoord2d(0, 0);
                        glVertex2d(display_width / 2 - gamelogo.getImageWidth() / 2, display_height / 2 - gamelogo.getImageHeight() / 2);
                        glTexCoord2d(gamelogo.getWidth(), 0);
                        glVertex2d(display_width / 2 + gamelogo.getImageWidth() / 2, display_height / 2 - gamelogo.getImageHeight() / 2);
                        glTexCoord2d(gamelogo.getWidth(), gamelogo.getHeight());
                        glVertex2d(display_width / 2 + gamelogo.getImageWidth() / 2, display_height / 2 + gamelogo.getImageHeight() / 2);
                        glTexCoord2d(0, gamelogo.getHeight());
                        glVertex2d(display_width / 2 - gamelogo.getImageWidth() / 2, display_height / 2 + gamelogo.getImageHeight() / 2);
                        glEnd();

                        Display.update();
                        jTextRender = VTextRender.getTextRender("Sans", 0, 16);
                        ArrayList<JLoadTask> jlt = new ArrayList<JLoadTask>();
                        jlt.addAll(JTextureManager.load());
                        jlt.addAll(JMusicManager.load());

                        int jlt_size = jlt.size();
                        while (!jlt.isEmpty()) {
                            if (Display.isCloseRequested()) {
                                System.exit(0);
                            }

                            display_width = Display.getWidth();
                            display_height = Display.getHeight();

                            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
                            GL11.glEnable(GL11.GL_TEXTURE_2D);
                            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                            GL11.glEnable(GL11.GL_BLEND);
                            GL11.glMatrixMode(GL11.GL_PROJECTION);
                            GL11.glLoadIdentity();
                            GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
                            GL11.glOrtho(0, Display.getWidth(), Display.getHeight(), 0, 1, -1);
                            GL11.glMatrixMode(GL11.GL_MODELVIEW);
                            glColor4f(1, 1, 1, 1);
                            background.bind();
                            glBegin(GL_QUADS);
                            glTexCoord2d(0, 0);
                            glVertex2d(0, 0);
                            glTexCoord2d(background.getWidth(), 0);
                            glVertex2d(display_width, 0);
                            glTexCoord2d(background.getWidth(), background.getHeight());
                            glVertex2d(display_width, display_height);
                            glTexCoord2d(0, background.getHeight());
                            glVertex2d(0, display_height);
                            glEnd();
                            logo.bind();
                            glBegin(GL_QUADS);
                            glTexCoord2d(0, 0);
                            glVertex2d(display_width - logo.getImageWidth(), 0);
                            glTexCoord2d(logo.getWidth(), 0);
                            glVertex2d(display_width, 0);
                            glTexCoord2d(logo.getWidth(), logo.getHeight());
                            glVertex2d(display_width, logo.getImageHeight());
                            glTexCoord2d(0, logo.getHeight());
                            glVertex2d(display_width - logo.getImageWidth(), logo.getImageHeight());
                            glEnd();
                            gamelogo.bind();
                            glBegin(GL_QUADS);
                            glTexCoord2d(0, 0);
                            glVertex2d(display_width / 2 - gamelogo.getImageWidth() / 2, display_height / 2 - gamelogo.getImageHeight() / 2);
                            glTexCoord2d(gamelogo.getWidth(), 0);
                            glVertex2d(display_width / 2 + gamelogo.getImageWidth() / 2, display_height / 2 - gamelogo.getImageHeight() / 2);
                            glTexCoord2d(gamelogo.getWidth(), gamelogo.getHeight());
                            glVertex2d(display_width / 2 + gamelogo.getImageWidth() / 2, display_height / 2 + gamelogo.getImageHeight() / 2);
                            glTexCoord2d(0, gamelogo.getHeight());
                            glVertex2d(display_width / 2 - gamelogo.getImageWidth() / 2, display_height / 2 + gamelogo.getImageHeight() / 2);
                            glEnd();
                            glBindTexture(GL_TEXTURE_2D, 0);
                            glColor4f(0.5f, 0.5f, 0.5f, 0.35f);
                            glBegin(GL_QUADS);
                            glVertex2d(20, display_height - 40);
                            glVertex2d(display_width - 20, display_height - 40);
                            glVertex2d(display_width - 20, display_height - 20);
                            glVertex2d(20, display_height - 20);
                            glEnd();
                            jTextRender.drawString(jlt.get(0).name, 25, display_height - 64, Color.black);
                            glColor4f(0.0f, 0.0f, 0.0f, 1);
                            glBegin(GL_QUADS);
                            glVertex2d(25, display_height - 35);
                            glVertex2d(((double) (display_width - 50) * ((float) (jlt_size - jlt.size()) / (float) jlt_size)) + 25, display_height - 35);
                            glVertex2d(((double) (display_width - 50) * ((float) (jlt_size - jlt.size()) / (float) jlt_size)) + 25, display_height - 25);
                            glVertex2d(25, display_height - 25);
                            glEnd();
                            jTextRender.drawString(jlt.get(0).name, 25, display_height - 65, Color.black);
                            Display.update();
                            jlt.get(0).load();
                            jlt.remove(0);

                        }
                    }
                    boolean b = false;
                    JMusicManager.start();
                    JMainMenu.show();

                    while (!Display.isCloseRequested()) {
                        display_width = Display.getWidth();
                        display_height = Display.getHeight();
                        long tmc = new Date().getTime();
                        if (tmc - mc < 10) {
                            continue;
                        } else {
                            mc = tmc;
                        }

                        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
                        GL11.glEnable(GL11.GL_TEXTURE_2D);
                        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                        GL11.glEnable(GL11.GL_BLEND);
                        GL11.glMatrixMode(GL11.GL_PROJECTION);
                        GL11.glLoadIdentity();
                        GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
                        GL11.glOrtho(0, Display.getWidth(), Display.getHeight(), 0, 1, -1);
                        GL11.glMatrixMode(GL11.GL_MODELVIEW);
                        glLoadIdentity();
                        renderTask.render();
                        gui.render();

                        b = !b;
                        Display.update();
                    }
                    window.set("width", Display.getWidth());
                    window.set("height", Display.getHeight());
                    window.set("x", Display.getX());
                    window.set("y", Display.getY());
                    try {
                        Writer.writeToFile(window, new File("cfg/window.cfg"));
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(JPlatformer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    Display.destroy();
                    System.exit(0);
                } catch (LWJGLException ex) {
                    JOptionPane.showMessageDialog(null, "Error!\n" + ex.toString());
                    System.exit(1);
                }
            }

        };
        renderingThread.setPriority(Thread.MAX_PRIORITY);
        renderingThread.start();
    }
}
