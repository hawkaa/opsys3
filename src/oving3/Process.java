package oving3;

import java.awt.*;
import java.util.*;


/**
 * This class contains data associated with processes,
 * and methods for manipulating this data as well as
 * methods for displaying a process in the GUI.
 *
 * You will probably want to add more methods to this class.
 */
public class Process implements Constants
{
	/** The ID of the next process to be created */
	private static long nextProcessId = 1;
	/** The font used by all processes */
	private static Font font = new Font("Arial", Font.PLAIN, 10);
	/** The ID of this process */
	private long processId;
	/** The color of this process */
	private Color color;
	
	
	/** The amount of memory needed by this process */
    private long memoryNeeded;
	/** The amount of cpu time still needed by this process */
    private long cpuTimeNeeded;
	/** The average time between the need for I/O operations for this process */
    private long avgIoInterval;
	/** The time left until the next time this process needs I/O */
    private long timeToNextIoOperation = 0;

    

    
	/** The time that this process has spent waiting in the memory queue */
	private long timeSpentWaitingForMemory = 0;
	/** The time that this process has spent waiting in the CPU queue */
	private long timeSpentInReadyQueue = 0;
	/** The time that this process has spent processing */
    private long timeSpentInCpu = 0;
	/** The time that this process has spent waiting in the I/O queue */
    private long timeSpentWaitingForIo = 0;
	/** The time that this process has spent performing I/O */
	private long timeSpentInIo = 0;

	/** The number of times that this process has been placed in the CPU queue */
	private long nofTimesInReadyQueue = 0;
	/** The number of times that this process has been placed in the I/O queue */
	private long nofTimesInIoQueue = 0;


	// Varibles for keeping track of previous events
	private long timeEnteredMemoryQueue = 0;
    private long timeOfLastCPUEnter = 0;
    private long timeOfLastCPUQueueEnter = 0;
    private long timeOfLastIOQueueEnter = 0;
    private long timeOfLastIOEnter = 0;

	/**
	 * Creates a new process with given parameters. Other parameters are randomly
	 * determined.
	 * @param memorySize	The size of the memory unit.
	 * @param creationTime	The global time when this process is created.
	 */
	public Process(long memorySize, long creationTime) {
		// Memory need varies from 100 kB to 25% of memory size
		memoryNeeded = 100 + (long)(Math.random()*(memorySize/4-100));
		// CPU time needed varies from 100 to 10000 milliseconds
		cpuTimeNeeded = 100 + (long)(Math.random()*9900);
		// Average interval between I/O requests varies from 1% to 25% of CPU time needed
		avgIoInterval = (1 + (long)(Math.random()*25))*cpuTimeNeeded/100;

		// Assign a process ID
		processId = nextProcessId++;
		// Assign a pseudo-random color used by the GUI
		int red = 64+(int)((processId*101)%128);
		int green = 64+(int)((processId*47)%128);
		int blue = 64+(int)((processId*53)%128);
		color = new Color(red, green, blue);
		
		
		timeEnteredMemoryQueue = creationTime;
	}

	/**
	 * Draws this process as a colored box with a process ID inside.
	 * @param g	The graphics context.
	 * @param x	The leftmost x-coordinate of the box.
	 * @param y	The topmost y-coordinate of the box.
	 * @param w	The width of the box.
	 * @param h	The height of the box.
	 */
	public void draw(Graphics g, int x, int y, int w, int h) {
		g.setColor(color);
		g.fillRect(x, y, w, h);
		g.setColor(Color.black);
		g.drawRect(x, y, w, h);
		g.setFont(font);
		FontMetrics fm = g.getFontMetrics(font);
		g.drawString(""+processId, x+w/2-fm.stringWidth(""+processId)/2, y+h/2+fm.getHeight()/2);
	}

	/**
	 * This method is called when the process leaves the memory queue (and
	 * enters the cpu queue).
     * @param clock The time when the process leaves the memory queue.
     */
    public void leftMemoryQueue(long clock) {
		  timeSpentWaitingForMemory = clock - timeEnteredMemoryQueue;
    }

    /**
	 * Returns the amount of memory needed by this process.
     * @return	The amount of memory needed by this process.
     */
	public long getMemoryNeeded() {
		return memoryNeeded;
	}

    /**
	 * Updates the statistics collected by the given Statistic object, adding
	 * data collected by this process. This method is called when the process
	 * leaves the system.
     * @param statistics	The Statistics object to be updated.
     */
	public void updateStatistics(Statistics statistics, long clock) {
		statistics.totalTimeSpentWaitingForMemory += timeSpentWaitingForMemory;
		statistics.totalTimeWaitingForCPU += timeSpentInReadyQueue;
		statistics.totalTimeInCPU += timeSpentInCpu;
		statistics.totalTimeWaitingForIO += timeSpentWaitingForIo;
		statistics.totalTimeInIO += timeSpentInIo;
		
		statistics.processPlacedInCPUQueue += nofTimesInReadyQueue;
		statistics.processPlacedInIOQueue += nofTimesInIoQueue;
		statistics.totalTimeInSystem += clock - this.timeEnteredMemoryQueue;
	}
	
	public long getCpuTimeNeeded() {
		return this.cpuTimeNeeded;
	}
	
	/**
	 * Event for updating statistics regarding total time in queue for CPU
	 * @param clock Current time
	 */
	public void enteredCPU(long clock) {
		this.timeOfLastCPUEnter = clock;
		this.timeSpentInReadyQueue += clock - this.timeOfLastCPUQueueEnter;
		
	}
	/**
	 * Event for statistics
	 * @param clock Current time
	 */
	public void enterCPUQueue(long clock) {
		this.timeOfLastCPUQueueEnter = clock;
		++this.nofTimesInReadyQueue;
	}
	
	/**
	 * Event for statistics
	 * @param clock Current time
	 */
	public void enterIOQueue(long clock) {
		++this.nofTimesInIoQueue;
		this.timeOfLastIOQueueEnter = clock;
	}
	/**
	 * Event for statistics
	 * @param clock Current time
	 */
	public void leftCPU(long clock) {
		this.cpuTimeNeeded -= clock-this.timeOfLastCPUEnter;
		this.timeToNextIoOperation -= clock - this.timeOfLastCPUEnter;
		this.timeSpentInCpu += clock - this.timeOfLastCPUEnter;
	}
	/**
	 * Event for statistics
	 * @param clock Current time
	 */
	public void enteredIO(long clock) {
		this.timeOfLastIOEnter = clock;
		this.timeSpentWaitingForIo += clock - this.timeOfLastIOQueueEnter;
	}
	/**
	 * Event for of how many MS spent in IO, and generating a new random time to next IO operation
	 * @param clock Current time
	 */
	public void leftIO(long clock) {
		// We must schedule a new IO
		this.timeToNextIoOperation = 2*(long) (Math.random() * avgIoInterval);
		//System.out.println(clock - this.timeOfLastIOEnter);
		this.timeSpentInIo += clock - this.timeOfLastIOEnter;
	}
	
	/**
	 * 
	 * @return true if the process is done
	 */
	public boolean isDone() {
		return this.cpuTimeNeeded <=0;
	}

	/**
	 * Get time to next IO operation.
	 * @return time in ms to next io operation
	 */
	public long getTimeToNextIO() {
		if(this.timeToNextIoOperation == 0) {
			this.timeToNextIoOperation = 2*((long)(Math.random() * avgIoInterval));
		}
		return timeToNextIoOperation;
	}
	/**
	 * 
	 * @return true if the process is ready for an IO operation
	 */
	public boolean isTimeForIO() {
		return this.timeToNextIoOperation <= 0;
	}
}