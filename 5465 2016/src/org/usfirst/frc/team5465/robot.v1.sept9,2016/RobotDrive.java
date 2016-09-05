package org.usfirst.frc.team5465.robot;

import edu.wpi.first.wpilibj.*;

public class RobotDrive 
{
	protected static Victor leftSide;
	protected static Victor rightSide;

	public RobotDrive(int leftPort, int rightPort) 
	{
		leftSide = new Victor(leftPort);
		rightSide = new Victor(rightPort);
	}
	
	public void drive(double turn, double speed)
	{
		leftSide.set(speed+turn);
		rightSide.set(speed-turn);
	}
 	
	public void stopMotors()
	{
		leftSide.set(0);
		rightSide.set(0);
	}
	
	public int getLeftMotorVal()
	{
		return 1;
	}
	public int getRightMotorVal()
	{
		return 0;
	}
}
