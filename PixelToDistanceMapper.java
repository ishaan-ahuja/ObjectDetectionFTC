//this code can convert the detected object in the camera feed to actual distance in inches. 
//Lowkey its just some regression like the thing you do in desmos but its in java now and 
//it is integrated into the computer vision part of my robot's autonomous system.
//Kinda found some math online for regression (NOT CHATGPT), but i implemented it myself in java.
//Callibration is a pain in the ass so make sure you dont move camera while your doing callibration.
//you have to move the object and manually find its distance from the camera each time.
//try to do as many trials as you can cause you dont want to have an innaccurate conversion.
//it took me 3 hours to get a good data set, make sure you also take ur time with this process.

package org.firstinspires.ftc.teamcode.PostLobsterCup.Layer1.Intake.Vision;

public class PixelToDistanceMapper {

    private double[] directDistCoeffs;
    private double[] forwardDistCoeffs;
    private double[] horizOffsetCoeffs;


    public PixelToDistanceMapper(double[][] calibrationData) {
        directDistCoeffs = computeLinearRegression(calibrationData, 2);  // directDist
        horizOffsetCoeffs = computeLinearRegression(calibrationData, 3); // horizOffset
        forwardDistCoeffs = computeLinearRegression(calibrationData, 4); //forwardDist
    }

    private double[] computeLinearRegression(double[][] data, int outputIndex) {
        int n = data.length;
        double sumX = 0, sumY = 0, sumZ = 0;
        double sumXX = 0, sumYY = 0, sumXY = 0;
        double sumXZ = 0, sumYZ = 0;

        for (int i = 0; i < n; i++) {
            double x = data[i][0];
            double y = data[i][1];
            double z = data[i][outputIndex];

            sumX += x;
            sumY += y;
            sumZ += z;
            sumXX += x * x;
            sumYY += y * y;
            sumXY += x * y;
            sumXZ += x * z;
            sumYZ += y * z;
        }


        double[][] A = {
                {sumXX, sumXY, sumX},
                {sumXY, sumYY, sumY},
                {sumX,  sumY,  n}
        };
        double[] B = {sumXZ, sumYZ, sumZ};

        return solveLinearSystem(A, B);  
    }
    //math stuff online, again not chatgpt or any AI, trust me gang
    private double[] solveLinearSystem(double[][] A, double[] B) {
        int n = 3;
        double[][] mat = new double[n][n+1];
        for (int i = 0; i < n; i++) {
            System.arraycopy(A[i], 0, mat[i], 0, n);
            mat[i][n] = B[i];
        }

        for (int i = 0; i < n; i++) {
            int maxRow = i;
            for (int k = i + 1; k < n; k++) {
                if (Math.abs(mat[k][i]) > Math.abs(mat[maxRow][i])) {
                    maxRow = k;
                }
            }
            double[] temp = mat[i];
            mat[i] = mat[maxRow];
            mat[maxRow] = temp;

            for (int k = i + 1; k < n; k++) {
                double factor = mat[k][i] / mat[i][i];
                for (int j = i; j <= n; j++) {
                    mat[k][j] -= factor * mat[i][j];
                }
            }
        }

        double[] x = new double[n];
        for (int i = n - 1; i >= 0; i--) {
            x[i] = mat[i][n] / mat[i][i];
            for (int k = i - 1; k >= 0; k--) {
                mat[k][n] -= mat[k][i] * x[i];
            }
        }
        return x;
    }

    public DistanceResult getDistanceFromPixel(double x, double y) {
        double directDist = evalModel(directDistCoeffs, x, y);
        double forwardDist = evalModel(forwardDistCoeffs, x, y);
        double horizOffset = evalModel(horizOffsetCoeffs, x, y);

        return new DistanceResult(directDist, forwardDist, horizOffset);
    }

    private double evalModel(double[] coeffs, double x, double y) {
        return coeffs[0] * x + coeffs[1] * y + coeffs[2];
    }

    public static class DistanceResult {
        public double directDist;
        public double forwardDist;
        public double horizOffset;

        public DistanceResult(double d, double f, double h) {
            this.directDist = d;
            this.forwardDist = f;
            this.horizOffset = h;
        }
    }
}
