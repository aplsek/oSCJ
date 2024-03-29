/*
 * Generated by org.objectweb.hulotte.extensions.javaRTSJ.generators.HulotteImmortalInstanciationClassGenerator on: 
 */
import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.factory.Factory;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.NameController;
import javax.realtime.LTMemory;
import javax.realtime.ImmortalMemory;

public class ImmortalInstanciationWrapperClass
implements java.lang.Runnable {

  private Object C;
  private Class<?> classToInstanciate;
  private String compName;
  public Object getComponentRef()  {
    return C;
  }

  public void setClass(Class<?> c)  {
    this.classToInstanciate = c;
  }

  public void setCompName(String compName)  {
    this.compName = compName;
  }

  final LTMemory scopedA = new LTMemory(8 * 1024);
  private Runnable runTh = new Runnable() {
  public void run() {
    System.out.println("Mem Consumed " + scopedA.memoryConsumed());
    System.out.println("immortal memory consumed "+ ImmortalMemory.instance().memoryConsumed());
  }
}
;
  private void memConsumed()  {
scopedA.enter(runTh);
  }

  public void run()  {
    try {
        //memConsumed();
        C = classToInstanciate.newInstance();
        if (C instanceof Factory) {
          C = ((Factory)C).newFcInstance();
        }
        try {
        	((NameController)((Component)C).getFcInterface("name-controller")).setFcName(compName);
        } catch (NoSuchInterfaceException e) {}
        //memConsumed();
    } catch (org.objectweb.fractal.api.factory.InstantiationException e) { e.printStackTrace();
    } catch (java.lang.InstantiationException e) { e.printStackTrace();
    } catch (IllegalAccessException e) { e.printStackTrace(); }
  }

}
