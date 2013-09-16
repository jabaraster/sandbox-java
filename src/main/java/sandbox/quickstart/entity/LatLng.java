/**
 * 
 */
package sandbox.quickstart.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;

/**
 * @author jabaraster
 */
@Embeddable
public class LatLng implements Serializable {
    private static final long serialVersionUID = 7937102119649040232L;

    private double            latitude;
    private double            longitude;

    /**
     * 
     */
    public LatLng() {
        // 処理なし
    }

    /**
     * @param pLatitude -
     * @param pLongitude -
     */
    public LatLng(final double pLatitude, final double pLongitude) {
        this.latitude = pLatitude;
        this.longitude = pLongitude;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final LatLng other = (LatLng) obj;
        if (Double.doubleToLongBits(this.latitude) != Double.doubleToLongBits(other.latitude)) {
            return false;
        }
        if (Double.doubleToLongBits(this.longitude) != Double.doubleToLongBits(other.longitude)) {
            return false;
        }
        return true;
    }

    /**
     * @return latitudeを返す.
     */
    public double getLatitude() {
        return this.latitude;
    }

    /**
     * @return longituteを返す.
     */
    public double getLongitude() {
        return this.longitude;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(this.latitude);
        result = prime * result + (int) (temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.longitude);
        result = prime * result + (int) (temp ^ temp >>> 32);
        return result;
    }

    /**
     * @param pLatitude latitudeを設定.
     */
    public void setLatitude(final double pLatitude) {
        this.latitude = pLatitude;
    }

    /**
     * @param pLongitute longituteを設定.
     */
    public void setLongitude(final double pLongitute) {
        this.longitude = pLongitute;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @SuppressWarnings("nls")
    @Override
    public String toString() {
        return "LatLng [latitude=" + this.latitude + ", longitude=" + this.longitude + "]";
    }
}
