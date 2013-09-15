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
    private double            longitute;

    /**
     * 
     */
    public LatLng() {
        // 処理なし
    }

    /**
     * @param pLatitude -
     * @param pLongitute -
     */
    public LatLng(final double pLatitude, final double pLongitute) {
        this.latitude = pLatitude;
        this.longitute = pLongitute;
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
        if (Double.doubleToLongBits(this.longitute) != Double.doubleToLongBits(other.longitute)) {
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
    public double getLongitute() {
        return this.longitute;
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
        temp = Double.doubleToLongBits(this.longitute);
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
    public void setLongitute(final double pLongitute) {
        this.longitute = pLongitute;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @SuppressWarnings("nls")
    @Override
    public String toString() {
        return "LatLng [latitude=" + this.latitude + ", longitute=" + this.longitute + "]";
    }
}
