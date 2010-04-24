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

package javax.realtime;

import java.lang.reflect.Array;

import javax.safetycritical.annotate.BlockFree;
import javax.safetycritical.annotate.Level;
import javax.safetycritical.annotate.SCJAllowed;
import javax.safetycritical.annotate.SCJProhibited;

import org.ovmj.java.Opaque;

import edu.purdue.scj.VMSupport;

@SCJAllowed
public abstract class MemoryArea implements AllocationContext {
    /**
     * space for temporary region when making a temporary scope stack. Normally
     * it just needs to hold the actual ScopeStack object, but assertions and
     * debug code can change that.
     */
    static final long _TEMPSIZE = ScopeStack._BASE_SIZE;
    public Opaque area;
    long _size;

    /** For ScopedMemory use only. */
    protected MemoryArea(long size) {
        if (size < 0)
            throw new IllegalArgumentException("size must be non-negative");
        _size = size;
        area = VMSupport.makeArea(this, _size);
    }

    /**
     * For ImmortalMemory use only. The immortal backing store is always there,
     * so we know how to set up areas.
     */
    protected MemoryArea(Opaque scopeID) {
        area = scopeID;
        _size = VMSupport.getScopeSize(area);
    }

    @SCJAllowed
    public static MemoryArea getMemoryArea(Object object) {
        return getMemoryAreaObject(VMSupport.areaOf(object));
    }

    @SCJProhibited
    public void enter(Runnable logic) {
        if (logic == null)
            throw new IllegalArgumentException("null logic not permitted");
        RealtimeThread thread = RealtimeThread.currentRealtimeThread();
        enterImpl(thread, logic);
    }

    @SCJAllowed(Level.LEVEL_1)
    public void executeInArea(Runnable logic) throws InaccessibleAreaException {
        if (logic == null)
            throw new IllegalArgumentException("null logic not permitted");
        RealtimeThread thread = RealtimeThread.currentRealtimeThread();
        execInAreaImpl(thread, logic);
    }

    @SCJAllowed
    public Object newInstance(Class clazz) throws IllegalAccessException,
            InstantiationException {
        RealtimeThread thread = RealtimeThread.currentRealtimeThread();
        return newInstanceImpl(thread, clazz);
    }

    @BlockFree
    @SCJAllowed
    public Object newArray(Class clazz, int number)
            throws IllegalAccessException {
        RealtimeThread thread = RealtimeThread.currentRealtimeThread();
        return newArrayImpl(thread, clazz, number);
    }

    @SCJAllowed
    public static Object newArrayInArea(Object object, Class clazz, int size)
            throws IllegalAccessException {
        return getMemoryArea(object).newArray(clazz, size);
    }

    @SCJAllowed
    public static Object newInstanceInArea(Object object, Class clazz)
            throws IllegalAccessException, InstantiationException {
        return getMemoryArea(object).newInstance(clazz);
    }

    @SCJAllowed
    public long memoryConsumed() {
        return VMSupport.memoryConsumed(area);
    }

    @SCJAllowed
    public long memoryRemaining() {
        return VMSupport.memoryRemaining(area);
    }

    @SCJAllowed
    public long size() {
        return _size;
    }

    /**
     * Assumption: only one thread is allowed to enter and be active in a
     * ScopedMemory at a time. This is particularly true according to SCJ
     * requirements, and guaranteed by the runtime check in PrivateMemory and
     * MissionMemory.
     * 
     * The right size of backing store is allocated during enter, and
     * deallocated on exit.
     */
    final void enterImpl(RealtimeThread thread, Runnable logic) {
        preScopeEnter(thread);
        thread.getScopeStack().push(this);
        Opaque oldScope = VMSupport.setCurrentArea(area);

        // TODO: what to do with exception?
        try {
            logic.run();
            // reThrowTBE(oldScope, e);
        } finally {
            thread.getScopeStack().pop();
            VMSupport.resetArea(area);
            VMSupport.setCurrentArea(oldScope);
            postScopeEnter();
        }
    }

    /**
     * Unwinds the scope stack so that <tt>this</tt> is the current area,
     * executes the logic and then restores the original scope stack and current
     * allocation context.
     */
    final void execInAreaImpl(RealtimeThread thread, Runnable logic) {
        ScopeStack stack = thread.getScopeStack();
        int oldActivePointer = stack.getDepth(true);
        int indexOfThis = stack.getIndex(this, true);

        stack.setActivePointer(indexOfThis);
        Opaque oldScope = VMSupport.setCurrentArea(area);

        try {
            logic.run();
        } finally {
            VMSupport.setCurrentArea(oldScope);
            stack.setActivePointer(oldActivePointer);
        }
    }

    final Object newArrayImpl(RealtimeThread thread, Class clazz, int number)
            throws IllegalAccessException, NegativeArraySizeException {
        ScopeStack stack = thread.getScopeStack();
        int oldActivePointer = stack.getDepth(true);
        int indexOfThis = stack.getIndex(this, true);

        stack.setActivePointer(indexOfThis);
        Opaque oldScope = VMSupport.setCurrentArea(area);

        try {
            return Array.newInstance(clazz, number);
        } finally {
            VMSupport.setCurrentArea(oldScope);
            stack.setActivePointer(oldActivePointer);
        }
    }

    final Object newInstanceImpl(RealtimeThread thread, Class clazz)
            throws IllegalAccessException, InstantiationException {
        ScopeStack stack = thread.getScopeStack();
        int oldActivePointer = stack.getDepth(true);
        int indexOfThis = stack.getIndex(this, true);

        stack.setActivePointer(indexOfThis);
        Opaque oldScope = VMSupport.setCurrentArea(area);

        try {
            return clazz.newInstance();
        } finally {
            VMSupport.setCurrentArea(oldScope);
            stack.setActivePointer(oldActivePointer);
        }
    }

    static MemoryArea getMemoryAreaObject(Opaque scopeID) {
        if (scopeID == ImmortalMemory.instance().area)
            return ImmortalMemory.instance();
        else
            return (MemoryArea) VMSupport.getAreaMirror(scopeID);
    }

    /**
     * Set the memory to a new size and allocate the backing store. This is for
     * mission memory use only. No other types of memory can be resized. NOTE:
     * resizing is allowed ONLY when NO thread is in the memory area.
     */
    protected void setSize(long size) {
        if (size < 0)
            throw new IllegalArgumentException(
                    "Mission memory size must be non-negative");
        _size = size;
        VMSupport.destroyArea(area);
        area = VMSupport.makeArea(this, _size);
    }

    protected void preScopeEnter(RealtimeThread thread) {
    }

    protected void postScopeEnter() {
    }
}
