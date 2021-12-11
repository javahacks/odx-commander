package com.javahacks.odx.index;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Interface used to indicate that the location of implementing elements should be kept for later access.
 */
public interface LocationAware {

    XmlElementLocation getLocation();

    void setLocation(XmlElementLocation location);

    @XmlAccessorType(XmlAccessType.FIELD)
    class AbstractLocationAware implements LocationAware {
        @XmlTransient
        private XmlElementLocation location;

        @Override
        public XmlElementLocation getLocation() {
            return location;
        }

        @Override
        public void setLocation(final XmlElementLocation location) {
            this.location = location;
        }
    }

}
