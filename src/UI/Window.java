/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import UI.Components.Component;
import Game.Scene;
import java.util.ArrayList;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

/**
 *
 * @author Ashton
 */
public final class Window
{
    private float X, Y;
    private int Width, Height, StartX, StartY, Radius;
    private boolean isFirstRender, isVisible, isMouseDown, isPinned;
    private ArrayList<Component> Components = new ArrayList<>();
    private Input input;
    
    public Window(String Title, float X, float Y, int Width, int Height, int Radius,boolean ShowClose, boolean isPinned)
    {
        this.setLocation(X, Y);
        this.setSize(Width, Height, Radius);   
        
        this.isFirstRender = true;
        this.isVisible = true;
        this.isMouseDown = false;
        this.isPinned = isPinned;
        
        input = Scene.getInstance().input;
    }
    
    public Window(String Title, float X, float Y, int Width, int Height, boolean ShowClose, boolean isPinned)
    {
        this(Title, X, Y, Width, Height, 0, ShowClose, isPinned);
    }
    
    public Window(String Title, float X, float Y, int Width, int Height)
    {
        this(Title, X, Y, Width, Height, 10, true, false);
    }
    
    public void doAction(int ActionX, int ActionY)
    {
        for(Component component : Components)
            if(component.Contains(ActionX, ActionY))
                component.doAction();
    }
    
    public void addComponent(Component component)
    {
        Components.add(component);
    }
    
    public void removeComponent(Component component)
    {
        Components.remove(component);
    }
    
    public void setVisible(boolean isVisible)
    {
        this.isVisible = isVisible;
    }
    
    public boolean isVisible()
    {
        return isVisible;
    }
    
    public void setPinned(boolean isPinned)
    {
        this.isPinned = isPinned;
    }
    
    public boolean isPinned()
    {
        return isPinned;
    }
    
    public void setLocation(float X, float Y)
    {
        this.X = X;
        this.Y = Y;
        
        this.StartX = (int)X;
        this.StartY = (int)Y;
    }
    
    public void setSize(int Width, int Height, int Radius)
    {
        this.Width = Width;
        this.Height = Height;
        this.Radius = Radius;
    }
    
    private void Move(int X, int Y)
    {
        this.X += X;
        this.Y += Y;
        
        StartX = input.getMouseX();
        StartY = input.getMouseY();
        
        isFirstRender = false;
    }
    
    public boolean Contains(int PointX, int PointY)
    {
        if(PointX >= X && PointY >= Y && PointX <= (X+Width) && PointY <= (Y+Height))
            return true;
        else
            return false;
    }
    
    public void Render(Graphics g)
    {
        if(isVisible)
        {
            if(input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON))
            {
                if(Contains(input.getMouseX(), input.getMouseY()) || isMouseDown)
                {
                    WindowManager.requestFocus(this);
                    isMouseDown = true;

                    if(!isPinned)
                    {
                        if(isFirstRender)
                        {
                            StartX = input.getMouseX();
                            StartY = input.getMouseY();
                        }

                        Move(input.getMouseX() - StartX, input.getMouseY() - StartY);
                    }
                }
            }
            else
            {
                isMouseDown = false;
                isFirstRender = true;
            }
        
            
            g.setColor(new Color(24, 24, 24));
            g.drawRoundRect(X, Y, Width, Height, Radius);
            g.setColor(new Color(98, 98, 98));
            g.drawRoundRect(X + 1, Y + 1, Width - 2, Height - 2, Radius);
            g.setColor(new Color(7, 17, 26));
            g.fillRoundRect(X + 2, Y + 2, Width - 3, Height - 3, Radius);
            g.setColor(new Color(12, 32, 50));
            g.fillRoundRect(X + 7, Y + 7, Width - 13, Height - 13, Radius);
            
            for(Component component : Components)
                component.Render(g, this.X, this.Y, this.Width, this.Height);
        }
    }
}