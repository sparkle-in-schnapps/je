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

/**
 *
 * @author yew_mentzaki
 */
public class JElement {
    public JElement parent;
    public boolean visible, enabled, selected;
    public int x, y;
    
    public int x(){
        if(parent!=null)return x + parent.x();
        else return x;
    }
    public int y(){
        if(parent!=null)return y + parent.y();
        else return y;
    }
    
    
    public void render(){
        
    }
    public void released(int x, int y, int mouseButton){
        
    }
    public void pressed(int x, int y, int mouseButton){
        
    }
    public void hover(int x, int y){
        
    }
    boolean isReleased(int x, int y, int mouseButton){
        return false;
    }
    boolean isPressed(int x, int y, int mouseButton){
        return false;
    }
    boolean isHover(int x, int y){
        return false;
    }
}
