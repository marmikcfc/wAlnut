package model.MARK_II.ConnectTypes;

import model.MARK_II.Region;

/**
 * @author Quinn Liu (quinnliu@vt.edu)
 * @version MARK II | June 7, 2013
 */
public interface RegionToRegionConnect {
    public abstract void connect(Region childRegion, Region parentRegion,
	    int numberOfColumnsToOverlapAlongXAxisOfRegion,
	    int numberOfColumnsToOverlapAlongYAxisOfRegion);
}
