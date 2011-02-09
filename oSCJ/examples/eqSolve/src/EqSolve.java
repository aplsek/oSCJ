/**
 *  This file is part of oSCJ.
 *
 *   oSCJ is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Lesser General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   oSCJ is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with oSCJ.  If not, see <http://www.gnu.org/licenses/>.
 *
 *
 *   Copyright 2009, 2010 
 *   @authors  Lei Zhao, Ales Plsek
 */

import javax.realtime.AbsoluteTime;
import javax.realtime.Clock;
import javax.realtime.ImmortalMemory;
import javax.realtime.MemoryArea;
import javax.realtime.RealtimeThread;
import javax.realtime.RelativeTime;
import javax.safetycritical.CyclicExecutive;
import javax.safetycritical.CyclicSchedule;
import javax.safetycritical.MissionManager;
import javax.safetycritical.PeriodicEventHandler;
import javax.safetycritical.Safelet;
import javax.safetycritical.StorageParameters;
import javax.safetycritical.Terminal;

import edu.purdue.scj.VMSupport;
import edu.purdue.scj.utils.Utils;

public class EqSolve extends CyclicExecutive {

    public EqSolve() {
        super(null);
    }

    public static void main(final String[] args) {
        Safelet safelet = new EqSolve();
        safelet.setUp();
        safelet.getSequencer().start();
        safelet.tearDown();
    }

    private static void writeln(String msg) {
        // Terminal.getTerminal().writeln(msg);
    }

    public CyclicSchedule getSchedule(PeriodicEventHandler[] handlers) {
        CyclicSchedule.Frame[] frames = new CyclicSchedule.Frame[1];
        CyclicSchedule schedule = new CyclicSchedule(frames);
        frames[0] = new CyclicSchedule.Frame(new RelativeTime(200, 0),
                handlers);
        return schedule;
    }

    public void initialize() {
        new Handler(800000, "EqSolve.\n");
    }

    /**
     * A method to query the maximum amount of memory needed by this
     * mission.
     * 
     * @return the amount of memory needed
     */
    // @Override
    public long missionMemorySize() {
        return 400000;
    }

    public void setUp() {
        Terminal.getTerminal().write("setUp.\n");
    }

    public void tearDown() {
        Terminal.getTerminal().write("teardown.\n");
    }

    public void cleanUp() {
        Terminal.getTerminal().write("cleanUp.\n");
    }





    public class Handler extends PeriodicEventHandler {

        static final int ITERATION = 7;
        
        /*  ==> input signals     */
        public float a, b, c;
        
        float[] aIN = new float[]{2,1,0,1,1,4,0};
        float[] bIN = new float[]{0,-5,0,2,4,8,0};
        float[] cIN = new float[]{-2,6,0,1,3,1,1};
        
        
        //private eqSolve_io _io_;
        //private KeyboardReader KR = new KeyboardReader();
        /*  ==> parameters and indexes */
       
        /*  ==> output signals    */
        public boolean x_st;
        public float x1, x2;
        /*  ==> local signals     */
        public boolean stable, x_st_1;
        public float x21, delta, x1_1, R_n, next_R_n;
        public boolean loop;
        public float S_n;
        public boolean next_stable;
        public float y, XZX_1562, XZX_1581, XZX_1585;
        public boolean C_, C_delta, C_delta_1634, C_x_st_1, C_x_st_1_1639, C_c1, C_c1_1648, C_x_st_2, C_x_st_2_1653, C_x1_1, C_x1_1_1664, C__1669, C__1675, C_x1_2, 
        C__1694, C_x_st, C_x21, C_x1;



        private int count_ = 0;

        private Handler(long psize, String name) {
            super(null, null, new StorageParameters(psize, 0 , 0), name);
            
            eqSolve_step_initialize();
        }

        public void handleAsyncEvent() {
            Terminal.getTerminal().write(getName() + "..." );

            
            try {
                eqSolve_step();
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            if (count_++ == ITERATION)
                getCurrentMission().requestSequenceTermination();
        }

        public void cleanUp() {
        }

        public StorageParameters getThreadConfigurationParameters() {
            return null;
        }
        
        
        
        /**
         * 
         * 
         * 
         * 
         * 
         * 
         */
        

        public void eqSolve_step_initialize()
        {
            C_ = false;
            C_delta = false;
            C_c1 = false;
            C_x1_1 = false;
            C__1669 = false;
        }
        
        
        private float read_A() {
            return aIN[count_];
        }

        
        private float read_B() {
            return bIN[count_];
        }
        
        private float read_C() {
            return cIN[count_];
        }
        
        
        boolean eqSolve_step() throws Exception {
            Terminal.getTerminal().write(" \t step :" + count_  + "\n" );
            
            Terminal.getTerminal().write(" \t\t stable :" + stable  + "\n" );
            
            if (stable)
            {
                a = read_A();  //if (!_io_.r_eqSolve_a(this)) return false;
                b = read_B();  //if (!_io_.r_eqSolve_b(this)) return false;
                c = read_C();  //if (!_io_.r_eqSolve_c(this)) return false;
                
                Terminal.getTerminal().write(" \t a :" + a  + "\n" );
                Terminal.getTerminal().write(" \t b :" + b  + "\n" );
                Terminal.getTerminal().write(" \t c :" + c  + "\n" );
                
                
                C_ = a == 0.0f;
                C_delta = !(a == 0.0f);
                if (C_delta)
                {
                    delta = b * b - (4.0f * a) * c;
                    C_x_st_2 = delta < 0.0f;
                    C_x1_1 = delta == 0.0f;
                    C__1669 = delta > 0.0f;
                    if (C_x1_1)
                    {
                        x1_1 = -b / (2.0f * a);
                    }
                }
                C_x_st_2_1653 = (C_delta ? C_x_st_2 : false);
                if (C_)
                {
                    C_x_st_1 = b == 0.0f;
                    C_c1 = !(b == 0.0f);
                    if (C_x_st_1)
                    {
                        x_st_1 = c != 0.0f;
                    }
                }
                C_x_st_1_1639 = (C_ ? C_x_st_1 : false);
                C_x_st = C_x_st_1_1639 || C_x_st_2_1653;
                if (C_x_st)
                {
                    if (C_x_st_2_1653) x_st = true; else x_st = x_st_1; 
                    output_x_st(x_st);  // _io_.w_eqSolve_x_st(this);
                    
                }
            }
            C_delta_1634 = (stable ? C_delta : false);
            C_c1_1648 = (C_ ? C_c1 : false);
            C_x1_1_1664 = (C_delta ? C_x1_1 : false);
            C__1675 = (C_delta ? C__1669 : false);
            if (C__1675)
                S_n = delta;
            if (C__1675) R_n = delta / 2.0f; else R_n = next_R_n; 
            if (C__1675) loop = true; else loop = !stable; 
            if (loop) next_R_n = (R_n + S_n / R_n) / 2.0f; else next_R_n = R_n; 
            XZX_1562 = next_R_n - R_n;
            if (XZX_1562 >= 0.0f) y = XZX_1562; else y = -XZX_1562; 
            next_stable = y < 0.02f;
            C_x1_2 = next_stable && loop;
            C__1694 = C_x1_2 || C_delta_1634;
            C_x21 = C_x1_2 || C_x1_1_1664;
            C_x1 = C_x21 || C_c1_1648;
            if (C__1694)
            {
                if (C_delta_1634)
                    XZX_1581 = a;
                if (C_delta_1634)
                    XZX_1585 = b;
            }
            if (C_x1_2)
            {
                x2 = -((XZX_1585 - R_n) / (2.0f * XZX_1581));
                output_x2(x2);   //_io_.w_eqSolve_x2(this);
            }
            if (C_x21)
            {
                if (C_x1_1_1664) x21 = x1_1; else x21 = -((XZX_1585 + R_n) / (2.0f * XZX_1581)); 
            }
            if (C_x1)
            {
                if (C_c1_1648) x1 = -(c / b); else x1 = x21; 
                output_x1(x1); // _io_.w_eqSolve_x1(this);
            }
            eqSolve_step_finalize();
            return true;
        }

        boolean eqSolve_step_finalize() throws Exception {
            stable = next_stable;
            C_x_st_1 = false;
            C_x_st_2 = false;
            eqSolve_step_initialize();
            return true;
        }

        /**
         * _io_.w_eqSolve_x2(this);
         */
        private void output_x2 (float x2) {
            Terminal.getTerminal().write(" x2" +x2 + "\n");
        }
        

        /**
         * _io_.w_eqSolve_x2(this);
         */
        private void output_x1 (float x1) {
            Terminal.getTerminal().write(" x1 " + x1 + "\n");
        }
        

        /**
         * _io_.w_eqSolve_x2(this);
         */
        private void output_x_st (boolean x_st) {
            Terminal.getTerminal().write(" x_st:" + x_st + "\n");
        }
        
    }

}
