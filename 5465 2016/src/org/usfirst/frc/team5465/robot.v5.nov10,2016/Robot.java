
package org.usfirst.frc.team5465.robot;

import java.lang.annotation.Target;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Joystick.ButtonType;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SPI.Port;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot
{
	CameraServer server;
	
	SmartDashboard dash;
	
	//Important Subsystems
	private RobotDrive drive;
	private RobotArm myRobotArm;
	private ADXRS450_Gyro gyro;
	private Compressor compressor;
	UDPThread udpThread;
	private Servo servo;
	
	//Joysticks
	private Joystick driverJoystick;
	private Joystick armJoystick;
	
	//Joystick Axes 
	private double driverJoyStick_Z;
	private double driverJoyStick_Y;
	private double armJoyStick_X;
	private double armJoyStick_Y;
	
	//Important Variables
	private Preferences prefs;
	private boolean auto = false;
	private boolean doManualDrive = true;
	private boolean actuatePiston = false;
	private double prevTurn = 0;
	private double newTurn = 0;
	private String UDPString = "";
	private double angle = 0;
	
	//Threads
	private Thread driveThread;
	private Thread armThread;
	private Thread visionThread;
	
	///////ROBOT CONSTANTS ***CONSULT ELECTRICAL TEAM FOR CONCURANCY***
	final int LEFT_PORT = 0;
	final int RIGHT_PORT = 1;
	final int SERVO_CHANNEL = 2;
	
    public void robotInit() 
    {
    	//initialize joysticks
    	driverJoystick = new Joystick(0);
    	armJoystick = new Joystick(1);
    	
    	//start gyro calibration
    	gyro = new ADXRS450_Gyro();
    	gyro.calibrate();
    	
    	drive = new RobotDrive(LEFT_PORT,RIGHT_PORT, gyro);
    	myRobotArm = new RobotArm();
    
        compressor = new Compressor();
        compressor.start();
        
        prefs = Preferences.getInstance();
  
        udpThread = new UDPThread("UDP Thread", 5465, 1024);
        udpThread.start();
        
        servo = new Servo(SERVO_CHANNEL);
        
        initializeThreads();
    }
    
    public void autonomousInit()
    {
    	auto = prefs.getBoolean("auto", false);
    }

    public void autonomousPeriodic() 
    {
    	
    }
    
    public void teleopPeriodic() 
    {    	
    	
    }

	public void teleopInit()
    {
    	driveThread.start();
    	armThread.start();
    	visionThread.start();
    	
    	if(!compressor.enabled())
    			compressor.start();
    }
    
    private void doDrive()
    {
    	updateJoysticks();
    	newTurn = driverJoyStick_Z;
    	
    	if(Math.abs(newTurn) > 0.05)
    	{
    		doManualDrive = true;
    	}
    		
    	else
    	{
    		if(prevTurn>0.05)
    			drive.updateSetPoint();
    	}
    	
    	if(doManualDrive) drive.drive(driverJoyStick_Z, driverJoyStick_Y);
    	else drive.drive(driverJoyStick_Y);
    	    	
    	prevTurn = newTurn;
	}

    public void disabledInit()
    {
    	compressor.stop();
    }
    
    public void disabledPeriodic()
    {
    	drive.stopMotors();
    	myRobotArm.stopRobotArm();
    }
    
    private void updateJoysticks()
    {
    	driverJoyStick_Z = driverJoystick.getY();//switched the y and z
    	driverJoyStick_Y = driverJoystick.getZ();
    	
    	SmartDashboard.putNumber("Forward/Back", driverJoystick.getY());
    	
    	armJoyStick_X = armJoystick.getX();
    	armJoyStick_Y = armJoystick.getY();
    	
    	doManualDrive = driverJoystick.getRawButton(1);
    	actuatePiston = armJoystick.getRawButton(1);
    }
    
    public void updateDashboad()
    {
    	SmartDashboard.putNumber("RobotArmMotor", myRobotArm.getRobotArmMotorValue());
    	SmartDashboard.putNumber("RobotDriveLeftVal", drive.getLeftMotorVal());
    	SmartDashboard.putNumber("RobotDriveRightVal", drive.getRightMotorVal());
    	SmartDashboard.putBoolean("RobotArmPiston", myRobotArm.getPiston());
    	SmartDashboard.putNumber("Gyro", drive.getGyroValue());
    	SmartDashboard.putBoolean("Piston" , actuatePiston);
    	SmartDashboard.putString("UDP Output: ", UDPString);
    }

    private void initializeThreads()
    {
    	driveThread = new Thread(new Runnable(){
			@Override
			public void run()
			{
				while(!Thread.interrupted())
				{
					doDrive();
			    	updateDashboad();
				}
			}
		});
    	
    	armThread = new Thread(new Runnable(){
    		@Override
    		public void run()
    		{
    			while(!Thread.interrupted())
    			{
	    			myRobotArm.moveRobotArm(-1*armJoyStick_Y);
	    			myRobotArm.actuate(actuatePiston);
	    	    	updateDashboad();
    			}
    		}
    	});
    	
    	visionThread = new Thread(new Runnable(){
    		@Override
    		public void run()
    		{
    			double targetAngle = 0;
    	    	
    			while(!Thread.interrupted())
    			{	
	    	    	angle = gyro.getAngle();
	    	    	
    				//parse out information from UDP
	    	    	UDPString = udpThread.getString();
	    	    	String[] UDPInfo = UDPString.split("\t");
	    	    	
	    	    	if(UDPInfo[0].equals("N")) //check if not tracking 
	    	    	{
	    	    		 targetAngle = angle; //if not tracking, business as usual
	    	    	}
	    	    	
	    	    	servo.setAngle(targetAngle);
    			}
    		}
    	});
    }
}
