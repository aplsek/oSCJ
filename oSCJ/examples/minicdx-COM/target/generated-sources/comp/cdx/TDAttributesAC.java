/*
 * Generated by: org.objectweb.fractal.juliac.proxy.AttributeControllerClassGenerator
 * on: Tue Nov 23 15:17:21 CET 2010
 */

package comp.cdx;


public class TDAttributesAC
implements comp.cdx.TDAttributes,org.objectweb.fractal.julia.control.attribute.CloneableAttributeController {

  private float VoxelSize;
  private boolean _VoxelSize;
  public TDAttributesAC()  {
  }

  public void cloneFcAttributes(org.objectweb.fractal.api.control.AttributeController attributecontroller)  {
    if(_VoxelSize)
      ((comp.cdx.TDAttributes)attributecontroller).setVoxelSize(VoxelSize);
  }

  public float getVoxelSize()  {
    return VoxelSize;
  }

  public void setVoxelSize(final float arg0)  {
    VoxelSize = arg0;
    _VoxelSize = true;
  }

}
