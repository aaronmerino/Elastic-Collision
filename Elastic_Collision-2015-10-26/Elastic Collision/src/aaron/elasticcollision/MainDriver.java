package aaron.elasticcollision;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.*;

public class MainDriver extends JFrame implements Runnable,KeyListener,MouseListener,MouseMotionListener
{
	private static final long serialVersionUID = 1L;
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private int WIDTH = (int) screenSize.getWidth() - 100;
	private int HEIGHT = (int) screenSize.getHeight() - 100;
	private Thread t;
	private int numberOfBalls = 50;
	private int radiusOfBalls = 10;
	private int mX;
	private int mY;
	private int m2X;
	private int m2Y;
	private boolean typeOfBall;
	private boolean mPressed;
	private boolean clear;
	private boolean showLines;
	private boolean showVectorLine;
	private int vectorLineX;
	private int vectorLineY;
	private int lolwat;
	private int lolwat2;
	private double lolwat3;
	private double lolwat4;
	private Image dbimg;
	private Graphics dbg;
	private Random rnd = new Random();
	private Time trailRespawnTime;
	private ArrayList<Ball> b = new ArrayList<Ball>();
	private ArrayList<Dots_FX> bt = new ArrayList<Dots_FX>();
	
	public static void main(String[] args)
	{
		new MainDriver();
	}
	
	public MainDriver()
	{
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setTitle("Elastic Collision");
		this.setVisible(true);
		this.setSize(WIDTH,HEIGHT);
		this.lolwat = 100;
		this.lolwat2 = 1;
		this.lolwat3 = 1;
		this.addKeyListener(this);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		for (int i = 0; i < numberOfBalls; i++)
		{
			int rndX = rnd.nextInt((WIDTH - 0) + 1) + (0);
			int rndY = rnd.nextInt((HEIGHT - 0) + 1) + (0);
			int rndV = rnd.nextInt((4 - 2) + 1) + (2);
			double rndAng = rnd.nextInt((360 - 0) + 1) + 0;
			rndAng = (Math.PI/180) * rndAng;
			
			b.add(new Ball(true, rndX, rndY, radiusOfBalls,10, rndV, rndAng));
		}
		
		t = new Thread(this);
		trailRespawnTime = new Time("Life Time");
		t.start();
		trailRespawnTime.start();
	}

	public void run() 
	{
		while (true)
		{
			if (lolwat == 150)
				lolwat2 = -1;
			else if (lolwat == 100)
				lolwat2 = 1;
			
			if (lolwat3 >= 1.5)
				lolwat4 = -0.2;
			else if (lolwat3 <= 1)
				lolwat4 = 0.2;
			
			lolwat += lolwat2;
			lolwat3 += lolwat4;
			
			for (int i = 0; i < b.size(); i++)
			{
				b.get(i).move();
				
				if (b.get(i).getX() > WIDTH + 30){
					b.get(i).setX(0 - 25);
				} else if (b.get(i).getX() < 0 - 30){
					b.get(i).setX(WIDTH + 25);
				}
				
				if (b.get(i).getY() > HEIGHT + 30){
					b.get(i).setY(0 - 35);
				} else if (b.get(i).getY() < 0 - 40){
					b.get(i).setY(HEIGHT + 25);
				}
			}
			
			for (int i = 0; i < b.size(); i++)
			{
				b.get(i).move();
				//System.out.println(b.get(i).istypeOfBall());
				for (int z = i + 1; z < b.size(); z++)
				{
					if(z != i)
					{
						
						if (b.get(i).isTouched(b.get(z)))
						{
							b.get(i).findNewVelocity(b.get(z));
						}
						//b.get(z).isTouched(b.get(i));
					}
					
				}
			}
			
			for (int i = 0; i < b.size(); i++)
			{
				b.get(i).findNewVelocityGrav(b);
			}

			if (trailRespawnTime.getTimePassed() >= 0.3)
			{
				trailRespawnTime.resetTime();
				for (int i = 0; i < b.size(); i++) 
				{
					if (b.get(i).getVelocity() > 0.2)
					{
						Dots_FX b2 = new Dots_FX(b.get(i).getX(), b.get(i).getY(), b.get(i).getRadius(), b.get(i).getColor());
						bt.add(b2);
					}
				}
			}
			
			for (int zx = 0; zx < bt.size(); zx++) 
			{
				if (!bt.get(zx).isAlive())
				{
					bt.remove(zx);
				}
			}
			
			
			
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			DoubleBuffer(this.getGraphics());
			if(clear)
			{
				b.clear();
				bt.clear();
			}
			
		}
	}
	
	
	public void DoubleBuffer(Graphics fg)
	{
		dbimg = createImage(WIDTH,HEIGHT);
		dbg = dbimg.getGraphics();
		Graphics2D g = (Graphics2D)dbg;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(new Color(10,10,10));
		//g.setColor(bt.get(z).getColor());
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		//TRAILS
//		for (int z = 0; z < bt.size(); z++)
//		{
//			//g.setColor(bt.get(z).getColor());
//			g.setStroke(new BasicStroke((float) 0.5));
//			g.setColor(new Color(255, 255, 255));
//			//g.setStroke(new BasicStroke((int)bt.get(z).getRadius()/12));
//			g.drawOval((int)bt.get(z).getX(), (int)bt.get(z).getY(), (int)bt.get(z).getRadius()*2, (int)bt.get(z).getRadius()*2);
//		}
		
		if (showVectorLine) 
		{
			g.setColor(new Color(0, 153, 76));
			g.drawLine(mX, mY, vectorLineX, vectorLineY);
		}
		
		
		for (int i = 0; i < b.size(); i++)
		{
//			if (i % 2 == 0)
//				g.setStroke(new BasicStroke((float) lolwat3));
//			else
//				g.setStroke(new BasicStroke(1));
			
//			g.setColor(new Color(lolwat,lolwat,100));
			g.setColor(new Color(255, 255, 55));
			if (!b.get(i).isAttracts())
			{
				g.setColor(new Color(55, 255, 255));
			}
			if(showLines)
			{
				for (int z = i+1; z < b.size(); z++)
				{
//					if (z != i)
//					{
					if (b.get(i).isAttracts() && b.get(z).isAttracts() || !b.get(i).isAttracts() && !b.get(z).isAttracts())
					{
						double bX = (int)b.get(i).getX() - (int)b.get(z).getX();
						double bY = (int)b.get(i).getY() - (int)b.get(z).getY();
						double thickness = 1 / Math.sqrt((bX*bX)+(bY*bY)) * 200;
						if (thickness > 3.5)
						{
							thickness = 3.5;
						}
						g.setStroke(new BasicStroke((float) thickness));
	//						System.out.println(thickness);
						if (thickness > 0.15)
						{
							g.drawLine(
									(int)b.get(i).getX() + (int)b.get(i).getRadius(), 
									(int)b.get(i).getY() + (int)b.get(i).getRadius(), 
									(int)b.get(z).getX() + (int)b.get(z).getRadius(), 
									(int)b.get(z).getY() + (int)b.get(z).getRadius());
						}
					}
//					}
				}
				
//				if (i < b.size()-1)
//					g.drawLine((int)b.get(i).getX() + (int)b.get(i).getRadius(), (int)b.get(i).getY() + (int)b.get(i).getRadius(), (int)b.get(i+1).getX() + (int)b.get(i+1).getRadius(), (int)b.get(i+1).getY() + (int)b.get(i+1).getRadius());
//				else
//					g.drawLine((int)b.get(i).getX() + (int)b.get(i).getRadius(), (int)b.get(i).getY() + (int)b.get(i).getRadius(), (int)b.get(0).getX() + (int)b.get(0).getRadius(), (int)b.get(0).getY() + (int)b.get(0).getRadius());
			}
			
			g.setStroke(new BasicStroke((float) 1.5));
			g.setColor(new Color(255, 255, 55));
			if (!b.get(i).isAttracts())
			{
				g.setColor(new Color(55, 255, 255));
			}
			//g.fillOval((int)b.get(i).getX() + ((int)b.get(i).getRadius()), (int)b.get(i).getY() + ((int)b.get(i).getRadius()), (int)b.get(i).getRadius()/8, (int)b.get(i).getRadius()/8);
			
			g.fillOval((int)b.get(i).getX(), (int)b.get(i).getY(), (int)b.get(i).getRadius()*2, (int)b.get(i).getRadius()*2);
			//g.setColor(new Color(255, 255, 255));
			//g.drawString("  mass: " + b.get(i).getMass(), (int)b.get(i).getX(), (int)b.get(i).getY());
			//g.drawString(b.get(i).getVelocity()+"", (int)b.get(i).getX(), (int)b.get(i).getY());
			
			
			
			//g.drawString(mX+"   "  + mY, 50, 50);
			//g.drawString(m2X+"   "  + m2Y, 50, 80);
		}
		
		
		fg.drawImage(dbimg, 0, 0, this); 
	}
	
	
	public void keyPressed(KeyEvent e) 
	{
		if (e.getKeyCode() == KeyEvent.VK_X)
		{
			if(showLines)
				showLines = false;
			else
				showLines = true;
		} else if (e.getKeyCode() == KeyEvent.VK_C) {
			clear = true;
		} 
	}
	
	public void keyReleased(KeyEvent e) 
	{
		clear = false;
	}

	@Override
	public void mousePressed(MouseEvent e) 
	{
		//System.out.println(e.getButton());
		showVectorLine = true;
		if(!mPressed)
		{
			mPressed = true;
			if (e.getButton() == MouseEvent.BUTTON3)
			{
				System.out.println("SDFSDFDSFDSFDSFSDfDSfSDfSDf");
//				radiusOfBalls = 4;
				radiusOfBalls = (rnd.nextInt((6 - 4) + 1) + (4));
				typeOfBall = true;
			} 
			else if (e.getButton() == MouseEvent.BUTTON1)
			{
				System.out.println("Ssssf");
				radiusOfBalls = 4;
//				radiusOfBalls = (rnd.nextInt((6 - 4) + 1) + (4));
				typeOfBall = false;
			}
			else if (e.getButton() == MouseEvent.BUTTON2)
			{
				radiusOfBalls = 8;
//				radiusOfBalls = (rnd.nextInt((6 - 4) + 1) + (4));
				typeOfBall = true;
			}
			mX = e.getX();
			mY = e.getY();
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) 
	{
		showVectorLine = false;
		m2X = e.getX();
		m2Y = e.getY();
		
		double dX = m2X - mX;
		double dY = m2Y - mY;
		double dist = Math.sqrt((dX*dX)+(dY*dY));
		if (dist > 80) dist = 80;
		
		if (dX == 0) 
		{
			dX = 0.001;
		}
		
		double angle = Math.atan(dY/dX);
		
		if (dX < 0 && dY >= 0)
			angle = Math.PI - Math.abs(angle);
		else if (dX < 0 && dY < 0)
			angle = Math.PI + Math.abs(angle);
		else if (dX > 0 && dY < 0)
			angle = 2*Math.PI - Math.abs(angle);
		
//		angle += Math.PI;
		Ball b1 = null;
//		double rndMass = rnd.nextInt((20 - 1) + 1) + (1);
		double rndMass = 1;
		
		if (radiusOfBalls == 8)
		{
			b1 = new Ball(typeOfBall, m2X, m2Y, radiusOfBalls + rndMass, rndMass*10000, dist, angle);
		}
		else
		{
			b1 = new Ball(typeOfBall, m2X, m2Y, radiusOfBalls*4 + rndMass, rndMass*20, dist, angle);
		}
//			b1 = new Ball(typeOfBall, m2X, m2Y, radiusOfBalls, radiusOfBalls*100);
		b1.setX(mX - radiusOfBalls*4 - rndMass);
		b1.setY(mY - radiusOfBalls*4 - rndMass);
		
		b.add(b1);
		
		mPressed = false;
		
	}
	
	public void keyTyped(KeyEvent e) 
	{
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) 
	{
		// TODO Auto-generated method stub
		vectorLineX = e.getX();
		vectorLineY = e.getY();
		
	}

	@Override
	public void mouseMoved(MouseEvent e) 
	{
		vectorLineX = e.getX();
		vectorLineY = e.getY();
	}
}
