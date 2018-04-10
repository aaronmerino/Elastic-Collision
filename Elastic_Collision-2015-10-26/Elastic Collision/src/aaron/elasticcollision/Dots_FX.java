package aaron.elasticcollision;

import java.awt.Color;

public class Dots_FX {
	
	private double x;
	private double y;
	private double r;
	private Color col;
	private Time lifeTime;

	public Dots_FX(double x, double y, double r, Color c)
	{
		this.x = x;
		this.y = y;
		this.r = r;
		this.col = c;
		this.lifeTime = new Time("Life Time");
		this.lifeTime.start();
	}
	
	public boolean isAlive()
	{
		if (lifeTime.getTimePassed() >= 2 || r <= 0)
		{
			lifeTime.killThread();
			return false;
		}
		else
		{
			if (r > 0)
			{
				r -= 1;
				this.x += 1;
				this.y += 1;
			}
		}
		
		return true;
	}
	
	public void setX(double x) 
	{
		this.x = x;
	}

	public void setY(double y) 
	{
		this.y = y;
	}

	
	public double getRadius() 
	{
		return this.r;
	}
	
	public Color getColor()
	{
		return this.col;
	}
	
	public double getX() 
	{
		return this.x;
	}

	public double getY() 
	{
		return this.y;
	}

}
