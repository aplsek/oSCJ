/*
 * Generated by: org.objectweb.fractal.juliac.proxy.InterfaceImplementationClassGenerator
 * on: Tue Nov 23 15:17:21 CET 2010
 */

package juliac.generated.java.lang;

import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.NameController;

public class RunnableFcItf
extends org.objectweb.fractal.julia.BasicComponentInterface
implements java.lang.Runnable {

  private java.lang.Runnable impl;
  public RunnableFcItf()  {
  }

  public RunnableFcItf(org.objectweb.fractal.api.Component component,String s,org.objectweb.fractal.api.Type type,boolean flag,Object obj)  {
    super(component,s,type,flag,obj);
  }

  public Object getFcItfImpl()  {
    return impl;
  }

  public void setFcItfImpl(Object obj)  {
    impl = (java.lang.Runnable)obj;
  }

  public void run()  {
    if( impl == null )
      throw new NullPointerException("Trying to invoke a method on a client interface, or on a server interface whose complementary interface is not bound.");
//try { System.out.println("run, owner: " + ((NameController)((Component)getFcItfOwner()).getFcInterface("name-controller")).getFcName() + ", internal: " + isFcInternalItf()); } catch (NoSuchInterfaceException e) { e.printStackTrace(); }
    impl.run();
  }

}