/*
 * Generated by: org.objectweb.fractal.juliac.opt.hulotte.InitializermHulotteCtrlClassGenerator
 * on: Tue Nov 23 15:17:22 CET 2010
 */

package hulotte.generated;


public class GenJavaContent_CrossScopeBindingInterceptor__CollisionDetector_iCdToIe__ImmortalEntry_iCdToIe_b80a7670FCmPrimitiveFCf844113
implements org.objectweb.fractal.juliac.runtime.Factory {

  private Object content;
  public org.objectweb.fractal.api.Type getFcInstanceType()  {
    try {
      return new org.objectweb.fractal.julia.type.BasicComponentType( new org.objectweb.fractal.api.type.InterfaceType[]{new org.objectweb.fractal.julia.type.BasicInterfaceType("IN","comp.cdx.ICollDetectToImmEntry",false,false,false),new org.objectweb.fractal.julia.type.BasicInterfaceType("OUT","comp.cdx.ICollDetectToImmEntry",true,false,false),new org.objectweb.fractal.julia.type.BasicInterfaceType("MemoryAreaSetter","rtsj.memory.api.SetMemoryAreaReferenceItf",false,false,false),} );
    }
    catch( org.objectweb.fractal.api.factory.InstantiationException ie ) {
      throw new RuntimeException(ie);
    }
  }

  public Object getFcControllerDesc()  {
      return "mPrimitive";
  }

  public Object getFcContentDesc()  {
      return "hulotte.generated.GenJavaContent_CrossScopeBindingInterceptor__CollisionDetector_iCdToIe__ImmortalEntry_iCdToIe_b80a7670";
  }

  public Object getFcContent()  {
    return content;
  }

  public Object newFcContent()  {
    Object content = new hulotte.generated.GenJavaContent_CrossScopeBindingInterceptor__CollisionDetector_iCdToIe__ImmortalEntry_iCdToIe_b80a7670();
    return content;
  }

  public org.objectweb.fractal.api.Component newFcInstance() throws org.objectweb.fractal.api.factory.InstantiationException  {
    Object content = newFcContent();
    return newFcInstance(content);
  }

  public org.objectweb.fractal.api.Component newFcInstance(Object content) throws org.objectweb.fractal.api.factory.InstantiationException  {
    org.objectweb.fractal.koch.factory.MPrimitiveImpl mcomp = new org.objectweb.fractal.koch.factory.MPrimitiveImpl(getFcInstanceType(),content);
    java.util.Map fcInterfaces = new java.util.HashMap();
    org.objectweb.fractal.api.Interface proxy = null;
    proxy = new org.objectweb.fractal.api.ComponentFcItf(mcomp,"component",new org.objectweb.fractal.julia.type.BasicInterfaceType("component","org.objectweb.fractal.api.Component",false,false,false),false,mcomp);
    fcInterfaces.put("component",proxy);
    proxy = new org.objectweb.fractal.api.control.BindingControllerFcItf(mcomp,"binding-controller",new org.objectweb.fractal.julia.type.BasicInterfaceType("binding-controller","org.objectweb.fractal.api.control.BindingController",false,false,false),false,mcomp);
    fcInterfaces.put("binding-controller",proxy);
    proxy = new org.objectweb.fractal.api.control.NameControllerFcItf(mcomp,"name-controller",new org.objectweb.fractal.julia.type.BasicInterfaceType("name-controller","org.objectweb.fractal.api.control.NameController",false,false,false),false,mcomp);
    fcInterfaces.put("name-controller",proxy);
    proxy = new org.objectweb.fractal.julia.control.content.SuperControllerNotifierFcItf(mcomp,"super-controller",new org.objectweb.fractal.julia.type.BasicInterfaceType("super-controller","org.objectweb.fractal.julia.control.content.SuperControllerNotifier",false,false,false),false,mcomp);
    fcInterfaces.put("super-controller",proxy);
    proxy = new org.objectweb.fractal.julia.control.lifecycle.LifeCycleCoordinatorFcItf(mcomp,"lifecycle-controller",new org.objectweb.fractal.julia.type.BasicInterfaceType("lifecycle-controller","org.objectweb.fractal.julia.control.lifecycle.LifeCycleCoordinator",false,false,false),false,mcomp);
    fcInterfaces.put("lifecycle-controller",proxy);
    org.objectweb.fractal.julia.InitializationContext ic = new org.objectweb.fractal.julia.InitializationContext();
    ic.interfaces = new java.util.HashMap();
    ic.interfaces.put("component",mcomp);
    ic.interfaces.put("lifecycle-controller",mcomp);
    proxy = new comp.cdx.ICollDetectToImmEntryFcItf(mcomp,"IN",new org.objectweb.fractal.julia.type.BasicInterfaceType("IN","comp.cdx.ICollDetectToImmEntry",false,false,false),false,content);
    fcInterfaces.put("IN",proxy);
    org.objectweb.fractal.julia.Interceptor intercept = new comp.cdx.ICollDetectToImmEntryInterceptorLC();
    intercept.initFcController(ic);
    ((org.objectweb.fractal.julia.ComponentInterface)proxy).setFcItfImpl(intercept);
    intercept.setFcItfDelegate(content);
    proxy = new comp.cdx.ICollDetectToImmEntryFcItf(mcomp,"OUT",new org.objectweb.fractal.julia.type.BasicInterfaceType("OUT","comp.cdx.ICollDetectToImmEntry",true,false,false),false,null);
    fcInterfaces.put("OUT",proxy);
    proxy = new rtsj.memory.api.SetMemoryAreaReferenceItfFcItf(mcomp,"MemoryAreaSetter",new org.objectweb.fractal.julia.type.BasicInterfaceType("MemoryAreaSetter","rtsj.memory.api.SetMemoryAreaReferenceItf",false,false,false),false,content);
    fcInterfaces.put("MemoryAreaSetter",proxy);
    intercept = new rtsj.memory.api.SetMemoryAreaReferenceItfInterceptorLC();
    intercept.initFcController(ic);
    ((org.objectweb.fractal.julia.ComponentInterface)proxy).setFcItfImpl(intercept);
    intercept.setFcItfDelegate(content);
    mcomp.setFcInterfaces(fcInterfaces);
    return mcomp;
  }

}
