package aaron.elasticcollision;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

public class Ball 
{
	private Random rnd = new Random();
	private Color col;
	
	private double x; // x position
	private double y; // y position
	private double r; // radius
	
	private double v;
	private double m;
	private double angle;
	private double vX; // initial x velocity
	private double vY; // initial y velocity
	private double dX;
	private double dY;
	private boolean type;
	private boolean attracts;
	
	
	public Ball()
	{
		
	}
	
	public Ball(boolean attracts, double x, double y, double r, double m, double v, double ang)
	{
		this.setAttracts(attracts);
		this.x = x + r;
		this.y = y + r;
		this.r = r;
		
		this.v = v;
		this.m = m;
		this.angle = ang;
		this.col = new Color(rnd.nextInt((255 - 50) + 1) + 50,rnd.nextInt((255 - 50) + 1) + 50,rnd.nextInt((255 - 50) + 1) + 50);
		//this.col = new Color(100,10,255);
		vX = (Math.cos(angle) * v);
	    vY = (Math.sin(angle) * v);
	    v = Math.sqrt((vX*vX) + (vY*vY));
	    dX = vX;
	    dY = vY;
	}
	
	public Ball(boolean type, double x, double y, double r, double m)
	{
		this.type = type;
		this.x = x + r;
		this.y = y + r;
		this.r = r;
		this.m = m;
		this.col = new Color(rnd.nextInt((255 - 50) + 1) + 50,rnd.nextInt((255 - 50) + 1) + 50,rnd.nextInt((255 - 50) + 1) + 50);
	}
	
	public void move()
	{
		x += dX/10;
		y += dY/10;
	}
	
	public void findNewVelocityGrav(ArrayList<Ball> b)
	{
		if (b.size() == 1)
		{
			return;
		}
		double chY = 0;
		double chX = 0;
		
		double v2 = 0;
		double angle2 = 0;
		double dist = 0;
		double distX = 0;
		double distY = 0;
		
		for (int i = 0; i < b.size(); i++)
		{
			if (b.get(i) != this)
			{
				distY = b.get(i).getY() - getY();
				distX = b.get(i).getX() - getX();
				dist = Math.sqrt((distX*distX)+(distY*distY)) / (15.5);
				
				//System.out.println("[][][][][][][][][][ "+dist);
				if (dist <= 42)
				{
					if (b.get(i).isAttracts() && this.isAttracts() || !b.get(i).isAttracts() && !this.isAttracts())
					{
						chY += distY / (Math.pow(dist, 2) * this.m);
						chX += distX / (Math.pow(dist, 2) * this.m);
					}else{
						if (dist <= 10)
						{
							chY -= distY / (Math.pow(dist, 2) * this.m);
							chX -= distX / (Math.pow(dist, 2) * this.m);
						}
					}
				}
			}
		}
		
		if (chX == 0) 
		{
			chX = 0.001;
		}
		
		//dist = Math.sqrt((chX*chX)+(chY*chY)) * 10;
		
		setVelocityX((chX) + getXInitVelocity());
		setVelocityY((chY) + getYInitVelocity());
		
		v2 = Math.sqrt((getXInitVelocity()*getXInitVelocity()) + (getYInitVelocity()*getYInitVelocity()));
		setVelocity(v2);
		
		angle2 = Math.asin(getYInitVelocity()/getVelocity());
		
		if (getXInitVelocity() < 0 && getYInitVelocity() >= 0)
			angle2 = Math.PI - Math.abs(angle2);
		else if (getXInitVelocity() < 0 && getYInitVelocity() < 0)
			angle2 = Math.PI + Math.abs(angle2);
		
		setAngle(angle2);
	}
	
	public boolean isTouched(Ball b)
	{
		return false;
//		double dx = (x + r) - (b.getX() + b.getRadius());
//		double dy = (y + r) - (b.getY() + b.getRadius());
//		double dist = Math.sqrt((dx*dx) + (dy*dy));
//		
//		//findNewVelocityGrav(b, dist);
//		
//		if (dist <= r + b.getRadius())
//		{
//			//touched = true;
//			// prevent overlapping of the two objects
//			do {
//				if (dist == 0) {
//					dist = 0.001;
//				}
//				double ang = Math.asin(dy / dist);
//				if (dx < 0 && dy >= 0) {
//					ang = Math.PI - Math.abs(ang);
//				} else if (dx < 0 && dy < 0) {
//					ang = Math.PI + Math.abs(ang);
//				} else if (dx > 0 && dy < 0) {
//					ang = 2*Math.PI - Math.abs(ang);
//				}
//				x += Math.cos(ang);
//				y += Math.sin(ang);
//				
//				dx = (x + r) - (b.getX() + b.getRadius());
//				dy = (y + r) - (b.getY() + b.getRadius());
//				dist = Math.sqrt((dx*dx) + (dy*dy));
//			} while (dist-1 < r + b.getRadius());
//			
//			/*Color c1 = b.getColor();
//			Color c2 = getColor();
//			setColor(c1);
//			b.setColor(c2);*/
//			
//			//findNewVelocity(b);
//			
//			return true;
//		}
//		
//		/*if (dist >r + b.getRadius())
//		{
//			touched = false;
//		}*/
//		return false;
	}
	
	public void findNewVelocity(Ball b)
	{
		Ball b1 = b;
		double v2 = b1.getVelocity();
		double v2X;
		double v2Y;
		double m2 = b1.getMass();
		double angle2 = b1.getAngle();
		
		
		// DETERMINE CONTACT ANGLE
		//**************************************************
		double chY = b1.getY()-getY();
		double chX = b1.getX()-getX();
		if (chX == 0) 
		{
			chX = 0.001;
		}
		
		double contactAngle = Math.atan(chY/chX);
		
		if (chX < 0 && chY >= 0)
			contactAngle = Math.PI - Math.abs(contactAngle);
		else if (chX < 0 && chY < 0)
			contactAngle= Math.PI + Math.abs(contactAngle);
		
		// **************************************************
		
		vX =
				(((v*Math.cos(angle-contactAngle)*(m-m2)+2*m2*v2*Math.cos(angle2-contactAngle))/(m+m2)) * Math.cos(contactAngle)) + 
				v*Math.sin(angle-contactAngle)*Math.cos(contactAngle + (Math.PI/2));
		
		vY =
				((v*Math.cos(angle-contactAngle)*(m-m2)+2*m2*v2*Math.cos(angle2-contactAngle))/(m+m2)) * Math.sin(contactAngle) + 
				v*Math.sin(angle-contactAngle)*Math.sin(contactAngle + (Math.PI/2));
		
		v2X =
				(((v2*Math.cos(angle2-contactAngle)*(m2-m)+2*m*v*Math.cos(angle-contactAngle))/(m2+m)) * Math.cos(contactAngle)) + 
				v2*Math.sin(angle2-contactAngle)*Math.cos(contactAngle + (Math.PI/2));
		
		v2Y =
				((v2*Math.cos(angle2-contactAngle)*(m2-m)+2*m*v*Math.cos(angle-contactAngle))/(m2+m)) * Math.sin(contactAngle) + 
				v2*Math.sin(angle2-contactAngle)*Math.sin(contactAngle + (Math.PI/2));
		
		// set new velocity and angle for this ball
		v = Math.sqrt((vX*vX) + (vY*vY));
		if (v == 0) 
			v = 0.01;
		
		angle = Math.asin(vY/v);
		
		if (vX < 0 && vY >= 0)
			angle = Math.PI - Math.abs(angle);
		else if (vX < 0 && vY < 0)
			angle = Math.PI + Math.abs(angle);
		
		setVelocity(v);
		setVelocityX(vX);
		setVelocityY(vY);
		
		// set new velocity and angle for ball2
		if (b1.getRadius() != 8)
		{
			v2 = Math.sqrt((v2X*v2X) + (v2Y*v2Y));
			if (v2 == 0) 
				v2 = 0.01;
			
			angle2 = Math.asin(v2Y/v2);
			
			if (v2X < 0 && v2Y >= 0)
				angle2 = Math.PI - Math.abs(angle2);
			else if (v2X < 0 && v2Y < 0)
				angle2 = Math.PI + Math.abs(angle2);
			
			b1.setVelocity(v2);
			b1.setVelocityX(v2X);
			b1.setVelocityY(v2Y);
			b1.setAngle(angle2);
		}
		
	}

	public double getX() 
	{
		return this.x;
	}
	
	public double getY()
	{
		return this.y;
	}
	
	public double getVelocity() 
	{
		double v2;
		v2 = Math.sqrt((getXInitVelocity()*getXInitVelocity()) + (getYInitVelocity() *getYInitVelocity() ));
		if (v2 == 0) 
			v2 = 0.01;
		this.v = v2;
		return this.v;
	}
	
	public double getMass() 
	{
		return this.m;
	}
	
	public double getXInitVelocity() 
	{
		return this.vX;
	}
	
	public double getYInitVelocity() 
	{
		return this.vY;
	}
	
	public double getRadius() 
	{
		return this.r;
	}
	
	public double getAngle() 
	{
		return this.angle;
	}
	
	public Color getColor()
	{
		return this.col;
	}
	
	public boolean getType()
	{
		return this.type;
	}
	
	public void setX(double x) 
	{
		this.x = x;
	}
	
	public void setY(double y)
	{
		this.y = y;
	}
	
	public void setVelocity(double v)
	{
		if (!this.type)
		{
			this.v = v;
		}
	}
	
	public void setVelocityX(double vX)
	{
		if (!this.type)
		{
			this.vX = vX;
			this.dX = vX;
		}
	}
	
	public void setVelocityY(double vY)
	{
		if (!this.type)
		{
			this.vY = vY;
			this.dY = vY;
		}
	}
	
	public void setAngle(double ang) 
	{
		if (!this.type)
		{
			this.angle = ang;
		}
	}
	
	public void setColor(Color c) 
	{
		this.col = c;
	}

	public boolean isAttracts() {
		return attracts;
	}

	public void setAttracts(boolean attracts) {
		this.attracts = attracts;
	}

}
