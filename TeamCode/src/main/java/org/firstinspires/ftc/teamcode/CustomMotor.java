import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;
import com.acmerobotics.roadrunner.control.PIDFController;
import com.acmerobotics.roadrunner.control.PIDCoefficients;
import java.lang.Math;

public class CustomMotor {
    public String name;
    public DcMotor motor =null;
    public PIDCoefficients coeffs;
    public PIDFController controller;
    public CustomMotor(String Name, PIDCoefficients coefficients){
        name =Name;
        coeffs = coefficients;
        controller = new PIDFController(coeffs);
    }
}