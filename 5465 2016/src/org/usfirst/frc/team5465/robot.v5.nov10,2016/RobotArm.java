package org.usfirst.frc.team5465.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Solenoid;

public class RobotArm 
{
	private Solenoid forwardSolenoid;
	private Solenoid reverseSolenoid;
	
	private CANTalon armTalon;
	
	public RobotArm()
	{
		armTalon = new CANTalon(0);
		forwardSolenoid = new Solenoid(0);
		reverseSolenoid = new Solenoid(1);
	}
	
	public void moveRobotArm(double y)
	{
		armTalon.set(y);
	}
	
	public void stopRobotArm()
	{
		armTalon.set(0);
	}
	public double getRobotArmMotorValue()
	{
		return armTalon.get();
	}
	
	public boolean getPiston()
	{
		return forwardSolenoid.get();
	}
	
	public void actuate(boolean out)
	{
		if(out)
		{
			forwardSolenoid.set(true);
			reverseSolenoid.set(false);
		}
		else
		{
			forwardSolenoid.set(false);
			reverseSolenoid.set(true);
		}
	}
}