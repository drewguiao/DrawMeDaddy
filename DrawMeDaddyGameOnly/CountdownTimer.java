import java.util.Timer;
import java.util.TimerTask;

public class CountdownTimer implements Runnable{
	private final int delay = 1000;
	private final int period = 1000;
	Thread t;
	
	private int remainingTime;
	
	private int timeLimit = 0;
	private Timer timer;
	
	public CountdownTimer(int timeLimit){
		this.timeLimit = timeLimit;
		this.remainingTime = timeLimit;
		this.timer = new Timer();
		
	}
	
	public int getTimeLimit(){
		return this.timeLimit;
	}
	
	public int getRemainingTime(){
		return this.remainingTime;
	}
	
	public void start(){
		t = new Thread(this);
		t.start();
		
	}
	
	public void divide(){
		this.remainingTime = this.remainingTime / 2;
	}
	
	private  int setInterval(){
		if(remainingTime == 1){
			timer.cancel();
		}
		this.remainingTime = remainingTime - 1;
		return this.remainingTime;
	}

	@Override
	public void run() {
		timer.scheduleAtFixedRate(new TimerTask(){
			public void run(){
				System.out.println(setInterval());
			}
		}, delay, period);	
		
	}
	
}
