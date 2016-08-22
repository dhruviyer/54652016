
package org.usfirst.frc.team5465.robot;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Joystick.ButtonType;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.SPI.Port;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class RobotMain extends IterativeRobot
{
	CameraServer server;
	
	SmartDashboard dash;
		
	private ManualDrive unassistedDrive;
	private RobotArm myRobotArm;
	
	private Joystick driverJoystick;
	private Joystick armJoystick;
	
	private double driverJoyStick_Z;
	private double driverJoyStick_Y;
	private double armJoyStick_X;
	private double armJoyStick_Y;
	
	private ADXRS450_Gyro gyro;
	private Compressor compressor;
	
	///////ROBOT CONSTANTS ***CONSULT ELECTRICAL TEAM FOR CONCURANCY***
	final int LEFT_PORT = 0;
	final int RIGHT_PORT = 1;
	
    public void robotInit() 
    {
    	unassistedDrive = new ManualDrive(LEFT_PORT, RIGHT_PORT);
    	myRobotArm = new RobotArm();
    	
    	driverJoystick = new Joystick(0);
    	armJoystick = new Joystick(1);
    	
    	server = CameraServer.getInstance();
        server.setQuality(100);
        server.startAutomaticCapture("cam0");
         
        dash = new SmartDashboard();
        
        gyro = new ADXRS450_Gyro();
        gyro.calibrate();
        
        compressor = new Compressor();
        compressor.start();
        
    }
    
    public void autonomousInit()
    {
   
    }

    public void autonomousPeriodic() 
    {
    
    }
    
    public void teleopPeriodic() 
    {
    	updateJoysticks();
    	unassistedDrive.drive(driverJoyStick_Y, driverJoyStick_Z);
    	myRobotArm.moveRobotArm(-1*armJoyStick_Y);
    	myRobotArm.actuate(armJoystick.getRawButton(0));
    }
    
    public void testPeriodic() 
    {
    
    }
    
    public void disabledInit()
    {
    	
    }
    
    public void disabledPeriodic()
    {
    	unassistedDrive.stopMotors();
    	myRobotArm.stopRobotArm();
    	compressor.stop();
    }
    
    private void updateJoysticks()
    {
    	driverJoyStick_Z = driverJoystick.getZ();
    	driverJoyStick_Y = driverJoystick.getY();
    	dash.putNumber("Turn", driverJoyStick_Z);
    	dash.putNumber("Gyro", gyro.getAngle());
    	
    	armJoyStick_X = armJoystick.getX();
    	armJoyStick_Y = armJoystick.getY();
    }
}
