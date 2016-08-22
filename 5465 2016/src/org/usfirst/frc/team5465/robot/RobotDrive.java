package org.usfirst.frc.team5465.robot;

import edu.wpi.first.wpilibj.*;

public abstract class RobotDrive 
{
	protected Victor leftSide;
	protected Victor rightSide;

	public RobotDrive(int leftPort, int rightPort) 
	{
		leftSide = new Victor(leftPort);
		rightSide = new Victor(rightPort);
	}
	
	public abstract void drive(double turn, double speed);
 	
	public void stopMotors()
	{
		leftSide.set(0);
		rightSide.set(0);
	}
}
