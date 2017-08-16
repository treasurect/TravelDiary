package com.treasure.traveldiary.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/5/16.
 */

public class ToolsTicketTrainBean implements Serializable {

    /**
     * msg : success
     * result : [{"arriveTime":"08:03","endStationName":"上海","lishi":"20:09","pricewz":"¥156.5","priceyw":"¥283.5","priceyz":"¥156.5","startStationName":"北京","startTime":"11:54","stationTrainCode":"1461","trainClassName":"普快"},{"arriveTime":"10:43","endStationName":"上海","lishi":"15:10","pricegrw":"¥879.5","pricerw":"¥476.5","pricewz":"¥177.5","priceyw":"¥304.5","priceyz":"¥177.5","startStationName":"北京","startTime":"19:33","stationTrainCode":"T109","trainClassName":"特快"},{"arriveTime":"19:38","endStationName":"上海虹桥","lishi":"05:33","priceed":"¥553.0","pricesw":"¥1748.0","pricewz":"¥553.0","priceyd":"¥933.0","startStationName":"北京南","startTime":"14:05","stationTrainCode":"G43","trainClassName":"高速"},{"arriveTime":"21:50","endStationName":"上海虹桥","lishi":"06:09","priceed":"¥553.0","pricesw":"¥1748.0","pricewz":"¥553.0","priceyd":"¥933.0","startStationName":"北京南","startTime":"15:41","stationTrainCode":"G147","trainClassName":"高速"},{"arriveTime":"22:52","endStationName":"上海虹桥","lishi":"05:37","priceed":"¥553.0","pricesw":"¥1748.0","pricewz":"¥553.0","priceyd":"¥933.0","startStationName":"北京南","startTime":"17:15","stationTrainCode":"G153","trainClassName":"高速"},{"arriveTime":"16:28","endStationName":"上海虹桥","lishi":"06:00","priceed":"¥553.0","pricesw":"¥1748.0","pricewz":"¥553.0","priceyd":"¥933.0","startStationName":"北京南","startTime":"10:28","stationTrainCode":"G121","trainClassName":"高速"},{"arriveTime":"13:15","endStationName":"上海虹桥","lishi":"05:40","priceed":"¥553.0","pricesw":"¥1748.0","pricewz":"¥553.0","priceyd":"¥933.0","startStationName":"北京南","startTime":"07:35","stationTrainCode":"G105","trainClassName":"高速"},{"arriveTime":"15:55","endStationName":"上海虹桥","lishi":"04:55","priceed":"¥553.0","pricesw":"¥1748.0","pricewz":"¥553.0","priceyd":"¥933.0","startStationName":"北京南","startTime":"11:00","stationTrainCode":"G15","trainClassName":"高速"},{"arriveTime":"12:38","endStationName":"上海虹桥","lishi":"05:54","priceed":"¥553.0","pricesw":"¥1748.0","pricewz":"¥553.0","priceyd":"¥933.0","startStationName":"北京南","startTime":"06:44","stationTrainCode":"G101","trainClassName":"高速"},{"arriveTime":"11:55","endStationName":"上海虹桥","lishi":"04:55","priceed":"¥553.0","pricesw":"¥1748.0","pricewz":"¥553.0","priceyd":"¥933.0","startStationName":"北京南","startTime":"07:00","stationTrainCode":"G5","trainClassName":"高速"},{"arriveTime":"22:39","endStationName":"上海虹桥","lishi":"05:39","priceed":"¥553.0","pricesw":"¥1748.0","pricewz":"¥553.0","priceyd":"¥933.0","startStationName":"北京南","startTime":"17:00","stationTrainCode":"G21","trainClassName":"高速"},{"arriveTime":"14:46","endStationName":"上海虹桥","lishi":"05:30","priceed":"¥553.0","pricesw":"¥1748.0","pricewz":"¥553.0","priceyd":"¥933.0","startStationName":"北京南","startTime":"09:16","stationTrainCode":"G41","trainClassName":"高速"},{"arriveTime":"20:26","endStationName":"上海虹桥","lishi":"05:55","priceed":"¥553.0","pricesw":"¥1748.0","pricewz":"¥553.0","priceyd":"¥933.0","startStationName":"北京南","startTime":"14:31","stationTrainCode":"G143","trainClassName":"高速"},{"arriveTime":"15:07","endStationName":"上海虹桥","lishi":"05:45","priceed":"¥553.0","pricesw":"¥1748.0","pricewz":"¥553.0","priceyd":"¥933.0","startStationName":"北京南","startTime":"09:22","stationTrainCode":"G115","trainClassName":"高速"},{"arriveTime":"19:01","endStationName":"上海虹桥","lishi":"05:54","priceed":"¥553.0","pricesw":"¥1748.0","pricewz":"¥553.0","priceyd":"¥933.0","startStationName":"北京南","startTime":"13:07","stationTrainCode":"G137","trainClassName":"高速"},{"arriveTime":"18:14","endStationName":"上海虹桥","lishi":"05:49","priceed":"¥553.0","pricesw":"¥1748.0","pricewz":"¥553.0","priceyd":"¥933.0","startStationName":"北京南","startTime":"12:25","stationTrainCode":"G131","trainClassName":"高速"},{"arriveTime":"23:23","endStationName":"上海虹桥","lishi":"05:40","priceed":"¥553.0","pricesw":"¥1748.0","pricewz":"¥553.0","priceyd":"¥933.0","startStationName":"北京南","startTime":"17:43","stationTrainCode":"G157","trainClassName":"高速"},{"arriveTime":"14:25","endStationName":"上海虹桥","lishi":"05:50","priceed":"¥553.0","pricesw":"¥1748.0","pricewz":"¥553.0","priceyd":"¥933.0","startStationName":"北京南","startTime":"08:35","stationTrainCode":"G111","trainClassName":"高速"},{"arriveTime":"09:13","endStationName":"上海","lishi":"11:50","priceed":"¥309.0","pricerw":"¥615.0","pricewz":"¥309.0","startStationName":"北京南","startTime":"21:23","stationTrainCode":"D321","trainClassName":"动车"},{"arriveTime":"20:13","endStationName":"上海虹桥","lishi":"06:03","priceed":"¥553.0","pricesw":"¥1748.0","pricewz":"¥553.0","priceyd":"¥933.0","startStationName":"北京南","startTime":"14:10","stationTrainCode":"G141","trainClassName":"高速"},{"arriveTime":"14:55","endStationName":"上海虹桥","lishi":"04:55","priceed":"¥553.0","pricesw":"¥1748.0","pricewz":"¥553.0","priceyd":"¥933.0","startStationName":"北京南","startTime":"10:00","stationTrainCode":"G13","trainClassName":"高速"},{"arriveTime":"19:56","endStationName":"上海虹桥","lishi":"04:56","priceed":"¥553.0","pricesw":"¥1748.0","pricewz":"¥553.0","priceyd":"¥933.0","startStationName":"北京南","startTime":"15:00","stationTrainCode":"G17","trainClassName":"高速"},{"arriveTime":"17:07","endStationName":"上海虹桥","lishi":"05:47","priceed":"¥553.0","pricesw":"¥1748.0","pricewz":"¥553.0","priceyd":"¥933.0","startStationName":"北京南","startTime":"11:20","stationTrainCode":"G411","trainClassName":"高速"},{"arriveTime":"15:31","endStationName":"上海虹桥","lishi":"05:51","priceed":"¥553.0","pricesw":"¥1748.0","pricewz":"¥553.0","priceyd":"¥933.0","startStationName":"北京南","startTime":"09:40","stationTrainCode":"G117","trainClassName":"高速"},{"arriveTime":"14:30","endStationName":"上海虹桥","lishi":"05:37","priceed":"¥553.0","pricesw":"¥1748.0","pricewz":"¥553.0","priceyd":"¥933.0","startStationName":"北京南","startTime":"08:53","stationTrainCode":"G113","trainClassName":"高速"},{"arriveTime":"18:37","endStationName":"上海虹桥","lishi":"05:57","priceed":"¥553.0","pricesw":"¥1748.0","pricewz":"¥553.0","priceyd":"¥933.0","startStationName":"北京南","startTime":"12:40","stationTrainCode":"G133","trainClassName":"高速"},{"arriveTime":"13:48","endStationName":"上海虹桥","lishi":"04:48","priceed":"¥553.0","pricesw":"¥1748.0","pricewz":"¥553.0","priceyd":"¥933.0","startStationName":"北京南","startTime":"09:00","stationTrainCode":"G1","trainClassName":"高速"},{"arriveTime":"07:41","endStationName":"上海","lishi":"12:07","priceed":"¥309.0","pricegrw":"¥1233.0","pricerw":"¥615.0","pricewz":"¥309.0","startStationName":"北京南","startTime":"19:34","stationTrainCode":"D313","trainClassName":"动车"},{"arriveTime":"22:24","endStationName":"上海虹桥","lishi":"05:59","priceed":"¥553.0","pricesw":"¥1748.0","pricewz":"¥553.0","priceyd":"¥933.0","startStationName":"北京南","startTime":"16:25","stationTrainCode":"G149","trainClassName":"高速"},{"arriveTime":"20:43","endStationName":"上海虹桥","lishi":"06:07","priceed":"¥553.0","pricesw":"¥1748.0","pricewz":"¥553.0","priceyd":"¥933.0","startStationName":"北京南","startTime":"14:36","stationTrainCode":"G145","trainClassName":"高速"},{"arriveTime":"13:38","endStationName":"上海虹桥","lishi":"05:33","priceed":"¥553.0","pricesw":"¥1748.0","pricewz":"¥553.0","priceyd":"¥933.0","startStationName":"北京南","startTime":"08:05","stationTrainCode":"G107","trainClassName":"高速"},{"arriveTime":"18:48","endStationName":"上海虹桥","lishi":"04:48","priceed":"¥553.0","pricesw":"¥1748.0","pricewz":"¥553.0","priceyd":"¥933.0","startStationName":"北京南","startTime":"14:00","stationTrainCode":"G3","trainClassName":"高速"},{"arriveTime":"19:52","endStationName":"上海虹桥","lishi":"06:12","priceed":"¥553.0","pricesw":"¥1748.0","pricewz":"¥553.0","priceyd":"¥933.0","startStationName":"北京南","startTime":"13:40","stationTrainCode":"G139","trainClassName":"高速"},{"arriveTime":"15:45","endStationName":"上海虹桥","lishi":"05:40","priceed":"¥553.0","pricesw":"¥1748.0","pricewz":"¥553.0","priceyd":"¥933.0","startStationName":"北京南","startTime":"10:05","stationTrainCode":"G119","trainClassName":"高速"},{"arriveTime":"16:59","endStationName":"上海虹桥","lishi":"05:49","priceed":"¥553.0","pricesw":"¥1748.0","pricewz":"¥553.0","priceyd":"¥933.0","startStationName":"北京南","startTime":"11:10","stationTrainCode":"G125","trainClassName":"高速"},{"arriveTime":"21:15","endStationName":"上海虹桥","lishi":"05:15","priceed":"¥553.0","pricesw":"¥1748.0","pricewz":"¥553.0","priceyd":"¥933.0","startStationName":"北京南","startTime":"16:00","stationTrainCode":"G19","trainClassName":"高速"},{"arriveTime":"23:55","endStationName":"上海虹桥","lishi":"04:55","priceed":"¥553.0","pricesw":"¥1748.0","pricewz":"¥553.0","priceyd":"¥933.0","startStationName":"北京南","startTime":"19:00","stationTrainCode":"G7","trainClassName":"高速"},{"arriveTime":"13:10","endStationName":"上海虹桥","lishi":"05:10","priceed":"¥553.0","pricesw":"¥1748.0","pricewz":"¥553.0","priceyd":"¥933.0","startStationName":"北京南","startTime":"08:00","stationTrainCode":"G11","trainClassName":"高速"},{"arriveTime":"17:49","endStationName":"上海虹桥","lishi":"05:39","priceed":"¥553.0","pricesw":"¥1748.0","pricewz":"¥553.0","priceyd":"¥933.0","startStationName":"北京南","startTime":"12:10","stationTrainCode":"G129","trainClassName":"高速"},{"arriveTime":"18:57","endStationName":"上海虹桥","lishi":"06:02","priceed":"¥553.0","pricesw":"¥1748.0","pricewz":"¥553.0","priceyd":"¥933.0","startStationName":"北京南","startTime":"12:55","stationTrainCode":"G135","trainClassName":"高速"}]
     * retCode : 200
     */

    private String msg;
    private String retCode;
    private List<ResultBean> result;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getRetCode() {
        return retCode;
    }

    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * arriveTime : 08:03
         * endStationName : 上海
         * lishi : 20:09
         * pricewz : ¥156.5
         * priceyw : ¥283.5
         * priceyz : ¥156.5
         * startStationName : 北京
         * startTime : 11:54
         * stationTrainCode : 1461
         * trainClassName : 普快
         * pricegrw : ¥879.5
         * pricerw : ¥476.5
         * priceed : ¥553.0
         * pricesw : ¥1748.0
         * priceyd : ¥933.0
         */

        private String arriveTime;
        private String endStationName;
        private String lishi;
        private String pricewz;
        private String priceyw;
        private String priceyz;
        private String startStationName;
        private String startTime;
        private String stationTrainCode;
        private String trainClassName;
        private String pricegrw;
        private String pricerw;
        private String priceed;
        private String pricesw;
        private String priceyd;

        public String getArriveTime() {
            return arriveTime;
        }

        public void setArriveTime(String arriveTime) {
            this.arriveTime = arriveTime;
        }

        public String getEndStationName() {
            return endStationName;
        }

        public void setEndStationName(String endStationName) {
            this.endStationName = endStationName;
        }

        public String getLishi() {
            return lishi;
        }

        public void setLishi(String lishi) {
            this.lishi = lishi;
        }

        public String getPricewz() {
            return pricewz;
        }

        public void setPricewz(String pricewz) {
            this.pricewz = pricewz;
        }

        public String getPriceyw() {
            return priceyw;
        }

        public void setPriceyw(String priceyw) {
            this.priceyw = priceyw;
        }

        public String getPriceyz() {
            return priceyz;
        }

        public void setPriceyz(String priceyz) {
            this.priceyz = priceyz;
        }

        public String getStartStationName() {
            return startStationName;
        }

        public void setStartStationName(String startStationName) {
            this.startStationName = startStationName;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getStationTrainCode() {
            return stationTrainCode;
        }

        public void setStationTrainCode(String stationTrainCode) {
            this.stationTrainCode = stationTrainCode;
        }

        public String getTrainClassName() {
            return trainClassName;
        }

        public void setTrainClassName(String trainClassName) {
            this.trainClassName = trainClassName;
        }

        public String getPricegrw() {
            return pricegrw;
        }

        public void setPricegrw(String pricegrw) {
            this.pricegrw = pricegrw;
        }

        public String getPricerw() {
            return pricerw;
        }

        public void setPricerw(String pricerw) {
            this.pricerw = pricerw;
        }

        public String getPriceed() {
            return priceed;
        }

        public void setPriceed(String priceed) {
            this.priceed = priceed;
        }

        public String getPricesw() {
            return pricesw;
        }

        public void setPricesw(String pricesw) {
            this.pricesw = pricesw;
        }

        public String getPriceyd() {
            return priceyd;
        }

        public void setPriceyd(String priceyd) {
            this.priceyd = priceyd;
        }
    }
}
