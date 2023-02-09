package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.POVButton;
import frc.robot.autos.automodes.Auto;
import frc.robot.commands.*;
import frc.robot.subsystems.*;
import frc.robot.subsystems.Intake.IntakeState;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {

    /* Controllers */
    private final XboxController driver = new XboxController(0);
    private final XboxController operator = new XboxController(1);

    /* Drive Controls */
    private final int translationAxis = XboxController.Axis.kLeftY.value;
    private final int strafeAxis = XboxController.Axis.kLeftX.value;
    private final int rotationAxis = XboxController.Axis.kRightX.value;

    /* Driver Buttons */
    private final JoystickButton zeroGyro = new JoystickButton(driver, XboxController.Button.kX.value);
    //private final JoystickButton robotCentric = new JoystickButton(driver, XboxController.Button.kRightBumper.value);

    /* Operator Buttons */
    private final JoystickButton cubeMode = new JoystickButton(operator, XboxController.Button.kRightBumper.value);
    private final JoystickButton coneMode = new JoystickButton(operator, XboxController.Button.kLeftBumper.value);
    private final JoystickButton robotHigh = new JoystickButton(operator, XboxController.Button.kY.value);
    private final JoystickButton reset = new JoystickButton(operator, XboxController.Button.kA.value);
    private final JoystickButton substation = new JoystickButton(operator, XboxController.Button.kX.value);
    private final JoystickButton robotMid = new JoystickButton(operator, XboxController.Button.kB.value);
    private final POVButton hoodDown = new POVButton(operator, 0);
    private final POVButton hoodUp = new POVButton(operator, 180);
    private final POVButton override = new POVButton(operator, 90);
    private final JoystickButton start = new JoystickButton(operator, XboxController.Button.kStart.value);
    private final JoystickButton back = new JoystickButton(operator, XboxController.Button.kBack.value);

    /* Subsystems */
    public static final SwerveDrivetrain m_drivetrain = new SwerveDrivetrain();
    public static final Elevator m_elevator = new Elevator();
    public static final Intake m_intake = new Intake();
    public static final Slide m_slide = new Slide();
    public static final RobotState m_robotState = new RobotState(m_intake, m_elevator, m_slide);

    /** The container for the robot. Contains subsystems, OI devices, and commands. */
    public RobotContainer() {

        m_drivetrain.setDefaultCommand(
            new SwerveDrive(
                m_drivetrain, 
                () -> -driver.getRawAxis(translationAxis), 
                () -> -driver.getRawAxis(strafeAxis), 
                () -> -driver.getRawAxis(rotationAxis), 
                () -> false
            )
        );

        // Configure the button bindings
        configureButtonBindings();
        
    }

    /**
     * Use this method to define your button->command mappings. Buttons can be created by
     * instantiating a {@link GenericHID} or one of its subclasses ({@link
     * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
     * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
     */
    private void configureButtonBindings() {

        /* Driver Buttons */
        zeroGyro.onTrue(new InstantCommand(() -> m_drivetrain.zeroGyro()));

        /* Operator Buttons */

        /*intake.whileTrue(new InstantCommand(() -> m_intake.setIntake(Constants.INTAKE_TEST_SPEED)));
        intake.onFalse(new InstantCommand(() -> m_intake.setIntake(0)));
        outtake.whileTrue(new InstantCommand(() -> m_intake.setIntake(-Constants.INTAKE_TEST_SPEED)));
        outtake.onFalse(new InstantCommand(() -> m_intake.setIntake(0))); */

        /*slideIn.onTrue(new InstantCommand(() -> m_elevator.setElevatorSetpoint(50000)));
        elevatorUp.onTrue(new InstantCommand(() -> m_elevator.setElevatorSetpoint(100000)));
        slideOut.onTrue(new InstantCommand(() -> m_elevator.setElevatorSetpoint(200000)));
        elevatorDown.onTrue(new InstantCommand(() -> m_elevator.setElevatorSetpoint(0))); */

        /*outtake.onTrue(new InstantCommand(() -> m_slide.setSlide(30000)));
        intake.onTrue(new InstantCommand(() -> m_slide.setSlide(0))); */

        /* hoodUp.onTrue(new InstantCommand(() -> m_intake.intakeHoodUp()));
        hoodUp.onFalse(new InstantCommand(() -> m_intake.setHoodSpeed(0)));
        hoodDown.onTrue(new InstantCommand(() -> m_intake.intakeHoodDown()));
        hoodDown.onFalse(new InstantCommand(() -> m_intake.setHoodSpeed(0))); */

           /*  slideOut.onTrue(new SequentialCommandGroup(new ParallelCommandGroup(new InstantCommand(() -> m_intake.setIntake(-Constants.INTAKE_TEST_SPEED)),
                                                new SequentialCommandGroup(new InstantCommand(() -> m_elevator.setElevatorSetpoint(100000)),
                                                                           new InstantCommand(() -> m_slide.setSlide(15000)))), 
                                                                           new InstantCommand(() -> m_intake.intakeHoodDown())));

            slideIn.onTrue(new SequentialCommandGroup(new InstantCommand(() -> m_intake.intakeHoodUp()),
             new ParallelCommandGroup(new InstantCommand(() -> m_intake.setIntake(0)),
             new SequentialCommandGroup(new InstantCommand(() -> m_elevator.setElevatorSetpoint(0)),
                                   new InstantCommand(() -> m_slide.setSlide(0)))))); */

        coneMode.onTrue(new InstantCommand(() -> m_robotState.setGamepieceState(true)));
        cubeMode.onTrue(new InstantCommand(() -> m_robotState.setGamepieceState(false)));

        robotHigh.onTrue(new SetRobotStateHigh(m_robotState, m_intake));
        reset.onTrue(new ResetRobot(m_robotState, m_intake));
        robotMid.onTrue(new SetRobotStateMid(m_robotState, m_intake));

        /* hoodDown.whileTrue(new SetIntake(m_intake, m_robotState));
        hoodDown.whileFalse(new InstantCommand(() -> m_intake.setIntake(0)));
        hoodDown.whileFalse(new InstantCommand(() -> m_intake.setWristPosition(0))); */

        start.onTrue(new SetIntake(m_intake, m_robotState));
        start.whileFalse(new InstantCommand(() -> m_intake.setIntakeState(IntakeState.STOP)));

        hoodDown.whileTrue(new InstantCommand(() -> m_intake.continuousWristMotion(-0.2)));
        hoodDown.whileFalse(new InstantCommand(() -> m_intake.continuousWristMotion(0)));


    }

    public Command getAutonomousCommand() {

        return Auto.exampleAuto();

    }

}