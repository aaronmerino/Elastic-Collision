package aaron.elasticcollision;

public class Time extends Thread
{
	private double timePassed;
	private boolean killThread;
	
	public Time(String name)
	{
		timePassed = 0;
		killThread = false;
		this.setName(name);
	}
	
	@Override
	public void run() 
	{
		while (!killThread)
		{
			timePassed+=0.1;
			
			try 
			{
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void killThread()
	{
		killThread = true;
	}
	
	public void resetTime()
	{
		timePassed = 0;
	}
	
	public void setTime(double time)
	{
		this.timePassed = time;
	}
	
	public double getTimePassed()
	{
		return timePassed;
	}

}
