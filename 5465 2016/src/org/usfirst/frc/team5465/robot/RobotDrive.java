package org.usfirst.frc.team5465.robot;

import edu.wpi.first.wpilibj.*;

public class RobotDrive 
{
	private Victor leftSide;
	private Victor rightSide;

	public RobotDrive() 
	{
		leftSide = new Victor(0);
		rightSide = new Victor(1);
	}
	
	public void drive(double x, double y)
	{
		leftSide.set(y+x);
		rightSide.set(y-x);
	}
 	
	public void stopMotors()
	{
		leftSide.set(0);
		rightSide.set(0);
	}
}
