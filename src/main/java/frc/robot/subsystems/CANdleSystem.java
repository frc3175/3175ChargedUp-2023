// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import com.ctre.phoenix.led.*;
import com.ctre.phoenix.led.CANdle.LEDStripType;

public class CANdleSystem extends SubsystemBase {
    private final CANdle m_candle = new CANdle(Constants.CANdleID, "rio");

    public LEDState ledstate;

    public CANdleSystem() {
        CANdleConfiguration configAll = new CANdleConfiguration();
        configAll.statusLedOffWhenActive = true;
        configAll.disableWhenLOS = false;
        configAll.stripType = LEDStripType.RGB;
        configAll.brightnessScalar = 0.1;
        m_candle.configAllSettings(configAll, 100);
        ledstate = LEDState.CUBE;
    }

    @Override
    public void periodic() {

    }

    @Override
    public void simulationPeriodic() {
        // This method will be called once per scheduler run during simulation
    }

    public void setLEDSTate(LEDState state) {

        ledstate = state;
        m_candle.setLEDs(state.r, state.g, state.b);

    }

    public LEDState getLEDState() {

        return ledstate;

    }

    public enum LEDState {

        CUBE(191, 64, 191),
        CONE(255, 255, 0);

        public final int r;
        public final int g;
        public final int b;

        private LEDState(int r, int g, int b) {
            this.r = r;
            this.g = g;
            this.b = b;
        }

    }

}
