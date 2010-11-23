package comp.cdx;

import org.objectweb.fractal.api.control.AttributeController;

public interface TDAttributes extends AttributeController {
	float getVoxelSize();
	void setVoxelSize(float voxelSize);
}
