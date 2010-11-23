/*
 * Generated by: org.objectweb.fractal.juliac.proxy.InterceptorClassGenerator
 * on: Tue Nov 23 15:17:21 CET 2010
 */

package cdx;


public class IStateTableInterceptorLC
implements cdx.IStateTable,org.objectweb.fractal.julia.Interceptor {

  private cdx.IStateTable _impl;
  private org.objectweb.fractal.koch.factory.MPrimitiveImpl _lc;
  public IStateTableInterceptorLC()  {
  }

  public IStateTableInterceptorLC(Object obj)  {
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
    IStateTableInterceptorLC clone = new IStateTableInterceptorLC(getFcItfDelegate());
    clone._lc = _lc;
    return clone;
  }

  public Object getFcItfDelegate()  {
    return _impl;
  }

  public void setFcItfDelegate(Object obj)  {
    _impl = (cdx.IStateTable)obj;
  }

  public statetable.Vector3d get(final cdx.CallSign arg0)  {
      synchronized(_lc) {
        if(_lc.fcState != 2)
          _lc.incrementFcInvocationCounter();
        else
          _lc.fcInvocationCounter++;
      }
      try {
    statetable.Vector3d ret = _impl.get(arg0);
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

  public void put(final cdx.CallSign arg0,final float arg1,final float arg2,final float arg3)  {
      synchronized(_lc) {
        if(_lc.fcState != 2)
          _lc.incrementFcInvocationCounter();
        else
          _lc.fcInvocationCounter++;
      }
      try {
    _impl.put(arg0,arg1,arg2,arg3);
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
