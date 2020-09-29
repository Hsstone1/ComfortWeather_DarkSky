package model;

public class MapCreator {

    public MapCreator.Radar radar = new MapCreator.Radar();
    public MapCreator.Global global = new MapCreator.Global();
    public MapCreator.Visable visable = new MapCreator.Visable();

    public class Radar {
        private String customHTMLRadar;
        private float radarLatitude;
        private float radarLongitude;

        public String getCustomHTMLRadar() {
            return customHTMLRadar;
        }

        public void setCustomHTMLRadar(String customHTMLRadar) {
            this.customHTMLRadar = customHTMLRadar;
        }

        public float getRadarLatitude() {
            return radarLatitude;
        }

        public void setRadarLatitude(float radarLatitude) {
            this.radarLatitude = radarLatitude;
        }

        public float getRadarLongitude() {
            return radarLongitude;
        }

        public void setRadarLongitude(float radarLongitude) {
            this.radarLongitude = radarLongitude;
        }
    }

    public class Global {
        private String customHTMLGlobal;

        public String getCustomHTMLGlobal() {
            return customHTMLGlobal;
        }

        public void setCustomHTMLGlobal(String customHTMLGlobal) {
            this.customHTMLGlobal = customHTMLGlobal;
        }
    }

    public class Visable {
        private String customHTMLVisable;

        public String getCustomHTMLVisable() {
            return customHTMLVisable;
        }

        public void setCustomHTMLVisable(String customHTMLVisable) {
            this.customHTMLVisable = customHTMLVisable;
        }
    }
}
