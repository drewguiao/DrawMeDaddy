import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JTextField;

public class CountdownTimer implements Runnable{
	private final int delay = 1000;
	private final int period = 1000;
	Thread t;
	
	private int remainingTime = 999;
	
	private int timeLimit = 0;
	private Timer timer;
	private JTextField timerField;

	public CountdownTimer(JTextField timerField){
		this.timer = new Timer();
		this.timerField = timerField;
		
	}
	
	public int getTimeLimit(){
		return this.timeLimit;
	}
	
	public int getRemainingTime(){
		return this.remainingTime;
	}
	
	public void start(int timeStart){
		this.remainingTime = timeStart;
		t = new Thread(this);
		t.start();
		
	}
public void cancel(){
	timer.cancel();
}

	
	public void divide(){
		this.remainingTime = this.remainingTime / 2;
	}
	
	private void setInterval(){
		if(remainingTime == 0){
			t.stop();
			timer.cancel();
			timer.purge();
		}
		this.remainingTime = remainingTime - 1;

	}

	@Override
	public void run() {
		timer.scheduleAtFixedRate(new TimerTask(){
			public void run(){
				timerField.setText(""+remainingTime);
				setInterval();
				// System.out.println(setInterval());
			}
		}, delay, period);	
		
	}
	
}
