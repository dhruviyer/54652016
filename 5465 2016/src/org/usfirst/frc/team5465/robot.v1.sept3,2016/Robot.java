
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

public class Robot extends IterativeRobot
{
	CameraServer server;
	
	SmartDashboard dash;
		
	private RobotDrive unassistedDrive;
	//private PIDDrive assistedDrive;
	
	private RobotArm myRobotArm;
	
	private Joystick driverJoystick;
	private Joystick armJoystick;
	
	private double driverJoyStick_Z;
	private double driverJoyStick_Y;
	private double armJoyStick_X;
	private double armJoyStick_Y;
	private boolean doManualDrive = false;
	private boolean actuatePiston = false;
	
	private ADXRS450_Gyro gyro;
	private Compressor compressor;
	
	private double prevTurn = 0;
	private double newTurn = 0;
	
	///////ROBOT CONSTANTS ***CONSULT ELECTRICAL TEAM FOR CONCURANCY***
	final int LEFT_PORT = 0;
	final int RIGHT_PORT = 1;
	
    public void robotInit() 
    {
    	gyro = new ADXRS450_Gyro();
        gyro.calibrate();
           
    	unassistedDrive = new RobotDrive(LEFT_PORT,RIGHT_PORT);
    	//assistedDrive = new PIDDrive(gyro, LEFT_PORT, RIGHT_PORT);
    	
    	myRobotArm = new RobotArm();
    	
    	driverJoystick = new Joystick(5);
    	armJoystick = new Joystick(0);
    	
    	server = CameraServer.getInstance();
        server.setQuality(100);
        server.startAutomaticCapture("cam0");
         
        //dash = new SmartDashboard();
        
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
    	doDrive();
    	
    	myRobotArm.moveRobotArm(-1*armJoyStick_Y);
    	myRobotArm.actuate(actuatePiston);
    	
    	updateDashboad();
    	
    	
    }
    
    private void doDrive() {
    	updateJoysticks();
    	newTurn = driverJoyStick_Z;
    	
    	if(Math.abs(newTurn) > 0.05)
    	{
    		doManualDrive = true;
    	}
    		
    	else
    	{
    		//if(prevTurn>0.05)assistedDrive.updateSetPoint();
    	}
    	
    	if(doManualDrive) unassistedDrive.drive(driverJoyStick_Y, driverJoyStick_Z);
    	//else assistedDrive.drive(driverJoyStick_Y);
    	
    	
    	prevTurn = newTurn;
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
    	//assistedDrive.stopMotors();
    	
    	myRobotArm.stopRobotArm();
    	compressor.stop();
    }
    
    private void updateJoysticks()
    {
    	driverJoyStick_Z = driverJoystick.getZ();
    	driverJoyStick_Y = driverJoystick.getY();
    	
    	
    	
    	SmartDashboard.putNumber("Forward/Back", driverJoystick.getY());
    	
    	armJoyStick_X = armJoystick.getX();
    	armJoyStick_Y = armJoystick.getY();
    	
    	doManualDrive = driverJoystick.getRawButton(0);
    	actuatePiston = armJoystick.getRawButton(0);
    }
    
    public void updateDashboad()
    {
    	SmartDashboard.putNumber("RobotArmMotor", myRobotArm.getRobotArmMotorValue());
    	SmartDashboard.putNumber("RobotDriveLeftVal", unassistedDrive.getLeftMotorVal());
    	SmartDashboard.putNumber("RobotDriveRightVal", unassistedDrive.getRightMotorVal());
    	SmartDashboard.putBoolean("RobotArmPiston", myRobotArm.getPiston());
    	
    	

    	//SmartDashboard.putNumber("PIDDrive", assistedDrive.getPIDVal());
    }

}
