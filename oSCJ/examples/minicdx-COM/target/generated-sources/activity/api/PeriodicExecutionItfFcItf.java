/*
 * Generated by: org.objectweb.fractal.juliac.proxy.InterfaceImplementationClassGenerator
 * on: Tue Nov 23 15:17:22 CET 2010
 */

package activity.api;

import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.NameController;

public class PeriodicExecutionItfFcItf
extends org.objectweb.fractal.julia.BasicComponentInterface
implements activity.api.PeriodicExecutionItf {

  private activity.api.PeriodicExecutionItf impl;
  public PeriodicExecutionItfFcItf()  {
  }

  public PeriodicExecutionItfFcItf(org.objectweb.fractal.api.Component component,String s,org.objectweb.fractal.api.Type type,boolean flag,Object obj)  {
    super(component,s,type,flag,obj);
  }

  public Object getFcItfImpl()  {
    return impl;
  }

  public void setFcItfImpl(Object obj)  {
    impl = (activity.api.PeriodicExecutionItf)obj;
  }

  public void startPeriodicComponent()  {
    if( impl == null )
      throw new NullPointerException("Trying to invoke a method on a client interface, or on a server interface whose complementary interface is not bound.");
    impl.startPeriodicComponent();
  }

  public void stopPeriodicComponent()  {
    if( impl == null )
      throw new NullPointerException("Trying to invoke a method on a client interface, or on a server interface whose complementary interface is not bound.");
    impl.stopPeriodicComponent();
  }

}
