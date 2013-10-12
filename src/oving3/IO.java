package oving3;


import oving3.gui.Gui;
import oving3.gui.Queue;

public class IO {
	private Queue ioQueue;
	
	// Active IO process
	private Process activeProcess;
	
	//GUI	
	private Gui gui;
	
	// Statistics
	private Statistics stats;
	
	public IO(Queue q, Gui gui, Statistics s) {
		this.ioQueue = q;
		this.gui = gui;
		this.stats = s;
	}
	/**
	 * Adds process to IO queue
	 * @param p Process to add
	 * @param clock Current time
	 */
	public void addProcess(Process p, long clock) {
		p.enterIOQueue(clock);
		this.ioQueue.insert(p);
	}
	
	/**
	 * Removes the current process that is running in the IO and replaces it with the next in line. Updates GUI
	 */
	public void pop() {
		if(ioQueue.isEmpty()) {
			this.activeProcess = null;
		} else {
			this.activeProcess = (Process)ioQueue.removeNext();
		}
		gui.setIoActive(this.activeProcess);
	}

	/**
	 * 
	 * @return the currently active process in the IO
	 */
	public Process getActiveProcess() {
		return this.activeProcess;
	}

	/**
	 * 
	 * @return If there is currently no active processes in IO
	 */
	public boolean isIdle() {
		return this.activeProcess == null;
	}

	/**
	 * Method for updating statistics regarding queuing and waiting time
	 * @param timePassed current time
	 */
	public void timePassed(long timePassed) {
		stats.ioQueueLengthTime += ioQueue.getQueueLength()*timePassed;
		if (ioQueue.getQueueLength() > stats.ioQueueLargestLength) {
			stats.ioQueueLargestLength = ioQueue.getQueueLength(); 
		}
    }
}
