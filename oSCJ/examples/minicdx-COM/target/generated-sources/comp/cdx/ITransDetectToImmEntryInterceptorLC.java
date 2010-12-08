/*
 * Generated by: org.objectweb.fractal.juliac.proxy.InterceptorClassGenerator
 * on: Tue Nov 23 15:17:21 CET 2010
 */

package comp.cdx;


public class ITransDetectToImmEntryInterceptorLC
implements comp.cdx.ITransDetectToImmEntry,org.objectweb.fractal.julia.Interceptor {

  private comp.cdx.ITransDetectToImmEntry _impl;
  private org.objectweb.fractal.koch.factory.MPrimitiveImpl _lc;
  public ITransDetectToImmEntryInterceptorLC()  {
  }

  public ITransDetectToImmEntryInterceptorLC(Object obj)  {
    setFcItfDelegate(obj);
  }

  public void initFcController(org.objectweb.fractal.julia.InitializationContext ic) throws org.objectweb.fractal.api.factory.InstantiationException  {
    Object olc = ic.getInterface("lifecycle-controller");
    if( ! (olc instanceof org.objectweb.fractal.koch.factory.MPrimitiveImpl) ) {
      while( olc instanceof org.objectweb.fractal.julia.Interceptor ) {
        olc = ((org.objectweb.fractal.julia.Interceptor)olc).getFcItfDelegate();
      }
    }
    _lc = (org.objectweb.fractal.koch.factory.MPrimitiveImpl) olc;
  }

  public Object clone()  {
    ITransDetectToImmEntryInterceptorLC clone = new ITransDetectToImmEntryInterceptorLC(getFcItfDelegate());
    clone._lc = _lc;
    return clone;
  }

  public Object getFcItfDelegate()  {
    return _impl;
  }

  public void setFcItfDelegate(Object obj)  {
    _impl = (comp.cdx.ITransDetectToImmEntry)obj;
  }

  public cdx.RawFrame getFrame()  {
      synchronized(_lc) {
        if(_lc.fcState != 2)
          _lc.incrementFcInvocationCounter();
        else
          _lc.fcInvocationCounter++;
      }
      try {
    cdx.RawFrame ret = _impl.getFrame();
    return ret;
      }
      finally {
        synchronized(_lc) {
          if(_lc.fcState != 2)
            _lc.decrementFcInvocationCounter();
          else
            _lc.fcInvocationCounter--;
        }
      }
  }

  public void incrementFrameNotReadyCount()  {
      synchronized(_lc) {
        if(_lc.fcState != 2)
          _lc.incrementFcInvocationCounter();
        else
          _lc.fcInvocationCounter++;
      }
      try {
    _impl.incrementFrameNotReadyCount();
      }
      finally {
        synchronized(_lc) {
          if(_lc.fcState != 2)
            _lc.decrementFcInvocationCounter();
          else
            _lc.fcInvocationCounter--;
        }
      }
  }

  public void incrementFramesProcessed()  {
      synchronized(_lc) {
        if(_lc.fcState != 2)
          _lc.incrementFcInvocationCounter();
        else
          _lc.fcInvocationCounter++;
      }
      try {
    _impl.incrementFramesProcessed();
      }
      finally {
        synchronized(_lc) {
          if(_lc.fcState != 2)
            _lc.decrementFcInvocationCounter();
          else
            _lc.fcInvocationCounter--;
        }
      }
  }

  public void setNumberOfCollisions(final int arg0)  {
      synchronized(_lc) {
        if(_lc.fcState != 2)
          _lc.incrementFcInvocationCounter();
        else
          _lc.fcInvocationCounter++;
      }
      try {
    _impl.setNumberOfCollisions(arg0);
      }
      finally {
        synchronized(_lc) {
          if(_lc.fcState != 2)
            _lc.decrementFcInvocationCounter();
          else
            _lc.fcInvocationCounter--;
        }
      }
  }

  public void setSuspectedSize(final int arg0)  {
      synchronized(_lc) {
        if(_lc.fcState != 2)
          _lc.incrementFcInvocationCounter();
        else
          _lc.fcInvocationCounter++;
      }
      try {
    _impl.setSuspectedSize(arg0);
      }
      finally {
        synchronized(_lc) {
          if(_lc.fcState != 2)
            _lc.decrementFcInvocationCounter();
          else
            _lc.fcInvocationCounter--;
        }
      }
  }

}