/*
 * Generated by: org.objectweb.fractal.juliac.proxy.InterceptorClassGenerator
 * on: Tue Nov 23 15:17:21 CET 2010
 */

package comp.cdx;


public class TDAttributesInterceptor
implements org.objectweb.fractal.julia.Interceptor {

  private comp.cdx.TDAttributes _impl;
  public TDAttributesInterceptor()  {
  }

  public TDAttributesInterceptor(Object obj)  {
    setFcItfDelegate(obj);
  }

  public void initFcController(org.objectweb.fractal.julia.InitializationContext ic) throws org.objectweb.fractal.api.factory.InstantiationException  {
  }

  public Object clone()  {
    TDAttributesInterceptor clone = new TDAttributesInterceptor(getFcItfDelegate());
    return clone;
  }

  public Object getFcItfDelegate()  {
    return _impl;
  }

  public void setFcItfDelegate(Object obj)  {
    _impl = (comp.cdx.TDAttributes)obj;
  }

  public float getVoxelSize()  {
    float ret = _impl.getVoxelSize();
    return ret;
  }

  public void setVoxelSize(final float arg0)  {
    _impl.setVoxelSize(arg0);
  }

}