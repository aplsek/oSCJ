package workload;

import javax.safetycritical.annotate.RunsIn;
import javax.safetycritical.annotate.SCJAllowed;
import cdx.Constants;
import cdx.RawFrame;
import static javax.safetycritical.annotate.Scope.CALLER;
import static javax.safetycritical.annotate.Scope.IMMORTAL;
import javax.safetycritical.annotate.Scope;

@Scope(IMMORTAL)
@SCJAllowed(members = true)
public class WorkloadStar extends FrameBuffer {

    final double   tic      = 0.25f;
    final double   lenght   = 20;
    static double  t        = 0;

    final double[] angels   = new double[Constants.NUMBER_OF_PLANES];

    final double   offset_x = 100;
    final double   offset_y = 100;
    final double   offset_z = 5.0f;

    double[]       last_px  = new double[Constants.NUMBER_OF_PLANES];
    double[]       last_py  = new double[Constants.NUMBER_OF_PLANES];

    boolean        outbound = true;

    int            CYCLES   = (int) (lenght / tic) * 2;

    public WorkloadStar() {
        super();
        init_angels();
    }

    @Override
    @RunsIn(CALLER)
    public RawFrame getFrame() {
        for (byte k = 0; k < Constants.NUMBER_OF_PLANES; k++) {
            callsigns[6 * k] = 112;
            callsigns[6 * k + 1] = 108;
            callsigns[6 * k + 2] = 97;
            callsigns[6 * k + 3] = 110;
            callsigns[6 * k + 4] = 101;
            callsigns[6 * k + 5] = (byte) (49 + k);
        }
        float positions[] = new float[Constants.NUMBER_OF_PLANES * 3];

        // System.out.println("frame " + frameno);
        for (byte k = 0; k < Constants.NUMBER_OF_PLANES; k++) {
            if (frameno != 0) // we dont move in the first frame
            if (outbound) line(k);
            else line_bck(k);

            positions[3 * k] = (float) last_px[k];
            positions[3 * k + 1] = (float) last_py[k];
            positions[3 * k + 2] = (float) offset_z;

            // System.out.println("plane :" + k + " x:" + last_px[k] + "-- y:" + last_py[k] );

        }

        if (frameno != 0 && ((frameno) % CYCLES) == 0) {
            outbound = !outbound;
        }

        // increase the time
        t = t + tic;
        buf.copy(lengths, callsigns, positions);

        frameno++;
        return buf;
    }

    @RunsIn(CALLER)
    private void line(int k) {
        double alpha = angels[k];
        last_px[k] = last_px[k] + tic * Math.cos(alpha);
        last_py[k] = last_py[k] - tic * Math.sin(alpha);
    }

    @RunsIn(CALLER)
    private void line_bck(int k) {
        double alpha = angels[k];
        last_px[k] = last_px[k] - tic * Math.cos(alpha);
        last_py[k] = last_py[k] + tic * Math.sin(alpha);
    }

    @RunsIn(CALLER)
    private void init_angels() {
        double step = 180 / Constants.NUMBER_OF_PLANES;
        double angel = 0;
        for (int k = 0; k < Constants.NUMBER_OF_PLANES; k++) {
            angels[k] = Math.toRadians(angel);
            last_px[k] = offset_x - lenght * Math.cos(angels[k]);
            last_py[k] = offset_y + lenght * Math.sin(angels[k]);

            // System.out.println("angel:" + angel);
            // System.out.println("lenght x:" + lenght * Math.cos(angels[k]));
            // System.out.println("lenght y:" + lenght * Math.sin(angels[k]));

            angel += step;
        }

        // System.out.println("angels" + angels);
    }

    @Override
    public void putFrame(float[] positions, int[] lengths, byte[] callsigns) {}

    @Override
    public void putFrameInternal(float[] positions, int[] lengths, byte[] callsigns) {}

}
