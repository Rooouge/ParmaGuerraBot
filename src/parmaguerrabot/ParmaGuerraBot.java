package parmaguerrabot;

import java.io.IOException;

public class ParmaGuerraBot {

	public static void main(String[] args) {
		try {
			if(args.length == 1) {
				switch (args[0]) {
				case "-bot":
					bot();
					break;
				case "-round":
					round();
					break;
				case "-fullRun":
					fullRun();
					break;
				case "-init":
					init();
					break;
				case "-reset":
					reset();
					break;
				default:
					System.err.println("Invalid argument: " + args[0]);
					break;
				}
			} 
			else if(args.length > 1) 
				System.err.println("Too many arguments: " + args.length + ", expected 1");
			else
				System.err.println("No arguments: " + args.length + ", expected 1");
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	/**
	 * Main method, called any our
	 * 
	 * @throws IOException
	 */
	private static void bot() throws IOException {
		if(!Core.isInitialized())
			Core.init();
		else
			Core.run();
	}
	
	/**
	 * (Test) Makes 1 round
	 * 
	 * @throws IOException
	 */
	private static void round() throws IOException {
		Core.run();
		
		System.out.println(" - Round over");
	}
	
	/**
	 * (Test) Simulates a full war (1 round any 5 seconds)
	 * 
	 * @throws InterruptedException 
	 * @throws IOException
	 */
	private static void fullRun() throws InterruptedException, IOException {
		Core.reset();
		Core.init();
		
		System.out.println(" - Round 0 over");
		
		Thread.sleep(5000);
		
		for(int i = 1; !Core.run(); i++) {
			System.out.println(" - Round " + i + " over");
			
			Thread.sleep(5000);
		}

		System.out.println(" - Full run succesfully complete!");
	}
	
	/**
	 * (Test) Method to initialize a new war
	 * 
	 * @throws IOException
	 */
	private static void init() throws IOException {
		Core.reset();
		Core.init();
		
		System.out.println(" - Init succesfully complete!");
	}
	
	/**
	 * (Test) Method to reset the bot
	 * 
	 * @throws IOException
	 */
	private static void reset() throws IOException {
		Core.reset();
		
		System.out.println(" - Reset succesfully complete!");
	}


}
