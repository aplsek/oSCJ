/*
 * Generated by: org.objectweb.fractal.juliac.proxy.InterceptorClassGenerator
 * on: Tue Nov 23 15:17:21 CET 2010
 */

package juliac.generated.java.lang;


public class RunnableInterceptorLC
implements java.lang.Runnable,org.objectweb.fractal.julia.Interceptor {

  private java.lang.Runnable _impl;
  private org.objectweb.fractal.koch.factory.MPrimitiveImpl _lc;
  public RunnableInterceptorLC()  {
  }

  public RunnableInterceptorLC(Object obj)  {
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
    RunnableInterceptorLC clone = new RunnableInterceptorLC(getFcItfDelegate());
    clone._lc = _lc;
    return clone;
  }

  public Object getFcItfDelegate()  {
    return _impl;
  }

  public void setFcItfDelegate(Object obj)  {
    _impl = (java.lang.Runnable)obj;
  }

  public void run()  {
      synchronized(_lc) {
        if(_lc.fcState != 2)
          _lc.incrementFcInvocationCounter();
        else
          _lc.fcInvocationCounter++;
      }
      try {
    _impl.run();
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