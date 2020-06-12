package com.privateinternetaccess.regions

import com.privateinternetaccess.common.regions.RegionsAPI
import com.privateinternetaccess.regions.internals.Regions

/**
 * Builder class responsible for creating an instance of an object conforming to
 * the `RegionsAPI` interface.
 */
public class RegionsBuilder {

    /**
     * @return `RegionsAPI` instance.
     */
    fun build(): RegionsAPI {
        return Regions()
    }
}