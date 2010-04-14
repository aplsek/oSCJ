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

package edu.purdue.scj;

import javax.realtime.LibraryImports;
import javax.realtime.MemoryArea;

import org.ovmj.java.Opaque;

public class VMSupport {

    /*------ Memory ------*/

    /**
     * TODO: OVM does not support long integer? We simply cast long to int
     * without caring it is right or not.
     */
    public static Opaque makeExplicitArea(long size) {
        return LibraryImports.makeExplicitArea((int) size);
    }

    /**
     */
    public static void destroyArea(Opaque area) {
        LibraryImports.destroyArea(area);
    }

    public static Opaque makeArea(MemoryArea mirror, long size) {
        return LibraryImports.makeArea(mirror, (int) size);
    }

    public static MemoryArea getAreaMirror(Opaque area) {
        return LibraryImports.getAreaMirror(area);
    }

    /**
     * Set the scope as current allocation space and return the previous one.
     * The "scope" can be on any one of the currently active threads' scope
     * stack or be the immortal memory.
     */
    public static Opaque setCurrentArea(Opaque scope) {
        return LibraryImports.setCurrentArea(scope);
    }

    /** Get the ID of the scope which serves as current allocation space. */
    public static Opaque getCurrentArea() {
        return LibraryImports.getCurrentArea();
    }

    /** Get the ID of immortal memory */
    public static Opaque getImmortalArea() {
        return LibraryImports.getImmortalArea();
    }

    /** Get the scope where object ref was allocated in */
    public static Opaque areaOf(Object ref) {
        return LibraryImports.areaOf(ref);
    }

    /** As the name suggests */
    public static long getScopeSize(Opaque scope) {
        return LibraryImports.getAreaSize(scope);
    }

    /** As the name suggests */
    public static long memoryConsumed(Opaque scope) {
        return LibraryImports.memoryConsumed(scope);
    }

    /** As the name suggests */
    public static long memoryRemaining(Opaque scope) {
        return LibraryImports.memoryRemaining(scope);
    }
    
    public static void resetArea(Opaque area) {
		LibraryImports.resetArea(area);
	}

    // /**
    // * Create and push a "size" byte large scope on the ScopeStack stack of
    // * thread t. Return the ID.
    // */
    // public static Opaque pushScopeInStackSpace(Thread t,
    // long size){}
    //
    // /** Pop the top of thread t's ScopeStack stack. */
    // public static void popScopeInStackSpace(Thread t){}

    /*------ Thread ------*/

    /** the minimum RT priority available for application threads */
    public static int getMinRTPriority() {
        return LibraryImports.getMinRTPriority();
    }

    /** the maximum RT priority available for application threads */
    public static int getMaxRTPriority() {
        return LibraryImports.getMaxRTPriority();
    }

    /** Not used now */
    public static void setInterruptable(boolean set) {
    }

    /** Not used now */
    public static void threadSetInterrupted(Thread t, boolean set) {
    }

    /**
     * @return 0 if succeed{} -1 if sleep time was in the past{} 1 if sleep was
     *         interrupted.
     */
    public static int delayCurrentThreadAbsolute(long nanos) {
        return LibraryImports.delayCurrentThreadAbsolute(nanos);
    }

    /*------ Per-Thread Parameters ------*/

    /**
     * TODO: setting backing store size for each thread is not supported by OVM
     * now
     */
    public static void setTotalBackingStore(Thread t, long size) {
    }

    /** Not used now */
    public static void setStackSize(Thread t, long size) {
    }

    /** Not used now */
    public static void setJavaStackSize(Thread t, long size) {
    }

    /*------ Time -------*/

    /** In nanosecond. */
    public static long getCurrentTime() {
        return LibraryImports.getCurrentTime();
    }

    /** In nanosecond. */
    public static long getClockResolution() {
        return LibraryImports.getClockResolution();
    }

    /*------ PCE -------*/

    /** Not used now */
    public static int getPriorityCeiling(Object obj) {
        return -1;
    }

    /** Not used now */
    public static void setPriorityCeiling(Object obj, int ceiling) {
    }

    /*------ Size Of -------*/

    public static long sizeOf(Class clazz) {
        return LibraryImports.sizeOf(clazz);
    }

    public static long sizeOfReferenceArray(int length) {
        return LibraryImports.sizeOfReferenceArray(length);
    }

    public static long sizeOfPrimitiveArray(int length, Class clazz) {
        return LibraryImports.sizeOfPrimitiveArray(length, clazz);
    }

    // static long sizeOfMonitor();

    /*------ Async Events & Interrupt Handling-------*/

    // static void waitForInterrupted(int interruptIndex);
    //
    // static void registerInterruptHandler();
    //	  
    // static void interruptServed(int interruptIndex);
    //	  
    // static boolean isMonitoredInterrupt(int interruptIndex);
    //	  
    // static boolean stopMonitoringInterrupt(int interruptIndex);
    //	  
    // static boolean startMonitoringInterrupt(int interruptIndex);
    /*------ misc -------*/

    public static void storeInOpaqueArray(Opaque[] arr, int index, Opaque val) {
        LibraryImports.storeInOpaqueArray(arr, index, val);
    }
    /*------ Debug -------*/
    // TBA
    
    public static void RPT_Ipoint(int i) {
    	LibraryImports.RPT_Ipoint(i);
    }
}
