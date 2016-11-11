
package org.usfirst.frc.team5465.robot;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Joystick.ButtonType;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SPI.Port;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot
{
	CameraServer server;
	
	SmartDashboard dash;
		
	private RobotDrive drive;
	
	private RobotArm myRobotArm;
	
	private Joystick driverJoystick;
	private Joystick armJoystick;
	
	private double driverJoyStick_Z;
	private double driverJoyStick_Y;
	private double armJoyStick_X;
	private double armJoyStick_Y;
	private boolean doManualDrive = true;
	private boolean actuatePiston = false;
	
	//private ADXRS450_Gyro gyro;
	private Compressor compressor;
	
	private double prevTurn = 0;
	private double newTurn = 0;
	
	///////ROBOT CONSTANTS ***CONSULT ELECTRICAL TEAM FOR CONCURANCY***
	final int LEFT_PORT = 0;
	final int RIGHT_PORT = 1;
	
	private Preferences prefs;
	private boolean auto = false;
	
    public void robotInit() 
    {
    	drive = new RobotDrive(LEFT_PORT,RIGHT_PORT);
    	
    	myRobotArm = new RobotArm();
    	
    	driverJoystick = new Joystick(0);
    	armJoystick = new Joystick(1);
    	
    	server = CameraServer.getInstance();
        server.setQuality(100);
        server.startAutomaticCapture("cam0");
          
        compressor = new Compressor();
        compressor.start();
        
        prefs = Preferences.getInstance();
        SmartDashboard.putString("Message from sentient robot:", "All systems go");
       
        UDPThread udpThread = new UDPThread();
    }
    
    public void autonomousInit()
    {
    	auto = prefs.getBoolean("auto", false);
    }

    public void autonomousPeriodic() 
    {
    	/*SmartDashboard.putString("Message from sentient robot:", "Tell your programmer I say hello :)");
 
    	if(auto)
    	{
	    	drive.setRaw(-0.2, 0.2);
	    	try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	    	drive.stopMotors();
	    	auto = false;
    	}else{};*/
    }
    
    public void teleopPeriodic() 
    {
    	doDrive();
    	
    	myRobotArm.moveRobotArm(-1*armJoyStick_Y);
    	myRobotArm.actuate(actuatePiston);
    	
    	updateDashboad();
    }
    
    public void teleopInit()
    {
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

	public void testPeriodic() 
    {
    
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
    	SmartDashboard.putNumber("Correction", drive.getPIDVal());
    	SmartDashboard.putNumber("Set point", drive.getSetPoint());
    	SmartDashboard.putNumber("Speed", drive.getRobot_speed());
    	SmartDashboard.putNumber("Turn", drive.getRobot_turn());
    	SmartDashboard.putBoolean("comp enabled" , compressor.enabled());
    	SmartDashboard.putBoolean("comp pressure switch value" , compressor.getPressureSwitchValue());
    	SmartDashboard.putBoolean("Piston" , actuatePiston);
    }

}
