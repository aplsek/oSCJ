/*
 * Generated by: org.objectweb.fractal.juliac.proxy.AttributeControllerClassGenerator
 * on: Tue Nov 23 15:17:22 CET 2010
 */

package activity.api;


public class PeriodicControllerAttributesAC
implements activity.api.PeriodicControllerAttributes,org.objectweb.fractal.julia.control.attribute.CloneableAttributeController {

  private int Priority;
  private boolean _Priority;
  private int Period;
  private boolean _Period;
  private int Maxiteration;
  private boolean _Maxiteration;
  public PeriodicControllerAttributesAC()  {
  }

  public void cloneFcAttributes(org.objectweb.fractal.api.control.AttributeController attributecontroller)  {
    if(_Priority)
      ((activity.api.PeriodicControllerAttributes)attributecontroller).setPriority(Priority);
    if(_Period)
      ((activity.api.PeriodicControllerAttributes)attributecontroller).setPeriod(Period);
    if(_Maxiteration)
      ((activity.api.PeriodicControllerAttributes)attributecontroller).setMaxiteration(Maxiteration);
  }

  public void setPriority(final int arg0)  {
    Priority = arg0;
    _Priority = true;
  }

  public int getPriority()  {
    return Priority;
  }

  public void setPeriod(final int arg0)  {
    Period = arg0;
    _Period = true;
  }

  public void setMaxiteration(final int arg0)  {
    Maxiteration = arg0;
    _Maxiteration = true;
  }

  public int getPeriod()  {
    return Period;
  }

  public int getMaxiteration()  {
    return Maxiteration;
  }

}