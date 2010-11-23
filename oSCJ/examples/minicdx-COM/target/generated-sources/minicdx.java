/*
 * Generated by org.objectweb.hulotte.backend.java.juliac.HulotteJuliacBackend$FactoryClassGenerationWrapper on: 
 */
import javax.realtime.ImmortalMemory;
import javax.realtime.RealtimeThread;
import javax.realtime.MemoryArea;
import javax.realtime.LTMemory;
import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.util.Fractal;
import org.objectweb.fractal.api.type.InterfaceType;
import org.objectweb.fractal.api.type.ComponentType;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.julia.ComponentInterface;
import org.objectweb.fractal.julia.Interceptor;
import org.objectweb.fractal.api.control.BindingController;
import org.objectweb.fractal.soleil.control.interceptor.ComponentInterceptorController;
import org.objectweb.fractal.api.control.ContentController;

public class minicdx
implements org.objectweb.fractal.api.factory.Factory {

  static MemoryArea TransientScope;
  static MemoryArea PersistentScope;

static class R
implements Runnable {

  public void run()  {
TransientScope = new LTMemory(4363000);
PersistentScope = new LTMemory(9460000);
  }

}
  public Object getFcContentDesc()  {
    throw new java.lang.UnsupportedOperationException();
  }

  public Object getFcControllerDesc()  {
    throw new java.lang.UnsupportedOperationException();
  }

  public Component newFcInstance() throws org.objectweb.fractal.api.factory.InstantiationException  {
      // Scope memory area instanciation.
ImmortalMemory.instance().executeInArea(new R());
      // Wrappers used for immortal instanciation and start:
      ImmortalInstanciationWrapperClass immortalInstanciation = new ImmortalInstanciationWrapperClass();
      HulotteImmortalStartFcWrapper immortalStart = new HulotteImmortalStartFcWrapper();
      if (!(Thread.currentThread() instanceof RealtimeThread)) {
          throw new RuntimeException("The launcher's thread should be a RealtimeThread.");
      }
    try {
      org.objectweb.fractal.api.Component root = org.objectweb.fractal.util.Fractal.getBootstrapComponent();
      org.objectweb.fractal.api.type.TypeFactory typeFactory = org.objectweb.fractal.util.Fractal.getTypeFactory(root);
      org.objectweb.fractal.api.factory.GenericFactory genericFactory = org.objectweb.fractal.util.Fractal.getGenericFactory(root);
      // --------------------------------------------------
      // Let's dance, HULOTTE !!!! 
      // --------------------------------------------------
Component C0 = null;
// Immortal instancitation for the component TransientDetector
immortalInstanciation.setClass((Class<org.objectweb.fractal.api.factory.Factory>)this.getClass().getClassLoader().loadClass("comp.cdx.TransientDetectorFCmPrimitiveFCac9f6db6"));
immortalInstanciation.setCompName("TransientDetector");
ImmortalMemory.instance().executeInArea(immortalInstanciation);
C0 = (Component)immortalInstanciation.getComponentRef();
((comp.cdx.TDAttributes)Fractal.getAttributeController(C0)).setVoxelSize(10.0f);
Component C1 = null;
// Immortal instancitation for the component StateTable
immortalInstanciation.setClass((Class<org.objectweb.fractal.api.factory.Factory>)this.getClass().getClassLoader().loadClass("comp.cdx.StateTableFCmPrimitiveFC4e325850"));
immortalInstanciation.setCompName("StateTable");
ImmortalMemory.instance().executeInArea(immortalInstanciation);
C1 = (Component)immortalInstanciation.getComponentRef();

// Start in immortal, component: StateTable
immortalStart.setLCC(Fractal.getLifeCycleController(C1));
ImmortalMemory.instance().executeInArea(immortalStart);
immortalStart.setLCC(null);
Component C2 = null;
// Immortal instancitation for the component ImmortalEntry
immortalInstanciation.setClass((Class<org.objectweb.fractal.api.factory.Factory>)this.getClass().getClassLoader().loadClass("comp.cdx.ImmortalEntryFCmPrimitiveFC5a3e773d"));
immortalInstanciation.setCompName("ImmortalEntry");
ImmortalMemory.instance().executeInArea(immortalInstanciation);
C2 = (Component)immortalInstanciation.getComponentRef();

// Start in immortal, component: ImmortalEntry
immortalStart.setLCC(Fractal.getLifeCycleController(C2));
ImmortalMemory.instance().executeInArea(immortalStart);
immortalStart.setLCC(null);
comp.cdx.Main C3 = null;
C3 = new comp.cdx.Main();
Component C4 = null;
// Immortal instancitation for the component CrossScopeBindingInterceptor__CollisionDetector_iTransientDetector__TransientDetector_iTransientDetector
immortalInstanciation.setClass((Class<org.objectweb.fractal.api.factory.Factory>)this.getClass().getClassLoader().loadClass("hulotte.generated.GenJavaContent_CrossScopeBindingInterceptor__CollisionDetector_iTransientDetector__TransientDetector_iTransientDetector_1529a16dFCmPrimitiveFCd4695643"));
immortalInstanciation.setCompName("CrossScopeBindingInterceptor__CollisionDetector_iTransientDetector__TransientDetector_iTransientDetector");
ImmortalMemory.instance().executeInArea(immortalInstanciation);
C4 = (Component)immortalInstanciation.getComponentRef();
Component C5 = null;
// Immortal instancitation for the component hulotte.connector.Connector__CollisionDetector_iTransientDetector__TransientDetector_iTransientDetector
immortalInstanciation.setClass((Class<org.objectweb.fractal.api.factory.Factory>)this.getClass().getClassLoader().loadClass("juliac.generated.mCompositeFCd4695643"));
immortalInstanciation.setCompName("hulotte.connector.Connector__CollisionDetector_iTransientDetector__TransientDetector_iTransientDetector");
ImmortalMemory.instance().executeInArea(immortalInstanciation);
C5 = (Component)immortalInstanciation.getComponentRef();
if (C4 != null) {Fractal.getContentController(C5).addFcSubComponent(C4);}
Fractal.getBindingController(C5).bindFc("IN",C4.getFcInterface("IN"));
Fractal.getBindingController(C4).bindFc("OUT",Fractal.getContentController(C5).getFcInternalInterface("OUT"));
Fractal.getBindingController(C5).bindFc("MemoryAreaSetter",C4.getFcInterface("MemoryAreaSetter"));
Component C6 = null;
// Immortal instancitation for the component CrossScopeBindingInterceptor__CollisionDetector_iCdToIe__ImmortalEntry_iCdToIe
immortalInstanciation.setClass((Class<org.objectweb.fractal.api.factory.Factory>)this.getClass().getClassLoader().loadClass("hulotte.generated.GenJavaContent_CrossScopeBindingInterceptor__CollisionDetector_iCdToIe__ImmortalEntry_iCdToIe_b80a7670FCmPrimitiveFCf844113"));
immortalInstanciation.setCompName("CrossScopeBindingInterceptor__CollisionDetector_iCdToIe__ImmortalEntry_iCdToIe");
ImmortalMemory.instance().executeInArea(immortalInstanciation);
C6 = (Component)immortalInstanciation.getComponentRef();
Component C7 = null;
// Immortal instancitation for the component hulotte.connector.Connector__CollisionDetector_iCdToIe__ImmortalEntry_iCdToIe
immortalInstanciation.setClass((Class<org.objectweb.fractal.api.factory.Factory>)this.getClass().getClassLoader().loadClass("juliac.generated.mCompositeFCf844113"));
immortalInstanciation.setCompName("hulotte.connector.Connector__CollisionDetector_iCdToIe__ImmortalEntry_iCdToIe");
ImmortalMemory.instance().executeInArea(immortalInstanciation);
C7 = (Component)immortalInstanciation.getComponentRef();
if (C6 != null) {Fractal.getContentController(C7).addFcSubComponent(C6);}
Fractal.getBindingController(C7).bindFc("IN",C6.getFcInterface("IN"));
Fractal.getBindingController(C6).bindFc("OUT",Fractal.getContentController(C7).getFcInternalInterface("OUT"));
Fractal.getBindingController(C7).bindFc("MemoryAreaSetter",C6.getFcInterface("MemoryAreaSetter"));
Component C8 = null;
// Immortal instancitation for the component CrossScopeBindingInterceptor__TransientDetector_iStateTable__StateTable_iStateTable
immortalInstanciation.setClass((Class<org.objectweb.fractal.api.factory.Factory>)this.getClass().getClassLoader().loadClass("hulotte.generated.GenJavaContent_CrossScopeBindingInterceptor__TransientDetector_iStateTable__StateTable_iStateTable_60b2947cFCmPrimitiveFCbd34615d"));
immortalInstanciation.setCompName("CrossScopeBindingInterceptor__TransientDetector_iStateTable__StateTable_iStateTable");
ImmortalMemory.instance().executeInArea(immortalInstanciation);
C8 = (Component)immortalInstanciation.getComponentRef();
Component C9 = null;
// Immortal instancitation for the component hulotte.connector.Connector__TransientDetector_iStateTable__StateTable_iStateTable
immortalInstanciation.setClass((Class<org.objectweb.fractal.api.factory.Factory>)this.getClass().getClassLoader().loadClass("juliac.generated.mCompositeFCbd34615d"));
immortalInstanciation.setCompName("hulotte.connector.Connector__TransientDetector_iStateTable__StateTable_iStateTable");
ImmortalMemory.instance().executeInArea(immortalInstanciation);
C9 = (Component)immortalInstanciation.getComponentRef();
if (C8 != null) {Fractal.getContentController(C9).addFcSubComponent(C8);}
Fractal.getBindingController(C9).bindFc("IN",C8.getFcInterface("IN"));
Fractal.getBindingController(C8).bindFc("OUT",Fractal.getContentController(C9).getFcInternalInterface("OUT"));
Fractal.getBindingController(C9).bindFc("MemoryAreaSetter",C8.getFcInterface("MemoryAreaSetter"));
Component C10 = null;
// Immortal instancitation for the component CrossScopeBindingInterceptor__TransientDetector_iTdToIe__ImmortalEntry_iTdToIe
immortalInstanciation.setClass((Class<org.objectweb.fractal.api.factory.Factory>)this.getClass().getClassLoader().loadClass("hulotte.generated.GenJavaContent_CrossScopeBindingInterceptor__TransientDetector_iTdToIe__ImmortalEntry_iTdToIe_11fab1b8FCmPrimitiveFC2a0aebbd"));
immortalInstanciation.setCompName("CrossScopeBindingInterceptor__TransientDetector_iTdToIe__ImmortalEntry_iTdToIe");
ImmortalMemory.instance().executeInArea(immortalInstanciation);
C10 = (Component)immortalInstanciation.getComponentRef();
Component C11 = null;
// Immortal instancitation for the component hulotte.connector.Connector__TransientDetector_iTdToIe__ImmortalEntry_iTdToIe
immortalInstanciation.setClass((Class<org.objectweb.fractal.api.factory.Factory>)this.getClass().getClassLoader().loadClass("juliac.generated.mCompositeFC2a0aebbd"));
immortalInstanciation.setCompName("hulotte.connector.Connector__TransientDetector_iTdToIe__ImmortalEntry_iTdToIe");
ImmortalMemory.instance().executeInArea(immortalInstanciation);
C11 = (Component)immortalInstanciation.getComponentRef();
if (C10 != null) {Fractal.getContentController(C11).addFcSubComponent(C10);}
Fractal.getBindingController(C11).bindFc("IN",C10.getFcInterface("IN"));
Fractal.getBindingController(C10).bindFc("OUT",Fractal.getContentController(C11).getFcInternalInterface("OUT"));
Fractal.getBindingController(C11).bindFc("MemoryAreaSetter",C10.getFcInterface("MemoryAreaSetter"));
Component C12 = null;
// Immortal instancitation for the component FunctionalPrimitive_CollisionDetector_contentComp
immortalInstanciation.setClass((Class<org.objectweb.fractal.api.factory.Factory>)this.getClass().getClassLoader().loadClass("comp.cdx.CollisionDetectorFCmPrimitiveFCb6ada7d0"));
immortalInstanciation.setCompName("FunctionalPrimitive_CollisionDetector_contentComp");
ImmortalMemory.instance().executeInArea(immortalInstanciation);
C12 = (Component)immortalInstanciation.getComponentRef();
Component C13 = null;
// Immortal instancitation for the component rtsj.activity.lib.NHRTPeriodicController
immortalInstanciation.setClass((Class<org.objectweb.fractal.api.factory.Factory>)this.getClass().getClassLoader().loadClass("rtsj.activity.lib.NHRTPeriodicControllerImplFCmPrimitiveFC33debfea"));
immortalInstanciation.setCompName("rtsj.activity.lib.NHRTPeriodicController");
ImmortalMemory.instance().executeInArea(immortalInstanciation);
C13 = (Component)immortalInstanciation.getComponentRef();
((activity.api.PeriodicControllerAttributes)Fractal.getAttributeController(C13)).setPeriod(50);
((activity.api.PeriodicControllerAttributes)Fractal.getAttributeController(C13)).setPriority(11);
((activity.api.PeriodicControllerAttributes)Fractal.getAttributeController(C13)).setMaxiteration(100);
Component C14 = null;
// Immortal instancitation for the component CollisionDetector_CBMembraneMergedWithContent
immortalInstanciation.setClass((Class<org.objectweb.fractal.api.factory.Factory>)this.getClass().getClassLoader().loadClass("juliac.generated.mCompositeFCabd40fc7"));
immortalInstanciation.setCompName("CollisionDetector_CBMembraneMergedWithContent");
ImmortalMemory.instance().executeInArea(immortalInstanciation);
C14 = (Component)immortalInstanciation.getComponentRef();
if (C12 != null) {Fractal.getContentController(C14).addFcSubComponent(C12);}
if (C13 != null) {Fractal.getContentController(C14).addFcSubComponent(C13);}
Fractal.getBindingController(C12).bindFc("iCdToIe",Fractal.getContentController(C14).getFcInternalInterface("iCdToIe"));
Fractal.getBindingController(C14).bindFc("entryPoint",C12.getFcInterface("entryPoint"));
Fractal.getBindingController(C12).bindFc("iTransientDetector",Fractal.getContentController(C14).getFcInternalInterface("iTransientDetector"));
Fractal.getBindingController(C14).bindFc("//MemoryAreaSetter",C13.getFcInterface("MemoryAreaSetter"));
Fractal.getBindingController(C14).bindFc("//PeriodicExecution",C13.getFcInterface("PeriodicExecution"));
Fractal.getBindingController(C13).bindFc("entryPoint",C12.getFcInterface("entryPoint"));
Component C15 = null;
C15 = new juliac.generated.mCompositeFCf938cfee().newFcInstance();
Fractal.getNameController(C15).setFcName("minicdx");

if (C0 != null) {Fractal.getContentController(C15).addFcSubComponent(C0);}
if (C1 != null) {Fractal.getContentController(C15).addFcSubComponent(C1);}
if (C2 != null) {Fractal.getContentController(C15).addFcSubComponent(C2);}
if (C5 != null) {Fractal.getContentController(C15).addFcSubComponent(C5);}
if (C7 != null) {Fractal.getContentController(C15).addFcSubComponent(C7);}
if (C9 != null) {Fractal.getContentController(C15).addFcSubComponent(C9);}
if (C11 != null) {Fractal.getContentController(C15).addFcSubComponent(C11);}
if (C14 != null) {Fractal.getContentController(C15).addFcSubComponent(C14);}
Fractal.getBindingController(C15).bindFc("r",(java.lang.Runnable)C3);
Fractal.getBindingController(C5).bindFc("OUT",C0.getFcInterface("iTransientDetector"));
Fractal.getBindingController(C7).bindFc("OUT",C2.getFcInterface("iCdToIe"));
Fractal.getBindingController(C9).bindFc("OUT",C1.getFcInterface("iStateTable"));
Fractal.getBindingController(C0).bindFc("iStateTable",C9.getFcInterface("IN"));
Fractal.getBindingController(C11).bindFc("OUT",C2.getFcInterface("iTdToIe"));
Fractal.getBindingController(C0).bindFc("iTdToIe",C11.getFcInterface("IN"));
Fractal.getBindingController(C14).bindFc("iTransientDetector",C5.getFcInterface("IN"));
Fractal.getBindingController(C14).bindFc("iCdToIe",C7.getFcInterface("IN"));

// Start in immortal, component: TransientDetector
immortalStart.setLCC(Fractal.getLifeCycleController(C0));
ImmortalMemory.instance().executeInArea(immortalStart);
immortalStart.setLCC(null);

// Start in immortal, component: CrossScopeBindingInterceptor__CollisionDetector_iTransientDetector__TransientDetector_iTransientDetector
immortalStart.setLCC(Fractal.getLifeCycleController(C4));
ImmortalMemory.instance().executeInArea(immortalStart);
immortalStart.setLCC(null);

// Set the reference to the scope memory area for the component CrossScopeBindingInterceptor__CollisionDetector_iTransientDetector__TransientDetector_iTransientDetector
((rtsj.memory.api.SetMemoryAreaReferenceItf)C4.getFcInterface("MemoryAreaSetter")).setMemoryAreaReference(TransientScope);


// Start in immortal, component: hulotte.connector.Connector__CollisionDetector_iTransientDetector__TransientDetector_iTransientDetector
immortalStart.setLCC(Fractal.getLifeCycleController(C5));
ImmortalMemory.instance().executeInArea(immortalStart);
immortalStart.setLCC(null);

// Set the reference to the scope memory area for the component hulotte.connector.Connector__CollisionDetector_iTransientDetector__TransientDetector_iTransientDetector
((rtsj.memory.api.SetMemoryAreaReferenceItf)C5.getFcInterface("MemoryAreaSetter")).setMemoryAreaReference(TransientScope);


// Start in immortal, component: CrossScopeBindingInterceptor__CollisionDetector_iCdToIe__ImmortalEntry_iCdToIe
immortalStart.setLCC(Fractal.getLifeCycleController(C6));
ImmortalMemory.instance().executeInArea(immortalStart);
immortalStart.setLCC(null);

// Set the reference to the immortal memory area for the component CrossScopeBindingInterceptor__CollisionDetector_iCdToIe__ImmortalEntry_iCdToIe
((rtsj.memory.api.SetMemoryAreaReferenceItf)C6.getFcInterface("MemoryAreaSetter")).setMemoryAreaReference(ImmortalMemory.instance());


// Start in immortal, component: hulotte.connector.Connector__CollisionDetector_iCdToIe__ImmortalEntry_iCdToIe
immortalStart.setLCC(Fractal.getLifeCycleController(C7));
ImmortalMemory.instance().executeInArea(immortalStart);
immortalStart.setLCC(null);

// Set the reference to the immortal memory area for the component hulotte.connector.Connector__CollisionDetector_iCdToIe__ImmortalEntry_iCdToIe
((rtsj.memory.api.SetMemoryAreaReferenceItf)C7.getFcInterface("MemoryAreaSetter")).setMemoryAreaReference(ImmortalMemory.instance());


// Start in immortal, component: CrossScopeBindingInterceptor__TransientDetector_iStateTable__StateTable_iStateTable
immortalStart.setLCC(Fractal.getLifeCycleController(C8));
ImmortalMemory.instance().executeInArea(immortalStart);
immortalStart.setLCC(null);

// Set the reference to the scope memory area for the component CrossScopeBindingInterceptor__TransientDetector_iStateTable__StateTable_iStateTable
((rtsj.memory.api.SetMemoryAreaReferenceItf)C8.getFcInterface("MemoryAreaSetter")).setMemoryAreaReference(PersistentScope);


// Start in immortal, component: hulotte.connector.Connector__TransientDetector_iStateTable__StateTable_iStateTable
immortalStart.setLCC(Fractal.getLifeCycleController(C9));
ImmortalMemory.instance().executeInArea(immortalStart);
immortalStart.setLCC(null);

// Set the reference to the scope memory area for the component hulotte.connector.Connector__TransientDetector_iStateTable__StateTable_iStateTable
((rtsj.memory.api.SetMemoryAreaReferenceItf)C9.getFcInterface("MemoryAreaSetter")).setMemoryAreaReference(PersistentScope);


// Start in immortal, component: CrossScopeBindingInterceptor__TransientDetector_iTdToIe__ImmortalEntry_iTdToIe
immortalStart.setLCC(Fractal.getLifeCycleController(C10));
ImmortalMemory.instance().executeInArea(immortalStart);
immortalStart.setLCC(null);

// Set the reference to the immortal memory area for the component CrossScopeBindingInterceptor__TransientDetector_iTdToIe__ImmortalEntry_iTdToIe
((rtsj.memory.api.SetMemoryAreaReferenceItf)C10.getFcInterface("MemoryAreaSetter")).setMemoryAreaReference(ImmortalMemory.instance());


// Start in immortal, component: hulotte.connector.Connector__TransientDetector_iTdToIe__ImmortalEntry_iTdToIe
immortalStart.setLCC(Fractal.getLifeCycleController(C11));
ImmortalMemory.instance().executeInArea(immortalStart);
immortalStart.setLCC(null);

// Set the reference to the immortal memory area for the component hulotte.connector.Connector__TransientDetector_iTdToIe__ImmortalEntry_iTdToIe
((rtsj.memory.api.SetMemoryAreaReferenceItf)C11.getFcInterface("MemoryAreaSetter")).setMemoryAreaReference(ImmortalMemory.instance());


// Start in immortal, component: FunctionalPrimitive_CollisionDetector_contentComp
immortalStart.setLCC(Fractal.getLifeCycleController(C12));
ImmortalMemory.instance().executeInArea(immortalStart);
immortalStart.setLCC(null);

// Start in immortal, component: rtsj.activity.lib.NHRTPeriodicController
immortalStart.setLCC(Fractal.getLifeCycleController(C13));
ImmortalMemory.instance().executeInArea(immortalStart);
immortalStart.setLCC(null);

// Start in immortal, component: CollisionDetector_CBMembraneMergedWithContent
immortalStart.setLCC(Fractal.getLifeCycleController(C14));
ImmortalMemory.instance().executeInArea(immortalStart);
immortalStart.setLCC(null);

// Set the reference to the scope memory area for the component CollisionDetector_CBMembraneMergedWithContent
((rtsj.memory.api.SetMemoryAreaReferenceItf)C14.getFcInterface("//MemoryAreaSetter")).setMemoryAreaReference(PersistentScope);


// Automatically start periodic component: CollisionDetector_CBMembraneMergedWithContent
immortalStart.setPE((activity.api.PeriodicExecutionItf)C14.getFcInterface("//PeriodicExecution"));
ImmortalMemory.instance().executeInArea(immortalStart);
immortalStart.setPE(null);


Fractal.getLifeCycleController(C15).startFc();
      // --------------------------------------------------
      return C15;
    }
    catch( Exception e ) {
      throw new org.objectweb.fractal.julia.factory.ChainedInstantiationException(e,null,e.getClass().getName()+": "+e.getMessage());
    }
  }

  public org.objectweb.fractal.api.Type getFcInstanceType()  {
    try {
      // -------------------------------------------------
      org.objectweb.fractal.api.type.ComponentType ct = 
      new org.objectweb.fractal.julia.type.BasicComponentType( new org.objectweb.fractal.api.type.InterfaceType[]{new org.objectweb.fractal.julia.type.BasicInterfaceType("r","java.lang.Runnable",false,false,false),} );
      // -------------------------------------------------
      return ct;
    }
    catch( org.objectweb.fractal.api.factory.InstantiationException e ) {
      throw new RuntimeException(e);
    }
  }

}
